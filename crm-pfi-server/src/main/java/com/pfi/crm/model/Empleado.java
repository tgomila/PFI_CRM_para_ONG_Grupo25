package com.pfi.crm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="empleado")
public class Empleado extends PersonaFisicaAbstract{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idEmpleado;
	
	private String funcion;
	private String descripcion;
	
	
	//Constructor
	public Empleado() {
		super();
	}
	
	public Empleado(Long idEmpleado, String funcion, String descripcion) {
		super();
		this.idEmpleado = idEmpleado;
		this.funcion = funcion;
		this.descripcion = descripcion;
	}
	
	
	
	
	//Getters and Setters
	public Long getIdEmpleado() {
		return idEmpleado;
	}

	public void setIdEmpleado(Long idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public String getFuncion() {
		return funcion;
	}

	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
}
