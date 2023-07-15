package com.pfi.crm.multitenant.tenant.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pfi.crm.multitenant.tenant.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);

	Optional<User> findByUsernameOrEmail(String username, String email);

	List<User> findByIdIn(List<Long> userIds);

	Optional<User> findByUsername(String username);

	Boolean existsByUsername(String username);

	Boolean existsByEmail(String email);
    
	Boolean existsByContacto_Id(Long id);
    
	List<User> findByContacto_Id(Long id);
	
	/*@Query("SELECT new map(" +
            "u.id as idUser, " +
            "u.name as name, " +
            "u.username as username, " +
            "u.email as email, " +
            "CAST(GROUP_CONCAT(r.roleName) AS java.lang.String) as roles, " +
            "c.id as idContacto, " +
            "c.nombreDescripcion as nombreDescripcion, " +
            "c.cuit as cuit, " +
            "c.domicilio as domicilio, " +
            "c.telefono as telefono, " +
            "pf.nombre as nombrePersona, " +
            "pf.apellido as apellidoPersona, " +
            "pf.fechaNacimiento as fechaNacimientoPersona) " +
            "FROM User u " +
            "LEFT JOIN u.contacto c " +
            "LEFT JOIN u.personaFisica pf " +
            "LEFT JOIN u.roles r " +
            "GROUP BY u.id, c.id")
    List<Map<String, Object>> getUsersWithContactoAndPersona();*/
	
	@Query("SELECT new map(" +
            "u.id as idUser, " +
            "u.name as name, " +
            "u.username as username, " +
            "u.email as email, " +
            "CAST(GROUP_CONCAT(r.roleName) AS java.lang.String) as roles, " +
            "c.id as idContacto, " +
            "c.nombreDescripcion as nombreDescripcion, " +
            "c.cuit as cuit, " +
            "c.domicilio as domicilio, " +
            "c.telefono as telefono) " +
            "FROM User u LEFT JOIN u.contacto c LEFT JOIN u.roles r " +
            "GROUP BY u.id, c.id")
    List<Map<String, Object>> getUsersWithContacto();
}
