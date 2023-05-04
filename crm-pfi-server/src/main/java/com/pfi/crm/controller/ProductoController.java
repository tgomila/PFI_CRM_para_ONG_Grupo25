package com.pfi.crm.controller;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.payload.ProductoPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ProductoNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ProductoService;

@RestController
@RequestMapping("/api/producto")
public class ProductoController {

	@Autowired
	private ProductoService productoService;

	@GetMapping("/{id}")
	//@PreAuthorize("hasRole('ROLE_PROFESIONAL') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public ProductoPayload getProductoById(@PathVariable Long id) {
		return productoService.getProductoById(id);
	}

	@GetMapping({ "/", "/all" })
	//@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_PROFESIONAL') or hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public List<ProductoPayload> getProductos() {
		return productoService.getProductos();
	}
	
	@GetMapping({ "/sin_stock" })
	public List<ProductoPayload> getProductosSinStock() {
		return productoService.getProductosSinStock();
	}
	
	@GetMapping({ "/con_stock" })
	public List<ProductoPayload> getProductosConStock() {
		return productoService.getProductosConStock();
	}
	
	@GetMapping({ "/con_bajo_stock" })
	public List<ProductoPayload> getProductosConBajoStock() {
		return productoService.getProductosConBajoStock();
	}
	
	@GetMapping({ "/con_suficiente_stock" })
	public List<ProductoPayload> getProductosConSuficienteStock() {
		return productoService.getProductosConSuficienteStock();
	}
	
	@GetMapping({ "/calcular_precio_reposicion_stock" })
	public String calcularPrecioReposicion() {
		return productoService.calcularPrecioReposicion();
	}

	@PostMapping({ "/", "/alta" })
	@PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public ProductoPayload altaProducto(@Valid @RequestBody ProductoPayload payload) {
		return productoService.altaProducto(payload);
	}

	@DeleteMapping({ "/{id}", "/baja/{id}" })
	@PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public void bajaProducto(@PathVariable Long id) {
		productoService.bajaProducto(id);
	}

	@PutMapping({ "/", "/modificar" })
	@PreAuthorize("hasRole('ROLE_EMPLOYEE') or hasRole('ROLE_ADMIN')")
	public ProductoPayload modificarProducto(@Valid @RequestBody ProductoPayload payload) {
		return productoService.modificarProducto(payload);
	}

	@GetMapping({ "/nombres_tabla" })
	public LinkedHashMap<String, String> getNombresTabla() {
		return new ProductoNombreTablaPayload().getNombresProductoTabla();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo del payload
	
	@Autowired
	ContactoController contactoController;
	
	@GetMapping("/test")
	public ProductoPayload getProductoTest(/* @Valid @RequestBody ProductoPayload payload */) {
		
		ProductoPayload p = new ProductoPayload();
		p.setId(Long.valueOf(1234));
		p.setTipo("Galletitas");
		p.setDescripcion("Galletitas rellenas");
		p.setPrecioVenta(BigDecimal.valueOf(150.00));
		p.setCantFijaCompra(6);
		p.setCantMinimaStock(30);
		p.setStockActual(60);
		p.setFragil(false);
		p.setProveedor(contactoController.getContactoTest());
		return p;
	}

}
