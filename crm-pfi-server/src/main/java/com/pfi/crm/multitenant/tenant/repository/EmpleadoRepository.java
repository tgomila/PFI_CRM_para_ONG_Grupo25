package com.pfi.crm.multitenant.tenant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.multitenant.tenant.model.Empleado;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    
	//Probar
	//@Query("SELECT e FROM  Empleado e WHERE e.Contacto.estadoActivoContacto=?1")
	Optional<Empleado> findByPersonaFisica_Contacto_Id(Long id);
	
	boolean existsByPersonaFisica_Contacto_Id(Long id);
	
	Optional<Empleado> findByPersonaFisica_Contacto_Email(String email);
	
	Optional<Empleado> findByPersonaFisica_Contacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByPersonaFisica_Contacto_NombreDescripcion(String username);
	
	Boolean existsByPersonaFisica_Contacto_Email(String email);
	
	Boolean existsByPersonaFisica_Contacto_Cuit(String cuit);
    
    
}