package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class ProgramaDeActividadesNombreTablaPayload {
	
	public LinkedHashMap<String, String> getNombresProgramaDeActividadesTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("id", "ID");
		nombres.put("fechaDesde", "Fecha desde");
		nombres.put("fechaHasta", "Fecha hasta");
		nombres.put("fechaAltaPrograma", "Fecha alta programa");
		nombres.put("fechaBajaPrograma", "Fecha baja programa");
		nombres.put("actividades", "Actividades");
		nombres.put("descripcion", "Descripción");
		return nombres;
	}

}
