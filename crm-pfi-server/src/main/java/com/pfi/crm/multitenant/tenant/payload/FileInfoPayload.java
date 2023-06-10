package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileInfoPayload {
	private Long id;
	private String name;
	private String url;
	private LocalDateTime fechaCreacion;
	
	public FileInfoPayload(String name, String url, LocalDateTime fechaCreacion) {
		super();
		this.name = name;
		this.id = obtenerNumero(name);
		this.url = url;
		this.fechaCreacion = fechaCreacion;
	}
	
	/**
	 * "contacto_1.jpg" obtendrías "1"
	 * @param nombreArchivo
	 * @return
	 */
	private Long obtenerNumero(String nombreArchivo) {
		Pattern patron = Pattern.compile("_(\\d+)\\.");
		Matcher matcher = patron.matcher(nombreArchivo);
		
		if (matcher.find()) {
			return Long.parseLong(matcher.group(1));
		}
		
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.id = obtenerNumero(name);
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
