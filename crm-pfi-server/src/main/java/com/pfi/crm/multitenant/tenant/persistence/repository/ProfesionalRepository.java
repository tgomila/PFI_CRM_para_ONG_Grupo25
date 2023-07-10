package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
	
	@Query("SELECT YEAR(p.personaFisica.contacto.createdAt) as year, MONTH(p.personaFisica.contacto.createdAt) as month, COUNT(p) as count FROM Profesional p WHERE p.personaFisica.contacto.createdAt BETWEEN :start AND :end GROUP BY YEAR(p.personaFisica.contacto.createdAt), MONTH(p.personaFisica.contacto.createdAt) ORDER BY YEAR(p.personaFisica.contacto.createdAt) ASC, MONTH(p.personaFisica.contacto.createdAt) ASC")
	List<Map<String, Object>> countCreatedLast12MonthsByMonth(@Param("start") Instant start, @Param("end") Instant end);
    
}