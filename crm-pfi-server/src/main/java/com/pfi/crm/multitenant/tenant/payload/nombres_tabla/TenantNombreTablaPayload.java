package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class TenantNombreTablaPayload {
	
	public LinkedHashMap<String, String> getNombresTenantTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("tenantClientId", "ID ONG");
		nombres.put("dbName", "Nombre de la base de datos");
		nombres.put("tenantName", "Nombre de la ONG");
		return nombres;
	}

}
