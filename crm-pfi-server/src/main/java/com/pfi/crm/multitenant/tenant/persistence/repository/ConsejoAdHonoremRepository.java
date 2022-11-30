package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.ConsejoAdHonorem;

@Repository
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