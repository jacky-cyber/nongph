package com.felix.std.role;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.felix.std.model.Role_;
import com.felix.nsf.CommonDao;
import com.felix.nsf.Pagination;
import com.felix.std.model.Role;

@ApplicationScoped
@Named
public class RoleDao extends CommonDao<Role>{
	
    public void doSearch( Pagination pagination, String name ) {
    	CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    	CriteriaQuery<Role> cQuery = builder.createQuery(Role.class);
     
    	Root<Role> rp = cQuery.from(Role.class);
    	cQuery.select( rp ).where( builder.like( builder.lower( rp.get(Role_.name) ), name) );
    	searchByPagination( cQuery, pagination);
    }
    
	@Override
	protected Class<Role> getModelClass() {
		return Role.class;
	}
}
