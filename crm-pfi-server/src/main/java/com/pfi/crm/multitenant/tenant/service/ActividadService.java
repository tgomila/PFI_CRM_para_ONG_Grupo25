package com.pfi.crm.multitenant.tenant.service;

import java.time.LocalDateTime;
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
import com.pfi.crm.multitenant.tenant.model.Actividad;
import com.pfi.crm.multitenant.tenant.model.Beneficiario;
import com.pfi.crm.multitenant.tenant.model.Profesional;
import com.pfi.crm.multitenant.tenant.payload.ActividadPayload;
import com.pfi.crm.multitenant.tenant.payload.BeneficiarioPayload;
import com.pfi.crm.multitenant.tenant.payload.ProfesionalPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ActividadRepository;

@Service
public class ActividadService {
	
	@Autowired
	private ActividadRepository actividadRepository;
	
	@Autowired
	private BeneficiarioService beneficiarioService;
	
	@Autowired
	private ProfesionalService profesionalService;
	
	@Autowired
	private FileStorageService fileStorageService;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ActividadService.class);
	
	public ActividadPayload getActividadById(@PathVariable Long id) {
		return actividadRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Actividad", "id", id)).toPayload();
	}
	
	public Actividad getActividadModelById(@PathVariable Long id) {
		return actividadRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Actividad", "id", id));
	}
	
	public List<ActividadPayload> getActividades() {
		return actividadRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	public ActividadPayload altaActividad(ActividadPayload payload) {
		return this.altaActividadModel(payload).toPayload();
	}
	
	public Actividad altaActividadModel(ActividadPayload payload) {
		if(payload == null)
			throw new BadRequestException("Ha introducido un null como actividad a dar de alta, por favor ingrese datos de una actividad.");
		if(payload.getId() != null) { //Si existe ID actividad:
			// 1) No permito modificar, solo alta si no existe.
			Long id = payload.getId();
			boolean existeActividad = this.existeActividad(id);
			if(existeActividad) {
				throw new BadRequestException("Ya existe Actividad con ID '" + id.toString() + "' cargado. "
						+ "Es posible que quiera ir a la pantalla de modificar.");
			} else {
				throw new BadRequestException("Ha ingresado un ID de actividad, por favor que el ID de actividad sea null"
						+ " si desea dar de alta.");
			}
		}
		return this.altaModificarActividadModel(payload);
	}
	
	public ActividadPayload modificarActividad(ActividadPayload payload) {
		return this.modificarActividadModel(payload).toPayload();
	}
	
	public Actividad modificarActividadModel(ActividadPayload payload) {
		
		if (payload != null && payload.getId() != null) {
			//Necesito el id de actividad o se crearia uno nuevo
			return this.altaModificarActividadModel(payload);
		}
		throw new BadRequestException("No se puede modificar Actividad sin ID");
	}
	
	public Actividad altaModificarActividadModel(ActividadPayload payload) {
		Actividad actividadModel = null;
		if(payload.getId() != null) {//Modificar
			actividadModel = actividadRepository.findById(payload.getId()).orElseThrow(
					() -> new ResourceNotFoundException("Actividad", "id", payload.getId()));
		}
		
		//Esto evita que puedan modificar models beneficiarios y profesional
		Set<Beneficiario> beneficiarios = new HashSet<Beneficiario>();
		if(payload.getBeneficiarios() != null) {
			for(BeneficiarioPayload b: payload.getBeneficiarios()) {
				if(b.getId() != null) {
					Beneficiario item = beneficiarioService.getBeneficiarioModelByIdContacto(b.getId());
					if(item != null) {
						beneficiarios.add(item);
					}
				}
				else {
					throw new BadRequestException("Ha introducido un ID='null' en un beneficiario para asociar en actividad, por favor ingrese un ID válido.");
				}
			}
		}
		

		Set<Profesional> profesionales = new HashSet<Profesional>();
		if(payload.getProfesionales() != null) {
			for(ProfesionalPayload p: payload.getProfesionales()) {
				if(p.getId() != null) {
					Profesional item = profesionalService.getProfesionalModelByIdContacto(p.getId());
					if(item != null) {
						profesionales.add(item);
					}
				}
				
			}
		}
		
		if(actividadModel == null) {//Alta
			actividadModel = new Actividad(payload, beneficiarios, profesionales);
		} else {
			actividadModel.modificar(payload, profesionales, beneficiarios);
		}
		
		return actividadRepository.save(actividadModel);
	}
	
	public String bajaActividad(Long id) {
		Actividad m = actividadRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Actividad", "id", id));
		m.setEstadoActivoActividad(false);
		m.setBeneficiarios(null);
		m.setProfesionales(null);
		m = actividadRepository.save(m);
		actividadRepository.delete(m);
		
		String message = "Se ha dado de baja a la actividad id: '" + id +"'";
		
		//Una vez eliminada la actividad, se elimina su foto si es que poseia
		boolean existeFoto = fileStorageService.deleteFotoGeneric(id, "actividad");
		message += existeFoto ? " junto a su foto de actividad" : "";
		
		return message;
	}
	
	public String quitarContactoEnActividades(Long idContacto) {
		if(idContacto == null)
			throw new BadRequestException("Ha introducido un id='null' para buscar, por favor ingrese un número válido.");
		String message = "";
		message += quitarBeneficiarioEnActividades(idContacto);
		message += quitarProfesionalEnActividades(idContacto);
		return message;
	}
	
	public String quitarBeneficiarioEnActividades(Long idContacto) {
		if(idContacto == null)
			throw new BadRequestException("Ha introducido un id='null' para buscar, por favor ingrese un número válido.");
		boolean existeBeneficiario = beneficiarioService.existeBeneficiarioPorIdContacto(idContacto);
		if(!existeBeneficiario) {
			return "";
		}
		Beneficiario beneficiario = beneficiarioService.getBeneficiarioModelByIdContacto(idContacto);
		Long idBeneficiario = beneficiario.getIdBeneficiario();
		List<Actividad> actividadesBeneficiario = actividadRepository.findByBeneficiariosIdBeneficiario(idBeneficiario);
		String message = "";
		if(actividadesBeneficiario.size()>1)
			message += "Se lo ha desasociado como beneficiario de las actividades id's: ";
		else if(actividadesBeneficiario.size()==1)
			message += "Se lo ha desasociado como beneficiario de la actividad id: ";
		else//size ==0
			return "";
		for(int i=0; i<actividadesBeneficiario.size(); i++) {
			message += actividadesBeneficiario.get(i);
			if(i<actividadesBeneficiario.size()-1)
				message += ", ";
			actividadesBeneficiario.get(i).getBeneficiarios().remove(beneficiario);
		}
		actividadRepository.saveAll(actividadesBeneficiario);
		return message;
	}
	
	public String quitarProfesionalEnActividades(Long idContacto) {
		if(idContacto == null)
			throw new BadRequestException("Ha introducido un id='null' para buscar, por favor ingrese un número válido.");
		boolean existeProfesional = profesionalService.existeProfesionalPorIdContacto(idContacto);
		if(!existeProfesional) {
			return "";
		}
		Profesional profesional = profesionalService.getProfesionalModelByIdContacto(idContacto);
		Long idProfesional = profesional.getIdProfesional();
		List<Actividad> actividadesProfesional = actividadRepository.findByProfesionalesIdProfesional(idProfesional);
		String message = "";
		if(actividadesProfesional.size()>1)
			message += "Se lo ha desasociado como profesional de las actividades id's: ";
		else if(actividadesProfesional.size()==1)
			message += "Se lo ha desasociado como profesional de la actividad id: ";
		else//size ==0
			return "";
		for(int i=0; i<actividadesProfesional.size(); i++) {
			message += actividadesProfesional.get(i);
			if(i<actividadesProfesional.size()-1)
				message += ", ";
			actividadesProfesional.get(i).getProfesionales().remove(profesional);
		}
		actividadRepository.saveAll(actividadesProfesional);
		return message;
	}
	
	
	public boolean existeActividad(Long id) {
		return actividadRepository.existsById(id);
	}
	
	/**
	 * Info para gráficos de front 
	 * @return
	 */
	public List<Map<String, Object>> countTop20Beneficiarios() {
		List<Map<String, Object>> countTop20Beneficiarios = actividadRepository.findTop20BeneficiariosOrderedByCount();
		//Esto es solo para que me lo devuelva con orden de propiedades: id, nombre...
		//Sin esto te devuelve como: cantidad, apellido, nombre..., nombtr, cantidad, apellido...
		//Orden de propiedades sin orden
		List<Map<String, Object>> nuevaLista = new ArrayList<>();
		for (Map<String, Object> item : countTop20Beneficiarios) {
            Map<String, Object> orderedBeneficiario = new LinkedHashMap<>();
            orderedBeneficiario.put("id", item.get("id"));
            orderedBeneficiario.put("nombre", item.get("nombre"));
            orderedBeneficiario.put("apellido", item.get("apellido"));
            orderedBeneficiario.put("descripcion", item.get("nombreDescripcion"));
            orderedBeneficiario.put("cantidad", item.get("cantidad"));
            item = orderedBeneficiario;
            nuevaLista.add(orderedBeneficiario);
        }
		return nuevaLista;
	}
	
	public List<Map<String, Object>> countUltimosProximos12meses() {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime start = now.minusMonths(12).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime end = now.plusMonths(12).withDayOfMonth(YearMonth.from(LocalDateTime.now().plusMonths(11)).lengthOfMonth())
				.withHour(23).withMinute(59).withSecond(59).withNano(999);
		List<Map<String, Object>> countContactosCreatedLast12MonthsByMonth = actividadRepository.findActividadesByFechaBetween(start, end);
		//return countContactosCreatedLast12MonthsByMonth;//No incluye meses con 0 actividades
		
		// Generar meses/años faltantes con cantidad 0
	    List<Map<String, Object>> result = new ArrayList<>();
	    LocalDateTime currentMonth = start;
	    while (currentMonth.isBefore(end)) {
	        int month = currentMonth.getMonthValue();
	        int year = currentMonth.getYear();
	        boolean found = false;

	        for (Map<String, Object> map : countContactosCreatedLast12MonthsByMonth) {
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
	
	
	
	
	
	
	
	
	
	

	
	/*public ActividadPayload modificarActividad(ActividadPayload payload) {
		return this.modificarActividadReturnModel(payload).toPayload();
	}
	
	public Actividad modificarActividadReturnModel(ActividadPayload payload) {
		Actividad model = getActividadModelById(payload.getId());
		
		//Esto evita que puedan modificar models beneficiarios y profesional
		Set<Beneficiario> beneficiarios = new HashSet<Beneficiario>();
		if(payload.getBeneficiarios() != null) {
			for(BeneficiarioPayload b: payload.getBeneficiarios()) {
				if(b.getId() != null) {
					Beneficiario item = beneficiarioService.getBeneficiarioModelByIdContacto(b.getId());
					if(item != null) {
						beneficiarios.add(item);
					}
				}
				
			}
		}
		

		Set<Profesional> profesionales = new HashSet<Profesional>();
		if(payload.getProfesionales() != null) {
			for(ProfesionalPayload p: payload.getProfesionales()) {
				if(p.getId() != null) {
					Profesional item = profesionalService.getProfesionalModelByIdContacto(p.getId());
					if(item != null) {
						profesionales.add(item);
					}
				}
				
			}
		}
		
		model.modificar(payload, profesionales, beneficiarios);
		return actividadRepository.save(model);
	}*/
	
}