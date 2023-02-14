package com.pfi.crm.multitenant.tenant.payload;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;

public class ModuloItemPayload {
	
	private int order;
	private String moduloEnum;
	private String name;
	private String path;
	private String iconName;
	private String tipoVisibilidad;
	private double priceOneMonth;
	private double priceOneYear;
	
	public ModuloItemPayload() {
		super();
	}
	
	public ModuloItemPayload(ModuloEnum moduloEnum, ModuloTipoVisibilidadEnum tipoVisibilidad) {
		super();
		this.order = moduloEnum.getOrder();
		this.moduloEnum = moduloEnum.toString();
		this.name = moduloEnum.getName();
		this.path = moduloEnum.getPath();
		this.iconName = moduloEnum.getIconName();
		this.tipoVisibilidad = tipoVisibilidad.toString();
		this.priceOneMonth = moduloEnum.getPriceOneMonth();
		this.priceOneYear = moduloEnum.getPriceOneYear();
	}
	
	public ModuloItemPayload(int order, String moduloEnum, String name, String path, String iconName,
			String tipoVisibilidad, double priceOneMonth, double priceOneYear) {
		super();
		this.order = order;
		this.moduloEnum = moduloEnum;
		this.name = name;
		this.path = path;
		this.iconName = iconName;
		this.tipoVisibilidad = tipoVisibilidad;
		this.priceOneMonth = priceOneMonth;
		this.priceOneYear = priceOneYear;
	}
	

	//Getters and Setters
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public String getModuloEnum() {
		return moduloEnum;
	}

	public void setModuloEnum(String moduloEnum) {
		this.moduloEnum = moduloEnum;
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

	public double getPriceOneMonth() {
		return priceOneMonth;
	}

	public void setPriceOneMonth(double priceOneMonth) {
		this.priceOneMonth = priceOneMonth;
	}

	public double getPriceOneYear() {
		return priceOneYear;
	}

	public void setPriceOneYear(double priceOneYear) {
		this.priceOneYear = priceOneYear;
	}
	
	
}
