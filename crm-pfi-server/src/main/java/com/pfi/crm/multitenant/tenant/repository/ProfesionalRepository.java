package com.pfi.crm.multitenant.tenant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.Profesional;

@Repository
public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {
    
	//Probar
	//@Query("SELECT e FROM  Voluntario e WHERE e.Contacto.estadoActivoContacto=?1")
	Optional<Profesional> findByPersonaFisica_Contacto_Id(Long id);
	
	boolean existsByPersonaFisica_Contacto_Id(Long id);
	
	Optional<Profesional> findByPersonaFisica_Contacto_Email(String email);
	
	Optional<Profesional> findByPersonaFisica_Contacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByPersonaFisica_Contacto_NombreDescripcion(String username);
	
	Boolean existsByPersonaFisica_Contacto_Email(String email);
	
	Boolean existsByPersonaFisica_Contacto_Cuit(String cuit);
    
    
}