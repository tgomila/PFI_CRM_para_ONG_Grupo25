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
	
	public TenantPayload altaTenant(TenantPayload payload) {
		return masterTenantRepository.save(new MasterTenant(payload)).toPayload();
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
		p.setName(m.getDbName());
		
		return p;
	}
}