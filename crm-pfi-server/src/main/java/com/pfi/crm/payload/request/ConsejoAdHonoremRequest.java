package com.pfi.crm.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ConsejoAdHonoremRequest extends PersonaFisicaAbstractRequest{
	
	//private Long idConsejoAdHonorem;
	
	@NotBlank
    @Size(max = 140)
	private String funcion;
	
	
	
	
	public String getFuncion() {
		return funcion;
	}

	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}
}
