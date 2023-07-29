package com.pfi.crm.controller;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.payload.ContactoPayload;
import com.pfi.crm.multitenant.tenant.payload.ProductoPayload;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.multitenant.tenant.service.ProveedorService;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/proveedor")
public class ProveedorController {

	@Autowired
	private ProveedorService proveedorService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;

	@GetMapping({ "/", "/all" })
	public List<ContactoPayload> getProductos(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver proveedores de productos");
		return proveedorService.getAllProveedores();
	}
	
	@GetMapping({ "/allWithNumeroDeProductos" })
	public List<Map<String, Object>>  getAllProveedoresConNumeroDeProductos(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver proveedores de productos");
		return proveedorService.getAllProveedoresConNumeroDeProductos();
	}
	
	@GetMapping({ "/{id}" })
	public List<ProductoPayload>  getAllProveedorProductos(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.PRODUCTO, "Ver productos del proveedor ID: '" + id +"'.");
		return proveedorService.getAllProveedorProductos(id);
	}

}
