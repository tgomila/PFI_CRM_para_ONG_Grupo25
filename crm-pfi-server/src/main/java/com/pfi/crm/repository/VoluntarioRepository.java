package com.pfi.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.model.Voluntario;

public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    
	//Probar
	//@Query("SELECT e FROM  Voluntario e WHERE e.Contacto.estadoActivoContacto=?1")
	Optional<Voluntario> findByContacto_Id(Long id);
	
	Optional<Voluntario> findByContacto_Email(String email);
	
	Optional<Voluntario> findByContacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByContacto_NombreDescripcion(String username);
	
	Boolean existsByContacto_Email(String email);
	
	Boolean existsByContacto_Cuit(String cuit);
    
    
}