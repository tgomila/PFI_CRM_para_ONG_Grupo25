package com.pfi.crm.multitenant.tenant.payload.request;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.model.RoleName;

public class ModificarVisibilidadRequestPayload {
	private RoleName roleEnum;
	private ModuloEnum moduloEnum;
	private ModuloTipoVisibilidadEnum tipoVisibilidadEnum;
	
	public ModificarVisibilidadRequestPayload() {
		super();
	}

	public RoleName getRoleEnum() {
		return roleEnum;
	}
	
	public String getRoleName() {
		return roleEnum != null ? roleEnum.getName() : null;
	}

	public void setRoleEnum(RoleName roleEnum) {
		this.roleEnum = roleEnum;
	}

	public ModuloEnum getModuloEnum() {
		return moduloEnum;
	}
	
	public String getModuloName() {
		return moduloEnum != null ? moduloEnum.getName() : null;
	}

	public void setModuloEnum(ModuloEnum moduloEnum) {
		this.moduloEnum = moduloEnum;
	}

	public ModuloTipoVisibilidadEnum getTipoVisibilidadEnum() {
		return tipoVisibilidadEnum;
	}
	
	public String getTipoVisibilidadName() {
		return tipoVisibilidadEnum != null ? tipoVisibilidadEnum.getName() : null;
	}

	public void setTipoVisibilidadEnum(ModuloTipoVisibilidadEnum tipoVisibilidadEnum) {
		this.tipoVisibilidadEnum = tipoVisibilidadEnum;
	}

}
