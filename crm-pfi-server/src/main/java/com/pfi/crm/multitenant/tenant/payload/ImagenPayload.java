package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDateTime;

public class ImagenPayload {
	private Long id;
	private String tipo;
	private LocalDateTime fechaDeCreacion;
	
	public ImagenPayload() {
		super();
	}

	public ImagenPayload(Long id, String tipo, LocalDateTime fechaDeCreacion) {
		super();
		this.id = id;
		this.tipo = tipo;
		this.fechaDeCreacion = fechaDeCreacion;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public LocalDateTime getFechaDeCreacion() {
		return fechaDeCreacion;
	}

	public void setFechaDeCreacion(LocalDateTime fechaDeCreacion) {
		this.fechaDeCreacion = fechaDeCreacion;
	}
}
