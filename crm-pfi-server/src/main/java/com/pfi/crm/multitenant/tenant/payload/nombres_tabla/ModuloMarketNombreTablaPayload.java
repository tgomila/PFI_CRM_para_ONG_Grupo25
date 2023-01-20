package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class ModuloMarketNombreTablaPayload {
	
	public LinkedHashMap<String, String> getNombresModuloMarketTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("id", "ID");
		nombres.put("moduloEnum", "Módulo");
		nombres.put("prueba7DiasUtilizada", "Período de prueba utilizado?");
		nombres.put("fechaPrueba7DiasUtilizada", "Fecha cuando se utilizó el período de prueba");
		nombres.put("fechaMaximaSuscripcion", "Fecha vencimiento de suscripción");
		nombres.put("suscripcionActiva", "Suscripción activa");
		return nombres;
	}

}
