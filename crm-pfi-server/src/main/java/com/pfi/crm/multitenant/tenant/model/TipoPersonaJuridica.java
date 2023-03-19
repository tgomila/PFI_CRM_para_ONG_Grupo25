package com.pfi.crm.multitenant.tenant.model;

public enum TipoPersonaJuridica {
	//id, name
	OSC("OSC"),
	EMPRESA("Empresa"),
	INSTITUCION("Institución"),
	ORGANISMO_DEL_ESTADO("Organismo del estado");
	
	private final String name;
	
	private TipoPersonaJuridica(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	
}
