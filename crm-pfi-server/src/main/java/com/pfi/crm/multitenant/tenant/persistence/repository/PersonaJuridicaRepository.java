package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.PersonaJuridica;

@Repository
public interface PersonaJuridicaRepository extends JpaRepository<PersonaJuridica, Long> {
	
	//Probar
	//@Query("SELECT e FROM  PersonaJuridica e WHERE e.contacto.estadoActivoContacto=?1")
	Optional<PersonaJuridica> findByContacto_Id(Long id);
	
	boolean existsByContacto_Id(Long id);
	
	Optional<PersonaJuridica> findByContacto_Email(String email);
	
	Optional<PersonaJuridica> findByContacto_NombreDescripcion(String nombreDescripcion);
	
	Boolean existsByContacto_NombreDescripcion(String username);
	
	Boolean existsByContacto_Email(String email);
	
	Boolean existsByContacto_Cuit(String cuit);
	
	@Query("SELECT YEAR(pj.contacto.createdAt) as year, MONTH(pj.contacto.createdAt) as month, COUNT(pj) as count FROM PersonaJuridica pj WHERE pj.contacto.createdAt BETWEEN :start AND :end GROUP BY YEAR(pj.contacto.createdAt), MONTH(pj.contacto.createdAt) ORDER BY YEAR(pj.contacto.createdAt) ASC, MONTH(pj.contacto.createdAt) ASC")
	List<Map<String, Object>> countCreatedLast12MonthsByMonth(@Param("start") Instant start, @Param("end") Instant end);
	
}
