package com.felix.msxt.position;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.felix.exam.model.Question;
import com.felix.msxt.model.Position;
import com.felix.msxt.model.PositionQuestion;
import com.felix.msxt.model.PositionQuestion_;
import com.felix.msxt.model.Position_;
import com.felix.nsf.CommonDao;
import com.felix.nsf.Pagination;

@ApplicationScoped
@Named
public class PositionDao extends CommonDao<Position>{
	
    public void doSearch( Pagination pagination, String name ) {
    	CriteriaBuilder builder = entityManager.getCriteriaBuilder();
    	CriteriaQuery<Position> cQuery = builder.createQuery(Position.class);
     
    	Root<Position> rp = cQuery.from(Position.class);
    	cQuery.select( rp ).where( builder.like( builder.lower( rp.get(Position_.name) ), name) );

    	searchByPagination( cQuery, pagination);
    }

	@Override
	protected Class<Position> getModelClass() {
		return Position.class;
	}
	
	public void cleanPositionQuestion(Question q){
		entityManager.createQuery("delete PositionQuestion pq where pq.question=:q").setParameter("q", q).executeUpdate();
	}
	
	public List<Position> getQuestionPosition( Question q) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Position> cq = cb.createQuery( Position.class );
		Root<PositionQuestion> root = cq.from( PositionQuestion.class );
		cq.select( root.get(PositionQuestion_.position) ).where( cb.equal( root.get(PositionQuestion_.question), q) );
		
		return entityManager.createQuery( cq ).getResultList();
	}
}
