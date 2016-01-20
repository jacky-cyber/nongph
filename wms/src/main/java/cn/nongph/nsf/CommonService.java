package com.felix.nsf;

import java.util.List;

import javax.ejb.TransactionManagement;

@TransactionManagement
public abstract class CommonService<T> {
	
	public abstract CommonDao<T> getDao();
	
	public T get(String id){
		return getDao().get( id );
	}
	
	public List<T> getAll(){
		return getDao().getAll();
	}
	
	public void persist(Object o) {
		getDao().persist( o );
	}
	
	public void delete(Object o){
		getDao().remove( o );
	}
	
	public void detach( Object o ){
		getDao().detach( o );
	}
}
