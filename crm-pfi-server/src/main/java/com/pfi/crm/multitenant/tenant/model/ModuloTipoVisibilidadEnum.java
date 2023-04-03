package com.pfi.crm.multitenant.tenant.model;

public enum ModuloTipoVisibilidadEnum {
	//CRUD
	EDITAR("Editar"),
	SOLO_VISTA("Solo vista"),
	NO_VISTA("No vista"),
	SIN_SUSCRIPCION("Sin suscripción");
	
	private final String name; //Es solo para saber su nombre sin minúsculas, no es necesario en el código.

	private ModuloTipoVisibilidadEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	
}
