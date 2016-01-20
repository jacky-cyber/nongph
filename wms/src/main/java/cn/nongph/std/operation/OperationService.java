package com.felix.std.operation;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.felix.std.model.Operation;

@ApplicationScoped
@Named
public class OperationService {
	
	@Inject
	private OperationDao dao;

	public Operation findByCode(String code) {
		return dao.getOperationByCode(code);
	}
	public Operation get(String id) {
		return dao.get(id);
	}

	public List<Operation> getAll() {
		return dao.getAll();
	}
}