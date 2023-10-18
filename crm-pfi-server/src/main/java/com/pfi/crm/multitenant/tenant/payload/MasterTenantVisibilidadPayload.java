package com.pfi.crm.multitenant.tenant.payload;

import java.util.ArrayList;
import java.util.List;

public class MasterTenantVisibilidadPayload {
	String tenantDbName;
	String tenantName;
	List<MasterTenantRoleVisibilidadesPayload> rolesModulos;
	
	public MasterTenantVisibilidadPayload() {
		super();
		rolesModulos = new ArrayList<MasterTenantRoleVisibilidadesPayload>();
	}
	
	public void addRole(MasterTenantRoleVisibilidadesPayload item) {
		if(rolesModulos == null) {
			rolesModulos = new ArrayList<MasterTenantRoleVisibilidadesPayload>();
		}
		rolesModulos.add(item);
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
	public List<MasterTenantRoleVisibilidadesPayload> getRolesModulos() {
		return rolesModulos;
	}
	public void setRolesModulos(List<MasterTenantRoleVisibilidadesPayload> rolesModulos) {
		this.rolesModulos = rolesModulos;
	}

}
