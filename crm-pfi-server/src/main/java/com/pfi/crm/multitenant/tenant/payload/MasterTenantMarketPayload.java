package com.pfi.crm.multitenant.tenant.payload;

import java.time.LocalDateTime;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;

public class MasterTenantMarketPayload {
	private Integer tenantId;
	private String tenantDbName;
	private String tenantName;
	
	private String modulo;
	private String moduloName;
	
	private boolean paidModule;
	private boolean prueba7DiasUtilizada;
	
	private LocalDateTime fechaPrueba7DiasUtilizada;

	private LocalDateTime fechaInicioSuscripcion;
	
	private LocalDateTime fechaMaximaSuscripcion;
	
	private boolean suscripcionActiva;
	
	//Getters and Setters

	public String getModulo() {
		return modulo;
	}

	public Integer getTenantId() {
		return tenantId;
	}

	public void setTenantId(Integer tenantId) {
		this.tenantId = tenantId;
	}

	public String getTenantDbName() {
		return tenantDbName;
	}

	public void setTenantDbName(String tenantDbName) {
		this.tenantDbName = tenantDbName;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public boolean isPaidModule() {
		return paidModule;
	}

	public void setPaidModule(boolean paidModule) {
		this.paidModule = paidModule;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	
	public void setModulo(ModuloEnum moduloEnum) {
		modulo = moduloEnum.toString();
		moduloName = moduloEnum.getName();
	}

	public String getModuloName() {
		return moduloName;
	}

	public void setModuloName(String moduloName) {
		this.moduloName = moduloName;
	}

	public boolean isPrueba7DiasUtilizada() {
		return prueba7DiasUtilizada;
	}

	public void setPrueba7DiasUtilizada(boolean prueba7DiasUtilizada) {
		this.prueba7DiasUtilizada = prueba7DiasUtilizada;
	}

	public LocalDateTime getFechaPrueba7DiasUtilizada() {
		return fechaPrueba7DiasUtilizada;
	}

	public void setFechaPrueba7DiasUtilizada(LocalDateTime fechaPrueba7DiasUtilizada) {
		this.fechaPrueba7DiasUtilizada = fechaPrueba7DiasUtilizada;
	}

	public LocalDateTime getFechaInicioSuscripcion() {
		return fechaInicioSuscripcion;
	}

	public void setFechaInicioSuscripcion(LocalDateTime fechaInicioSuscripcion) {
		this.fechaInicioSuscripcion = fechaInicioSuscripcion;
	}

	public LocalDateTime getFechaMaximaSuscripcion() {
		return fechaMaximaSuscripcion;
	}

	public void setFechaMaximaSuscripcion(LocalDateTime fechaMaximaSuscripcion) {
		this.fechaMaximaSuscripcion = fechaMaximaSuscripcion;
	}

	public boolean isSuscripcionActiva() {
		return suscripcionActiva;
	}

	public void setSuscripcionActiva(boolean suscripcionActiva) {
		this.suscripcionActiva = suscripcionActiva;
	}
	
	
}
