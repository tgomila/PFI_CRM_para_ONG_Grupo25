package com.pfi.crm.multitenant.tenant.payload;

public class MasterTenantRoleVisibilidadPayload {
	String moduloEnum;
	String moduloName;
	String tipoVisibilidad;
	String tipoVisibilidadName;
	
	public String getModuloEnum() {
		return moduloEnum;
	}
	public void setModuloEnum(String moduloEnum) {
		this.moduloEnum = moduloEnum;
	}
	public String getModuloName() {
		return moduloName;
	}
	public void setModuloName(String moduloName) {
		this.moduloName = moduloName;
	}
	public String getTipoVisibilidad() {
		return tipoVisibilidad;
	}
	public void setTipoVisibilidad(String tipoVisibilidad) {
		this.tipoVisibilidad = tipoVisibilidad;
	}
	public String getTipoVisibilidadName() {
		return tipoVisibilidadName;
	}
	public void setTipoVisibilidadName(String tipoVisibilidadName) {
		this.tipoVisibilidadName = tipoVisibilidadName;
	}

}
