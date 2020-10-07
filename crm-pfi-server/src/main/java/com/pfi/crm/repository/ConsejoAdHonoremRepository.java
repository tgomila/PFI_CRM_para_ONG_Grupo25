package com.pfi.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.model.ConsejoAdHonorem;



public interface ConsejoAdHonoremRepository extends JpaRepository<ConsejoAdHonorem, Long> {
    
	//Probar
	//@Query("SELECT e FROM  ConsejoAdHonorem e WHERE e.contacto.estadoActivoContacto=?1")
	Optional<ConsejoAdHonorem> findByContacto_Id(Long id);
	
	Optional<ConsejoAdHonorem> findByContacto_Email(String email);
	
	boolean existsByContacto_Id(Long id);
	
	Optional<ConsejoAdHonorem> findByContacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByContacto_NombreDescripcion(String username);
	
	Boolean existsByContacto_Email(String email);
	
	Boolean existsByContacto_Cuit(String cuit);
    
    
}