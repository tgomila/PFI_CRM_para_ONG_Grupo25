package com.pfi.crm.multitenant.tenant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.multitenant.tenant.model.ModuloVisibilidadPorRol;
import com.pfi.crm.multitenant.tenant.model.RoleName;

public interface ModuloVisibilidadPorRolRepository extends JpaRepository<ModuloVisibilidadPorRol, Long> {
	
	Optional<ModuloVisibilidadPorRol> findByRoleName(RoleName roleName);
}
