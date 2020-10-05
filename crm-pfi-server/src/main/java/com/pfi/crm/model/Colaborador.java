package com.pfi.crm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name ="colaborador")
public class Colaborador extends TrabajadorAbstract{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idColaborador;
	
	private String area;
	
	
	//Constructor
	public Colaborador() {
		super();
	}

	public Colaborador(Long idColaborador, String area) {
		super();
		this.idColaborador = idColaborador;
		this.area = area;
	}
	
	
	
	
	//Getters and Setters
	public Long getIdColaborador() {
		return idColaborador;
	}

	public void setIdColaborador(Long idColaborador) {
		this.idColaborador = idColaborador;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	
}
