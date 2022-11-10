package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class FacturaItemNombreTablaPayload {
	
	public LinkedHashMap<String, String> getNombresFacturaItemTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("id", "ID");
		nombres.put("descripcion", "Descripción");
		nombres.put("unidades", "Unidades");
		nombres.put("precioUnitario", "Precio unitario");
		nombres.put("precio", "Precio");
		return nombres;
	}

}
