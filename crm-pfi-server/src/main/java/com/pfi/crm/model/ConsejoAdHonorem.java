package com.pfi.crm.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="consejoAdHonorem")
public class ConsejoAdHonorem extends ContactoAbstract {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idConsejoAdHonorem;
	
	private String funcion;
	
	
	//Constructores
	public ConsejoAdHonorem() {
		super();
	}	
	
	
	public ConsejoAdHonorem(Long idConsejoAdHonorem, String funcion) {
		super();
		this.idConsejoAdHonorem = idConsejoAdHonorem;
		this.funcion = funcion;
	}
	
	
	//Getters y Setters
	public Long getIdConsejoAdHonorem() {
		return idConsejoAdHonorem;
	}

	public void setIdConsejoAdHonorem(Long idConsejoAdHonorem) {
		this.idConsejoAdHonorem = idConsejoAdHonorem;
	}

	public String getFuncion() {
		return funcion;
	}

	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}
	
	
}
