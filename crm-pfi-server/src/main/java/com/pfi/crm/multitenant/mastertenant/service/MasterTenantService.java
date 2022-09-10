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

	private TenantPayload toPayload(MasterTenant m) {
		TenantPayload p = new TenantPayload();
		
		p.setTenantClientId(m.getTenantClientId());
		p.setName(m.getDbName());
		
		return p;
	}
}
