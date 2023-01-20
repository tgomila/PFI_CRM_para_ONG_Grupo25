package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDate;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;

public class ModuloMarketPayload {
	
	private Long id;
	
	private ModuloEnum moduloEnum;
	
	private boolean prueba7DiasUtilizada;
	
	private LocalDate fechaPrueba7DiasUtilizada;
	
	private LocalDate fechaMaximaSuscripcion;
	
	private boolean suscripcionActiva;
	
	//Constructores
	/*public ModuloMarketPayload() {
		super();
		id = null;
		moduloEnum = null;
		prueba7DiasUtilizada = true;
		fechaMaximaSuscripcion = null;
		suscripcionActiva = false;
	}

	public ModuloMarketPayload(Long id, ModuloEnum moduloEnum, boolean prueba7DiasUtilizada,
			LocalDate fechaMaximaSuscripcion, boolean suscripcionActiva) {
		super();
		this.id = id;
		this.moduloEnum = moduloEnum;
		this.prueba7DiasUtilizada = prueba7DiasUtilizada;
		this.fechaMaximaSuscripcion = fechaMaximaSuscripcion;
		this.suscripcionActiva = suscripcionActiva;
	}*/
	
	//Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ModuloEnum getModuloEnum() {
		return moduloEnum;
	}

	public void setModuloEnum(ModuloEnum moduloEnum) {
		this.moduloEnum = moduloEnum;
	}

	public boolean isPrueba7DiasUtilizada() {
		return prueba7DiasUtilizada;
	}

	public void setPrueba7DiasUtilizada(boolean prueba7DiasUtilizada) {
		this.prueba7DiasUtilizada = prueba7DiasUtilizada;
	}

	public LocalDate getFechaPrueba7DiasUtilizada() {
		return fechaPrueba7DiasUtilizada;
	}

	public void setFechaPrueba7DiasUtilizada(LocalDate fechaPrueba7DiasUtilizada) {
		this.fechaPrueba7DiasUtilizada = fechaPrueba7DiasUtilizada;
	}

	public LocalDate getFechaMaximaSuscripcion() {
		return fechaMaximaSuscripcion;
	}

	public void setFechaMaximaSuscripcion(LocalDate fechaMaximaSuscripcion) {
		this.fechaMaximaSuscripcion = fechaMaximaSuscripcion;
	}

	public boolean isSuscripcionActiva() {
		return suscripcionActiva;
	}

	public void setSuscripcionActiva(boolean suscripcionActiva) {
		this.suscripcionActiva = suscripcionActiva;
	}
	
	
}
