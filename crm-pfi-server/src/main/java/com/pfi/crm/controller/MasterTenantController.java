package com.pfi.crm.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfi.crm.multitenant.tenant.payload.request.ModificarMasterTenantMarketRequestPayload;
import com.pfi.crm.multitenant.tenant.service.ModuloMarketService;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;
import com.pfi.crm.payload.response.ApiResponse;
import com.pfi.crm.security.CurrentUser;
import com.pfi.crm.security.UserPrincipal;

@RestController
@RequestMapping("/api/master_tenant")
public class MasterTenantController {
	
	@Autowired
	private ModuloVisibilidadPorRolService seguridad;
	
	@Autowired
	ModuloVisibilidadPorRolService moduloVisibilidadPorRolService;
	
	@Autowired
	ModuloMarketService moduloMarketService;

	/*@Autowired
	private MasterTenantService masterTenantService;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtTokenProviderUtil tokenProvider;

	@Autowired
	private AuthController authController;*/
	
	@GetMapping({"/", "/test"})
	public String getTenant(@CurrentUser UserPrincipal currentUser) {
		seguridad.esMasterTenantAdmin(currentUser);
		return "Soy master tenant admin";
	}
	
	@GetMapping({"/modulos/visibilidad/all"})
	public ResponseEntity<?> getAllModulosVisibilidad(@CurrentUser UserPrincipal currentUser) {
		seguridad.esMasterTenantAdmin(currentUser);
		return ResponseEntity.ok(moduloVisibilidadPorRolService.getModulosVisibilidadDeTodosLosTenants());
	}
	
	@GetMapping({"/modulos/market/all"})
	public ResponseEntity<?> getAllModulosMarket(@CurrentUser UserPrincipal currentUser) {
		seguridad.esMasterTenantAdmin(currentUser);
		return ResponseEntity.ok(moduloMarketService.getModulosMarketDeTodosLosTenants());
	}
	
	@GetMapping({"/modulos/market/sumar_tiempo/test"})
	public ModificarMasterTenantMarketRequestPayload testSumarTiempoModulosMarket(@CurrentUser UserPrincipal currentUser) {
		seguridad.esMasterTenantAdmin(currentUser);
		return moduloMarketService.testSuscribirModulosMarket();
	}
	
	@PostMapping({"/modulos/market/sumar_tiempo"})
	public ResponseEntity<?> sumarTiempoModulosMarket(@Valid @RequestBody ModificarMasterTenantMarketRequestPayload payload, @CurrentUser UserPrincipal currentUser) {
		seguridad.esMasterTenantAdmin(currentUser);
		moduloMarketService.sumarTiempoModulosMarket(payload);
		return ResponseEntity.ok().body(new ApiResponse(true, "Suscripción exitosa"));
	}
}
