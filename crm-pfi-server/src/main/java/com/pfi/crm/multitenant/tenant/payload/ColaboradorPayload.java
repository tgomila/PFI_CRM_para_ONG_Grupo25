package com.pfi.crm.multitenant.tenant.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ColaboradorPayload extends TrabajadorAbstractPayload{
	
	//private Long idColaborador;
	
	@NotBlank
	@Size(max = 140)
	private String area;

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	
}
