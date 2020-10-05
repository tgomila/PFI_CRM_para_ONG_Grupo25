package com.pfi.crm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="profesional")
public class Profesional extends PersonaFisicaAbstract{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idProfesional;
	
	private String profesion;
	
	
	//Constructores
	public Profesional() {
		super();
	}

	public Profesional(Long idProfesional, String profesion) {
		super();
		this.idProfesional = idProfesional;
		this.profesion = profesion;
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
	
	
	
}
