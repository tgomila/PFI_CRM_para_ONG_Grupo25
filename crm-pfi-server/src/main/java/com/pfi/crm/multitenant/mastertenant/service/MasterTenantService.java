package com.pfi.crm.multitenant.mastertenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfi.crm.exception.BadRequestException;
import com.pfi.crm.mastertenant.config.DBContextHolder;
import com.pfi.crm.multitenant.mastertenant.entity.MasterTenant;
import com.pfi.crm.multitenant.mastertenant.repository.MasterTenantRepository;
import com.pfi.crm.multitenant.tenant.payload.TenantPayload;

@Service
public class MasterTenantService {
	private static final Logger LOG = LoggerFactory.getLogger(MasterTenantService.class);

	@Autowired
	private MasterTenantRepository masterTenantRepository;

	public MasterTenant findByClientId(Integer clientId) {
		LOG.info("findByClientId() method call...");
		return masterTenantRepository.findByTenantClientId(clientId);
	}

	//Se utiliza en front para obtener el número telefónico
	public TenantPayload getUserTenant() {
		String tenantName = DBContextHolder.getCurrentDb();
		if(tenantName != null) {
			MasterTenant tenantModel = getTenantByDbName(tenantName);
			return tenantModel.toPayload();
		}
		throw new BadRequestException("Algo salió mal. No hay tenant iniciado sesión.");
	}
	
	public List<TenantPayload> getTenants() {
		return masterTenantRepository.findAllByOrderByTenantClientId().stream().map(e -> e.toPayload()).collect(Collectors.toList());
	}
	
	public List<String> getDbNames() {
		return masterTenantRepository.findAll().stream().map(e -> e.getDbName()).collect(Collectors.toList());
	}
	
	public boolean existTenantId(String db_name) {
		if(null == getTenantByDbName(db_name))
			return false;
		else
			return true;
	}
	
	public MasterTenant getTenantByDbName(String db_name) {
		List<MasterTenant> tenants = masterTenantRepository.findAll();
		for(int i=0; i<tenants.size(); i++) {
			if(tenants.get(i).getDbName().equalsIgnoreCase(db_name)) {
				return tenants.get(i);
			}
		}
		return null;
	}
	
	//A partir de acá se utiliza en cargarDatosEjemplo.
	
	/** 
	 * Se utilizaba en cargarDatosEjemplo.java. No deberia usarse por Admin de Tenants
	 * Quedó desactualizado al pedir Model, ahora se usa otro método con Payload.
	 * @param payload
	 * @return
	 */
	@SuppressWarnings("unused")
	private TenantPayload altaTenant(MasterTenant masterTenantpayload) {
		if(masterTenantpayload.getTenantClientId() == null || masterTenantpayload.getTenantClientId() < 1) {
			//Busco un id adecuado
			Integer newTenantId = 100;
			while(existsTenantById(newTenantId))
				newTenantId+=100;
			masterTenantpayload.setTenantClientId(newTenantId);
			masterTenantpayload.setDbName("tenant" + newTenantId.intValue()/100);
		}
		return masterTenantRepository.save(masterTenantpayload).toPayload();
	}
	
	/** 
	 * Se utiliza en cargarDatosEjemplo.java. No deberia usarse por Admin de Tenants
	 * @param payload
	 * @return
	 */
	public TenantPayload altaTenant(TenantPayload payload) {
		if(payload.getTenantClientId() == null || payload.getTenantClientId() < 1) {
			//Busco un id adecuado
			Integer newTenantId = 100;
			while(existsTenantById(newTenantId))
				newTenantId+=100;
			payload.setTenantClientId(newTenantId);
			payload.setDbName("tenant" + newTenantId.intValue()/100);
		}
		MasterTenant masterTenant;
		if(payload.getTenantName() != null && payload.getTenantName().equalsIgnoreCase("tenant2"))
			masterTenant = new MasterTenant(payload, "America/Argentina/Buenos_Aires");
		else
			masterTenant = new MasterTenant(payload);
		return masterTenantRepository.save(masterTenant).toPayload();
	}
	
	private boolean existsTenantById(Integer idTenant) {
		List<MasterTenant> tenants = masterTenantRepository.findAll();
		for(MasterTenant tenant: tenants) {
			if(tenant.getTenantClientId().equals(idTenant)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Se utiliza en cargar datos ejemplo. No deberia usarse por Admin de Tenants
	 * @param db_name
	 */
	public void bajaTenant(String db_name) {
		MasterTenant tenant = getTenantByDbName(db_name);
		if(tenant != null) {
			masterTenantRepository.delete(tenant);
		}
		
	}
	
	//Fin de uso en cargarDatosEjemplo.java
}
