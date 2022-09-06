package com.pfi.crm.multitenant.tenant.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.multitenant.tenant.model.Donacion;

public interface DonacionRepository extends JpaRepository<Donacion, Long> {
    //ya esta
    
}