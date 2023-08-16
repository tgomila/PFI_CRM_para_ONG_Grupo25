package com.pfi.crm.multitenant.tenant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.pfi.crm.multitenant.tenant.payload.ConsejoAdHonoremPayload;

@Entity
@Table(name="consejoAdHonorem")
public class ConsejoAdHonorem extends PersonaFisicaAbstract {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4866410821724580413L;

	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "consejo_ad_honorem_seq")
	@SequenceGenerator(name = "consejo_ad_honorem_seq", sequenceName = "consejo_ad_honorem_sequence", allocationSize = 1)
	private Long idConsejoAdHonorem;
	
	private String funcion;
	
	private boolean estadoActivoConsejoAdHonorem;
	
	
	//Constructores
	public ConsejoAdHonorem() {
		super();
		this.setIdConsejoAdHonorem(null);
		this.setEstadoActivoConsejoAdHonorem(true);
	}
	
	public ConsejoAdHonorem(ConsejoAdHonoremPayload payload) {
		super();
		this.modificar(payload);
		this.setIdConsejoAdHonorem(null);
		this.setEstadoActivoConsejoAdHonorem(true);
	}
	
	public void modificar(ConsejoAdHonoremPayload payload) {
		this.modificarPersonaFisica(payload);
		this.setFuncion(payload.getFuncion());
	}
	
	public ConsejoAdHonoremPayload toPayload() {
		ConsejoAdHonoremPayload payload = new ConsejoAdHonoremPayload();
		payload.modificarPersonaFisica(this.toPersonaFisicaPayload());
		payload.setFuncion(this.getFuncion());
		return payload;
	}
	
	
	//Getters y Setters
	public Long getIdConsejoAdHonorem() {
		return idConsejoAdHonorem;
	}

	public void setIdConsejoAdHonorem(Long idConsejoAdHonorem) {
		this.idConsejoAdHonorem = idConsejoAdHonorem;
	}

	public String getFuncion() {
		return funcion;
	}

	public void setFuncion(String funcion) {
		this.funcion = funcion;
	}

	public boolean isEstadoActivoConsejoAdHonorem() {
		return estadoActivoConsejoAdHonorem;
	}

	public void setEstadoActivoConsejoAdHonorem(boolean estadoActivoConsejoAdHonorem) {
		this.estadoActivoConsejoAdHonorem = estadoActivoConsejoAdHonorem;
	}
}
