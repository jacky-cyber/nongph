package com.felix.msxt.candidate;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import com.felix.msxt.model.Candidate;
import com.felix.nsf.CommonDao;
import com.felix.nsf.CommonService;
import com.felix.nsf.Pagination;

@ApplicationScoped
@Named
public class CandidateService extends CommonService<Candidate>{
	
	@Inject
	private CandidateDao interviewerDao;
	
	public void search(Pagination pagination, String pattern){
		interviewerDao.search(pagination, pattern);
	}

	@Override
	public CommonDao<Candidate> getDao() {
		return interviewerDao;
	}
	
	public boolean verifyIdCodeIsAvailable( Candidate it ) {
		Candidate oit = interviewerDao.getByIdCode( it.getIdCode() );
    	if( StringUtils.isEmpty( it.getId() ) ) {
    		if( oit!=null ) 
    			return false;
    	} else {
    		if( !oit.getId().equals( it.getId() ) )
    			return false;
    	}
        
        return true;
    }
	
	public boolean checkExistInterview(Candidate it){
		return interviewerDao.getInterviews( it ).size()>0;
	}
}
