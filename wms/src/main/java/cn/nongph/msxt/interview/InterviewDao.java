package com.felix.msxt.interview;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.hibernate.ejb.criteria.CriteriaBuilderImpl;
import org.hibernate.ejb.criteria.predicate.ComparisonPredicate;
import org.hibernate.ejb.criteria.predicate.ComparisonPredicate.ComparisonOperator;

import com.felix.msxt.model.Interview;
import com.felix.msxt.model.Interview_;
import com.felix.msxt.model.InterviewRoundUser_;
import com.felix.msxt.model.InterviewRound_;
import com.felix.msxt.model.Candidate_;
import com.felix.msxt.model.InterviewRound;
import com.felix.msxt.model.InterviewRoundExamination;
import com.felix.msxt.model.InterviewRoundUser;
import com.felix.msxt.model.Position_;
import com.felix.nsf.CommonDao;
import com.felix.nsf.Pagination;

/**
 * 
 * @author Felix
 *
 */
@Named
@ApplicationScoped
public class InterviewDao extends CommonDao<Interview>{
	
	public Interview getInterviewByLoginNameAndPassword( String loginName, String password){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
    	
    	CriteriaQuery<Interview> cq = cb.createQuery(Interview.class);
    	Root<Interview> rp = cq.from( Interview.class );
    	cq.select( rp ).where( cb.equal(rp.get(Interview_.loginName), loginName), 
    			               cb.equal(rp.get(Interview_.loginPassword), password) );
    	
    	List<Interview> ivs = entityManager.createQuery( cq ).getResultList();
    	if( ivs.size()>0 ) 
    		return ivs.get(0);
    	else
    		return null;
	}

	@Override
	protected Class<Interview> getModelClass() {
		return Interview.class;
	}
	
	public void search(Pagination page, InterviewSearchCriteria sc){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Interview> cq = cb.createQuery(Interview.class);
        Root<Interview> root = cq.from( Interview.class );
    	
        cq.select( root );
        
        List<Predicate> pl = new ArrayList<Predicate>();
        if( StringUtils.isNotEmpty( sc.getPattern() ) )
        	pl.add( cb.like( root.get(Interview_.candidate).get( Candidate_.name) , sc.getPattern() ) );
        
        if( sc.getStartBegin() !=null ) 
        	pl.add( new ComparisonPredicate( (CriteriaBuilderImpl)cb, ComparisonOperator.GREATER_THAN_OR_EQUAL, root.get(Interview_.planStartTime), sc.getStartBegin() ) );
        
        if( sc.getStartEnd() !=null )
        	pl.add( new ComparisonPredicate( (CriteriaBuilderImpl)cb, ComparisonOperator.LESS_THAN_OR_EQUAL, root.get(Interview_.planStartTime), sc.getStartEnd() ) );
        
        if( StringUtils.isNotEmpty( sc.getPositionId() ) ) 
        	pl.add( cb.equal( root.get(Interview_.applyPosition).get(Position_.id), sc.getPositionId() ) );
        
        if( pl.size()>0 ) {
	        Predicate rp = null;
	        for( Predicate p: pl ) {
	        	if( rp == null )
	        		rp = p;
	        	else
	        		rp = cb.and( rp, p );
	        }
	        
	        cq.where( rp );
        }
        searchByPagination(cq, page);
	}
	
	public Interview getInterviewByLoginName(String loginName) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Interview> cq = cb.createQuery( Interview.class ); 
		
		Root<Interview> pathRoot = cq.from( Interview.class );
		cq.select( pathRoot ).where( cb.equal( pathRoot.get( Interview_.loginName), loginName ) );
		
		List<Interview> result = entityManager.createQuery( cq ).getResultList();
		if( result.size()>0 ) {
			return result.get(0);
		} else
			return null;
		
    }
    
     public List<InterviewRoundUser> getPendingInterviewRoundUser( String useId ){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		
        CriteriaQuery<InterviewRoundUser> cq = cb.createQuery( InterviewRoundUser.class ); 
        
        Root<InterviewRoundUser> pathRoot = cq.from( InterviewRoundUser.class );
        Predicate p1 = cb.or( cb.equal( pathRoot.get(InterviewRoundUser_.interviewRound).get(InterviewRound_.state), InterviewRound.STATE.PENDING),
        					  cb.equal( pathRoot.get(InterviewRoundUser_.interviewRound).get(InterviewRound_.state), InterviewRound.STATE.GOING) );
        Predicate p2 = cb.or( cb.isNull( pathRoot.get(InterviewRoundUser_.state ) ),
        					  cb.equal(  pathRoot.get(InterviewRoundUser_.state ), InterviewRoundUser.STATE.GOING) );
        
        cq.select( pathRoot ).where( p1, p2 );
        List<InterviewRoundUser> result = entityManager.createQuery(cq).getResultList();
        return result;
    }
     
    public InterviewRoundUser getInterviewRoundUserById( String id ){
    	return entityManager.find( InterviewRoundUser.class, id );
    }
    
    public InterviewRound getInterviewRoundById( String id ) {
        return entityManager.find( InterviewRound.class, id );
    }
    
     public InterviewRoundExamination getInterviewRoundExaminationById( String id ) {
         return entityManager.find( InterviewRoundExamination.class, id );
     }
}
