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

import com.pfi.crm.multitenant.tenant.model.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
	
	//Probar
	//@Query("SELECT e FROM  Empleado e WHERE e.Contacto.estadoActivoContacto=?1")
	Optional<Empleado> findByPersonaFisica_Contacto_Id(Long id);
	
	boolean existsByPersonaFisica_Contacto_Id(Long id);
	
	Optional<Empleado> findByPersonaFisica_Contacto_Email(String email);
	
	Optional<Empleado> findByPersonaFisica_Contacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByPersonaFisica_Contacto_NombreDescripcion(String username);
	
	Boolean existsByPersonaFisica_Contacto_Email(String email);
	
	Boolean existsByPersonaFisica_Contacto_Cuit(String cuit);
	
	@Query("SELECT YEAR(e.personaFisica.contacto.createdAt) as year, MONTH(e.personaFisica.contacto.createdAt) as month, COUNT(e) as count FROM Empleado e WHERE e.personaFisica.contacto.createdAt BETWEEN :start AND :end GROUP BY YEAR(e.personaFisica.contacto.createdAt), MONTH(e.personaFisica.contacto.createdAt) ORDER BY YEAR(e.personaFisica.contacto.createdAt) ASC, MONTH(e.personaFisica.contacto.createdAt) ASC")
	List<Map<String, Object>> countCreatedLast12MonthsByMonth(@Param("start") Instant start, @Param("end") Instant end);

	@Query("SELECT e.personaFisica.fechaNacimiento FROM Empleado e")
	List<LocalDate> findAllFechaNacimiento();
}