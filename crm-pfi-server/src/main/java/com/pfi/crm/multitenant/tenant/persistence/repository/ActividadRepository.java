package com.pfi.crm.multitenant.tenant.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.Actividad;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
	
	//@Query("SELECT e FROM  Actividad e WHERE e.estadoActivoActividad=?1")
	//Optional<Actividad> findById(Long id);
	
	//@Query("SELECT e FROM  Actividad e WHERE e.estadoActivoActividad=?1")
	//List<Actividad> findAll();
	
	
	
	
}
