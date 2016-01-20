package com.felix.std.operation;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.felix.std.model.Operation;

@ApplicationScoped
@Named
public class OperationAction {
	
	@Inject
	private OperationService operationService;
	
	public List<Operation> getAllOperations(){
		return operationService.getAll();
	}
}
