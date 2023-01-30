package com.pfi.crm.multitenant.tenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.pfi.crm.exception.ResourceNotFoundException;
import com.pfi.crm.multitenant.tenant.model.Empleado;
import com.pfi.crm.multitenant.tenant.payload.EmpleadoPayload;
import com.pfi.crm.multitenant.tenant.persistence.repository.EmpleadoRepository;

@Service
public class EmpleadoService {
	
	@Autowired
	private EmpleadoRepository empleadoRepository;
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(EmpleadoService.class);
	
	public EmpleadoPayload getEmpleadoByIdContacto(@PathVariable Long id) {
		return empleadoRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
        		() -> new ResourceNotFoundException("Empleado", "id", id)).toPayload();
    }
	
	public List<EmpleadoPayload> getEmpleados() {
		//return empleadoRepository.findAll();
		return empleadoRepository.findAll().stream().map(e -> e.toPayload()).collect(Collectors.toList());
    }
	
	public EmpleadoPayload altaEmpleado (EmpleadoPayload payload) {
		payload.setId(null);
		return empleadoRepository.save(new Empleado(payload)).toPayload();
	}
	
	public void bajaEmpleado(Long id) {
		
		//Si Optional es null o no, lo conocemos con ".isPresent()".		
		Empleado m = empleadoRepository.findByPersonaFisica_Contacto_Id(id).orElseThrow(
				() -> new ResourceNotFoundException("Empleado", "id", id));
		//Empleado m = optionalModel.get();
		m.setEstadoActivoEmpleado(false);
		m.setContacto(null);
		m.setPersonaFisica(null);
		empleadoRepository.save(m);
		empleadoRepository.delete(m);
	}
	
	public EmpleadoPayload modificarEmpleado(EmpleadoPayload payload) {
		Empleado model = empleadoRepository.findByPersonaFisica_Contacto_Id(payload.getId()).orElseThrow(
				() -> new ResourceNotFoundException("Empleado", "id", payload.getId()));
		model.modificar(payload);
		return empleadoRepository.save(model).toPayload();	
		
		/*if (payload != null && payload.getId() != null) {
			//Necesito el id de persona Fisica o se crearia uno nuevo
			Optional<Empleado> optional = empleadoRepository.findByPersonaFisica_Contacto_Id(payload.getId());
			if(optional.isPresent()) {   //Si existe
				Empleado model = optional.get();
				model.modificar(payload);
				return empleadoRepository.save(model).toPayload();				
			}
			//si llegue aca devuelvo null
			new ResourceNotFoundException("Empleado", "id", "null");
		}
		return null;*/
	}
	
	
	
	// Conversiones Payload Model
	/*public Empleado toModel(EmpleadoPayload p) {

		Empleado m = new Empleado();

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
		
		// Empleado
		// this.setIdEmpleado(null);
		m.setFuncion(p.getFuncion());
		m.setDescripcion(p.getDescripcion());
		// this.setEstadoActivoEmpleado(true);
		// Fin Empleado

		return m;
	}

	public EmpleadoPayload toPayload(Empleado m) {

		EmpleadoPayload p = new EmpleadoPayload();

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

		// Empleado
		// this.setIdEmpleado(null);
		p.setFuncion(m.getFuncion());
		p.setDescripcion(m.getDescripcion());
		// this.setEstadoActivoEmpleado(true);
		// Fin Empleado

		return p;
	}*/
}
