package com.pfi.crm.multitenant.tenant.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EmpleadoPayload extends TrabajadorAbstractPayload{
	
	//private Long idEmpleado;
	
	@NotBlank
	@Size(max = 140)
	private String funcion;
	
	@Size(max = 140)
	private String descripcion;

	public String getFuncion() {
		return funcion;
	}

	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
}
