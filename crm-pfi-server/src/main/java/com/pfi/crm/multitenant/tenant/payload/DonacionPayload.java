package com.pfi.crm.multitenant.tenant.payload;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pfi.crm.multitenant.tenant.model.DonacionTipo;

public class DonacionPayload {
	
	private Long id;
	private LocalDateTime fecha;
	private ContactoPayload donante;
	private DonacionTipo tipoDonacion;
	private String descripcion;
	private BigDecimal valorAproximadoDeLaDonacion;
	
	
	//Getters and Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getFecha() {
		return fecha;
	}
	public void setFecha(LocalDateTime fecha) {
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
	public BigDecimal getValorAproximadoDeLaDonacion() {
		return valorAproximadoDeLaDonacion;
	}
	public void setValorAproximadoDeLaDonacion(BigDecimal valorAproximadoDeLaDonacion) {
		this.valorAproximadoDeLaDonacion = valorAproximadoDeLaDonacion;
	}
	
	
}
