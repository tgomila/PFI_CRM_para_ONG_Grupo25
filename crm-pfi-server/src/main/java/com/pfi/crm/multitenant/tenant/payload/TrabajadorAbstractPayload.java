package com.pfi.crm.multitenant.tenant.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TrabajadorAbstractPayload extends PersonaFisicaAbstractPayload{
	
	@Size(max = 140)
	private String datosBancarios;

	public String getDatosBancarios() {
		return datosBancarios;
	}

	public void setDatosBancarios(String datosBancarios) {
		this.datosBancarios = datosBancarios;
	}
	
}
