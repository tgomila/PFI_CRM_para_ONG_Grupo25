package com.pfi.crm.multitenant.tenant.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.multitenant.tenant.model.Prestamo;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
	
}
