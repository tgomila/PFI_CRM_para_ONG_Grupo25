package com.pfi.crm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pfi.crm.model.Colaborador;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Long> {
    
	//Probar
	//@Query("SELECT e FROM  Colaborador e WHERE e.Contacto.estadoActivoContacto=?1")
	Optional<Colaborador> findByContacto_Id(Long id);
	
	boolean existsByContacto_Id(Long id);
	
	Optional<Colaborador> findByContacto_Email(String email);
	
	Optional<Colaborador> findByContacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByContacto_NombreDescripcion(String username);
	
	Boolean existsByContacto_Email(String email);
	
	Boolean existsByContacto_Cuit(String cuit);
    
    
}