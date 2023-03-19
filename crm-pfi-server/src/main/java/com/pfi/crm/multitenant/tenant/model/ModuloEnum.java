package com.pfi.crm.multitenant.tenant.model;

public enum ModuloEnum {
	//id, name, path, iconName, paid version, double money (dollar)
	CONTACTO(1, "Contacto", "/contacto", "AiFillContacts", 0.00),
	PERSONA(2, "Persona", "/personafisica", "GoPerson", 0.00),
	BENEFICIARIO(3, "Beneficiario", "/beneficiario", "GoRocket", 0.00),
	VOLUNTARIO(4, "Voluntario", "/voluntario", "GoRocket", 0.00),
	EMPLEADO(5, "Empleado", "/empleado", "GoBriefcase", 0.00),
	COLABORADOR(6, "Colaborador", "/colaborador", "GoOrganization", 0.00),
	CONSEJOADHONOREM(7, "Consejo Adhonorem", "/consejoadhonorem", "GoNote", 0.00),
	PERSONAJURIDICA(8, "Persona Juridica", "/personajuridica", "FaBuilding", 0.00),
	PROFESIONAL(9, "Profesional", "/profesional", "FaUserCheck", 0.00),
	USERS(10, "Users", "/users", "FaUsersCog", 0.00),
	
	ACTIVIDAD(11, "Actividad", "/actividad", "BiTask", 3.00),
	PROGRAMA_DE_ACTIVIDADES(12, "Programa de Actividades", "/programadeactividades", "GoCalendar", 1.00),
	PRODUCTO(13, "Producto", "/producto", "GiHandTruck", 0.00),
	DONACION(14, "Donacion", "/donacion", "FaDonate", 0.00),
	FACTURA(15, "Factura", "/factura", "FaFileInvoiceDollar", 2.00),
	INSUMO(16, "Insumo", "/insumo", "FiShoppingCart", 2.00),
	PRESTAMO(17, "Prestamo", "/prestamo", "FaHandHolding", 2.00),
	PROYECTO(18, "Proyecto", "/proyecto", "AiOutlineFundProjectionScreen", 2.00),
	CHAT(19, "Chat", "/chat", "HiChatBubbleLeftRight", 2.00),
	MARKETPLACE(20, "Marketplace", "/marketplace", "MdLocalGroceryStore", 0.00);
	
	
	private final int order;
	private final String name;
	private final String path;
	private final String iconName;
	//private final boolean paidModule; //0.00 significa free, >0.00 significa paid
	private final double priceOneMonth;
	
	private ModuloEnum(int order, String name, String path, String iconName, double priceOneMonth) {
		this.order = order;
		this.name = name;
		this.path = path;
		this.iconName = iconName;
		this.priceOneMonth = priceOneMonth;
	}

	public int getOrder() {
		return order;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getIconName() {
		return iconName;
	}

	public boolean isFreeModule() {
		return !isPaidModule();
	}

	public boolean isPaidModule() {
		return priceOneMonth > 0.00;
	}

	public double getPriceOneMonth() {
		return priceOneMonth;
	}
	
	public double getPriceOneYear() {
		return priceOneMonth*10;
	}
}
