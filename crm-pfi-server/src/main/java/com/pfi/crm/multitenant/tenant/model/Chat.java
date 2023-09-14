package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.ChatPayload;

@Entity
@Table(name = "chat")
public class Chat {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_seq")
	//@SequenceGenerator(name = "chat_seq", sequenceName = "chat_sequence", allocationSize = 1)
	private Long id;
	
	private String userNameFrom;
	
	private String userNameTo;
	
	/*@ManyToOne(cascade = CascadeType.ALL)
	@OrderBy("nombreDescripcion ASC")
	private User from;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@OrderBy("nombreDescripcion ASC")
	private User to;*/
	
	private String mensaje;
	
	private boolean leido;

	public Chat() {
		super();
	}
	
	public Chat(ChatPayload p) {
		super();
		p.setId(this.getId());
		p.setUserNameFrom(this.getUserNameFrom());
		p.setUserNameTo(this.getUserNameTo());
		p.setMensaje(this.getMensaje());
		p.setLeido(this.getLeido());;
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
	
	
	public ChatPayload toPayload() {
		ChatPayload p = new ChatPayload();
		p.setId(this.getId());
		p.setUserNameFrom(this.getUserNameFrom());
		p.setUserNameTo(this.getUserNameTo());
		p.setMensaje(this.getMensaje());
		p.setLeido(this.getLeido());
		return p;
	}
	
}
