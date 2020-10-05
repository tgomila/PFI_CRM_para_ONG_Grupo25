package com.pfi.crm.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EmpleadoRequest extends TrabajadorAbstractRequest{
	
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
