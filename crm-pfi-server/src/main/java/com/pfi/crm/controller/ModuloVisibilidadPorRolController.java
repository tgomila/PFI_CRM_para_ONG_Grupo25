package com.pfi.crm.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloVisibilidadPorRol;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.payload.ModuloItemPayload;
import com.pfi.crm.multitenant.tenant.payload.ModuloPayload;
import com.pfi.crm.multitenant.tenant.payload.request.ModificarVisibilidadRequestPayload;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/modulo")
public class ModuloVisibilidadPorRolController {
	
	@Autowired
	ModuloVisibilidadPorRolService moduloVisibilidadPorRolService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;

	@GetMapping("/{id}")
	public ModuloVisibilidadPorRol getModuloVisibilidadPorRolById(@PathVariable Long id, @CurrentUser UserPrincipal currentUser) {
		return moduloVisibilidadPorRolService.getModuloVisibilidadPorRolById(id);
	}
	
	@GetMapping({"/"})
	public List<ModuloItemPayload> getModulosPorRolDelUsuario(@CurrentUser UserPrincipal currentUser) {
		return  moduloVisibilidadPorRolService.getModulosVisibilidadPorRol(currentUser);
	}
	
	@GetMapping({"/moduloname/{moduloName}"})
	public ModuloItemPayload getUnModuloPorRolDelUsuario(@PathVariable("moduloName") ModuloEnum moduloName, @CurrentUser UserPrincipal currentUser) {
		return  moduloVisibilidadPorRolService.getUnModuloPorRolDelUsuario(moduloName, currentUser);
	}
	
	/**
	 * Si no funciona "getModuloPorRolDelUsuario", se utilizará este.
	 * @param roleName
	 * @return modulos
	 */
	@GetMapping({"/rolename/{roleName}"})
	public List<ModuloItemPayload> getModuloPorRol(@PathVariable("roleName") RoleName roleName) {
		return moduloVisibilidadPorRolService.getModulosVisibilidadPorRol(roleName).getItems();
	}
	
	@GetMapping({"/rolename/default"})
	public ModuloPayload getModuloDefault(@CurrentUser UserPrincipal currentUser) {
		ModuloPayload payload = new ModuloPayload();
		return payload;
	}
	
	@GetMapping({"/all"})
	public List<ModuloPayload> getModulos(@CurrentUser UserPrincipal currentUser) {
		return moduloVisibilidadPorRolService.getModulosVisibilidadPorRol();
	}
	
	@PostMapping({"/agregar_todos_los_modulos"})
	public List<ModuloPayload> agregarTodosLosModulos(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.MARKETPLACE, "Asegurar que estén todos los módulos dados de alta");
		return moduloVisibilidadPorRolService.agregarTodosLosModulos();
	}
	
	//Solo admin
	@PutMapping({"/", "/modificar"})
	public ModuloPayload cambiarVisibilidad(@Valid @RequestBody ModificarVisibilidadRequestPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.MARKETPLACE, "Editar visibilidad de módulos");
		return moduloVisibilidadPorRolService.modificarModuloVisibilidadTipos(payload.getRol(), payload.getModuloEnum(), payload.getTipoVisibilidad());
	}
	
	//@PostMapping({"/", "/alta"})
	//public ModuloPayload altaModuloVisibilidadPorRol(@Valid @RequestBody ModuloPayload payload) {
	//	return moduloVisibilidadPorRolService.altaModuloVisibilidadPorRol(payload);
	//}
	
	
	
	//@GetMapping("/nombredeheadersdetabla")
	//public Object getNombreDeTabla() {
	//	return null;// new ModuloVisibilidadPorRol().nombreDeColumnaParaTablaFrontend();
	//}
}
