package com.felix.msxt.interview;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import com.felix.exam.examination.ExaminationDao;
import com.felix.exam.model.Examination;
import com.felix.form.FormService;
import com.felix.form.model.Form;
import com.felix.form.model.FormTemplate;
import com.felix.form.template.FormTemplateDao;
import com.felix.msxt.common.DateUtil;
import com.felix.msxt.model.Interview;
import com.felix.msxt.model.InterviewRound;
import com.felix.msxt.model.InterviewRoundExamination;
import com.felix.msxt.model.InterviewRoundUser;
import com.felix.nsf.CommonDao;
import com.felix.nsf.CommonService;
import com.felix.nsf.Pagination;
import com.felix.std.model.SystemParameter;
import com.felix.std.model.User;
import com.felix.std.parameter.SystemParameterManager;
import com.felix.std.user.UserDao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Felix
 *
 */
@Named
@ApplicationScoped
public class InterviewService extends CommonService<Interview>{
	
	@Inject
	private InterviewDao interviewDao;
	@Inject
	private ExaminationDao examinationDao;
	@Inject
   private UserDao userDao;
   @Inject
   private FormTemplateDao formTemplateDao;
   @Inject
   private SystemParameterManager spm;
   @Inject
	private FormService formService;
   @Inject
   SystemParameterManager systemParameterManager; 
   
	@Override
	public CommonDao<Interview> getDao() {
		return interviewDao;
	}
	
	public Interview getInterviewByLoginNameAndPassword( String loginName, String password){
		return interviewDao.getInterviewByLoginNameAndPassword(loginName, password);
	}
	
	public void search(Pagination page, InterviewSearchCriteria sc){
		interviewDao.search(page, sc);
	}
	
	public boolean verifyLoginNameIsAvailable(String loginName, String orgId){
		Interview it = interviewDao.getInterviewByLoginName(loginName);
		if( StringUtils.isEmpty(orgId) )
			return it==null;
		else
			return it==null || it.getId().equals( orgId );	
	}
	
	public void save( Interview iv, String rounds ) throws JSONException {
		if( !StringUtils.isEmpty(iv.getId()) ) {
        for( InterviewRound ir : iv.getRounds() ) {
           interviewDao.remove( ir );
           }
        iv.getRounds().clear();
        }
		List<InterviewRound> irList = transferRounds(iv, rounds);
        irList.get(0).setState( InterviewRound.STATE.PENDING );
        iv.setPlanStartTime( irList.get(0).getPlanStartTime() );
        iv.setRounds( irList );
        cascadeSave( iv );
	}
    
	public void addRounds( Interview iv, String rounds ) throws JSONException {
		List<InterviewRound> irList = transferRounds(iv, rounds);
      InterviewRound lastRound = iv.getRounds().get( iv.getRounds().size()-1 );
      if( lastRound.isDone() )
    	    irList.get(0).setState( InterviewRound.STATE.PENDING );
      if( iv.isDone() )
    	    iv.setStatus( Interview.STATUS.GOING );
      iv.getRounds().addAll( irList );
      cascadeSave( iv );
	}
	
	public void saveRound( InterviewRound ir, String round ) throws JSONException {
		JSONObject jo = new JSONObject( round );
		ir.setPlanStartTime( DateUtil.getDateFromString( jo.getString("planStartTime") ) ); 
		if( ir.getType()==InterviewRound.TYPE.EXAM ) {
			for( InterviewRoundExamination ire : ir.getInterviewRoundExaminations() )
				interviewDao.remove( ire );
			ir.getInterviewRoundExaminations().clear();
         ir.getInterviewRoundExaminations().addAll( transferExams( ir, jo.getJSONArray("gridDatas") ) );
		} else {
			for( InterviewRoundUser iru : ir.getInterviewRoundUsers() )
				interviewDao.remove( iru );
			ir.getInterviewRoundUsers().clear();
			ir.getInterviewRoundUsers().addAll( transferUsers( ir, jo.getJSONArray("gridDatas") ) );
    	}
		cascadeSaveRound( ir );
	}
	
