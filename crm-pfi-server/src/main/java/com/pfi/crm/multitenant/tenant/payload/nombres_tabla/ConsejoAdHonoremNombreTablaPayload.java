package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class ConsejoAdHonoremNombreTablaPayload extends PersonaFisicaNombreTablaAbstractPayload {
	
	public LinkedHashMap<String, String> getNombresConsejoAdHonoremTabla(){
		LinkedHashMap<String, String> nombres = getNombresPersonaFisicaTabla();
		nombres.put("funcion", "Función");
		return nombres;
	}
}
