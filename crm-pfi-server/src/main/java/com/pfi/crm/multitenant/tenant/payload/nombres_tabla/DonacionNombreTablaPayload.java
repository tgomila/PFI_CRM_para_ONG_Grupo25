package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class DonacionNombreTablaPayload {
	
	public LinkedHashMap<String, String> getNombresDonacionTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("id", "ID");
		nombres.put("fecha", "Fecha");
		nombres.put("donante", "Donante");
		nombres.put("tipoDonacion", "Tipo de donación");
		nombres.put("descripcion", "Descripción");
		return nombres;
	}
}
