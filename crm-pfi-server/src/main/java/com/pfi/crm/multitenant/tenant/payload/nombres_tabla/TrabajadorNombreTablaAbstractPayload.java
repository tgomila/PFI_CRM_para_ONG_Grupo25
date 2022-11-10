package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public abstract class TrabajadorNombreTablaAbstractPayload extends PersonaFisicaNombreTablaAbstractPayload {
	
	public LinkedHashMap<String, String> getNombresTrabajadorTabla(){
		LinkedHashMap<String, String> nombres = getNombresPersonaFisicaTabla();
		nombres.put("datosBancarios", "Datos Bancarios");
		return nombres;
	}
}
