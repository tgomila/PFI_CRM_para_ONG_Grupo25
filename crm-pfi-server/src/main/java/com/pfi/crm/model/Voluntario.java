package com.pfi.crm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="voluntario")
public class Voluntario extends ContactoAbstract {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idVoluntario;
	
	//Constructores
	public Voluntario() {
		super();
	}

	public Voluntario(Long idVoluntario) {
		super();
		this.idVoluntario = idVoluntario;
	}
	
	
	//Getters y Setters
	public Long getIdVoluntario() {
		return idVoluntario;
	}

	public void setIdVoluntario(Long idVoluntario) {
		this.idVoluntario = idVoluntario;
	}
	
}
