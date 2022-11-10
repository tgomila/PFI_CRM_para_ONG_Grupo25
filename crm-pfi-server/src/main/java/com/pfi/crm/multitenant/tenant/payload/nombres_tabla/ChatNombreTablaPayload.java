package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class ChatNombreTablaPayload {
	
	public LinkedHashMap<String, String> getNombresChatTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("id", "ID");
		nombres.put("userNameFrom", "Mensaje de");
		nombres.put("userNameTo", "Mensaje a");
		nombres.put("mensaje", "Mensaje");
		nombres.put("leido", "Mensaje leído? (Si/No)");
		return nombres;
	}
}
