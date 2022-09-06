package com.pfi.crm.multitenant.tenant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.multitenant.tenant.model.Factura;

public interface FacturaRepository  extends JpaRepository<Factura, Long> {
    
	Optional<Factura> findByCliente_Id(Long id);
	Optional<Factura> findByCliente_Cuit(String cuit);
	Optional<Factura> findByCliente_Email(String email);
	
	Boolean existsByCliente_Id(Long id);
    Boolean existsByCliente_Cuit(String cuit);
    Boolean existsByCliente_Email(String email);
}