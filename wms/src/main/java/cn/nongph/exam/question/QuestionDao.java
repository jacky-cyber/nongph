package com.felix.exam.question;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.felix.exam.model.Question;
import com.felix.exam.model.QuestionType_;
import com.felix.exam.model.Question_;

import com.felix.msxt.model.PositionQuestion;
import com.felix.msxt.model.PositionQuestion_;
import com.felix.msxt.model.Position_;

import com.felix.nsf.CommonDao;
import com.felix.nsf.Pagination;

@ApplicationScoped
@Named
public class QuestionDao extends CommonDao<Question>{
		
	public void searchAll(Pagination pagination, String typeId, String positionId, String pattern){
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Question> cquery = builder.createQuery(Question.class);
		
		if( positionId!=null && !positionId.isEmpty() ) {
	        Root<PositionQuestion> rp = cquery.from(PositionQuestion.class);
	        Path<Question> qp = rp.get(PositionQuestion_.question);
	        
	        Predicate p1 = builder.or(
					builder.like( builder.lower( qp.get(Question_.name) ), pattern ),
					builder.like( builder.lower( qp.get(Question_.content) ), pattern ) );
	        
	        Predicate p2 = builder.equal( rp.get(PositionQuestion_.position).get(Position_.id), positionId);
	        
	        if( typeId!=null && !typeId.isEmpty() ) {
	        	Predicate p3 = builder.equal( qp.get(Question_.questionType).get(QuestionType_.id), typeId );
	        	cquery.select( qp ).where( p1, p2, p3);
	        } else {
	        	cquery.select( qp ).where( p1, p2);
	        } 
	        cquery.orderBy( builder.asc(qp.get( Question_.name) ) );
		} else {
			Root<Question> qp = cquery.from(Question.class);
	        Predicate p1 = builder.or(
					builder.like( builder.lower( qp.get(Question_.name) ), pattern ),
					builder.like( builder.lower( qp.get(Question_.content) ), pattern ) );	        
	        if( typeId!=null && typeId.length()>0 ) {
	        	Predicate p3 = builder.equal( qp.get(Question_.questionType).get(QuestionType_.id), typeId );
	        	cquery.select( qp ).where( p1, p3);
	        } else {
	        	cquery.select( qp ).where( p1 );
	        }   
	        cquery.orderBy( builder.asc(qp.get( Question_.name) ) );
		}
		
		searchByPagination( cquery, pagination );
		List<Question> result = (List<Question>)pagination.getItems(); 
		
		for( Question q : result ) {
			String sql = "select count(*) from examination_catalog_question eq " +
					     "                     join examination_catalog ec on eq.catalog_id = ec.id" +
                         "                     join examination e on ec.exam_id = e.id" +
                         "                     join interview_round_examination ire on e.id=ire.exam_id" +
                         "				  where eq.question_id = ?";
			 Object c = entityManager.createNativeQuery(sql).setParameter( 1, q.getId() ).getSingleResult();
			 if( ((Number)c).intValue()>0 )
    			q.setOnUsed( true );
    		 else
    			q.setOnUsed( false );
		}
	}

	@Override
	protected Class<Question> getModelClass() {
		return Question.class;
	}
	
	public void cleanQuestionChoiceItem(Question q){
		entityManager.createQuery("delete QuestionChoiceItem item where item.question=:q").setParameter("q", q).executeUpdate();
	}
	
	public int getExaminationCount(Question q){
		Object result = entityManager.createQuery("select COUNT(ecq) from ExaminationCatalogQuestion ecq where ecq.question=:q")
		                             .setParameter("q", q)
		                             .getSingleResult();
		return ((Number)result).intValue();
	}
}
