package com.pfi.crm.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TrabajadorAbstractRequest extends PersonaFisicaAbstractRequest{
	
	@Size(max = 140)
	private String datosBancarios;

	public String getDatosBancarios() {
		return datosBancarios;
	}

	public void setDatosBancarios(String datosBancarios) {
		this.datosBancarios = datosBancarios;
	}
	
}
