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
import com.pfi.crm.multitenant.tenant.model.Colaborador;
import com.pfi.crm.multitenant.tenant.payload.ColaboradorPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.ColaboradorRepository;

@Service
public class ColaboradorService {
	
	@Autowired
	private ColaboradorRepository colaboradorRepository;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ColaboradorService.class);
	
	public ColaboradorPayload getColaboradorByIdContacto(@PathVariable Long id) {
        return colaboradorRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
                () -> new ResourceNotFoundException("Colaborador", "id", id)).toPayload();
    }
	
	public List<ColaboradorPayload> getColaboradores() {
		//return colaboradorRepository.findAll();
		return colaboradorRepository.findAll().stream().map(e -> toPayload(e)).collect(Collectors.toList());
    }
	
	public ColaboradorPayload altaColaborador (ColaboradorPayload payload) {
		payload.setId(null);
		return colaboradorRepository.save(new Colaborador(payload)).toPayload();
	}
	
	public void bajaColaborador(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Optional<Colaborador> optionalModel = colaboradorRepository.findByPersonaFisica_Contacto_Id(id);
		if(optionalModel.isPresent()) {
			Colaborador m = optionalModel.get();
			m.setEstadoActivoColaborador(false);
			m.setContacto(null);
			m.setPersonaFisica(null);
			colaboradorRepository.save(m);
			colaboradorRepository.delete(m);											//Temporalmente se elimina de la BD			
		}
		else {
			//No existe persona Fisica
		}
		
	}
	
	public ColaboradorPayload modificarColaborador(ColaboradorPayload payload) {
		if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			Optional<Colaborador> optional = colaboradorRepository.findByPersonaFisica_Contacto_Id(payload.getId());
			if(optional.isPresent()) {   //Si existe
				Colaborador model = optional.get();
				model.modificar(payload);
				return colaboradorRepository.save(model).toPayload();				
			}
			//si llegue aca devuelvo null
		}
		return null;
	}
	
	
	
	// Conversiones Payload Model
	public Colaborador toModel(ColaboradorPayload p) {

		Colaborador m = new Colaborador();

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
		
		// Colaborador
		// this.setIdColaborador(null);
		m.setArea(p.getArea());
		// this.setEstadoActivoColaborador(true);
		// Fin Colaborador

		return m;
	}

	public ColaboradorPayload toPayload(Colaborador m) {

		ColaboradorPayload p = new ColaboradorPayload();

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

		// Colaborador
		// this.setIdColaborador(null);
		p.setArea(m.getArea());
		// this.setEstadoActivoColaborador(true);
		// Fin Colaborador

		return p;
	}
}
