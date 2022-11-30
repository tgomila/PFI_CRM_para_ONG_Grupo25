package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.ModuloVisibilidadPorRol;
import com.pfi.crm.multitenant.tenant.model.RoleName;

@Repository
public interface ModuloVisibilidadPorRolRepository extends JpaRepository<ModuloVisibilidadPorRol, Long> {
	
	Optional<ModuloVisibilidadPorRol> findByRoleName(RoleName roleName);
}
