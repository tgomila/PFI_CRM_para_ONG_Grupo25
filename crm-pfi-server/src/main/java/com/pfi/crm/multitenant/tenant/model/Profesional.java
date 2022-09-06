package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.ProfesionalPayload;


@Entity
@Table(name ="profesional")
public class Profesional extends TrabajadorAbstract{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProfesional;
	
	private String profesion;
	
	private boolean estadoActivoProfesional;
	
	//Constructores
	public Profesional() {
		super();
		estadoActivoProfesional = true;
	}

	public Profesional(Long idProfesional, String profesion) {
		super();
		this.idProfesional = idProfesional;
		this.profesion = profesion;
		this.estadoActivoProfesional = true;
	}
	
	public Profesional(ProfesionalPayload p) {
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
		
		// TrabajadorAbstract
		this.setDatosBancarios(p.getDatosBancarios());
		// Fin TrabajadorAbstract
		
		// Profesional
		this.setIdProfesional(null);
		this.setProfesion(p.getProfesion());
		this.setEstadoActivoProfesional(true);
		// Fin Profesional
	}
	
	
	//Getters and Setters
	public Long getIdProfesional() {
		return idProfesional;
	}

	public void setIdProfesional(Long idProfesional) {
		this.idProfesional = idProfesional;
	}

	public String getProfesion() {
		return profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

	public boolean getEstadoActivoProfesional() {
		return estadoActivoProfesional;
	}

	public void setEstadoActivoProfesional(boolean estadoActivoProfesional) {
		this.estadoActivoProfesional = estadoActivoProfesional;
	}
	
	
	
	
	
	public ProfesionalPayload toPayload() {
		ProfesionalPayload p = new ProfesionalPayload();
		
		// Contacto
		p.setId(this.getId());
		p.setNombreDescripcion(this.getNombreDescripcion());
		p.setCuit(this.getCuit());
		p.setDomicilio(this.getDomicilio());
		p.setEmail(this.getEmail());
		p.setTelefono(this.getTelefono());
		// Fin Contacto
		
		// Persona Juridica
			// p.setIdPersonaFisica(null);
		p.setDni(this.getDni());
		p.setNombre(this.getNombre());
		p.setApellido(this.getApellido());
		p.setFechaNacimiento(this.getFechaNacimiento());
		//p.setEstadoActivoPersonaFisica(this.getEstadoActivoPersonaFisica());
		// Fin Persona Juridica
		
		// TrabajadorAbstract
		p.setDatosBancarios(this.getDatosBancarios());
		// Fin TrabajadorAbstract
		
		// Profesional
		//this.setIdProfesional(null);
		p.setProfesion(this.getProfesion());
		//p.setEstadoActivoProfesional(p.getEstadoActivoProfesional());
		// Fin Profesional
		
		return p;
	}
	
	public void modificar(ProfesionalPayload p) {
		
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
		
		// TrabajadorAbstract
		this.setDatosBancarios(p.getDatosBancarios());
		// Fin TrabajadorAbstract
		
		// Profesional
		//this.setIdProfesional(null);
		this.setProfesion(p.getProfesion());
		//this.setEstadoActivoProfesional(p.getEstadoActivoProfesional());
		// Fin Profesional
	}
}
