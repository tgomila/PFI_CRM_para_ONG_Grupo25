package com.pfi.crm.multitenant.tenant.model;

public enum ModuloEnum {
	CONTACTO("Contacto", "/contacto", "AiFillContacts"),
	PERSONA("Persona", "/personafisica", "GoPerson"),
	BENEFICIARIO("Beneficiario", "/beneficiario", "GoRocket"),
	EMPLEADO("Empleado", "/empleado", "GoBriefcase"),
	COLABORADOR("Colaborador", "/colaborador", "GoOrganization"),
	CONSEJOADHONOREM("Consejo Adhonorem", "/consejoadhonorem", "GoNote"),
	PERSONAJURIDICA("/personajuridica", "/personajuridica", "FaBuilding"),
	PROFESIONAL("Profesional", "/profesional", "FaUserCheck"),
	DONACION("Donacion", "/donacion", "FaDonate"),
	FACTURA("Factura", "/factura", "FaFileInvoiceDollar"),
	USERS("Users", "/users", "FaUsersCog");
	
	
	private final String name;
	private final String path;
	private final String iconName;
	
	private ModuloEnum(String name, String path, String iconName) {
		this.name = name;
		this.path = path;
		this.iconName = iconName;
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
