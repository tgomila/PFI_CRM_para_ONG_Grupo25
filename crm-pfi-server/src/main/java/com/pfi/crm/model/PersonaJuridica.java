package com.pfi.crm.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name ="personaJuridica")
public class PersonaJuridica extends ContactoAbstract{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPersonaJuridica;
	
	private String internoTelefono;
	
	@Enumerated(EnumType.STRING)
	private TipoPersonaJuridica tipoPersonaJuridica;
	
	
	
	
}
