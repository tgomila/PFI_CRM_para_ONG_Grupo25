package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.MappedSuperclass;

import com.pfi.crm.multitenant.tenant.payload.TrabajadorAbstractPayload;

@MappedSuperclass
public abstract class TrabajadorAbstract extends PersonaFisicaAbstract{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String datosBancarios;
	
	//Las facturas se buscan de manera aparte
	
	
	//Constructor
	public TrabajadorAbstract() {
		super();
	}
	
	
	public TrabajadorAbstract(String datosBancarios) {
		super();
		this.datosBancarios = datosBancarios;
	}
	
	public TrabajadorAbstractPayload toTrabajadorAbstractPayload() {
		TrabajadorAbstractPayload payload = new TrabajadorAbstractPayload();
		payload.modificarPersonaFisica(toPersonaFisicaPayload());
		payload.setDatosBancarios(this.getDatosBancarios());
		return payload;
	}
	
	public void modificarTrabajador(TrabajadorAbstractPayload payload) {
		this.modificarPersonaFisica(payload);
		this.setDatosBancarios(payload.getDatosBancarios());
	}
		
	


	//Getters and Setters
	public String getDatosBancarios() {
		return datosBancarios;
	}

	public void setDatosBancarios(String datosBancarios) {
		this.datosBancarios = datosBancarios;
	}
	
}
