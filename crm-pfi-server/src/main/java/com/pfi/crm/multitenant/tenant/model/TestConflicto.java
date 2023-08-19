package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class TestConflicto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "test_conflicto_seq")
	//@SequenceGenerator(name = "test_conflicto_seq", sequenceName = "test_conflicto_sequence", allocationSize = 1)
	Long id;
	
	String nombre;
	
	
}
