package com.pfi.crm.multitenant.tenant.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.PersonaFisica;
import com.pfi.crm.multitenant.tenant.payload.PersonaFisicaPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.PersonaFisicaRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.PersonaJuridicaRepository;

@Service
public class PersonaFisicaService {
	
	@Autowired
	private PersonaJuridicaRepository personaJuridicaRepository;
	
	@Autowired
	private PersonaFisicaRepository personaFisicaRepository;
	
	//@Autowired
	//private PersonaJuridicaService personaJuridicaService;
	
	public PersonaFisicaPayload getPersonaFisicaByIdContacto(@PathVariable Long id) {
        return personaFisicaRepository.findByContacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("PersonaFisica", "id", id)).toPayload();
    }
	
	public List<PersonaFisicaPayload> getPersonasFisicas() {
		//return personaFisicaRepository.findAll();
		return personaFisicaRepository.findAll().stream().map(e -> toPayload(e)).collect(Collectors.toList());
    }
	
	public PersonaFisicaPayload altaPersonaFisica (PersonaFisicaPayload payload) {
		payload.setId(null);
		return personaFisicaRepository.save(new PersonaFisica(payload)).toPayload();
	}
	
	public void bajaPersonaFisica(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Optional<PersonaFisica> optionalModel = personaFisicaRepository.findByContacto_Id(id);
		if(optionalModel.isPresent()) {
			PersonaFisica m = optionalModel.get();
			m.setEstadoActivoPersonaFisica(false);
			if(!personaJuridicaRepository.existsByContacto_Id(id)) {
				m.setEstadoActivoContacto(false);				
			}
			personaFisicaRepository.save(m);
			//personaFisicaRepository.delete(m);											//Temporalmente se elimina de la BD			
		}
		else {
			//No existe persona Fisica
		}
		
	}
	
	public PersonaFisicaPayload modificarPersonaFisica(PersonaFisicaPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			Optional<PersonaFisica> optional = personaFisicaRepository.findByContacto_Id(payload.getId());
			if(optional.isPresent()) {   //Si existe
				PersonaFisica model = optional.get();
				model.modificar(payload);
				return personaFisicaRepository.save(model).toPayload();				
			}
			//si llegue aca devuelvo null
		}
		return null;
	}
	
	
	
	//Conversiones Payload Model
	public PersonaFisica toModel (PersonaFisicaPayload p) {
		
		PersonaFisica m = new PersonaFisica();
		
		//Contacto		
		m.setId(p.getId());
		m.setEstadoActivoContacto(true);
		m.setFechaAltaContacto(LocalDate.now());
		m.setFechaBajaContacto(null);
		m.setNombreDescripcion(p.getNombreDescripcion());
		m.setCuit(p.getCuit());
		m.setDomicilio(p.getDomicilio());
		m.setEmail(p.getEmail());
		m.setTelefono(p.getTelefono());
		//Fin Contacto
		
		// Persona Juridica
		m.setIdPersonaFisica(null);
		m.setDni(p.getDni());
		m.setNombre(p.getNombre());
		m.setApellido(p.getApellido());
		m.setFechaNacimiento(p.getFechaNacimiento());
		// Fin Persona Juridica
		
		return m;
	}
	
	public PersonaFisicaPayload toPayload(PersonaFisica m) {
		
		PersonaFisicaPayload p = new PersonaFisicaPayload();
		
		//Contacto		
		p.setId(m.getId());
		p.setNombreDescripcion(m.getNombreDescripcion());
		p.setCuit(m.getCuit());
		p.setDomicilio(m.getDomicilio());
		p.setEmail(m.getEmail());
		p.setTelefono(m.getTelefono());
		//Fin Contacto
		
		// Persona Juridica
			// p.setIdPersonaFisica(null);
		p.setDni(m.getDni());
		p.setNombre(m.getNombre());
		p.setApellido(m.getApellido());
		p.setFechaNacimiento(m.getFechaNacimiento());
		// Fin Persona Juridica
		
		return p;
	}
}
