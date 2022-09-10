package com.pfi.crm.multitenant.tenant.payload;

public class TenantPayload {
	
	private Integer tenantClientId;
	private String name;
	
	
	public TenantPayload() {
		super();
	}
	
	public TenantPayload(Integer tenantClientId, String dbName) {
		super();
		this.tenantClientId = tenantClientId;
		this.name = dbName;
	}
	
	
	public Integer getTenantClientId() {
		return tenantClientId;
	}
	
	public void setTenantClientId(Integer tenantClientId) {
		this.tenantClientId = tenantClientId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
