package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class PersonaJuridicaNombreTablaPayload extends ContactoNombreTablaAbstractPayload {
	
	public LinkedHashMap<String, String> getNombresPersonaJuridicaTabla(){
		LinkedHashMap<String, String> nombres = getNombresContactoTabla();
		nombres.put("internoTelefono", "Interno telefono");
		nombres.put("tipoPersonaJuridica", "Tipo de persona juridica");
		return nombres;
	}

}
