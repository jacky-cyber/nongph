package com.felix.std.role;


import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.felix.nsf.CommonDao;
import com.felix.nsf.CommonService;
import com.felix.nsf.Pagination;
import com.felix.std.model.Role;
import com.felix.std.operation.OperationDao;

@ApplicationScoped
@Named
public class RoleService extends CommonService<Role> {
	
	@Inject
	private RoleDao roleDao;
	
	@Inject
	private OperationDao operationDao;
	
    public void search( Pagination p, String name ){
    	roleDao.doSearch( p, name );
    }
    
    public void save( Role role, List<String> operationIds){
    	for( String operationId: operationIds ) {
    		role.getPermisions().add( operationDao.get( operationId ) );
    	}
    	
    	roleDao.persist( role );
    }
    
	@Override
	public CommonDao<Role> getDao() {
		return roleDao;
	}
    
}
