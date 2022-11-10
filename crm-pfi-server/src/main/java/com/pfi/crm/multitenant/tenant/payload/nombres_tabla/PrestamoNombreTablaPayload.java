package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class PrestamoNombreTablaPayload {
	
	public LinkedHashMap<String, String> getNombresPrestamoTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("id", "ID");
		nombres.put("descripcion", "Descripción");
		nombres.put("cantidad", "Cantidad");
		nombres.put("fechaPrestamoInicio", "Fecha de inicio de préstamo");
		nombres.put("fechaPrestamoFin", "Fecha de fín de préstamo");
		return nombres;
	}

}
