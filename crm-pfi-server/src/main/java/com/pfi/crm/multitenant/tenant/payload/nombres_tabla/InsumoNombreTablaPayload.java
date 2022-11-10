package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class InsumoNombreTablaPayload {
	
	public LinkedHashMap<String, String> getNombresInsumoTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("id", "ID");
		nombres.put("fechaInsumo", "Fecha insumo");
		nombres.put("cantidad", "Cantidad");
		nombres.put("producto", "Producto");
		nombres.put("responsableInsumo", "Responsable insumo");
		return nombres;
	}

}
