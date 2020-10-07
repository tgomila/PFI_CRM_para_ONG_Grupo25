package com.pfi.crm.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class TrabajadorAbstract extends PersonaFisicaAbstract{
	
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
		
	


	//Getters and Setters
	public String getDatosBancarios() {
		return datosBancarios;
	}

	public void setDatosBancarios(String datosBancarios) {
		this.datosBancarios = datosBancarios;
	}
	
}
