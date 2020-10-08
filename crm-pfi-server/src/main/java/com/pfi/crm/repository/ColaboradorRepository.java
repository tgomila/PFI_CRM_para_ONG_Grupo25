package com.pfi.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.model.Colaborador;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    
	//Probar
	//@Query("SELECT e FROM  Colaborador e WHERE e.Contacto.estadoActivoContacto=?1")
	Optional<Colaborador> findByPersonaFisica_Contacto_Id(Long id);
	
	boolean existsByPersonaFisica_Contacto_Id(Long id);
	
	Optional<Colaborador> findByPersonaFisica_Contacto_Email(String email);
	
	Optional<Colaborador> findByPersonaFisica_Contacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByPersonaFisica_Contacto_NombreDescripcion(String username);
	
	Boolean existsByPersonaFisica_Contacto_Email(String email);
	
	Boolean existsByPersonaFisica_Contacto_Cuit(String cuit);
    
    
}