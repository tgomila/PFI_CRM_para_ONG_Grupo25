package com.pfi.crm.multitenant.tenant.payload;

import java.util.ArrayList;
import java.util.List;

public class MasterTenantRoleVisibilidadesPayload {
	String role;
	String roleName;
	List<MasterTenantRoleVisibilidadPayload> items;
	
	public MasterTenantRoleVisibilidadesPayload() {
		super();
		items = new ArrayList<MasterTenantRoleVisibilidadPayload>();
	}
	
	public void addItem(MasterTenantRoleVisibilidadPayload item) {
		if(items == null) {
			items = new ArrayList<MasterTenantRoleVisibilidadPayload>();
		}
		items.add(item);
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public List<MasterTenantRoleVisibilidadPayload> getItems() {
		return items;
	}

	public void setItems(List<MasterTenantRoleVisibilidadPayload> items) {
		this.items = items;
	}
}
