package com.pfi.crm.multitenant.tenant.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pfi.crm.multitenant.tenant.model.Contacto;

public interface ContactoRepository extends JpaRepository<Contacto, Long> {
	
	//Los QUERY's es para ver si el contacto esta activo, "e" es entity
	
	
	List<Contacto> findAllByEstadoActivoContactoTrue();
	
	@Query("SELECT e FROM  Contacto e WHERE e.estadoActivoContacto=?1")
	Optional<Contacto> findByEmail(String email);
	
	@Query("SELECT e FROM  Contacto e WHERE e.estadoActivoContacto=?1")
	Optional<Contacto> findByNombreDescripcion(String nombreDescripcion);
	
	@Query("SELECT e FROM  Contacto e WHERE e.estadoActivoContacto=?1")
	Boolean existsByNombreDescripcion(String username);
	
	@Query("SELECT e FROM  Contacto e WHERE e.estadoActivoContacto=?1")
	Boolean existsByEmail(String email);
	
	@Query("SELECT e FROM  Contacto e WHERE e.estadoActivoContacto=?1")
	Boolean existsByCuit(String cuit);
	
}