   private void cascadeSave( Interview iv ) {
        interviewDao.persist( iv );
        for( InterviewRound ir: iv.getRounds() ) 
            cascadeSaveRound( ir );
    }
    
   private void cascadeSaveRound( InterviewRound ir ) {
	    interviewDao.persist( ir );
       if( ir.getType()== InterviewRound.TYPE.EXAM)
           for( InterviewRoundExamination ire : ir.getInterviewRoundExaminations() )
               interviewDao.persist( ire );
       else
           for( InterviewRoundUser iru : ir.getInterviewRoundUsers() ) {
               interviewDao.persist( iru.getForm() );
               interviewDao.persist( iru );
           }
    }
   
   private List<InterviewRound> transferRounds(Interview iv, String rounds ) throws JSONException{
        List<InterviewRound> result = new ArrayList<InterviewRound>();

        JSONArray ja = new JSONArray( rounds );
        for( int i=0; i<ja.length(); i++ ) {
            JSONObject round = ja.getJSONObject(i);

            InterviewRound ir = new InterviewRound();
            ir.setType( InterviewRound.TYPE.valueOf( round.getString("type")));
            ir.setName( round.getString("name") );
            ir.setState( InterviewRound.STATE.WAITING );
            ir.setPlanStartTime( DateUtil.getDateFromString( round.getString("planStartTime") ) );
            if( ir.getType()==InterviewRound.TYPE.EXAM ) {
                ir.setInterviewRoundExaminations( transferExams( ir, round.getJSONArray("gridDatas") ) );
            } else {
                ir.setInterviewRoundUsers( transferUsers( ir, round.getJSONArray("gridDatas") ) );
            	}

            ir.setInterview( iv );
            
            result.add( ir );
        }
        return result;
    }
    
    private List<InterviewRoundExamination> transferExams( InterviewRound ir, JSONArray exams ) throws JSONException {
        List<InterviewRoundExamination> result = new ArrayList<InterviewRoundExamination>();
        for( int i=0; i<exams.length(); i++){
            JSONObject exam = exams.getJSONObject(i);
            
            Examination e = examinationDao.get( exam.getString("id") );
            
            InterviewRoundExamination ire = new InterviewRoundExamination();
            ire.setExam( e );
            ire.setExamConfuse( exam.getInt("confuse") );
            ire.setInterviewRound( ir );
            
            result.add( ire );
        }
        return result;
    }
    
    private List<InterviewRoundUser> transferUsers(InterviewRound ir, JSONArray views ) throws JSONException {
        List<InterviewRoundUser> result = new ArrayList<InterviewRoundUser>();
        for(int i=0; i<views.length(); i++){
            JSONObject view = views.getJSONObject(i);
            
            User u = userDao.get( view.getString("id") );
            
            FormTemplate ft = formTemplateDao.get( view.getString("formTemplate") );
            Form f = new Form();
            f.setState( Form.STATE.UNFINISH );
            f.setFormTemplate( ft );
            
            InterviewRoundUser iru = new InterviewRoundUser();
            iru.setUser( u );
            iru.setForm( f );
            iru.setInterviewRound( ir );
            
            result.add( iru );
        }    
        return result;
    }
    
    public List<InterviewRoundUser> getPendingInterviewRoundUser( String userId ){
        return interviewDao.getPendingInterviewRoundUser( userId );
    }
    
    public InterviewRoundUser getInterviewRoundUserById( String id ) {
        return interviewDao.getInterviewRoundUserById(id);
    }
    
    public InterviewRound getInterviewRoundById( String id ){
        return interviewDao.getInterviewRoundById(id);
    }
    
    public InterviewRoundExamination getInterviewRoundExaminationById( String id ) {
        return interviewDao.getInterviewRoundExaminationById(id);
    }
    
