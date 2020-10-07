package com.pfi.crm.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.payload.ContactoPayload;
import com.pfi.crm.payload.PersonaJuridicaPayload;

@Entity
@Table(name ="personaJuridica")
public class PersonaJuridica extends ContactoAbstract{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPersonaJuridica;
	
	private String internoTelefono;
	
	@Enumerated(EnumType.STRING)
	private TipoPersonaJuridica tipoPersonaJuridica;
	
	private boolean estadoActivoPersonaJuridica;
	
	
	
	//Constructores
	public PersonaJuridica() {
		super();
	}

	public PersonaJuridica(Long idPersonaJuridica, String internoTelefono, TipoPersonaJuridica tipoPersonaJuridica) {
		super();
		this.idPersonaJuridica = idPersonaJuridica;
		this.internoTelefono = internoTelefono;
		this.tipoPersonaJuridica = tipoPersonaJuridica;
		this.estadoActivoPersonaJuridica = true;
	}
	
	public PersonaJuridica(PersonaJuridicaPayload p) {
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
		
		// Persona Juridica
		this.setIdPersonaJuridica(null);
		this.setInternoTelefono(p.getInternoTelefono());
		this.setTipoPersonaJuridica(p.getTipoPersonaJuridica());
		this.estadoActivoPersonaJuridica = true;
		// Fin Persona Juridica
	}
	
	
	
	//Getters and Setters
	public Long getIdPersonaJuridica() {
		return idPersonaJuridica;
	}

	public void setIdPersonaJuridica(Long idPersonaJuridica) {
		this.idPersonaJuridica = idPersonaJuridica;
	}

	public String getInternoTelefono() {
		return internoTelefono;
	}

	public void setInternoTelefono(String internoTelefono) {
		this.internoTelefono = internoTelefono;
	}

	public TipoPersonaJuridica getTipoPersonaJuridica() {
		return tipoPersonaJuridica;
	}

	public void setTipoPersonaJuridica(TipoPersonaJuridica tipoPersonaJuridica) {
		this.tipoPersonaJuridica = tipoPersonaJuridica;
	}	
	
	public boolean getEstadoActivoPersonaJuridica() {
		return estadoActivoPersonaJuridica;
	}

	public void setEstadoActivoPersonaJuridica(boolean estadoActivoPersonaJuridica) {
		this.estadoActivoPersonaJuridica = estadoActivoPersonaJuridica;
	}
	
	
	

	public PersonaJuridicaPayload toPayload() {

		PersonaJuridicaPayload p = new PersonaJuridicaPayload();

		// Contacto
		p.setId(this.getId());
		p.setNombreDescripcion(this.getNombreDescripcion());
		p.setCuit(this.getCuit());
		p.setDomicilio(this.getDomicilio());
		p.setEmail(this.getEmail());
		p.setTelefono(this.getTelefono());
		// Fin Contacto
		
		// Persona Juridica
			//idPersonaJuridica
		p.setTipoPersonaJuridica(this.getTipoPersonaJuridica());		
		p.setInternoTelefono(this.getInternoTelefono());
		// Fin Persona Juridica
		

		return p;
	}
	
	public void modificar(PersonaJuridicaPayload p) {

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
		
		// Persona Juridica
		this.setIdPersonaJuridica(null);
		this.setInternoTelefono(p.getInternoTelefono());
		this.setTipoPersonaJuridica(p.getTipoPersonaJuridica());
		//this.estadoActivoPersonaJuridica = true;
		// Fin Persona Juridica
	}
}
