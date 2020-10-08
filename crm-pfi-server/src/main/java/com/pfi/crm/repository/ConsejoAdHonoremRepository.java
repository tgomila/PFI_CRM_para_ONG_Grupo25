package com.pfi.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.model.ConsejoAdHonorem;



public interface ConsejoAdHonoremRepository extends JpaRepository<ConsejoAdHonorem, Long> {
    
	//Probar
	//@Query("SELECT e FROM  ConsejoAdHonorem e WHERE e.contacto.estadoActivoContacto=?1")
	Optional<ConsejoAdHonorem> findByPersonaFisica_Contacto_Id(Long id);
	
	Optional<ConsejoAdHonorem> findByPersonaFisica_Contacto_Email(String email);
	
	boolean existsByPersonaFisica_Contacto_Id(Long id);
	
	Optional<ConsejoAdHonorem> findByPersonaFisica_Contacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByPersonaFisica_Contacto_NombreDescripcion(String username);
	
	Boolean existsByPersonaFisica_Contacto_Email(String email);
	
	Boolean existsByPersonaFisica_Contacto_Cuit(String cuit);
    
    
}