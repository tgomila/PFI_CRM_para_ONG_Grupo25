package com.pfi.crm.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="personaJuridica")
public class PersonaJuridica extends ContactoAbstract{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPersonaJuridica;
	
	private String internoTelefono;
	
	@Enumerated(EnumType.STRING)
	private TipoPersonaJuridica tipoPersonaJuridica;
	
	
	
	//Constructores
	public PersonaJuridica() {
		super();
	}

	public PersonaJuridica(Long idPersonaJuridica, String internoTelefono, TipoPersonaJuridica tipoPersonaJuridica) {
		super();
		this.idPersonaJuridica = idPersonaJuridica;
		this.internoTelefono = internoTelefono;
		this.tipoPersonaJuridica = tipoPersonaJuridica;
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
	
	
	
	
}
