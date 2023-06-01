package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDateTime;

public class FileInfoPayload {
	private String name;
	private String url;
	private LocalDateTime fechaCreacion;
	
	public FileInfoPayload(String name, String url, LocalDateTime fechaCreacion) {
		this.name = name;
		this.url = url;
		this.fechaCreacion = fechaCreacion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(LocalDateTime fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
}
