package com.pfi.crm.multitenant.tenant.payload;

public class TenantPayload {
	
	private Integer tenantClientId;
	private String dbName;
	private String tenantName;
	
	
	public TenantPayload() {
		super();
	}
	
	public TenantPayload(Integer tenantClientId, String dbName, String tenantName) {
		super();
		this.tenantClientId = tenantClientId;
		this.dbName = dbName;
		this.tenantName = tenantName;
	}
	
	
	public Integer getTenantClientId() {
		return tenantClientId;
	}
	
	public void setTenantClientId(Integer tenantClientId) {
		this.tenantClientId = tenantClientId;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}
	
}
