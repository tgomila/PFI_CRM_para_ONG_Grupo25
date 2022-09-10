package com.pfi.crm.multitenant.tenant.payload;

public class ChatPayload {
	
	private Long id;
	private String userNameFrom;
	private String userNameTo;
	private String mensaje;
	private boolean leido;
	
	
	public ChatPayload() {
		super();
	}
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserNameFrom() {
		return userNameFrom;
	}
	public void setUserNameFrom(String userNameFrom) {
		this.userNameFrom = userNameFrom;
	}
	public String getUserNameTo() {
		return userNameTo;
	}
	public void setUserNameTo(String userNameTo) {
		this.userNameTo = userNameTo;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public boolean isLeido() {
		return leido;
	}
	public void setLeido(boolean leido) {
		this.leido = leido;
	}
	
	
}
