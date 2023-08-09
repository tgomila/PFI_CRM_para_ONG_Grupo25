package com.pfi.crm.multitenant.tenant.persistence.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pfi.crm.multitenant.tenant.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
	
	//@Query("SELECT e FROM  Producto e WHERE e.estadoActivo=?1")
	//Optional<Producto> findById(Long id);
	
	//@Query("SELECT e FROM  Producto e WHERE e.estadoActivo=?1")
	//List<Producto> findAll();
    
	List<Producto> findByProveedor_Id(Long id);
	List<Producto> findByProveedor_Cuit(String cuit);
	List<Producto> findByProveedor_Email(String email);
	
	@Query("SELECT p FROM Producto p WHERE p.stockActual = 0")
	List<Producto> findProductosWithoutStock();
	
	@Query("SELECT p FROM Producto p WHERE p.stockActual > 0")
	List<Producto> findProductosWithStock();
	
	@Query("SELECT p FROM Producto p WHERE p.stockActual < p.cantMinimaStock")
	List<Producto> findProductosWithLowStock();
	
	@Query("SELECT p FROM Producto p WHERE p.stockActual >= p.cantMinimaStock")
	List<Producto> findProductosWithEnoughStock();
	
	@Query("SELECT SUM(CEILING((p.cantMinimaStock - p.stockActual) / p.cantFijaCompra) * p.cantFijaCompra * p.precioVenta) FROM Producto p WHERE p.stockActual < p.cantMinimaStock")
	BigDecimal calcularPrecioReposicion();
	
}