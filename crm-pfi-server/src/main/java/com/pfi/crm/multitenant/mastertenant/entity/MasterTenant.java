package com.pfi.crm.multitenant.mastertenant.entity;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;

import com.pfi.crm.constant.AWSConstants;
import com.pfi.crm.multitenant.tenant.payload.TenantPayload;

@Entity
@Table(name = "tbl_tenant_master")
public class MasterTenant implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tenant_client_id")
	private Integer tenantClientId;

	@Size(max = 50)
	@Column(name = "db_name", nullable = false)
	private String dbName;
	
	@Size(max = 100)
	@Column(name = "tenant_name", nullable = false)
	private String tenantName;

	@Size(max = 250)
	@Column(name = "url", nullable = false)
	private String url;

	@Size(max = 50)
	@Column(name = "user_name", nullable = false)
	private String userName;
	@Size(max = 100)
	@Column(name = "password", nullable = false)
	private String password;
	@Size(max = 100)
	@Column(name = "driver_class", nullable = false)
	private String driverClass;
	@Size(max = 10)
	@Column(name = "status", nullable = false)
	private String status;

	@Size(max = 50)
	@Column(name = "tenant_phone_number", nullable = false)
	private String tenantPhoneNumber;

	public MasterTenant() {
	}

	public MasterTenant(@Size(max = 50) String dbName, @Size(max = 50) String tenantName, @Size(max = 100) String url,
			@Size(max = 50) String userName, @Size(max = 100) String password, @Size(max = 100) String driverClass,
			@Size(max = 10) String status, String tenantPhoneNumber) {
		this.dbName = dbName;
		this.tenantName = tenantName;
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.driverClass = driverClass;
		this.status = status;
		this.tenantPhoneNumber = tenantPhoneNumber;
	}

	public MasterTenant(TenantPayload p) {
		this.tenantClientId = p.getTenantClientId();
		this.dbName = p.getDbName();
		this.tenantName = p.getTenantName();
		//this.url = "jdbc:mysql://localhost:3307/" + p.getDbName()
		this.url = "jdbc:mysql://" + AWSConstants.DB_URL_DEFAULT + ":" + AWSConstants.PORT_DEFAULT + "/" + p.getDbName()
				+ "?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false";
		this.userName = AWSConstants.USERNAME_DEFAULT;//"root";
		this.password = "password-pfi";//1234
		this.driverClass = "com.mysql.cj.jdbc.Driver";
		this.status = "Active";
		this.tenantPhoneNumber = "+541131105305";
	}
	
	public MasterTenant(TenantPayload p, String timeZone) {
		this.tenantClientId = p.getTenantClientId();
		this.dbName = p.getDbName();
		this.tenantName = p.getTenantName();
		if(timeZone != null&& !timeZone.isEmpty()) {
			this.url = "jdbc:mysql://" + AWSConstants.DB_URL_DEFAULT + ":" + AWSConstants.PORT_DEFAULT + "/" + p.getDbName()
					+ "?useSSL=false&serverTimezone=" + timeZone 
					+ "&useLegacyDatetimeCode=false";
		} else {
			this.url = "jdbc:mysql://" + AWSConstants.DB_URL_DEFAULT + ":" + AWSConstants.PORT_DEFAULT + "/" + p.getDbName()
			+ "?useSSL=false&serverTimezone=UTC&useLegacyDatetimeCode=false";
		}
		this.userName = AWSConstants.USERNAME_DEFAULT;//"root";
		this.password = "password-pfi";//1234
		this.driverClass = "com.mysql.cj.jdbc.Driver";
		this.status = "Active";
		this.tenantPhoneNumber = "+541131105305";
	}

	public Integer getTenantClientId() {
		return tenantClientId;
	}

	public MasterTenant setTenantClientId(Integer tenantClientId) {
		this.tenantClientId = tenantClientId;
		return this;
	}

	public String getDbName() {
		return dbName;
	}

	public MasterTenant setDbName(String dbName) {
		this.dbName = dbName;
		return this;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public String getUrl() {
		return url;
	}

	public MasterTenant setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getUserName() {
		return userName;
	}

	public MasterTenant setUserName(String userName) {
		this.userName = userName;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public MasterTenant setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public MasterTenant setDriverClass(String driverClass) {
		this.driverClass = driverClass;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public MasterTenant setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getTenantPhoneNumber() {
		return tenantPhoneNumber;
	}

	public void setTenantPhoneNumber(String tenantPhoneNumber) {
		this.tenantPhoneNumber = tenantPhoneNumber;
	}

	public TenantPayload toPayload() {
		TenantPayload p = new TenantPayload();
		p.setTenantClientId(tenantClientId);
		p.setDbName(dbName);
		p.setTenantName(tenantName);
		p.setTenantPhoneNumber(tenantPhoneNumber);
		return p;
	}
}
