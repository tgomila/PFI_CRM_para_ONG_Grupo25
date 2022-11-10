package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class FacturaNombreTablaPayload {
	
	public LinkedHashMap<String, String> getNombresFacturaTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("id", "ID");
		nombres.put("fecha", "Fecha");
		nombres.put("cliente", "Cliente");
		nombres.put("emisorFactura", "Emisor factura");
		nombres.put("itemsFactura", "Items factura");
		return nombres;
	}

}
