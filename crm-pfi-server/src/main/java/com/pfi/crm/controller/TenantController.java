package com.pfi.crm.controller;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.mastertenant.service.MasterTenantService;
import com.pfi.crm.multitenant.tenant.payload.TenantPayload;
import com.pfi.crm.multitenant.tenant.payload.nombres_tabla.TenantNombreTablaPayload;

@RestController
@RequestMapping("/api/tenant")
public class TenantController {
	
	@Autowired
	private MasterTenantService masterTenantService;
	
	@GetMapping({"/", "/all"})
	public List<TenantPayload> getTenant() {
		return  masterTenantService.getTenants();
	}
	
	@GetMapping({"/nombres_tabla"})
	public LinkedHashMap<String, String> getNombresTabla() {
		return new TenantNombreTablaPayload().getNombresTenantTabla();
	}
}
