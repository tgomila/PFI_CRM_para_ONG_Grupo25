package com.pfi.crm.multitenant.tenant.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Profesional;
import com.pfi.crm.multitenant.tenant.payload.ProfesionalPayload;
import com.pfi.crm.multitenant.tenant.repository.ProfesionalRepository;

@Service
public class ProfesionalService {
	
	@Autowired
	private ProfesionalRepository profesionalRepository;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProfesionalService.class);
	
	public ProfesionalPayload getProfesionalByIdContacto(@PathVariable Long id) {
        return profesionalRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("Profesional", "id", id)).toPayload();
    }
	
	public List<ProfesionalPayload> getProfesionales() {
		//return profesionalRepository.findAll();
		return profesionalRepository.findAll().stream().map(e -> toPayload(e)).collect(Collectors.toList());
    }
	
	public ProfesionalPayload altaProfesional (ProfesionalPayload payload) {
		payload.setId(null);
		return profesionalRepository.save(new Profesional(payload)).toPayload();
	}
	
	public void bajaProfesional(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Optional<Profesional> optionalModel = profesionalRepository.findByPersonaFisica_Contacto_Id(id);
		if(optionalModel.isPresent()) {
			Profesional m = optionalModel.get();
			m.setEstadoActivoProfesional(false);
			m.setContacto(null);
			m.setPersonaFisica(null);
			profesionalRepository.save(m);
			profesionalRepository.delete(m);											//Temporalmente se elimina de la BD			
		}
		else {
			//No existe persona Fisica
		}
		
	}
	
	public ProfesionalPayload modificarProfesional(ProfesionalPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			Optional<Profesional> optional = profesionalRepository.findByPersonaFisica_Contacto_Id(payload.getId());
			if(optional.isPresent()) {   //Si existe
				Profesional model = optional.get();
				model.modificar(payload);
				return profesionalRepository.save(model).toPayload();				
			}
			//si llegue aca devuelvo null
		}
		return null;
	}
	
	
	
	// Conversiones Payload Model
	public Profesional toModel(ProfesionalPayload p) {

		Profesional m = new Profesional();

		// Contacto
		m.setId(p.getId());
		m.setEstadoActivoContacto(true);
		m.setFechaAltaContacto(LocalDate.now());
		m.setFechaBajaContacto(null);
		m.setNombreDescripcion(p.getNombreDescripcion());
		m.setCuit(p.getCuit());
		m.setDomicilio(p.getDomicilio());
		m.setEmail(p.getEmail());
		m.setTelefono(p.getTelefono());
		// Fin Contacto

		// Persona Fisica
		m.setIdPersonaFisica(null);
		m.setDni(p.getDni());
		m.setNombre(p.getNombre());
		m.setApellido(p.getApellido());
		m.setFechaNacimiento(p.getFechaNacimiento());
		// Fin Persona Fisica
		
		// TrabajadorAbstract
		m.setDatosBancarios(p.getDatosBancarios());
		// Fin TrabajadorAbstract
		
		// Profesional
		//this.setIdProfesional(null);
		m.setProfesion(p.getProfesion());
		//this.setEstadoActivoProfesional(p.getEstadoActivoProfesional());
		// Fin Profesional

		return m;
	}

	public ProfesionalPayload toPayload(Profesional m) {

		ProfesionalPayload p = new ProfesionalPayload();

		// Contacto
		p.setId(m.getId());
		p.setNombreDescripcion(m.getNombreDescripcion());
		p.setCuit(m.getCuit());
		p.setDomicilio(m.getDomicilio());
		p.setEmail(m.getEmail());
		p.setTelefono(m.getTelefono());
		// Fin Contacto

		// Persona Fisica
		// p.setIdPersonaFisica(null);
		p.setDni(m.getDni());
		p.setNombre(m.getNombre());
		p.setApellido(m.getApellido());
		p.setFechaNacimiento(m.getFechaNacimiento());
		// Fin Persona Fisica
		
		// TrabajadorAbstract
		p.setDatosBancarios(m.getDatosBancarios());
		// Fin TrabajadorAbstract

		// Profesional
		//this.setIdProfesional(null);
		p.setProfesion(m.getProfesion());
		//this.setEstadoActivoProfesional(p.getEstadoActivoProfesional());
		// Fin Profesional

		return p;
	}
}
