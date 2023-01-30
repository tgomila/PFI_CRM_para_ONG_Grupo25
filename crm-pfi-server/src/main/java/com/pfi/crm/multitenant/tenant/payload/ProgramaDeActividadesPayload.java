package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ProgramaDeActividadesPayload {
	
	private Long id;
	
	//Datos simbólicos solo para mostrar en pantalla, no se guardan.
	private LocalDateTime fechaDesde;
	private LocalDateTime fechaHasta;
	
	private List<ActividadPayload> actividades;
	private String descripcion;
	
	public ProgramaDeActividadesPayload() {
		super();
		actividades = new ArrayList<ActividadPayload>();
	}
	
	public void agregarActividad(ActividadPayload actividad) {
		actividades.add(actividad);
		ordenarActividades();
	}
	
	private void ordenarActividades() {
		if(actividades == null)
			actividades = new ArrayList<ActividadPayload>();
		if(actividades.isEmpty())
			return;
		
		actividades = actividades
                .stream()
                .sorted(Comparator.comparing(ActividadPayload::getFechaHoraDesde))
                .collect(Collectors.toList());
	}
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(LocalDateTime fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public LocalDateTime getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(LocalDateTime fechaHasta) {
		this.fechaHasta = fechaHasta;
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
