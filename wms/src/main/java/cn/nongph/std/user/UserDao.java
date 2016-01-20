package com.felix.std.user;

import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.felix.std.model.User;
import com.felix.std.model.User_;
import com.felix.nsf.CommonDao;
import com.felix.nsf.Pagination;

@ApplicationScoped
@Named("userDao")
public class UserDao extends CommonDao<User>{
	 
	 public User checkUser(String userName, String password){
	    	CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	    	
	    	CriteriaQuery<User> cq = cb.createQuery(User.class);
	    	Root<User> fr = cq.from(User.class);
	    	cq.select( fr ).where( cb.equal(fr.get(User_.loginname), userName) );
	    	
	    	TypedQuery<User> q = entityManager.createQuery( cq );
	    	List<User> users = q.getResultList();
	    	if( users.size()>0 )
	    		return users.get(0);
	    	else
	    		return null;
	}

	public void find(Pagination page, Map<String, String> param) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		
    	CriteriaQuery<User> cQuery = builder.createQuery(User.class);
    	Root<User> rp = cQuery.from(User.class);
    	cQuery.select( rp )
    	      .where( builder.like( builder.lower( rp.get(User_.name) ), param.get("name") ),
    	    		  builder.or( builder.isNull( rp.get(User_.isDelete) ) , 
    	    				      builder.equal( rp.get(User_.isDelete), Boolean.FALSE) ) );
    	searchByPagination( cQuery, page);
	}

	@Override
	protected Class<User> getModelClass() {
		return User.class;
	}
}
