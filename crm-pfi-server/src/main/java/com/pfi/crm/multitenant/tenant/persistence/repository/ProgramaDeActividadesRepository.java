package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pfi.crm.multitenant.tenant.model.ProgramaDeActividades;

public interface ProgramaDeActividadesRepository extends JpaRepository<ProgramaDeActividades, Long> {
	
	@Query("SELECT e FROM  ProgramaDeActividades e WHERE e.estadoActivoPrograma=?1")
	Optional<ProgramaDeActividades> findById();
	
	@Query("SELECT e FROM  ProgramaDeActividades e WHERE e.estadoActivoPrograma=?1")
	List<ProgramaDeActividades> findAll();
	
}
