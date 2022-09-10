package com.pfi.crm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.mastertenant.service.MasterTenantService;
import com.pfi.crm.multitenant.tenant.payload.TenantPayload;

@RestController
@RequestMapping("/api/tenant")
public class TenantController {
	
	@Autowired
	private MasterTenantService masterTenantService;
	
	@GetMapping("/all")
    public List<TenantPayload> getTenant() {
    	return  masterTenantService.getTenants();
	}
}
