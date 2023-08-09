package com.pfi.crm.multitenant.tenant.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.PersonaFisica;
import com.pfi.crm.multitenant.tenant.model.Proyecto;
import com.pfi.crm.multitenant.tenant.payload.PersonaFisicaPayload;
import com.pfi.crm.multitenant.tenant.payload.ProyectoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ProyectoRepository;

@Service
public class ProyectoService {
	
	@Autowired
	private ProyectoRepository proyectoRepository;
	
	@Autowired
	private PersonaFisicaService personaFisicaService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProyectoService.class);
	
	public ProyectoPayload getProyectoById(@PathVariable Long id) {
		System.out.println("ID proyecto: " + id);
		return proyectoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Proyecto", "id", id)).toPayload();
	}
	
	public List<ProyectoPayload> getProyectos() {
		return proyectoRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	
	public ProyectoPayload altaProyecto(ProyectoPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como Proyecto a dar de alta, por favor ingrese datos de un proyecto.");
		if(payload.getId() != null) { //Si existe ID proyectp:
			// 1) No permito modificar, solo alta si no existe.
			Long id = payload.getId();
			boolean existeProyecto = this.existeProyecto(id);
			if(existeProyecto) {
				throw new BadRequestException("Ya existe Proyecto con ID '" + id.toString() + "' cargado. "
						+ "Es posible que quiera ir a la pantalla de modificar.");
			} else {
				throw new BadRequestException("Ha ingresado un ID de proyecto, por favor que el ID de actividad sea null"
						+ " si desea dar de alta.");
			}
		}
		return altaModificarProyecto(payload).toPayload();
	}
	
	public String bajaProyecto(Long id) {
		Proyecto m = proyectoRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Proyecto", "id", id));
		String message = "Se ha dado de baja al proyecto id: '" + id + "'";
		if(m.getInvolucrados()!=null) {
			message += ", y desasociado a sus involucrados";
			m.setInvolucrados(null);
		}
		m = proyectoRepository.save(m);
		proyectoRepository.delete(m);
		
		return message;
	}
	
	public ProyectoPayload modificarProyecto(ProyectoPayload payload) {
		return this.altaModificarProyecto(payload).toPayload();
	}
	
	
	
	
	private Proyecto altaModificarProyecto(ProyectoPayload payload) {
		Proyecto proyectoModel = null;
		if(payload.getId() != null) {//Modificar
			proyectoModel = proyectoRepository.findById(payload.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Proyecto", "id", payload.getId()));
		}
		
		//Busco sus integrantes
		Set<PersonaFisica> involucrados = new HashSet<PersonaFisica>();
		if(payload.getInvolucrados() != null) {
			for(PersonaFisicaPayload b: payload.getInvolucrados()) {
				if(b.getId() != null) {
					PersonaFisica item = personaFisicaService.getPersonaFisicaModelByIdContacto(b.getId());
					if(item != null) {
						involucrados.add(item);
					}
				}
				
			}
		}
		
		if(proyectoModel == null){//Alta
			proyectoModel = new Proyecto(payload, involucrados);
		} else {
			proyectoModel.modificar(payload, involucrados);
		}
		
		return proyectoRepository.save(proyectoModel);
	}
	
	public String quitarIntegranteDeSusProyectos(Long idContacto) {
		if(idContacto == null)
			throw new BadRequestException("Ha introducido un id='null' para buscar, por favor ingrese un número válido.");
		
		
		boolean existePersona = personaFisicaService.existePersonaFisicaPorIdContacto(idContacto);
		if(!existePersona) {
			return "";
		}
		PersonaFisica personaFisica = personaFisicaService.getPersonaFisicaModelByIdContacto(idContacto);
		Long idPersonaFisica = personaFisica.getIdPersonaFisica();
		
		List<Proyecto> proyectosPersonaFisica = proyectoRepository.findByInvolucradosIdPersonaFisica(idPersonaFisica);
		String message = "";
		if(proyectosPersonaFisica.size()>1)
			message += "Se lo ha desasociado como involucrado de los proyectos id's: ";
		else if(proyectosPersonaFisica.size()==1)
			message += "Se lo ha desasociado como involucrado del proyecto id: ";
		else//size ==0
			return "";
		for(int i=0; i<proyectosPersonaFisica.size(); i++) {
			message += proyectosPersonaFisica.get(i);
			if(i<proyectosPersonaFisica.size()-1)
				message += ", ";
			proyectosPersonaFisica.get(i).getInvolucrados().remove(personaFisica);
		}
		proyectoRepository.saveAll(proyectosPersonaFisica);
		return message;
	}
	
	public boolean existeProyecto(Long id) {
		return proyectoRepository.existsById(id);
	}
	
	
	
	/**
	 * Info para gráficos de front 
	 * @return
	 */
	public List<Map<String, Object>> countTop20Involucrados() {
		List<Map<String, Object>> countTop20Involucrados = proyectoRepository.findTop20InvolucradosOrderedByCount();
		//Esto es solo para que me lo devuelva con orden de propiedades: id, nombre...
		//Sin esto te devuelve como: cantidad, apellido, nombre..., nombtr, cantidad, apellido...
		//Orden de propiedades sin orden
		List<Map<String, Object>> nuevaLista = new ArrayList<>();
		for (Map<String, Object> item : countTop20Involucrados) {
            Map<String, Object> orderedPersona = new LinkedHashMap<>();
            orderedPersona.put("id", item.get("id"));
            orderedPersona.put("nombre", item.get("nombre"));
            orderedPersona.put("apellido", item.get("apellido"));
            orderedPersona.put("descripcion", item.get("nombreDescripcion"));
            orderedPersona.put("cantidad", item.get("cantidad"));
            item = orderedPersona;
            nuevaLista.add(orderedPersona);
        }
		return nuevaLista;
	}
	
	public List<Map<String, Object>> countUltimosProximos12meses() {
		LocalDate now = LocalDate.now();
		LocalDate start = now.minusMonths(12).withDayOfMonth(1);
		LocalDate end = now.plusMonths(12).withDayOfMonth(YearMonth.from(LocalDate.now().plusMonths(11)).lengthOfMonth());
		List<Map<String, Object>> countProyectosCreatedLast12MonthsByMonth = proyectoRepository.findProyectosByFechaBetween(start, end);
		//return countContactosCreatedLast12MonthsByMonth;//No incluye meses con 0 actividades
		
		// Generar meses/años faltantes con cantidad 0
	    List<Map<String, Object>> result = new ArrayList<>();
	    LocalDate currentMonth = start;
	    while (currentMonth.isBefore(end)) {
	        int month = currentMonth.getMonthValue();
	        int year = currentMonth.getYear();
	        boolean found = false;

	        for (Map<String, Object> map : countProyectosCreatedLast12MonthsByMonth) {
	            int mapMonth = (int) map.get("month");
	            int mapYear = (int) map.get("year");

	            if (month == mapMonth && year == mapYear) {
	                result.add(map);
	                found = true;
	                break;
	            }
	        }

	        if (!found) {
	            Map<String, Object> missingMonth = new HashMap<>();
	            missingMonth.put("month", month);
	            missingMonth.put("year", year);
	            missingMonth.put("cantidad", 0);
	            result.add(missingMonth);
	        }

	        currentMonth = currentMonth.plusMonths(1);
	    }

	    return result;
	}
	
	
	
}