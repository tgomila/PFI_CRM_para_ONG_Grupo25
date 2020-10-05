package com.pfi.crm.payload.request;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.pfi.crm.model.TipoPersonaJuridica;

public class PersonaJuridicaRequest extends ContactoAbstractRequest{
	
	private String internoTelefono;
	
	@Enumerated(EnumType.STRING)
	private TipoPersonaJuridica tipoPersonaJuridica;

	public String getInternoTelefono() {
		return internoTelefono;
	}

	public void setInternoTelefono(String internoTelefono) {
		this.internoTelefono = internoTelefono;
	}

	public TipoPersonaJuridica getTipoPersonaJuridica() {
		return tipoPersonaJuridica;
	}

	public void setTipoPersonaJuridica(TipoPersonaJuridica tipoPersonaJuridica) {
		this.tipoPersonaJuridica = tipoPersonaJuridica;
	}
	
	
}
