package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class ProyectoNombreTablaPayload {
	
	public LinkedHashMap<String, String> getNombresProyectoTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("id", "ID");
		nombres.put("descripcion", "Descripción");
		nombres.put("fechaInicio", "Fecha de inicio");
		nombres.put("fechaFin", "Fecha finalización");
		nombres.put("involucrados", "Involucrados");
		return nombres;
	}

}
