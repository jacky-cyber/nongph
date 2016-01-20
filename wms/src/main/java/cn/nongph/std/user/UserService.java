package com.felix.std.user;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.felix.std.model.Operation;
import com.felix.std.model.Role;
import com.felix.std.model.User;
import com.felix.nsf.CommonDao;
import com.felix.nsf.CommonService;
import com.felix.nsf.Pagination;
import com.felix.std.role.RoleDao;

@ApplicationScoped
@Named
public class UserService extends CommonService<User> {
	@Inject
	UserDao userDao;
	@Inject
	RoleDao roleDao;
	public User checkUser(String userName, String password){
		return userDao.checkUser( userName, password );
	}
	public void find(Pagination page, Map<String, String> param) {
		userDao.find(page,param);
	}
	public void save(User o, List<String> roleIds){
		o.getRoles().clear();
		for( String roleId : roleIds ) 
			o.getRoles().add( roleDao.get( roleId ) );
		if( o.getId().isEmpty() ) 
			o.setId( null );
		userDao.persist(o);
	}

	public void delete(String userId){
		User user = userDao.get(userId);
		user.setDelete(true);
		userDao.persist(user);
	}
	public void loadPermisions(User user) {
		for (Role role : user.getRoles()) {
			for (Operation p : role.getPermisions()) {
				user.getPrivileges().add(p.getCode());
			}
		}
	}
	
	@Override
	public CommonDao<User> getDao() {
		return userDao;
	}
}
