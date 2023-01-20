package com.pfi.crm.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	@GetMapping("/{id}")
	public ModuloVisibilidadPorRol getModuloVisibilidadPorRolById(@PathVariable Long id) {
		return moduloVisibilidadPorRolService.getModuloVisibilidadPorRolById(id);
	}
	
	@GetMapping({"/"})
	public ModuloPayload getModuloPorRol(@CurrentUser UserPrincipal currentUser) {
		return  moduloVisibilidadPorRolService.getModulosVisibilidadPorRol(currentUser);
	}
	
	@GetMapping({"/name/default"})
	public ModuloPayload getModuloDefault() {
		ModuloPayload payload = new ModuloPayload();
		return payload;
	}
	
	@GetMapping({"/name/{roleName}"})
	public List<ModuloItemPayload> getModuloPorRol(@PathVariable("roleName") RoleName roleName) {
		return moduloVisibilidadPorRolService.getModulosVisibilidadPorRol(roleName).getItems();
	}
	
	@GetMapping({"/all"})
	//@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_EMPLOYEE')")
	public List<ModuloPayload> getModulos() {
		return  moduloVisibilidadPorRolService.getModulosVisibilidadPorRol();
	}
	
	@PostMapping({"/", "/alta"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ModuloPayload altaModuloVisibilidadPorRol(@Valid @RequestBody ModuloPayload payload) {
		return moduloVisibilidadPorRolService.altaModuloVisibilidadPorRol(payload);
	}
	
	
	
	//@GetMapping("/nombredeheadersdetabla")
	//public Object getNombreDeTabla() {
	//	return null;// new ModuloVisibilidadPorRol().nombreDeColumnaParaTablaFrontend();
	//}
}
