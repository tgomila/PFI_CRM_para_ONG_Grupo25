package com.pfi.crm.multitenant.tenant.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
	
	public ActividadPayload altaActividad(ActividadPayload p) {
		return this.altaActividadModel(p).toPayload();
	}
	
	public Actividad altaActividadModel(ActividadPayload payload) {
		
		//Esto evita que puedan modificar models beneficiarios y profesional
		Set<Beneficiario> beneficiarios = new HashSet<Beneficiario>();
		if(payload.getBeneficiarios() != null) {
			for(BeneficiarioPayload b: payload.getBeneficiarios()) {
				if(b.getId() != null) {
					Beneficiario item = beneficiarioService.getBeneficiarioModelByIdContacto(b.getId());
					beneficiarios.add(item);
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
					profesionales.add(item);
				}
				
			}
		}
		
		Actividad m = new Actividad(payload, beneficiarios, profesionales);
		m.setId(null);
		return actividadRepository.save(m);
	}
	
	public void bajaActividad(Long id) {
		Actividad m = actividadRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Actividad", "id", id));
		m.setEstadoActivoActividad(false);
		m.setBeneficiarios(null);
		m.setProfesionales(null);
		m = actividadRepository.save(m);
		actividadRepository.delete(m);
	}
	
	public ActividadPayload modificarActividad(ActividadPayload payload) {
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
					beneficiarios.add(item);
				}
				
			}
		}
		

		Set<Profesional> profesionales = new HashSet<Profesional>();
		if(payload.getProfesionales() != null) {
			for(ProfesionalPayload p: payload.getProfesionales()) {
				if(p.getId() != null) {
					Profesional item = profesionalService.getProfesionalModelByIdContacto(p.getId());
					profesionales.add(item);
				}
				
			}
		}
		
		model.modificar(payload, profesionales, beneficiarios);
		return actividadRepository.save(model);
	}
	
	public void bajaBeneficiarioEnActividades(Long idContacto) {
		Beneficiario beneficiario = beneficiarioService.getBeneficiarioModelByIdContacto(idContacto);
		List<Actividad> actividades = actividadRepository.findAll();
		List<Actividad> actividadesAModificar = new ArrayList<Actividad>();
		for(Actividad actividad: actividades) {
			if(actividad.getBeneficiarios().contains(beneficiario)) {
				actividad.getBeneficiarios().remove(beneficiario);
				actividadesAModificar.add(actividad);
			}
		}
		if(!actividadesAModificar.isEmpty())
			actividadRepository.saveAll(actividadesAModificar);
	}
	

	
	public void bajaProfesionalEnActividades(Long idContacto) {
		Profesional profesional = profesionalService.getProfesionalModelByIdContacto(idContacto);
		List<Actividad> actividades = actividadRepository.findAll();
		List<Actividad> actividadesAModificar = new ArrayList<Actividad>();
		for(Actividad actividad: actividades) {
			if(actividad.getProfesionales().contains(profesional)) {
				actividad.getProfesionales().remove(profesional);
				actividadesAModificar.add(actividad);
			}
		}
		if(!actividadesAModificar.isEmpty())
			actividadRepository.saveAll(actividadesAModificar);
	}
	
	
	public boolean existeActividad(Long id) {
		return actividadRepository.existsById(id);
	}
	
}
