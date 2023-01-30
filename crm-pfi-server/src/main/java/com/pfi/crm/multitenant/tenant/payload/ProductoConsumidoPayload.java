package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDate;

public class ProductoConsumidoPayload {
	
	private Long id;
	private LocalDate fechaProductoConsumido;
	private int cantidad;
	private ProductoPayload producto;
	private ContactoPayload responsableConsumicion;
	
	public ProductoConsumidoPayload() {
		super();
		fechaProductoConsumido = LocalDate.now();
		cantidad = 0;
		producto = null;
		responsableConsumicion = null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDate getFechaProductoConsumido() {
		return fechaProductoConsumido;
	}

	public void setFechaProductoConsumido(LocalDate fechaProductoConsumido) {
		this.fechaProductoConsumido = fechaProductoConsumido;
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

	public ContactoPayload getResponsableConsumicion() {
		return responsableConsumicion;
	}

	public void setResponsableConsumicion(ContactoPayload responsableConsumicion) {
		this.responsableConsumicion = responsableConsumicion;
	}
	
	
}
