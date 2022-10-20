package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.HashMap;

public abstract class ContactoNombreTablaAbstractPayload {
	
	public HashMap<String, String> getNombresContactoTabla(){
		HashMap<String, String> nombres = new HashMap<String, String>();
		nombres.put("nombreDescripcion", "Nombre/Descripción");
		nombres.put("cuit", "Cuit");
		nombres.put("domicilio", "Domicilio");
		nombres.put("email", "Email");
		nombres.put("telefono", "Telefono");
		return nombres;
	}
	
	
}
