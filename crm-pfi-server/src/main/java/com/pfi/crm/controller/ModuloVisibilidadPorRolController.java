package com.pfi.crm.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.model.ModuloVisibilidadPorRol;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;

@RestController
@RequestMapping("/api/modulo")
public class ModuloVisibilidadPorRolController {
	
	@Autowired
	ModuloVisibilidadPorRolService moduloVisibilidadPorRolService;
	
	@GetMapping("/{id}")
    public ModuloVisibilidadPorRol getModuloVisibilidadPorRolById(@PathVariable Long id) {
        return moduloVisibilidadPorRolService.getModuloVisibilidadPorRolById(id);
    }
	
	@GetMapping({"/", "/all"})
	//@PreAuthorize("hasRole('EMPLOYEE')")
    public List<ModuloVisibilidadPorRol> getModulosVisibilidadPorRol() {
    	return  moduloVisibilidadPorRolService.getModulosVisibilidadPorRol();
	}
	
	@GetMapping({"/{name}"})
    public List<ModuloVisibilidadPorRol> getModuloVisibilidadPorRolByNameByRoleName(@PathVariable RoleName roleName) {
    	return  moduloVisibilidadPorRolService.getModuloVisibilidadPorRolByNameByRoleName(roleName);
	}
	
	@PostMapping({"/", "/alta"})
    public ModuloVisibilidadPorRol altaModuloVisibilidadPorRol(@Valid @RequestBody ModuloVisibilidadPorRol payload) {
    	return moduloVisibilidadPorRolService.altaModuloVisibilidadPorRol(payload);
    }
	
	
	
	@GetMapping("/nombredeheadersdetabla")
	public Object getNombreDeTabla() {
		return null;// new ModuloVisibilidadPorRol().nombreDeColumnaParaTablaFrontend();
	}
}
