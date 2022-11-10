package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class UserNombreTablaPayload {
	
	public LinkedHashMap<String, String> getNombresuserTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("id", "ID");
		nombres.put("name", "Nombre");
		nombres.put("email", "Email");
		nombres.put("roles", "Roles del usuario");
		return nombres;
	}

}
