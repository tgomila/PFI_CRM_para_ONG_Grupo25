package com.pfi.crm.model;

import javax.persistence.CascadeType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OrderBy;

@MappedSuperclass
public abstract class TrabajadorAbstract {
	
	private String datosBancarios;
	
	//Mejorar
	
	@ManyToOne(cascade = CascadeType.ALL)
	@OrderBy("nombreDescripcion ASC")
	private Contacto contacto;
	
}
