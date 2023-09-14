package com.pfi.crm.multitenant.tenant.payload;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ModuloPayload {
	
	String rol;
	
	List<ModuloItemPayload> items;
	
	
	public ModuloPayload() {
		super();
		items = new ArrayList<ModuloItemPayload>();
	}

	public ModuloPayload(String rol, List<ModuloItemPayload> items) {
		super();
		this.rol = rol;
		this.items = items;
	}

	public void agregarItem(ModuloItemPayload item) {
		items.add(item);
	}
	
	class myItemComparator implements Comparator<ModuloItemPayload>
	{
	   
		public int compare(ModuloItemPayload s1, ModuloItemPayload s2)
		{
			return s1.getOrder()-s2.getOrder();
		}
	}

	
	
	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public List<ModuloItemPayload> getItems() {
		return items;
	}

	public void setItems(List<ModuloItemPayload> items) {
		this.items = items;
	}
	
	
}
