package com.pfi.crm.multitenant.tenant.payload;

import java.util.HashSet;
import java.util.Set;

public class ModuloPayload {
	
	String rol;
	
	Set<ModuloItemPayload> items;
	
	
	public ModuloPayload() {
		super();
		items = new HashSet<ModuloItemPayload>();
	}

	public ModuloPayload(String rol, Set<ModuloItemPayload> items) {
		super();
		this.rol = rol;
		this.items = items;
	}

	public void agregarItem(ModuloItemPayload item) {
		items.add(item);
	}

	
	
	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public Set<ModuloItemPayload> getItems() {
		return items;
	}

	public void setItems(Set<ModuloItemPayload> items) {
		this.items = items;
	}
	
	
}
