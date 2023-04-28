package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.Contacto;

@Repository
public interface ContactoRepository extends JpaRepository<Contacto, Long> {
	
	//Los QUERY's es para ver si el contacto esta activo, "e" es entity
	
	
	List<Contacto> findAllByEstadoActivoContactoTrue();
	
	@Query("SELECT e FROM  Contacto e WHERE e.estadoActivoContacto=?1")
	Optional<Contacto> findByEmail(String email);
	
	@Query("SELECT e FROM  Contacto e WHERE e.estadoActivoContacto=?1")
	Optional<Contacto> findByNombreDescripcion(String nombreDescripcion);
	
	@Query("SELECT e FROM  Contacto e WHERE e.estadoActivoContacto=?1")
	Boolean existsByNombreDescripcion(String username);
	
	@Query("SELECT e FROM  Contacto e WHERE e.estadoActivoContacto=?1")
	Boolean existsByEmail(String email);
	
	@Query("SELECT e FROM  Contacto e WHERE e.estadoActivoContacto=?1")
	Boolean existsByCuit(String cuit);
	
	//A partir de acá, es para gráficos dashboard en Frontend
	
	//Contadores mensuales
	
	/**
	 * Contador creados por mes
	 * @param Mes input
	 * @return cuántos se crearon este mes
	 */
	@Query("SELECT p FROM Contacto p WHERE YEAR(p.createdAt) = YEAR(:now) AND MONTH(p.createdAt) = MONTH(:now)")
	List<Contacto> findContactosCreatedThisMonth(@Param("now") LocalDate now);
	
	/**
	 * Contador creados este año input, por mes
	 * @param Año input
	 * @return cuántos se crearon ese año, por mes
	 */
	@Query("SELECT MONTH(p.createdAt) as month, COUNT(p) as count FROM Contacto p WHERE YEAR(p.createdAt) = YEAR(:now) GROUP BY MONTH(p.createdAt)")
	List<Map<String, Object>> countContactosCreatedThisYearByMonth(@Param("now") LocalDate now);
	
	/**
	 * Devuelve N altas por mes entre 2 fechas. Debería ser en un mismo año o se combinan.
	 * @param start Deberia ser 1 de enero mismo año que end
	 * @param end Debería ser 31 de diciembre mismo año que start.
	 * @return
	 */
	@Query("SELECT MONTH(p.createdAt) as month, COUNT(p) as count FROM Contacto p WHERE p.createdAt BETWEEN :start AND :end GROUP BY MONTH(p.createdAt) ORDER BY MONTH(p.createdAt) ASC")
	List<Map<String, Object>> countContactosCreatedByMonthBetweenDates(@Param("start") Instant start, @Param("end") Instant end);
	
	
	
	/**
	 * Originalmente creado para los últimos 12 meses
	 * @param start cualquier fecha/año
	 * @param end   cualquier fecha/año
	 * @return Devuelve int de contactos creados por mes/año
	 */
	@Query("SELECT YEAR(p.createdAt) as year, MONTH(p.createdAt) as month, COUNT(p) as count FROM Contacto p WHERE p.createdAt BETWEEN :start AND :end GROUP BY YEAR(p.createdAt), MONTH(p.createdAt) ORDER BY YEAR(p.createdAt) ASC, MONTH(p.createdAt) ASC")
	List<Map<String, Object>> countContactosCreatedLast12MonthsByMonth(@Param("start") Instant start, @Param("end") Instant end);
	
	//TODO quitarlo a futuro, ingreso year=2020 start=1/1/2017 end=31/12/2022 y solo cuenta 2020.
	/**
	 * count per month between the first and last day of an input year
	 * @param year
	 * @param start
	 * @param end
	 * @return
	 */
	@Query("SELECT MONTH(p.createdAt) as month, COUNT(p) as count FROM Contacto p WHERE YEAR(p.createdAt) = :year AND p.createdAt BETWEEN :start AND :end GROUP BY MONTH(p.createdAt)")
	List<Map<String, Object>> countContactosCreatedByMonthInYear(@Param("year") int year, @Param("start") LocalDateTime start, @Param("end") LocalDateTime end);
}
