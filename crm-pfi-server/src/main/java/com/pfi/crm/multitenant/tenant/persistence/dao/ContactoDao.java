package com.pfi.crm.multitenant.tenant.persistence.dao;

import com.pfi.crm.multitenant.tenant.model.Contacto;
import com.pfi.crm.multitenant.tenant.persistence.filter.ContactoFilter;

public interface ContactoDao extends BaseDao<Contacto, Long, ContactoFilter> {
	
}
