package com.pfi.crm.multitenant.tenant.payload;

import javax.validation.constraints.Size;

public class TrabajadorAbstractPayload extends PersonaFisicaAbstractPayload{
	
	@Size(max = 140)
	private String datosBancarios;
	
	
	public TrabajadorAbstractPayload modificarTrabajadorPayload(TrabajadorAbstractPayload payload) {
		this.modificarPersonaFisica(payload);
		this.setDatosBancarios(payload.getDatosBancarios());
		return this;
	}
	
	//Esto esta de más.
	public TrabajadorAbstractPayload toTrabajadorAbstractPayload() {
		return this;
	}
	
	
	
	
	//Getters and Setters
	public String getDatosBancarios() {
		return datosBancarios;
	}

	public void setDatosBancarios(String datosBancarios) {
		this.datosBancarios = datosBancarios;
	}
	
}
