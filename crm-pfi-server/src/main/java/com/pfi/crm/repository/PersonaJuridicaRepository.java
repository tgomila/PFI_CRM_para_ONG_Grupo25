package com.pfi.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.model.PersonaJuridica;

public interface PersonaJuridicaRepository extends JpaRepository<PersonaJuridica, Long> {
    
	//Probar
	//@Query("SELECT e FROM  PersonaJuridica e WHERE e.contacto.estadoActivoContacto=?1")
	Optional<PersonaJuridica> findByContacto_Id(Long id);
	
	boolean existsByContacto_Id(Long id);
	
	Optional<PersonaJuridica> findByContacto_Email(String email);
	
	Optional<PersonaJuridica> findByContacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByContacto_NombreDescripcion(String username);
	
	Boolean existsByContacto_Email(String email);
	
	Boolean existsByContacto_Cuit(String cuit);
    
    
}
