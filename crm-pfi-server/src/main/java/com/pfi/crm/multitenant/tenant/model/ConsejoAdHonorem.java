package com.pfi.crm.multitenant.tenant.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.ConsejoAdHonoremPayload;

@Entity
@Table(name="consejoAdHonorem")
public class ConsejoAdHonorem extends PersonaFisicaAbstract {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idConsejoAdHonorem;
	
	private String funcion;
	
	private boolean estadoActivoConsejoAdHonorem;
	
	
	//Constructores
	public ConsejoAdHonorem() {
		super();
		this.setFechaAltaContacto(LocalDate.now());
		this.setEstadoActivoPersonaFisica(true);
		this.setEstadoActivoConsejoAdHonorem(true);
	}
	
	
	public ConsejoAdHonorem(Long idConsejoAdHonorem, String funcion) {
		super();
		this.setFechaAltaContacto(LocalDate.now());
		this.setEstadoActivoPersonaFisica(true);
		this.setEstadoActivoConsejoAdHonorem(true);
		this.idConsejoAdHonorem = idConsejoAdHonorem;
		this.funcion = funcion;
	}
	
	public ConsejoAdHonorem(ConsejoAdHonoremPayload p) {
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
		
		// ConsejoAdHonorem
		this.setFuncion(p.getFuncion());
		this.setEstadoActivoConsejoAdHonorem(true);
		// Fin ConsejoAdHonorem
	}
	
	
	//Getters y Setters
	public Long getIdConsejoAdHonorem() {
		return idConsejoAdHonorem;
	}

	public void setIdConsejoAdHonorem(Long idConsejoAdHonorem) {
		this.idConsejoAdHonorem = idConsejoAdHonorem;
	}

	public String getFuncion() {
		return funcion;
	}

	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}

	public boolean isEstadoActivoConsejoAdHonorem() {
		return estadoActivoConsejoAdHonorem;
	}

	public void setEstadoActivoConsejoAdHonorem(boolean estadoActivoConsejoAdHonorem) {
		this.estadoActivoConsejoAdHonorem = estadoActivoConsejoAdHonorem;
	}
	
	
	
	
	
	public ConsejoAdHonoremPayload toPayload() {
		ConsejoAdHonoremPayload p = new ConsejoAdHonoremPayload();
		
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
		
		// ConsejoAdHonorem
		p.setFuncion(this.getFuncion());
		// Fin ConsejoAdHonorem
		
		return p;
	}
	
	
	public void modificar(ConsejoAdHonoremPayload p) {
		
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
		
		// ConsejoAdHonorem
		this.setFuncion(p.getFuncion());
		// Fin ConsejoAdHonorem
	}
}
