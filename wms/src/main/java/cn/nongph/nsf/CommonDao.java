package com.felix.nsf;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;

import org.hibernate.ejb.criteria.CriteriaQueryImpl;

import com.felix.nsf.Pagination;

public abstract class CommonDao<T> {

	@PersistenceContext
    protected EntityManager entityManager;
	
	/**
	 * find by pagination
	 * @param cQuery
	 * @param pagination
	 */
	public void searchByPagination(CriteriaQuery<T> cQuery, Pagination pagination){
		List<T> results = entityManager.createQuery( cQuery )
		                                .setMaxResults( pagination.getLimit() )
		                                .setFirstResult( pagination.getStart() )
		                                .getResultList();
		pagination.setItems( results );
		 
		CriteriaBuilder cQueryBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> cQueryCount = cQueryBuilder.createQuery( Long.class );
		 
		try{
			Field field = CriteriaQueryImpl.class.getDeclaredField( "queryStructure" );
			field.setAccessible( true );
			field.set( cQueryCount, field.get( cQuery ) );
		}catch(Exception e){
			e.printStackTrace();
		}
		 
		cQueryCount.select( cQueryBuilder.count( (Expression<T>)cQuery.getSelection() ) );
		 
		Long totalCount = entityManager.createQuery( cQueryCount ).getSingleResult();
		 
		pagination.setTotalCount( totalCount.intValue() );
	}
	
	public List<T> getAll(){
		CriteriaBuilder cqb =  entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = (CriteriaQuery<T>)cqb.createQuery( getModelClass() );
		cq.select( cq.from( getModelClass() ) );
		
		return entityManager.createQuery( cq ).getResultList();
	}
	
	public T get( String id ) {
		return entityManager.find( getModelClass(), id );
	}
	
	public void persist( Object object ) {
		entityManager.persist( object );
	}
	
	public void remove( Object o ) {
		entityManager.remove(o);
	}
	
	public void detach( Object o ) {
		entityManager.detach( o );
	}
	
	protected abstract Class<T> getModelClass();
}
