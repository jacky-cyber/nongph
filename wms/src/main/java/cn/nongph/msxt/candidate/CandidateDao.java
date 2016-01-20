package com.felix.msxt.candidate;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.felix.msxt.model.Interview;
import com.felix.msxt.model.Interview_;
import com.felix.msxt.model.Candidate;
import com.felix.msxt.model.Candidate_;
import com.felix.nsf.CommonDao;
import com.felix.nsf.Pagination;

@ApplicationScoped
@Named
public class CandidateDao extends CommonDao<Candidate>{

	@Override
	protected Class<Candidate> getModelClass() {
		return Candidate.class;
	}
	
	public void search(Pagination pagination, String pattern){
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Candidate> cquery = builder.createQuery(Candidate.class);
        Root<Candidate> root = cquery.from(Candidate.class);

        cquery.select(root).where( builder.or(
        								builder.like(builder.lower( root.get(Candidate_.name) ), pattern),
        								builder.like(builder.lower( root.get(Candidate_.idCode) ), pattern) ) );
        searchByPagination(cquery, pagination);
   }
	
   public Candidate getByIdCode(String idCode){
	    CriteriaBuilder cb = entityManager.getCriteriaBuilder();
   	
	   	CriteriaQuery<Candidate> cq = cb.createQuery(Candidate.class);
	   	Root<Candidate> fr = cq.from(Candidate.class);
	   	cq.select( fr ).where( cb.equal( fr.get(Candidate_.idCode), idCode ) );
	   	
	   	TypedQuery<Candidate> q = entityManager.createQuery( cq );
	   	List<Candidate> existing = q.getResultList();
	   	return existing.size()>0?existing.get(0):null;
   }
   
   public List<Interview> getInterviews(Candidate it){
	   CriteriaBuilder cb = entityManager.getCriteriaBuilder();
       CriteriaQuery<Interview> cq = cb.createQuery( Interview.class );
	   Root<Interview> rp =  cq.from( Interview.class );
	   cq.select( rp ).where( cb.equal( rp.get( Interview_.candidate), it) );
		
	   List<Interview> result = entityManager.createQuery( cq ).getResultList();
	   return result;
   }
}
