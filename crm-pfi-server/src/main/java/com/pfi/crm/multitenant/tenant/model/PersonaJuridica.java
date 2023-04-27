package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.PersonaJuridicaPayload;

@Entity
@Table(name ="personaJuridica")
public class PersonaJuridica extends ContactoAbstract{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7847670806500841485L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPersonaJuridica;
	
	private String internoTelefono;
	
	@Enumerated(EnumType.STRING)
	private TipoPersonaJuridica tipoPersonaJuridica;
	
	private boolean estadoActivoPersonaJuridica = true;
	
	
	
	//Constructores
	public PersonaJuridica() {
		super();
		this.setIdPersonaJuridica(null);
		this.setEstadoActivoPersonaJuridica(true);
	}
	
	public PersonaJuridica(PersonaJuridicaPayload payload) {
		super();
		this.modificar(payload);
		this.setIdPersonaJuridica(null);
		this.setEstadoActivoPersonaJuridica(true);
	}
	
	public void modificar(PersonaJuridicaPayload payload) {
		this.modificarContacto(payload);
		this.setInternoTelefono(payload.getInternoTelefono());
		this.setTipoPersonaJuridica(payload.getTipoPersonaJuridica());
	}
	
	public PersonaJuridicaPayload toPayload() {
		PersonaJuridicaPayload payload = new PersonaJuridicaPayload();
		payload.modificarContacto(this.toContactoPayload());
		payload.setTipoPersonaJuridica(this.getTipoPersonaJuridica());		
		payload.setInternoTelefono(this.getInternoTelefono());
		return payload;
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
}
