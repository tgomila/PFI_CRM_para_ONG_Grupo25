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

import com.pfi.crm.multitenant.tenant.model.Beneficiario;

@Repository
public interface BeneficiarioRepository extends JpaRepository<Beneficiario, Long> {
    
	//Probar
	//@Query("SELECT e FROM  Voluntario e WHERE e.Contacto.estadoActivoContacto=?1")
	Optional<Beneficiario> findByPersonaFisica_Contacto_Id(Long id);
	
	boolean existsByPersonaFisica_Contacto_Id(Long id);
	
	Optional<Beneficiario> findByPersonaFisica_Contacto_Email(String email);
	
	Optional<Beneficiario> findByPersonaFisica_Contacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByPersonaFisica_Contacto_NombreDescripcion(String username);
	
	Boolean existsByPersonaFisica_Contacto_Email(String email);
	
	Boolean existsByPersonaFisica_Contacto_Cuit(String cuit);
	
	@Query("SELECT YEAR(b.personaFisica.contacto.createdAt) as year, MONTH(b.personaFisica.contacto.createdAt) as month, COUNT(b) as count FROM Beneficiario b WHERE b.personaFisica.contacto.createdAt BETWEEN :start AND :end GROUP BY YEAR(b.personaFisica.contacto.createdAt), MONTH(b.personaFisica.contacto.createdAt) ORDER BY YEAR(b.personaFisica.contacto.createdAt) ASC, MONTH(b.personaFisica.contacto.createdAt) ASC")
	List<Map<String, Object>> countCreatedLast12MonthsByMonth(@Param("start") Instant start, @Param("end") Instant end);

	@Query("SELECT b.personaFisica.fechaNacimiento FROM Beneficiario b")
    List<LocalDate> findAllFechaNacimiento();
	
}