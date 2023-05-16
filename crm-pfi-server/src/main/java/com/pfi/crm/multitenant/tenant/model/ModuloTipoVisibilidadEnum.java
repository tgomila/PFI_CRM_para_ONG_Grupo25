package com.pfi.crm.multitenant.tenant.model;

public enum ModuloTipoVisibilidadEnum {
	//CRUD
	//Números priority poseen acceso a números igual o menores.
	EDITAR(3, "Editar"),
	SOLO_VISTA(2, "Solo vista"),
	NO_VISTA(1, "No vista"),
	SIN_SUSCRIPCION(0, "Sin suscripción");
	
	private final int priority;
	private final String name; //Es solo para saber su nombre sin minúsculas, no es necesario en el código.

	private ModuloTipoVisibilidadEnum(int priority, String name) {
		this.priority = priority;
		this.name = name;
	}
	
	/**
	 * Chequea si el usuario tiene el requisito necesario para acceder.
	 * El enum actual "ModuloTipoVisibilidadEnum.SOLO_VISTA.poseeAcceso(...)" tiene que ser del usuario, el input el requerido
	 * @param visibilidadRequerida visibilidad requerida mínima aquí, ejemplo "ModuloTipoVisibilidadEnum.EDITAR"
	 * @return true/false. Para el ejemplo dicho anteriormente usuario tien solo vista, requisito editar, entonces devuelve false.
	 */
	public boolean poseePermiso(ModuloTipoVisibilidadEnum visibilidadRequerida) {
		return priority >= visibilidadRequerida.getPriority();//true/false
	}
	
	public boolean esMejorQue(ModuloTipoVisibilidadEnum comparar) {
		if(comparar == null || this.getPriority() >= comparar.getPriority())
			return true;
		else
			return false;
	}
	
	public ModuloTipoVisibilidadEnum obtenerMejor(ModuloTipoVisibilidadEnum comparar) {
		if(comparar == null || this.getPriority() >= comparar.getPriority())
			return this;
		else
			return comparar;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public String getName() {
		return name;
	}
}
