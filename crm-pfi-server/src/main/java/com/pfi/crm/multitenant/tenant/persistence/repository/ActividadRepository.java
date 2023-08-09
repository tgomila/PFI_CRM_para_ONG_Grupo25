package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.Actividad;

@Repository
public interface ActividadRepository extends JpaRepository<Actividad, Long> {
	
	//@Query("SELECT e FROM  Actividad e WHERE e.estadoActivoActividad=?1")
	//Optional<Actividad> findById(Long id);
	
	//@Query("SELECT e FROM  Actividad e WHERE e.estadoActivoActividad=?1")
	//List<Actividad> findAll();
	
	List<Actividad> findByBeneficiariosPersonaFisicaContactoId(Long idContacto);
	
	List<Actividad> findByProfesionalesPersonaFisicaContactoId(Long idContacto);
	
	List<Actividad> findByBeneficiariosIdBeneficiario(Long personaId);
	
	List<Actividad> findByProfesionalesIdProfesional(Long personaId);
	
	@Query("SELECT b.personaFisica.contacto.id as id, b.personaFisica.nombre as nombre, b.personaFisica.apellido as apellido, b.personaFisica.contacto.nombreDescripcion as nombreDescripcion, COUNT(a) as cantidad FROM Actividad a JOIN a.beneficiarios b GROUP BY b.personaFisica.contacto.id, b.personaFisica.nombre, b.personaFisica.apellido, b.personaFisica.contacto.nombreDescripcion ORDER BY cantidad DESC")
	List<Map<String, Object>> findTop20BeneficiariosOrderedByCount();
	
	@Query("SELECT MONTH(a.fechaHoraDesde) AS month, YEAR(a.fechaHoraDesde) AS year, COUNT(a) AS cantidad " +
				"FROM Actividad a " +
				"WHERE a.fechaHoraDesde >= :fechaDesde AND a.fechaHoraDesde <= :fechaHasta " +
				"GROUP BY month, year " +
				"ORDER BY a.fechaHoraDesde ASC")
	List<Map<String, Object>> findActividadesByFechaBetween(@Param("fechaDesde") LocalDateTime fechaDesde, @Param("fechaHasta") LocalDateTime fechaHasta);

}
