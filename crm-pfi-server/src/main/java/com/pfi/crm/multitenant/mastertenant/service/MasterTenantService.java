package com.pfi.crm.multitenant.mastertenant.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public List<TenantPayload> getTenants() {
		return masterTenantRepository.findAll().stream().map(e -> toPayload(e)).collect(Collectors.toList());
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
	
	private MasterTenant getTenantByDbName(String db_name) {
		List<MasterTenant> tenants = masterTenantRepository.findAll();
		for(int i=0; i<tenants.size(); i++) {
			if(tenants.get(i).getDbName().equalsIgnoreCase(db_name)) {
				return tenants.get(i);
			}
		}
		return null;
	}
	
	public TenantPayload altaTenant(MasterTenant masterTenantpayload) {
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
	
	public TenantPayload altaTenant(TenantPayload payload) {
		if(payload.getTenantClientId() == null || payload.getTenantClientId() < 1) {
			//Busco un id adecuado
			Integer newTenantId = 100;
			while(existsTenantById(newTenantId))
				newTenantId+=100;
			payload.setTenantClientId(newTenantId);
			payload.setDbName("tenant" + newTenantId.intValue()/100);
		}
		return masterTenantRepository.save(new MasterTenant(payload)).toPayload();
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
	
	public void bajaTenant(String db_name) {
		MasterTenant tenant = getTenantByDbName(db_name);
		if(tenant != null) {
			masterTenantRepository.delete(tenant);
		}
		
	}

	private TenantPayload toPayload(MasterTenant m) {
		TenantPayload p = new TenantPayload();
		
		p.setTenantClientId(m.getTenantClientId());
		p.setDbName(m.getDbName());
		p.setTenantName(m.getTenantName());
		
		return p;
	}
}
