package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.model.Producto;

public interface ProveedorRepository extends JpaRepository<Producto, Long> {
	
	//@Query("SELECT DISTINCT p.proveedor FROM Producto p")
	@Query("SELECT p.proveedor " +
			"FROM Producto p " +
			"GROUP BY p.proveedor " +
			"ORDER BY COUNT(p) DESC")
	Set<Contacto> findAllProveedores();
	
	//@Query("SELECT p.proveedor, COUNT(p) as numeroDeProductos " +
	//		"FROM Producto p " +
	//		"GROUP BY p.proveedor " +
	//		"ORDER BY numeroDeProductos DESC")
	@Query("SELECT "
			+ "p.proveedor.id as id, "
			+ "p.proveedor.nombreDescripcion as nombreDescripcion, "
			+ "p.proveedor.cuit as cuit, "
			+ "p.proveedor.domicilio as domicilio, "
			+ "p.proveedor.telefono as telefono, "
			+ "p.proveedor.email as email, "
			+ "COUNT(p) as numeroDeProductos "
			+ "FROM Producto p "
			+ "GROUP BY p.proveedor.id "
			+ "ORDER BY numeroDeProductos DESC")
	List<Map<String, Object>> findAllProveedoresWithNumeroDeProductos();
	
	@Query("SELECT p FROM Producto p WHERE p.proveedor.id = :proveedorId ORDER BY p.descripcion DESC")
	List<Producto> findProductosByProveedorId(@Param("proveedorId") Long proveedorId);
	
}