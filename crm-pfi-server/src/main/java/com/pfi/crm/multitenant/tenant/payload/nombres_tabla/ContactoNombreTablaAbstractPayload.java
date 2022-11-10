package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public abstract class ContactoNombreTablaAbstractPayload {
	
	public LinkedHashMap<String, String> getNombresContactoTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("nombreDescripcion", "Nombre/Descripción");
		nombres.put("cuit", "Cuit");
		nombres.put("domicilio", "Domicilio");
		nombres.put("email", "Email");
		nombres.put("telefono", "Telefono");
		return nombres;
	}
	
	
}
