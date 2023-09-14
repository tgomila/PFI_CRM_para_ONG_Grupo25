package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.Donacion;

@Repository
public interface DonacionRepository extends JpaRepository<Donacion, Long> {
	
	boolean existsByDonante_Id(Long id);
	
	List<Donacion> findByDonante_Id(Long id);
	
	@Query("SELECT donante.id as id, donante.cuit as cuit, donante.nombreDescripcion as nombreDescripcion, COUNT(donacion) as cantidad " + 
				"FROM Donacion donacion " + 
				"JOIN donacion.donante donante " + 
				"GROUP BY donante.id, donante.cuit, donante.nombreDescripcion " + 
				"ORDER BY cantidad DESC")
	List<Map<String, Object>> findTop20DonantesOrderedByCount();
	
	@Query("SELECT donante.id as id, donante.cuit as cuit, donante.nombreDescripcion as nombreDescripcion" + 
				", COUNT(donacion) as cantidad, SUM(donacion.valorAproximadoDeLaDonacion) as total " +
				"FROM Donacion donacion " + 
				"GROUP BY donante.id, donante.cuit, donante.nombreDescripcion " + 
				"ORDER BY total DESC")
	List<Map<String, Object>> findTop20DonantesOrderedByTotalSpent();
	
	@Query("SELECT MONTH(d.fecha) AS month, YEAR(d.fecha) AS year, COUNT(d) AS cantidad " +
				"FROM Donacion d " +
				"WHERE d.fecha >= :fechaDesde AND d.fecha <= :fechaHasta " +
				"GROUP BY month, year " +
				"ORDER BY d.fecha ASC")
	List<Map<String, Object>> findDonacionesByFechaBetween(@Param("fechaDesde") LocalDateTime fechaDesde, @Param("fechaHasta") LocalDateTime fechaHasta);
	
}