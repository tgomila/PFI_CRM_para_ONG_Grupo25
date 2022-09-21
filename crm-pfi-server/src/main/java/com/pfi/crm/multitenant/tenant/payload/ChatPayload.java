package com.pfi.crm.multitenant.tenant.payload;

public class ChatPayload {
	
	private Long id;
	private String userNameFrom;
	private String userNameTo;
	private String mensaje;
	private boolean leido;
	
	
	//Actualizar cada vez que se agrega una nueva variable a la clase
	class NombreDeColumnaParaTablaFrontend {
		public String id = "ID";
		public String userNameFrom = "Enviaro por";
		public String userNameTo = "Enviado a";
		public String mensaje = "Mensaje";
		public String leido = "Leido Si/No";
		public String getId() {
			return id;
		}
		public String getUserNameFrom() {
			return userNameFrom;
		}
		public String getUserNameTo() {
			return userNameTo;
		}
		public String getMensaje() {
			return mensaje;
		}
		public String getLeido() {
			return leido;
		}
	}
	public NombreDeColumnaParaTablaFrontend nombreDeColumnaParaTablaFrontend() {
		return new NombreDeColumnaParaTablaFrontend();
	}
	
	
	
	
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
	public boolean getLeido() {
		return leido;
	}
	public void setLeido(boolean leido) {
		this.leido = leido;
	}
	
	
}
