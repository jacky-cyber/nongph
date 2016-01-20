package com.felix.exam.examination;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;

import com.felix.exam.model.Examination;
import com.felix.exam.model.ExaminationCatalog;
import com.felix.exam.model.Examination_;
import com.felix.msxt.model.Position_;
import com.felix.nsf.CommonDao;
import com.felix.nsf.Pagination;

@Named
@ApplicationScoped
public class ExaminationDao extends CommonDao<Examination>{

	@Override
	protected Class<Examination> getModelClass() {
		return Examination.class;
	}
	
	public void search(Pagination pagination, String positionId, String pattern){
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Examination> cquery = builder.createQuery(Examination.class);
		Root<Examination> rp = cquery.from(Examination.class);
		
		if( StringUtils.isEmpty( positionId ) ) {
			Predicate p1 = builder.like( builder.lower( rp.get(Examination_.name) ), pattern );
	        cquery.select( rp ).where( p1);  
		} else {
			Predicate p1 = builder.like( builder.lower( rp.get(Examination_.name) ), pattern );
	        Predicate p2 = builder.equal( rp.get(Examination_.position).get(Position_.id), positionId);
	        cquery.select( rp ).where( p1, p2);   
		}
		
		searchByPagination( cquery, pagination );
		
		List<Examination> exams = (List<Examination>)pagination.getItems();
        for( Examination e : exams ) {
        	e.setOnUsed( isOnUsed(e) );
        }
	}
	
	public boolean isOnUsed( Examination e ) {
		Object result = entityManager.createQuery("select COUNT(ire) from InterviewRoundExamination ire where ire.exam=:exam").setParameter("exam", e).getSingleResult();
		return (Long)result>0;
	}
	
	public void deleteCatalog( ExaminationCatalog ec ) {
		entityManager.remove( ec );
	}
}
