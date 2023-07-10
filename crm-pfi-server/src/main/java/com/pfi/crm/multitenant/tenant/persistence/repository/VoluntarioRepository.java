package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.Voluntario;

@Repository
public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    
	//Probar
	//@Query("SELECT e FROM  Voluntario e WHERE e.Contacto.estadoActivoContacto=?1")
	Optional<Voluntario> findByPersonaFisica_Contacto_Id(Long id);
	
	boolean existsByPersonaFisica_Contacto_Id(Long id);
	
	Optional<Voluntario> findByPersonaFisica_Contacto_Email(String email);
	
	Optional<Voluntario> findByPersonaFisica_Contacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByPersonaFisica_Contacto_NombreDescripcion(String username);
	
	Boolean existsByPersonaFisica_Contacto_Email(String email);
	
	Boolean existsByPersonaFisica_Contacto_Cuit(String cuit);
	
	@Query("SELECT YEAR(v.personaFisica.contacto.createdAt) as year, MONTH(v.personaFisica.contacto.createdAt) as month, COUNT(v) as count FROM Voluntario v WHERE v.personaFisica.contacto.createdAt BETWEEN :start AND :end GROUP BY YEAR(v.personaFisica.contacto.createdAt), MONTH(v.personaFisica.contacto.createdAt) ORDER BY YEAR(v.personaFisica.contacto.createdAt) ASC, MONTH(v.personaFisica.contacto.createdAt) ASC")
	List<Map<String, Object>> countCreatedLast12MonthsByMonth(@Param("start") Instant start, @Param("end") Instant end);
    
}