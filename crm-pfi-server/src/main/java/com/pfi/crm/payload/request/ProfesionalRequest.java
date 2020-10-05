package com.pfi.crm.payload.request;

public class ProfesionalRequest extends TrabajadorAbstractRequest{
	
	//private Long idProfesional;
	
	private String profesion;
	
	
	
	
	public String getProfesion() {
		return profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

}
