package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class ColaboradorNombreTablaPayload extends TrabajadorNombreTablaAbstractPayload {
	
	public LinkedHashMap<String, String> getNombresColaboradorTabla(){
		LinkedHashMap<String, String> nombres = getNombresPersonaFisicaTabla();
		nombres.put("area", "Área");
		return nombres;
	}
}
