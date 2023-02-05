package com.pfi.crm.multitenant.tenant.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.FacturaItem;

@Repository
public interface FacturaItemRepository extends JpaRepository<FacturaItem, Long> {
	
}
