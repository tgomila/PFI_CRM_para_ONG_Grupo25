package com.pfi.crm.multitenant.tenant.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.multitenant.tenant.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
	
	//@Query("SELECT e FROM  Producto e WHERE e.estadoActivo=?1")
	//Optional<Producto> findById(Long id);
	
	//@Query("SELECT e FROM  Producto e WHERE e.estadoActivo=?1")
	//List<Producto> findAll();
	
}