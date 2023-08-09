package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pfi.crm.multitenant.tenant.model.Proyecto;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
	
	List<Proyecto> findByInvolucradosContactoId(Long idContacto);
	
	List<Proyecto> findByInvolucradosIdPersonaFisica(Long personaId);
	
	@Query("SELECT i.contacto.id as id, i.nombre as nombre, i.apellido as apellido, i.contacto.nombreDescripcion as nombreDescripcion, COUNT(p) as cantidad FROM Proyecto p JOIN p.involucrados i GROUP BY i.contacto.id, i.nombre, i.apellido, i.contacto.nombreDescripcion ORDER BY cantidad DESC")
	List<Map<String, Object>> findTop20InvolucradosOrderedByCount();
	
	@Query("SELECT MONTH(p.fechaInicio) AS month, YEAR(p.fechaInicio) AS year, COUNT(p) AS cantidad " +
				"FROM Proyecto p " +
				"WHERE p.fechaInicio >= :fechaDesde AND p.fechaFin <= :fechaHasta " +
				"GROUP BY month, year " +
				"ORDER BY p.fechaInicio ASC")
	List<Map<String, Object>> findProyectosByFechaBetween(@Param("fechaDesde") LocalDate fechaDesde, @Param("fechaHasta") LocalDate fechaHasta);
	
}