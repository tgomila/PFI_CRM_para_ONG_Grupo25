package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDate;

public class InsumoPayload {
	
	private Long id;
	private LocalDate fechaInsumo;
	private int cantidad;
	private ProductoPayload producto;
	private ContactoPayload responsableInsumo;
	
	public InsumoPayload() {
		super();
		fechaInsumo = LocalDate.now();
		cantidad = 0;
		producto = null;
		responsableInsumo = null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaInsumo() {
		return fechaInsumo;
	}

	public void setFechaInsumo(LocalDate fechaInsumo) {
		this.fechaInsumo = fechaInsumo;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public ProductoPayload getProducto() {
		return producto;
	}

	public void setProducto(ProductoPayload producto) {
		this.producto = producto;
	}

	public ContactoPayload getResponsableInsumo() {
		return responsableInsumo;
	}

	public void setResponsableInsumo(ContactoPayload responsableInsumo) {
		this.responsableInsumo = responsableInsumo;
	}
	
	
}