    public void changeInterviewRoundState( InterviewRound ir, InterviewRound.STATE newState){
        ir.setState( newState );
        persist(ir);
        
        if( InterviewRound.STATE.GOING == newState ) {
            if( ir.getInterview().getStatus() != Interview.STATUS.GOING ) {
        	       ir.getInterview().setStatus( Interview.STATUS.GOING );
                persist( ir.getInterview() );
                }
        } else if (InterviewRound.STATE.ABSENT == newState) {
            InterviewRound next = ir.getNextRound();
            while( next!=null ) {
                next.setState( InterviewRound.STATE.CANCEL );
                persist( next );
                next = next.getNextRound();
            	}
            ir.getInterview().setStatus( Interview.STATUS.ABSENT );
            persist( ir.getInterview() );
        } else if( InterviewRound.STATE.PASS == newState ) {
            InterviewRound next = ir.getNextRound();
            if( next!=null ) {
                next.setState( InterviewRound.STATE.PENDING );
                persist( next );
            } else {
                ir.getInterview().setStatus( Interview.STATUS.PASS );
                persist( ir.getInterview() );
            	}
        } else if( InterviewRound.STATE.REJECT == newState ) {
            String pv = spm.getParameterValue( SystemParameter.PN_VIEW_ROUND_TO_NEXT_ROUND );
            if( pv.equals( SystemParameter.VIEW_ROUND_TO_NEXT_ROUND.PASS.name()) ) {
                InterviewRound next = ir.getNextRound();
                while( next!=null ) {
                    next.setState( InterviewRound.STATE.CANCEL );
                    persist( next );
                    next = next.getNextRound();
                }
                ir.getInterview().setStatus( Interview.STATUS.REJECT );
                persist( ir.getInterview() );
            } else {
                InterviewRound next = ir.getNextRound();
                if( next!=null ) {
                    next.setState( InterviewRound.STATE.PENDING );
                    persist( next );
                } else {
                    ir.getInterview().setStatus( Interview.STATUS.REJECT );
                    persist( ir.getInterview() );
                }
            }
        }    
    }
    
	public void cleanConversation( Interview iv ){
		iv.setConversationId( null );
		InterviewRound ir = iv.getCurrentRound();
		if( ir.getType() == InterviewRound.TYPE.EXAM ) {
			for( InterviewRoundExamination ire : ir.getInterviewRoundExaminations() ) {
				if( ire.getStatus() == InterviewRoundExamination.STATUS.GOING ) {
					ire.setStartTime( null );
					persist( ire );
				}
			}
		}
		persist( iv );
	}
	
	public void complateInterviewRoundUser(InterviewRoundUser iru, InterviewRoundUser.STATE state, Map<String, String> fields){
		formService.saveForm( iru.getForm(), fields);
		iru.setFinishTime( new Date() );
		iru.setState( state );
		persist( iru );
		
		float nextStep = 0;
		float base = 1/iru.getInterviewRound().getInterviewRoundUsers().size();
		for( InterviewRoundUser iru2 : iru.getInterviewRound().getInterviewRoundUsers() ) {
			if( iru2.getState()==InterviewRoundUser.STATE.PASS )
				nextStep += base;
			else if( iru2.getState()==InterviewRoundUser.STATE.REJECT )
				nextStep += 0;
			else 
				nextStep += -1;
		}
		String toNextRoundPolicy = systemParameterManager.getParameterValue(SystemParameter.PN_VIEW_ROUND_TO_NEXT_ROUND);
		if( nextStep>=0 ) {
			if( toNextRoundPolicy.equals( SystemParameter.VIEW_ROUND_TO_NEXT_ROUND.DIRECT ) ) {
				changeInterviewRoundState( iru.getInterviewRound(), InterviewRound.STATE.PASS );
			} else {
				if( nextStep>=0.5 ) 
					changeInterviewRoundState( iru.getInterviewRound(), InterviewRound.STATE.PASS );
				else
					changeInterviewRoundState( iru.getInterviewRound(), InterviewRound.STATE.REJECT );
			}
		}
	}
}
