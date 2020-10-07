package com.pfi.crm.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.payload.VoluntarioPayload;

@Entity
@Table(name="voluntario")
public class Voluntario extends PersonaFisicaAbstract {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idVoluntario;
	private boolean estadoActivoVoluntario;
	
	//Constructores
	public Voluntario() {
		super();
		this.estadoActivoVoluntario = true;
	}

	public Voluntario(Long idVoluntario) {
		super();
		this.idVoluntario = idVoluntario;
		this.estadoActivoVoluntario = true;
	}
	
	public Voluntario(VoluntarioPayload p) {
		super();
		// Contacto
		this.setId(p.getId());
		this.setEstadoActivoContacto(true);
		this.setFechaAltaContacto(LocalDate.now());
		this.setFechaBajaContacto(null);
		this.setNombreDescripcion(p.getNombreDescripcion());
		this.setCuit(p.getCuit());
		this.setDomicilio(p.getDomicilio());
		this.setEmail(p.getEmail());
		this.setTelefono(p.getTelefono());
		// Fin Contacto

		// Persona Fisica
		this.setIdPersonaFisica(null);
		this.setDni(p.getDni());
		this.setNombre(p.getNombre());
		this.setApellido(p.getApellido());
		this.setFechaNacimiento(p.getFechaNacimiento());
		this.setEstadoActivoPersonaFisica(true);
		// Fin Persona Fisica
		
		// Voluntario
		this.setEstadoActivoVoluntario(true);
		// Fin Voluntario
	}
	
	
	//Getters y Setters
	public Long getIdVoluntario() {
		return idVoluntario;
	}

	public void setIdVoluntario(Long idVoluntario) {
		this.idVoluntario = idVoluntario;
	}
	
	public boolean getEstadoActivoVoluntario() {
		return estadoActivoVoluntario;
	}

	public void setEstadoActivoVoluntario(boolean estadoActivoVoluntario) {
		this.estadoActivoVoluntario = estadoActivoVoluntario;
	}
	
	
	
	

	public VoluntarioPayload toPayload() {
		VoluntarioPayload p = new VoluntarioPayload();
		
		// Contacto
		p.setId(this.getId());
		p.setNombreDescripcion(this.getNombreDescripcion());
		p.setCuit(this.getCuit());
		p.setDomicilio(this.getDomicilio());
		p.setEmail(this.getEmail());
		p.setTelefono(this.getTelefono());
		// Fin Contacto
		
		// Persona Fisica
			// p.setIdPersonaFisica(null);
		p.setDni(this.getDni());
		p.setNombre(this.getNombre());
		p.setApellido(this.getApellido());
		p.setFechaNacimiento(this.getFechaNacimiento());
		//p.setEstadoActivoPersonaFisica(this.getEstadoActivoPersonaFisica());
		// Fin Persona Fisica
		
		// Voluntario
		//p.setEstadoActivoVoluntario(this.getEstadoActivoVoluntario());
		// Fin Voluntario
		
		return p;
	}
	
	public void modificar(VoluntarioPayload p) {
		
		// Contacto
		//this.setId(p.getId());
		//this.setEstadoActivoContacto(true);
		//this.setFechaAltaContacto(LocalDate.now());
		//this.setFechaBajaContacto(null);
		this.setNombreDescripcion(p.getNombreDescripcion());
		this.setCuit(p.getCuit());
		this.setDomicilio(p.getDomicilio());
		this.setEmail(p.getEmail());
		this.setTelefono(p.getTelefono());
		// Fin Contacto
		
		// Persona Fisica
		this.setDni(p.getDni());
		this.setNombre(p.getNombre());
		this.setApellido(p.getApellido());
		this.setFechaNacimiento(p.getFechaNacimiento());
		//this.setEstadoActivoVoluntario(true);
		// Fin Persona Fisica
		
		// Voluntario
		// Fin Voluntario
	}
	
}
