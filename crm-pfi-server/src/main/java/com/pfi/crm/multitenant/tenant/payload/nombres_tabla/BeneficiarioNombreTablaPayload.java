package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class BeneficiarioNombreTablaPayload extends PersonaFisicaNombreTablaAbstractPayload {
	
	public LinkedHashMap<String, String> getNombresBeneficiarioTabla(){
		LinkedHashMap<String, String> nombres = getNombresPersonaFisicaTabla();
		nombres.put("idONG", "ID de ONG");
		nombres.put("legajo", "Nro de legajo");
		nombres.put("lugarDeNacimiento", "Lugar de nacimiento");
		nombres.put("seRetiraSolo", "Se retira solo");
		nombres.put("cuidadosEspeciales", "Cuidados especiales");
		nombres.put("escuela", "Escuela");
		nombres.put("grado", "Grado");
		nombres.put("turno", "Turno");
		return nombres;
	}
}
