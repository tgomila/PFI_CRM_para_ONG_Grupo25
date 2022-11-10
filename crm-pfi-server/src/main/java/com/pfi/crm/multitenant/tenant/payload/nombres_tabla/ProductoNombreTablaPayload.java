package com.pfi.crm.multitenant.tenant.payload.nombres_tabla;

import java.util.LinkedHashMap;

public class ProductoNombreTablaPayload {
	
	public LinkedHashMap<String, String> getNombresProductoTabla(){
		LinkedHashMap<String, String> nombres = new LinkedHashMap<String, String>();
		nombres.put("id", "ID");
		nombres.put("descripcion", "Descripción");
		nombres.put("precioVenta", "Precio de venta");
		nombres.put("cantFijaCompra", "Cantidad fija de compra");
		nombres.put("cantMinimaStock", "Cantidad mínima stock");
		nombres.put("stockActual", "Stock actual");
		nombres.put("estadoActivo", "Esta activo?");
		nombres.put("fragil", "Frágil");
		nombres.put("proveedor", "Proveedor");
		return nombres;
	}

}
