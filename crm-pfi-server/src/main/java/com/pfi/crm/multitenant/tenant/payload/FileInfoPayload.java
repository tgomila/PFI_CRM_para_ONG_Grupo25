package com.pfi.crm.multitenant.tenant.payload;

public class FileInfoPayload {
	private String name;
	private String url;
	
	public FileInfoPayload(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
