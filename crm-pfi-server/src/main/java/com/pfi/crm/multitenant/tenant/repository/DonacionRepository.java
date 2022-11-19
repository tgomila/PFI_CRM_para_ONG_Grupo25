package com.pfi.crm.multitenant.tenant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.Donacion;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Long> {
    //ya esta
    
}