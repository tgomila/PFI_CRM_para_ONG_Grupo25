package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
	
	//Esto debería ser un chequeo para el alta, si existe no deberia suceder el alta.
	boolean existsByDni(int dni);
	Optional<PersonaFisica> findByDni(int dni);
	
	@Query("SELECT YEAR(pf.contacto.createdAt) as year, MONTH(pf.contacto.createdAt) as month, COUNT(pf) as count FROM PersonaFisica pf WHERE pf.contacto.createdAt BETWEEN :start AND :end GROUP BY YEAR(pf.contacto.createdAt), MONTH(pf.contacto.createdAt) ORDER BY YEAR(pf.contacto.createdAt) ASC, MONTH(pf.contacto.createdAt) ASC")
	List<Map<String, Object>> countCreatedLast12MonthsByMonth(@Param("start") Instant start, @Param("end") Instant end);

	
	
    
	/**
	 * La idea es que si es contacto es null en personaFisica, se borre personaFisica. 
	 * Pero también hay que borrar en otros objetos, lo asociado a personas (beneficiario).
	 * Asique no deberia tener que usarse este método, pero queda aquí.
	 */
	void deleteByContactoIsNull();
}