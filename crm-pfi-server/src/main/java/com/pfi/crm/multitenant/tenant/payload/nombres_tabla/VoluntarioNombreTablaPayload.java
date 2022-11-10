package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class VoluntarioNombreTablaPayload extends PersonaFisicaNombreTablaAbstractPayload {
	
	public LinkedHashMap<String, String> getNombresVoluntarioTabla(){
		LinkedHashMap<String, String> nombres = getNombresPersonaFisicaTabla();
		return nombres;
	}

}
