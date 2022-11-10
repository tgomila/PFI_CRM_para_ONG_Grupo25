package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class ActividadNombreTablaPayload {
	public LinkedHashMap<String, String> getNombresActividadTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("id", "ID");
		nombres.put("fechaHoraDesde", "Fecha y hora desde");
		nombres.put("fechaHoraHasta", "Fecha y hora hasta");
		nombres.put("beneficiarios", "Beneficiarios");
		nombres.put("profesionales", "Profesionales");
		nombres.put("descripcion", "Descripción");
		return nombres;
	}
}
