package com.pfi.crm.multitenant.tenant.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.multitenant.tenant.model.Insumo;

public interface InsumoRepository extends JpaRepository<Insumo, Long> {
	
	//@Query("SELECT e FROM  Insumo e WHERE e.estadoActivo=?1")
	//Optional<Insumo> findById(Long id);
	
	//@Query("SELECT e FROM  Insumo e WHERE e.estadoActivo=?1")
	//List<Insumo> findAll();
	
}