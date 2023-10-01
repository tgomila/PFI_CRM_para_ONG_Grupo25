package com.pfi.crm.controller;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.mastertenant.service.MasterTenantService;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.TenantNombreTablaPayload;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/master_tenant")
public class MasterTenantController {
	
	@Autowired
	private MasterTenantService masterTenantService;
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	@GetMapping({"/", "/test"})
	public String getTenant(@CurrentUser UserPrincipal currentUser) {
		seguridad.esMasterTenantAdmin(currentUser);
		return "Soy master tenant admin";
	}
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla() {
		return new TenantNombreTablaPayload().getNombresTenantTabla();
	}
}
