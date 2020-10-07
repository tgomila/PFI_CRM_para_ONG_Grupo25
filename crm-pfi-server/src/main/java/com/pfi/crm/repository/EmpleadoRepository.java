package com.pfi.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    
	//Probar
	//@Query("SELECT e FROM  Empleado e WHERE e.Contacto.estadoActivoContacto=?1")
	Optional<Empleado> findByContacto_Id(Long id);
	
	boolean existsByContacto_Id(Long id);
	
	Optional<Empleado> findByContacto_Email(String email);
	
	Optional<Empleado> findByContacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByContacto_NombreDescripcion(String username);
	
	Boolean existsByContacto_Email(String email);
	
	Boolean existsByContacto_Cuit(String cuit);
    
    
}