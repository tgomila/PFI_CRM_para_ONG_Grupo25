package com.pfi.crm.multitenant.tenant.persistence.dao;

import java.util.List;

//M model, ID id, F filter
public interface BaseDao<M, ID, F> {
	
	M findById(ID id);
	
	List<M> search(F filter);
	
}
