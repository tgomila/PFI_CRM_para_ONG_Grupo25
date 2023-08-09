package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class ProyectoPayload {
	
	private Long id;
	private String descripcion;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private Set<PersonaFisicaPayload> involucrados;
	
	public ProyectoPayload() {
		super();
		fechaInicio = LocalDate.now();
		involucrados = new HashSet<PersonaFisicaPayload>();
	}
	
	public void agregarInvolucrado(PersonaFisicaPayload p) {
		involucrados.add(p);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public LocalDate getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(LocalDate fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public LocalDate getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(LocalDate fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Set<PersonaFisicaPayload> getInvolucrados() {
		return involucrados;
	}

	public void setInvolucrados(Set<PersonaFisicaPayload> involucrados) {
		this.involucrados = involucrados;
	}
	
}
