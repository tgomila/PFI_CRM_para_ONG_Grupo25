package com.pfi.crm.multitenant.tenant.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.PersonaJuridica;
import com.pfi.crm.multitenant.tenant.payload.PersonaJuridicaPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.PersonaFisicaRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.PersonaJuridicaRepository;

@Service
public class PersonaJuridicaService {
	
	@Autowired
	private PersonaJuridicaRepository personaJuridicaRepository;
	
	@Autowired
	private PersonaFisicaRepository personaFisicaRepository;
	
	//@Autowired
	//private PersonaJuridicaService personaJuridicaService;
	
	public PersonaJuridicaPayload getPersonaJuridicaByIdContacto(@PathVariable Long id) {
        return personaJuridicaRepository.findByContacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("PersonaJuridica", "id", id)).toPayload();
    }
	
	public List<PersonaJuridicaPayload> getPersonasJuridicas() {
		//return personaJuridicaRepository.findAll();
		return personaJuridicaRepository.findAll().stream().map(e -> toPayload(e)).collect(Collectors.toList());
    }
	
	public PersonaJuridicaPayload altaPersonaJuridica (PersonaJuridicaPayload payload) {
		payload.setId(null);
		return personaJuridicaRepository.save(new PersonaJuridica(payload)).toPayload();
	}
	
	public void bajaPersonaJuridica(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Optional<PersonaJuridica> optionalModel = personaJuridicaRepository.findByContacto_Id(id);
		if(optionalModel.isPresent()) {
			PersonaJuridica m = optionalModel.get();
			m.setEstadoActivoPersonaJuridica(false);
			if(!personaFisicaRepository.existsByContacto_Id(id))
				m.setEstadoActivoContacto(false);
			personaJuridicaRepository.save(m);
			//personaJuridicaRepository.delete(m);											//Temporalmente se elimina de la BD			
		}
		else {
			//No existe persona juridica
		}
		
	}
	
	public PersonaJuridicaPayload modificarPersonaJuridica(PersonaJuridicaPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona juridica o se crearia uno nuevo
			Optional<PersonaJuridica> optional = personaJuridicaRepository.findByContacto_Id(payload.getId());
			if(optional.isPresent()) {   //Si existe
				PersonaJuridica model = optional.get();
				model.modificar(payload);
				return personaJuridicaRepository.save(model).toPayload();				
			}
			//si llegue aca devuelvo null
		}
		return null;
	}
	
	
	
	//Conversiones Payload Model
	public PersonaJuridica toModel (PersonaJuridicaPayload p) {
		
		PersonaJuridica m = new PersonaJuridica();
		
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
		
		//PersonaJuridica
		m.setIdPersonaJuridica(null);
		m.setInternoTelefono(p.getInternoTelefono());
		m.setTipoPersonaJuridica(p.getTipoPersonaJuridica());
		//Fin PersonaJuridica
		
		return m;
	}
	
	public PersonaJuridicaPayload toPayload(PersonaJuridica m) {
		
		PersonaJuridicaPayload p = new PersonaJuridicaPayload();
		
		//Contacto		
		p.setId(m.getId());
		p.setNombreDescripcion(m.getNombreDescripcion());
		p.setCuit(m.getCuit());
		p.setDomicilio(m.getDomicilio());
		p.setEmail(m.getEmail());
		p.setTelefono(m.getTelefono());
		//Fin Contacto
		
		//PersonaJuridica
		p.setInternoTelefono(p.getInternoTelefono());
		p.setTipoPersonaJuridica(p.getTipoPersonaJuridica());
		//Fin PersonaJuridica
		
		return p;
	}
}
