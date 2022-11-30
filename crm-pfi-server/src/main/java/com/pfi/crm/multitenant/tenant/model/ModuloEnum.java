package com.pfi.crm.multitenant.tenant.model;

public enum ModuloEnum {
	CONTACTO(1, "Contacto", "/contacto", "AiFillContacts"),
	PERSONA(2, "Persona", "/personafisica", "GoPerson"),
	BENEFICIARIO(3, "Beneficiario", "/beneficiario", "GoRocket"),
	EMPLEADO(4, "Empleado", "/empleado", "GoBriefcase"),
	COLABORADOR(5, "Colaborador", "/colaborador", "GoOrganization"),
	CONSEJOADHONOREM(6, "Consejo Adhonorem", "/consejoadhonorem", "GoNote"),
	PERSONAJURIDICA(7, "Persona Juridica", "/personajuridica", "FaBuilding"),
	PROFESIONAL(8, "Profesional", "/profesional", "FaUserCheck"),
	DONACION(9, "Donacion", "/donacion", "FaDonate"),
	FACTURA(10, "Factura", "/factura", "FaFileInvoiceDollar"),
	USERS(11, "Users", "/users", "FaUsersCog"),
	MARKETPLACE(12, "Marketplace", "/marketplace", "MdLocalGroceryStore");
	
	
	private final int order;
	private final String name;
	private final String path;
	private final String iconName;
	
	private ModuloEnum(int order, String name, String path, String iconName) {
		this.order = order;
		this.name = name;
		this.path = path;
		this.iconName = iconName;
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
}
