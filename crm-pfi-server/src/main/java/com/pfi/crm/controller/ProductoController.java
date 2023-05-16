package com.pfi.crm.controller;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.ProductoPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.ProductoNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.multitenant.tenant.service.ProductoService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/producto")
public class ProductoController {

	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;

	@GetMapping("/{id}")
	public ProductoPayload getProductoById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver producto con id: '" + id + "'");
		return productoService.getProductoById(id);
	}

	@GetMapping({ "/", "/all" })
	public List<ProductoPayload> getProductos(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver productos");
		return productoService.getProductos();
	}
	
	@GetMapping({ "/sin_stock" })
	public List<ProductoPayload> getProductosSinStock(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver productos sin stock");
		return productoService.getProductosSinStock();
	}
	
	@GetMapping({ "/con_stock" })
	public List<ProductoPayload> getProductosConStock(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver productos con stock");
		return productoService.getProductosConStock();
	}
	
	@GetMapping({ "/con_bajo_stock" })
	public List<ProductoPayload> getProductosConBajoStock(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver productos con bajo stock");
		return productoService.getProductosConBajoStock();
	}
	
	@GetMapping({ "/con_suficiente_stock" })
	public List<ProductoPayload> getProductosConSuficienteStock(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver productos con suficiente stock");
		return productoService.getProductosConSuficienteStock();
	}
	
	@GetMapping({ "/calcular_precio_reposicion_stock" })
	public String calcularPrecioReposicion(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Calcular precio de reposición de stock");
		return productoService.calcularPrecioReposicion();
	}

	@PostMapping({ "/", "/alta" })
	public ProductoPayload altaProducto(@Valid @RequestBody ProductoPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PRODUCTO, "Cargar nuevo producto");
		return productoService.altaProducto(payload);
	}

	@DeleteMapping({ "/{id}", "/baja/{id}" })
	public ResponseEntity<?> bajaProducto(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PRODUCTO, "Eliminar un producto con id: '" + id + "'");
		String message = productoService.bajaProducto(id);
    	if(!message.isEmpty())
    		return ResponseEntity.ok().body(new ApiResponse(true, message));
    	else
    		throw new BadRequestException("Algo salió mal en la baja. Verifique message que retorna en backend.");
	}

	@PutMapping({ "/", "/modificar" })
	public ProductoPayload modificarProducto(@Valid @RequestBody ProductoPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.PRODUCTO, "Modificar un producto");
		return productoService.modificarProducto(payload);
	}

	@GetMapping({ "/nombres_tabla" })
	public LinkedHashMap<String, String> getNombresTabla(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver nombre de cada columna de tabla producto");
		return new ProductoNombreTablaPayload().getNombresProductoTabla();
	}
	
	
	
	
	
	// TEST
	// Devuelve un ejemplo del payload
	
	@Autowired
	ContactoController contactoController;
	
	@GetMapping("/test")
	public ProductoPayload getProductoTest(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver ejemplo de un producto");
		
		ProductoPayload p = new ProductoPayload();
		p.setId(Long.valueOf(1234));
		p.setTipo("Galletitas");
		p.setDescripcion("Galletitas rellenas");
		p.setPrecioVenta(BigDecimal.valueOf(150.00));
		p.setCantFijaCompra(6);
		p.setCantMinimaStock(30);
		p.setStockActual(60);
		p.setFragil(false);
		p.setProveedor(contactoController.getContactoTest(currentUser));
		return p;
	}

}
