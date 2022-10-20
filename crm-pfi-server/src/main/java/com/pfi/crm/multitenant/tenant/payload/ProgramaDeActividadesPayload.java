package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProgramaDeActividadesPayload {
	
	private Long id;
	private LocalDate fechaDesde;
	private LocalDate fechaHasta;
	private LocalDate fechaAltaPrograma;
	private LocalDate fechaBajaPrograma;
	private List<ActividadPayload> actividades;
	private String descripcion;
	
	public ProgramaDeActividadesPayload() {
		super();
		actividades = new ArrayList<ActividadPayload>();
	}
	
	public void agregarActividad(ActividadPayload actividad) {
		actividades.add(actividad);
	}
	
	
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(LocalDate fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public LocalDate getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(LocalDate fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public LocalDate getFechaAltaPrograma() {
		return fechaAltaPrograma;
	}

	public void setFechaAltaPrograma(LocalDate fechaAltaPrograma) {
		this.fechaAltaPrograma = fechaAltaPrograma;
	}

	public LocalDate getFechaBajaPrograma() {
		return fechaBajaPrograma;
	}

	public void setFechaBajaPrograma(LocalDate fechaBajaPrograma) {
		this.fechaBajaPrograma = fechaBajaPrograma;
	}

	public List<ActividadPayload> getActividades() {
		return actividades;
	}

	public void setActividades(List<ActividadPayload> actividades) {
		this.actividades = actividades;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
