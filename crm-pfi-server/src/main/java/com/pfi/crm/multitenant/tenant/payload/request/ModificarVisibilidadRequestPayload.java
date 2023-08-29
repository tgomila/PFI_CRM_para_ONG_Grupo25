package com.pfi.crm.multitenant.tenant.payload.request;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.model.RoleName;

public class ModificarVisibilidadRequestPayload {
	private RoleName rol;
	private ModuloEnum moduloEnum;
	private ModuloTipoVisibilidadEnum tipoVisibilidad;
	
	
	public ModificarVisibilidadRequestPayload() {
		super();
	}
	

	public RoleName getRol() {
		return rol;
	}

	public void setRol(RoleName rol) {
		this.rol = rol;
	}

	public ModuloEnum getModuloEnum() {
		return moduloEnum;
	}

	public void setModuloEnum(ModuloEnum moduloEnum) {
		this.moduloEnum = moduloEnum;
	}

	public ModuloTipoVisibilidadEnum getTipoVisibilidad() {
		return tipoVisibilidad;
	}

	public void setTipoVisibilidad(ModuloTipoVisibilidadEnum tipoVisibilidad) {
		this.tipoVisibilidad = tipoVisibilidad;
	}

}
