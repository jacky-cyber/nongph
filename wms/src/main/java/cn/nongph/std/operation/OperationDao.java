package com.felix.std.operation;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.felix.std.model.Operation_;
import com.felix.nsf.CommonDao;
import com.felix.std.model.Operation;
@ApplicationScoped
@Named
public class OperationDao extends CommonDao<Operation>{

	public Operation getOperationByCode(String code) {
		CriteriaBuilder cb =  entityManager.getCriteriaBuilder();
		CriteriaQuery<Operation> cq = cb.createQuery(Operation.class);
		Root<Operation> ro = cq.from(Operation.class);
		cq.select(ro).where(cb.equal(cb.lower(ro.get(Operation_.code)), "code"));
		return null;
	}

	public Operation get(String id) {
		return super.get(id);
	}

	public List<Operation> getAll() {
		return super.getAll();
	}

	@Override
	protected Class<Operation> getModelClass() {
		return Operation.class;
	}

}
