package com.pfi.crm.payload;

import java.time.LocalDate;

import com.pfi.crm.model.DonacionTipo;

public class DonacionPayload {
	
	private Long id;
	private LocalDate fecha;
	private ContactoPayload donante;
	private DonacionTipo tipoDonacion;
	private String descripcion;
	
	
	//Getters and Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public ContactoPayload getDonante() {
		return donante;
	}
	public void setDonante(ContactoPayload donante) {
		this.donante = donante;
	}
	public DonacionTipo getTipoDonacion() {
		return tipoDonacion;
	}
	public void setTipoDonacion(DonacionTipo tipoDonacion) {
		this.tipoDonacion = tipoDonacion;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
