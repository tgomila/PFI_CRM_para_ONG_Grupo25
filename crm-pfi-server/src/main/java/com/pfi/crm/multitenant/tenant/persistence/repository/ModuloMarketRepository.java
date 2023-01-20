package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloMarket;

public interface ModuloMarketRepository extends JpaRepository<ModuloMarket, Long>{
	
	Optional<ModuloMarket> findByModuloEnum(ModuloEnum moduloEnum);
	
	Boolean existsByModuloEnum(ModuloEnum moduloEnum);
}
