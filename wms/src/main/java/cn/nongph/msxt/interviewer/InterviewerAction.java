package com.felix.msxt.interviewer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.json.JSONObject;

import com.felix.form.model.FormField;
import com.felix.msxt.interview.InterviewService;
import com.felix.msxt.model.InterviewRound;
import com.felix.msxt.model.InterviewRoundUser;
import com.felix.nsf.response.CommonResult;
import com.felix.std.model.User;

@RequestScoped
@Named
public class InterviewerAction {
	@Inject
	private InterviewService interviewService;
	
	private HttpServletRequest request;
	private String id;
	private String formFields;
	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getFormFields() {
		return formFields;
	}

	public void setFormFields(String formFields) {
		this.formFields = formFields;
	}

	public List<InterviewRoundUser> getPendingInterviewRounds(){
		User user = (User)request.getSession().getAttribute( "CURRENT_USR" );
		List<InterviewRoundUser> irus = interviewService.getPendingInterviewRoundUser( user.getId() );
		for( InterviewRoundUser iru : irus ){
			iru.getForm().getFormTemplate();
		}
		return irus;
    }
	
	public CommonResult markStart(){
		InterviewRoundUser iru = interviewService.getInterviewRoundUserById(id);
		iru.setStartTime( new Date() );
		iru.setState( InterviewRoundUser.STATE.GOING );
		interviewService.persist( iru );
		if( iru.getInterviewRound().getState()== InterviewRound.STATE.PENDING ) {
			interviewService.changeInterviewRoundState( iru.getInterviewRound(), InterviewRound.STATE.GOING);
		}
		return CommonResult.createSuccessResult();
	}
	
	public CommonResult passInterviewRoundUser(){
		InterviewRoundUser iru = interviewService.getInterviewRoundUserById(id);
		try{
			JSONObject jo = new JSONObject( formFields );
			Map<String, String> fields = new HashMap<String, String>();
			String[] names = JSONObject.getNames( jo );
			for( String name : names ) {
				fields.put( name, jo.getString( name ) );
			}
			interviewService.complateInterviewRoundUser(iru, InterviewRoundUser.STATE.PASS, fields);
			return CommonResult.createSuccessResult();
		} catch (JSONException e){
			e.printStackTrace();
			return CommonResult.createFailureResult(null, "invalid json format");
		}
	}
	
	public CommonResult rejectInterviewRoundUser(){
		InterviewRoundUser iru = interviewService.getInterviewRoundUserById(id);
		try{
			JSONObject jo = new JSONObject( formFields );
			Map<String, String> fields = new HashMap<String, String>();
			String[] names = JSONObject.getNames( jo );
			for( String name : names ) {
				fields.put( name, jo.getString( name ) );
			}
			interviewService.complateInterviewRoundUser(iru, InterviewRoundUser.STATE.REJECT, fields);
			return CommonResult.createSuccessResult();
		} catch (JSONException e){
			e.printStackTrace();
			return CommonResult.createFailureResult(null, "invalid json format");
		}
	}
	
	public String getFormValues(){
		InterviewRoundUser iru = interviewService.getInterviewRoundUserById(id);
		StringBuilder sb = new StringBuilder("{");
		for( FormField ff: iru.getForm().getFields() ) {
			if( sb.length()>1 )
				sb.append( ',' );
			sb.append( ff.getName() ).append(":'").append( ff.getValue() ).append("'");
		}
		sb.append("}");
		return sb.toString();
	}
}
