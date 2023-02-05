package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    
	List<Factura> findByCliente_Id(Long id);
	List<Factura> findByCliente_Cuit(String cuit);
	List<Factura> findByCliente_Email(String email);
	
	Boolean existsByCliente_Id(Long id);
    Boolean existsByCliente_Cuit(String cuit);
    Boolean existsByCliente_Email(String email);
}