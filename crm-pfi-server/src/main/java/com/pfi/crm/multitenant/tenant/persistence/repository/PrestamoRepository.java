package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.multitenant.tenant.model.Prestamo;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
	
	List<Prestamo> findByPrestamista_Id(Long id);
	Boolean existsByPrestamista_Id(Long id);
	
	List<Prestamo> findByPrestatario_Id(Long id);
	Boolean existsByPrestatario_Id(Long id);
	
}
