package com.pfi.crm.service;

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
import com.pfi.crm.model.Voluntario;
import com.pfi.crm.payload.VoluntarioPayload;
import com.pfi.crm.repository.VoluntarioRepository;

@Service
public class VoluntarioService {
	
	@Autowired
	private VoluntarioRepository voluntarioRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(VoluntarioService.class);
	
	public VoluntarioPayload getVoluntarioByIdContacto(@PathVariable Long id) {
        return voluntarioRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("Voluntario", "id", id)).toPayload();
    }
	
	public List<VoluntarioPayload> getVoluntarios() {
		//return voluntarioRepository.findAll();
		return voluntarioRepository.findAll().stream().map(e -> toPayload(e)).collect(Collectors.toList());
    }
	
	public VoluntarioPayload altaVoluntario (VoluntarioPayload payload) {
		payload.setId(null);
		return voluntarioRepository.save(new Voluntario(payload)).toPayload();
	}
	
	public void bajaVoluntario(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Optional<Voluntario> optionalModel = voluntarioRepository.findByPersonaFisica_Contacto_Id(id);
		if(optionalModel.isPresent()) {
			Voluntario m = optionalModel.get();
			m.setEstadoActivoVoluntario(false);
			m.setContacto(null);
			m.setPersonaFisica(null);
			voluntarioRepository.save(m);
			voluntarioRepository.delete(m);											//Temporalmente se elimina de la BD			
		}
		else {
			//No existe persona Fisica
		}
		
	}
	
	public VoluntarioPayload modificarVoluntario(VoluntarioPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			Optional<Voluntario> optional = voluntarioRepository.findByPersonaFisica_Contacto_Id(payload.getId());
			if(optional.isPresent()) {   //Si existe
				Voluntario model = optional.get();
				model.modificar(payload);
				return voluntarioRepository.save(model).toPayload();				
			}
			//si llegue aca devuelvo null
		}
		return null;
	}
	
	
	
	// Conversiones Payload Model
	public Voluntario toModel(VoluntarioPayload p) {

		Voluntario m = new Voluntario();

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
		
		// Voluntario
		//p.setEstadoActivoVoluntario(this.getEstadoActivoVoluntario());
		// Fin Voluntario

		return m;
	}

	public VoluntarioPayload toPayload(Voluntario m) {

		VoluntarioPayload p = new VoluntarioPayload();

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
		
		// Voluntario
		//p.setEstadoActivoVoluntario(this.getEstadoActivoVoluntario());
		// Fin Voluntario

		return p;
	}
}
