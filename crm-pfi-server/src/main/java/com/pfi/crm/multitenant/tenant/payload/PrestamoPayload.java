package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;

public class PrestamoPayload {
	
	private Long id;
	private String descripcion;
	private int cantidad;
	private LocalDate fechaPrestamoInicio;
	private LocalDate fechaPrestamoFin;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@OrderBy("nombreDescripcion ASC")
	private ContactoPayload prestamista; //quien tiene el producto
	
	@ManyToOne(cascade = CascadeType.ALL)
	@OrderBy("nombreDescripcion ASC")
	private ContactoPayload prestatario; //quien tiene el producto
	
	public PrestamoPayload() {
		super();
		cantidad = 0;
		prestamista = null;
		fechaPrestamoInicio = LocalDate.now();
		prestatario = new ContactoPayload();
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

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public LocalDate getFechaPrestamoInicio() {
		return fechaPrestamoInicio;
	}

	public void setFechaPrestamoInicio(LocalDate fechaPrestamoInicio) {
		this.fechaPrestamoInicio = fechaPrestamoInicio;
	}

	public LocalDate getFechaPrestamoFin() {
		return fechaPrestamoFin;
	}

	public void setFechaPrestamoFin(LocalDate fechaPrestamoFin) {
		this.fechaPrestamoFin = fechaPrestamoFin;
	}

	public ContactoPayload getPrestamista() {
		return prestamista;
	}

	public void setPrestamista(ContactoPayload prestamista) {
		this.prestamista = prestamista;
	}

	public ContactoPayload getPrestatario() {
		return prestatario;
	}

	public void setPrestatario(ContactoPayload prestatario) {
		this.prestatario = prestatario;
	}
	
}
