package com.pfi.crm.multitenant.tenant.payload;

public class ModuloItemPayload {
	
	private String name;
	private String path;
	private String iconName;
	private String tipoVisibilidad;
	
	public ModuloItemPayload() {
		super();
	}
	
	public ModuloItemPayload(String name, String path, String iconName, String tipoVisibilidad) {
		super();
		this.name = name;
		this.path = path;
		this.iconName = iconName;
		this.tipoVisibilidad = tipoVisibilidad;
	}



	//Getters and Setters
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
