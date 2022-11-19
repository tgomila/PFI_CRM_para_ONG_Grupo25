package com.pfi.crm.multitenant.tenant.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.PersonaFisica;

@Repository
public interface PersonaFisicaRepository extends JpaRepository<PersonaFisica, Long> {
    
	//Probar SELECT e FROM  Employee e WHERE e.active=?1
	//@Query("SELECT e FROM  PersonaFisica e WHERE e.contacto.estadoActivoContacto=?1")
	Optional<PersonaFisica> findByContacto_Id(Long id);
	
	boolean existsByContacto_Id(Long id);
	
	Optional<PersonaFisica> findByContacto_Email(String email);
	
	Optional<PersonaFisica> findByContacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByContacto_NombreDescripcion(String username);
	
	Boolean existsByContacto_Email(String email);
	
	Boolean existsByContacto_Cuit(String cuit);
    
    
}