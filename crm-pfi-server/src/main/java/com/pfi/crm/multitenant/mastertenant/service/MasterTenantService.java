package com.pfi.crm.multitenant.mastertenant.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfi.crm.multitenant.mastertenant.entity.MasterTenant;
import com.pfi.crm.multitenant.mastertenant.repository.MasterTenantRepository;

@Service
public class MasterTenantService {
	private static final Logger LOG = LoggerFactory.getLogger(MasterTenantService.class);

	@Autowired
	private MasterTenantRepository masterTenantRepository;

	public MasterTenant findByClientId(Integer clientId) {
		LOG.info("findByClientId() method call...");
		return masterTenantRepository.findByTenantClientId(clientId);
	}
}
