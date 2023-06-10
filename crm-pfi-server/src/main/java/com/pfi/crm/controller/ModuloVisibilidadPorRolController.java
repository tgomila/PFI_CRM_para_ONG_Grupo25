package com.pfi.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.model.ModuloEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloTipoVisibilidadEnum;
import com.pfi.crm.multitenant.tenant.model.ModuloVisibilidadPorRol;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.payload.ModuloItemPayload;
import com.pfi.crm.multitenant.tenant.payload.ModuloPayload;
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
		//seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CHAT, "Ver módulos accesibles por el usuario");
		return moduloVisibilidadPorRolService.getModuloVisibilidadPorRolById(id);
	}
	
	@GetMapping({"/"})
	public List<ModuloItemPayload> getModulosPorRolDelUsuario(@CurrentUser UserPrincipal currentUser) {
		//seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CHAT, "Ver módulos accesibles por el usuario");
		return  moduloVisibilidadPorRolService.getModulosVisibilidadPorRol(currentUser);
	}
	
	@GetMapping({"/moduloname/{moduloName}"})
	public ModuloItemPayload getUnModuloPorRolDelUsuario(@PathVariable("moduloName") ModuloEnum moduloName, @CurrentUser UserPrincipal currentUser) {
		//seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CHAT, "Ver módulos accesibles por el usuario");
		return  moduloVisibilidadPorRolService.getUnModuloPorRolDelUsuario(moduloName, currentUser);
	}
	
	/**
	 * Si no funciona "getModuloPorRolDelUsuario", se utilizará este.
	 * @param roleName
	 * @return modulos
	 */
	@GetMapping({"/rolename/{roleName}"})
	public List<ModuloItemPayload> getModuloPorRol(@PathVariable("roleName") RoleName roleName) {
		//seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.SOLO_VISTA, ModuloEnum.CHAT, "Ver módulos accesibles por el usuario");
		return moduloVisibilidadPorRolService.getModulosVisibilidadPorRol(roleName).getItems();
	}
	
	@GetMapping({"/rolename/default"})
	public ModuloPayload getModuloDefault(@CurrentUser UserPrincipal currentUser) {
		//seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.MARKETPLACE, "Ver módulos de rol default");
		ModuloPayload payload = new ModuloPayload();
		return payload;
	}
	
	@GetMapping({"/all"})
	public List<ModuloPayload> getModulos(@CurrentUser UserPrincipal currentUser) {
		//seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.MARKETPLACE, "Ver todos los módulos que puede ver cada rol");
		return  moduloVisibilidadPorRolService.getModulosVisibilidadPorRol();
	}
	
	@PostMapping({"/", "/alta"})
	public List<ModuloPayload> agregarTodosLosModulos(@CurrentUser UserPrincipal currentUser) {
		seguridad.poseePermisosParaAccederAlMetodo(currentUser, ModuloTipoVisibilidadEnum.EDITAR, ModuloEnum.MARKETPLACE, "Asegurar que estén todos los módulos dados de alta");
		return moduloVisibilidadPorRolService.agregarTodosLosModulos();
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
