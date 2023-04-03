package com.pfi.crm.multitenant.tenant.model;

public enum  DonacionTipo {
	INSUMO("Insumo"),
	DINERO("Dinero"),
	SERVICIO("Servicio"),
	OTRO("Otro");
	
	private final String name; //Esto es extra para saber su nombre sin mayúsculas, no es necesario en código.

	private DonacionTipo(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}