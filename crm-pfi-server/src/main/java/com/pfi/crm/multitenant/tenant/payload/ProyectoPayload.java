package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProyectoPayload {
	
	private Long id;
	private String descripcion;
	private LocalDate fechaInicio;
	private LocalDate fechaFin;
	private List<PersonaFisicaPayload> involucrados;
	
	public ProyectoPayload() {
		super();
		fechaInicio = LocalDate.now();
		involucrados = new ArrayList<>();
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

	public List<PersonaFisicaPayload> getInvolucrados() {
		return involucrados;
	}

	public void setInvolucrados(List<PersonaFisicaPayload> involucrados) {
		this.involucrados = involucrados;
	}
	
}
