package com.pfi.crm.multitenant.mastertenant.scheduling;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.pfi.crm.mastertenant.config.DBContextHolder;
import com.pfi.crm.multitenant.mastertenant.service.MasterTenantService;
import com.pfi.crm.multitenant.tenant.model.ModuloMarket;
import com.pfi.crm.multitenant.tenant.model.Role;
import com.pfi.crm.multitenant.tenant.model.RoleName;
import com.pfi.crm.multitenant.tenant.persistence.repository.ModuloMarketRepository;
import com.pfi.crm.multitenant.tenant.persistence.repository.RoleRepository;
import com.pfi.crm.multitenant.tenant.service.ModuloVisibilidadPorRolService;

public class Event {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private ModuloVisibilidadPorRolService moduloVisibilidadPorRolService;
	
	@Autowired
	private ModuloMarketRepository moduloMarketRepository;
	
	@Autowired
	private MasterTenantService masterTenantService;
	
	//TODO comprobar
	@Scheduled(cron = "*/5 * * * * MON-FRI")
	public void eventoCada5segundos() {
		System.out.println("Test evento cada 5 segundos");
	}
	
	
	//Setear check cada 1 día a las 00hs, horario utc
	@Scheduled(cron="0 0 0 * * ?")//, zone="America/Argentina/Buenos_Aires")
	public void eventoDiario() {
		moduloCheckSuscripcion();
	}
	
	
	public void moduloCheckSuscripcion() {
		List<String> tenants = masterTenantService.getDbNames();//Arrays.asList(new String[]{"tenant1", "tenant2", "tenant3"});
		for(String tenantString: tenants) {
			moduloCheckSuscripcionTenant(tenantString);
		}
		
	}
	
	public void moduloCheckSuscripcionTenant(String tenantName) {
		if(!cargoBienTenant(tenantName)) {
			System.out.println("Chequear tenant '" + tenantName + "' mal cargado");
			return;
		}
		//Llamar a service y pedir chequear suscripción si esta vencido, entonces modificar.
		//	Dicho service tendría que cambiar la visibilidad a no_vista a todos sus usuarios.
		//
		List<ModuloMarket> modulosPremium = moduloMarketRepository.findAll();
		for(ModuloMarket modulo: modulosPremium) {
			if(modulo.isSuscripcionActiva() && modulo.poseeSuscripcionVencida()) {
				// Cambiar la visibilidad en todos los roles
				moduloVisibilidadPorRolService.quitarVisibilidadModulo(modulo.getModuloEnum());
				// Finalmente terminado el cambio de visibilidad, realizo la actualización suscripción activa true a false.
				// Esto permite no actualizar los roles y minimizar consultas a la BD.
				modulo.setSuscripcionActiva(false);
				moduloMarketRepository.save(modulo);
			}
		}
	}
	
	/**
	 * 
	 * @param tenantName
	 * @return True si ha cargado bien el tenant, false si no lo ha hecho (Multitenancy)
	 */
	private boolean cargoBienTenant(String tenantName) {
		//Chequear si existen datos en la BD
		DBContextHolder.setCurrentDb(tenantName);
		Optional<Role> rol = roleRepository.findByName(RoleName.ROLE_USER);
		if (rol.isPresent())
			return true;
		else
			return false;
	}
}
