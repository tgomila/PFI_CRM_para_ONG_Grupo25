package com.pfi.crm.multitenant.tenant.payload;

public class ProfesionalPayload extends TrabajadorAbstractPayload{
	
	//private Long idProfesional;
	
	private String profesion;
	
	
	
	
	public String getProfesion() {
		return profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

}