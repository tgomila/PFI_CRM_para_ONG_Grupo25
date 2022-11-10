package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class ProfesionalNombreTablaPayload extends TrabajadorNombreTablaAbstractPayload {
	
	public LinkedHashMap<String, String> getNombresProfesionalTabla(){
		LinkedHashMap<String, String> nombres = getNombresTrabajadorTabla();
		nombres.put("profesion", "Profesión");
		return nombres;
	}
}
