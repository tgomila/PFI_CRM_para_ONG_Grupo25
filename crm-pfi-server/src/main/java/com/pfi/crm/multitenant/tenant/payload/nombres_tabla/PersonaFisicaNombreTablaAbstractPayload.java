package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public abstract class PersonaFisicaNombreTablaAbstractPayload extends ContactoNombreTablaAbstractPayload {
	
	public LinkedHashMap<String, String> getNombresPersonaFisicaTabla(){
		LinkedHashMap<String, String> nombres = getNombresContactoTabla();
		nombres.put("dni", "DNI");
		nombres.put("nombre", "Nombre");
		nombres.put("apellido", "Apellido");
		nombres.put("fechaNacimiento", "Fecha de nacimiento");
		return nombres;
	}
	
}
