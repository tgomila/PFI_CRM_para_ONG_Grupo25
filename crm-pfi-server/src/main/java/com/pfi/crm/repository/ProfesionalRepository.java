package com.pfi.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.model.Profesional;

public interface ProfesionalRepository extends JpaRepository<Profesional, Long> {
    
	//Probar
	//@Query("SELECT e FROM  Voluntario e WHERE e.Contacto.estadoActivoContacto=?1")
	Optional<Profesional> findByContacto_Id(Long id);
	
	boolean existsByContacto_Id(Long id);
	
	Optional<Profesional> findByContacto_Email(String email);
	
	Optional<Profesional> findByContacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByContacto_NombreDescripcion(String username);
	
	Boolean existsByContacto_Email(String email);
	
	Boolean existsByContacto_Cuit(String cuit);
    
    
}