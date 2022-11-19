package com.pfi.crm.multitenant.tenant.payload;

public class ModuloItemPayload {
	
	private int order;
	private String name;
	private String path;
	private String iconName;
	private String tipoVisibilidad;
	
	public ModuloItemPayload() {
		super();
	}
	
	public ModuloItemPayload(int order, String name, String path, String iconName, String tipoVisibilidad) {
		super();
		this.order = order;
		this.name = name;
		this.path = path;
		this.iconName = iconName;
		this.tipoVisibilidad = tipoVisibilidad;
	}



	//Getters and Setters
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getIconName() {
		return iconName;
	}
	
	public void setIconName(String iconName) {
		this.iconName = iconName;
	}
	
	public String getTipoVisibilidad() {
		return tipoVisibilidad;
	}
	
	public void setTipoVisibilidad(String tipoVisibilidad) {
		this.tipoVisibilidad = tipoVisibilidad;
	}
	
	
}
