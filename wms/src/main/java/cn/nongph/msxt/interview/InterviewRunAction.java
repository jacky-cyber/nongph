package com.felix.msxt.interview;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import sun.misc.BASE64Encoder;

import com.felix.msxt.candidate.CandidateService;
import com.felix.msxt.common.DateUtil;
import com.felix.msxt.model.Candidate;
import com.felix.msxt.model.InterviewRound;
import com.felix.msxt.model.InterviewRoundExamination;
import com.felix.msxt.model.InterviewRoundUser;
import com.felix.std.model.User;
import com.felix.std.user.UserService;

@Named
@RequestScoped
public class InterviewRunAction {

	@Inject
	private InterviewService interviewService; 
	@Inject
	private UserService userService;
	@Inject
    private CandidateService candidateService; 
    
	HttpServletRequest request;
	private String loginName;
	private String loginPassword;
	private String userId;
    private String interviewRoundUserId;
	private String interviewRoundId;
    private String candidateId;
    
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginPassword() {
		return loginPassword;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
	public String getInterviewRoundUserId() {
		return interviewRoundUserId;
	}

	public void setInterviewRoundUserId(String interviewRoundUserId) {
		this.interviewRoundUserId = interviewRoundUserId;
	}

    public String getInterviewRoundId() {
        return interviewRoundId;
    }

    public void setInterviewRoundId(String interviewRoundId) {
        this.interviewRoundId = interviewRoundId;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(String candidateId) {
        this.candidateId = candidateId;
    }
   
	public String interviewerLogin(){
		StringBuilder result = new StringBuilder( "<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		result.append( "<result>" );
			
        if ( loginName == null || loginName.isEmpty() ) {
        		result.append( "<failure>" );
           	result.append( "<error_code>" ).append("interview.login.E001").append( "</error_code>" );
           	result.append( "<error_desc>" ).append("username is empty").append( "</error_desc>" );       		
           	result.append( "</failure>" );
        } else if( loginPassword == null || loginPassword.isEmpty() ) {
        		result.append( "<failure>" );
           	result.append( "<error_code>" ).append("interview.login.E002").append( "</error_code>" );
           	result.append( "<error_desc>" ).append("password is empty").append( "</error_desc>" );       		
           	result.append( "</failure>" );
        } else {
	        User user = userService.checkUser(loginName, loginPassword);
	        if ( user != null ) {
					if( user.getPassword().equals( loginPassword ) ) {
						result.append("<success>");
						result.append("<user_id>").append( user.getId() ).append( "</user_id>" );
						result.append("<name>").append( user.getName() ).append("</name>");
						result.append("<token>").append( UUID.randomUUID().toString() ).append("</token>");
						result.append("</success>");
					} else {
						result.append( "<failure>" );
					   result.append( "<error_code>" ).append("interview.login.E003").append( "</error_code>" );
						result.append( "<error_desc>" ).append("password error").append( "</error_desc>" );       		
						result.append( "</failure>" );
					}
	        } else {
	        		result.append( "<failure>" );
	           	result.append( "<error_code>" ).append("interview.login.E004").append( "</error_code>" );
	           	result.append( "<error_desc>" ).append("username not exist").append( "</error_desc>" );       		
	           	result.append( "</failure>" );
	        }
        } 
        result.append( "</result>" );
        return result.toString();
	}
    
    public String getPendingInterviewRounds(){
    	List<InterviewRoundUser> irus = interviewService.getPendingInterviewRoundUser(userId);
    	StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    	sb.append("<result>");
    	sb.append("<success>");
    	sb.append("<rounds>");
    	for( InterviewRoundUser iru : irus ) {
    		sb.append( "<round>" );
    		sb.append( "<id>" ).append( iru.getId() ).append( "</id>" );
    		sb.append( "<plan_start_time>" ).append( DateUtil.getStringFromDate(iru.getInterviewRound().getPlanStartTime()) ).append( "</plan_start_time>" );
    		sb.append( "<name>" ).append( iru.getInterviewRound().getName() ).append( "</name>" );
    		sb.append( "<candidate>" ).append( iru.getInterviewRound().getInterview().getCandidate().getName() ).append( "</candidate>" );
    		sb.append( "<apply_position>" ).append( iru.getInterviewRound().getInterview().getApplyPosition().getName() ).append( "</apply_position>" );
    		sb.append( "</round>" );
    	}
    			
    	sb.append( "</rounds>" );
    	sb.append( "</success>" );
    	sb.append( "</result>" );
    	return sb.toString();
    }
    
    public String getInterviewInfo(){
        InterviewRoundUser iru = interviewService.getInterviewRoundUserById( interviewRoundUserId );
    	StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    	sb.append("<result>");
    	sb.append("<success>");
		sb.append("<candidate>" );
            sb.append("<id>").append(iru.getInterviewRound().getInterview().getCandidate().getId()).append("</id>");
		    sb.append("<name>").append(iru.getInterviewRound().getInterview().getCandidate().getName()).append("</name>");		
		sb.append("</candidate>");	
		sb.append("<apply_position>").append(iru.getInterviewRound().getInterview().getApplyPosition().getName()).append("</apply_position>");      
		sb.append("<curr_round>");
			sb.append("<id>").append(iru.getInterviewRound().getId()).append("</id>");
			sb.append("<type>").append(iru.getInterviewRound().getType()).append("</type>");
			sb.append("<name>").append(iru.getInterviewRound().getName()).append("</name>");
			sb.append("<plan_start_time>").append(DateUtil.getStringFromDate( iru.getInterviewRound().getPlanStartTime())).append("</plan_start_time>");
		sb.append("</curr_round>");		
		sb.append("<done_rounds>");
        List<InterviewRound> rounds = iru.getInterviewRound().getInterview().getRounds();
        for(InterviewRound ir: rounds){
            if( ir.getState()==InterviewRound.STATE.PASS 
                || ir.getState()==InterviewRound.STATE.REJECT ) {
                sb.append("<done_round>");
			    sb.append("<id>").append(ir.getId()).append("</id>");
			    sb.append("<type>").append(ir.getType()).append("</type>");
                sb.append("<name>").append(ir.getName()).append("</name>");
				sb.append("<done_time>").append(DateUtil.getStringFromDate(ir.getActualFinishTime())).append("</done_time>");
                sb.append("</done_round>");
            }
        }
		sb.append("</done_rounds>");
	    sb.append("</success>");
        sb.append("</result>");
        
        return sb.toString();
    }
    
    public String getInterviewRoundInfo(){
        InterviewRound ir = interviewService.getInterviewRoundById( interviewRoundId );
       
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    	sb.append("<result>");
    	sb.append("<success>");
        if( ir.getType() == InterviewRound.TYPE.EXAM ) {
            sb.append("<interview_round_examinations>");
            for( InterviewRoundExamination ire : ir.getInterviewRoundExaminations() ) {
                sb.append("<interview_round_examination>");
			    sb.append("<id>").append(ire.getId()).append("</id>");
			    sb.append("<examination_name>").append(ire.getExam().getName()).append("</examination_name>");
                sb.append("<consume_time>").append((ire.getEndTime().getTime()-ire.getStartTime().getTime())/6000).append("</consume_time>");
                sb.append("<score>").append(ire.getExamScore()).append("</score>");
                sb.append("</interview_round_examination>");
            }
            sb.append("</interview_round_examinations>");
        } else {
            sb.append("<interview_round_users>");
            for( InterviewRoundUser iru: ir.getInterviewRoundUsers() ) {
                sb.append("<interview_round_user>");
			    sb.append("<user_name>").append(iru.getUser().getName()).append("</user_name>");
			    sb.append("<state>").append(iru.getState()).append("</state>");
                sb.append("<finish_time>").append(DateUtil.getStringFromDate(iru.getFinishTime())).append("</finish_time>");
                sb.append("<form_id>").append(iru.getForm().getId()).append("</form_id>");
                sb.append("</interview_round_user>");
            }
            sb.append("</interview_round_users>");
        }
        sb.append("</success>");
        sb.append("</result>");
        
        return sb.toString();
    }
    
    public String getCandidateResume() {
        Candidate c = candidateService.get(candidateId);
        String name = c.getResumeName();
        String type = name.substring( name.lastIndexOf('.') ).toUpperCase();
        String resume;
        if( type.equals("HTM") )
            type = "HTML";
        if( type.equals("HTML") )
            try {
                resume = new String(c.getResume(), "utf-8");
            } catch (UnsupportedEncodingException ex) {
                resume = "UnsupportedEncodingException:"+ex.toString();
            }
        else
            resume = new BASE64Encoder().encode( c.getResume() );
        
        StringBuilder sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    	sb.append("<result>");
    	sb.append("<success>");
        sb.append("<type>").append(type).append("</type>");
		sb.append("<resume>").append(resume).append("</resume>");
        sb.append("</success>");
        sb.append("</result>");
        return sb.toString();
    }
}
