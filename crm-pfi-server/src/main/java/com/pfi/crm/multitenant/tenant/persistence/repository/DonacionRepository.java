package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.Donacion;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Long> {
	
	boolean existsByDonante_Id(Long id);
	
	List<Donacion> findByDonante_Id(Long id);
	
}