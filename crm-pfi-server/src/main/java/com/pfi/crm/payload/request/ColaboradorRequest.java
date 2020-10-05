package com.pfi.crm.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class ColaboradorRequest extends TrabajadorAbstractRequest{
	
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
