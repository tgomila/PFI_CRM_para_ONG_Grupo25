package com.pfi.crm.multitenant.mastertenant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.mastertenant.entity.MasterTenant;

@Repository
public interface MasterTenantRepository extends JpaRepository<MasterTenant, Integer> {
	
	MasterTenant findByTenantClientId(Integer id);
}
