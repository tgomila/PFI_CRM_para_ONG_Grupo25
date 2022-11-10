package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class EmpleadoNombreTablaPayload extends TrabajadorNombreTablaAbstractPayload {
	
	public LinkedHashMap<String, String> getNombresEmpleadoTabla(){
		LinkedHashMap<String, String> nombres = getNombresTrabajadorTabla();
		nombres.put("funcion", "Función");
		nombres.put("descripcion", "Descripción");
		return nombres;
	}
}
