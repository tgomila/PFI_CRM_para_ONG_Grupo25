package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.Colaborador;

@Repository
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
	
	@Query("SELECT YEAR(c.personaFisica.contacto.createdAt) as year, MONTH(c.personaFisica.contacto.createdAt) as month, COUNT(c) as count FROM Colaborador c WHERE c.personaFisica.contacto.createdAt BETWEEN :start AND :end GROUP BY YEAR(c.personaFisica.contacto.createdAt), MONTH(c.personaFisica.contacto.createdAt) ORDER BY YEAR(c.personaFisica.contacto.createdAt) ASC, MONTH(c.personaFisica.contacto.createdAt) ASC")
	List<Map<String, Object>> countCreatedLast12MonthsByMonth(@Param("start") Instant start, @Param("end") Instant end);

	@Query("SELECT c.personaFisica.fechaNacimiento FROM Colaborador c")
    List<LocalDate> findAllFechaNacimiento();
}