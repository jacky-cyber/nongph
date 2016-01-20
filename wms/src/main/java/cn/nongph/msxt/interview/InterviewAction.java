package com.felix.msxt.interview;

import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import com.felix.exam.model.Examination;
import com.felix.exam.model.ExaminationCatalog;
import com.felix.exam.model.ExaminationCatalogQuestion;
import com.felix.exam.model.Question;
import com.felix.exam.model.QuestionChoiceItem;
import com.felix.msxt.candidate.CandidateService;
import com.felix.msxt.common.HtmlUtil;
import com.felix.msxt.model.Interview;
import com.felix.msxt.model.Candidate;
import com.felix.msxt.model.InterviewRound;
import com.felix.msxt.model.InterviewRoundExamination;
import com.felix.msxt.model.InterviewRoundUser;
import com.felix.msxt.model.Position;
import com.felix.msxt.position.PositionService;
import com.felix.nsf.Pagination;
import com.felix.nsf.response.CommonResult;

import org.json.JSONException;

@RequestScoped
@Named
public class InterviewAction {
	private int start;
	private int limit;
	private InterviewSearchCriteria searchCriteria = new InterviewSearchCriteria();
	
	private String id;
	private String candidateId;
	private String positionId;
	private String loginName;
	private String loginPassword;
	private String status;
	private String rounds;
	private String roundId;
	private String round;
	@Inject
	private InterviewService interviewService;
	@Inject
	private PositionService positionService;
	@Inject
	private CandidateService interviewerService;
	
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public InterviewSearchCriteria getSearchCriteria() {
		return searchCriteria;
	}
	public void setSearchCriteria(InterviewSearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCandidateId() {
		return candidateId;
	}
	public void setCandidateId(String candidateId) {
		this.candidateId = candidateId;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

   public String getRounds() {
        return rounds;
    }
   public void setRounds(String rounds) {
        this.rounds = rounds;
    }
	
	public String getRoundId() {
		return roundId;
	}
	public void setRoundId(String roundId) {
		this.roundId = roundId;
	}
	public String getRound() {
		return round;
	}
	public void setRound(String round) {
		this.round = round;
	}
	
	public Pagination search(){
		Pagination page = new Pagination();
		page.setStart( start );
		page.setLimit( limit );
		
		interviewService.search(page, searchCriteria);
		
		return page;
	}
	
	public CommonResult save(){
		if( interviewService.verifyLoginNameIsAvailable(loginName, id) ){
			Interview interview;
			if( StringUtils.isEmpty(id) ) {
				interview = new Interview();
				Candidate interviewer = interviewerService.get( candidateId );
				Position applyPosition = positionService.get( positionId );
				
				interview.setCandidate( interviewer );
				interview.setApplyPosition( applyPosition );
				interview.setCreateOn( new Date() );
				interview.setStatus( Interview.STATUS.PENDING );
			} else {
				interview = interviewService.get( id );
			}
			 
		    interview.setLoginName( loginName );
		    interview.setLoginPassword( loginPassword );
            try {
                interviewService.save( interview, rounds );
            } catch (JSONException ex) {
               return CommonResult.createFailureResult(null, "json format error!");
            }
		    return CommonResult.createSuccessResult();
		} else {
			return CommonResult.createFailureResult("login_name_exist", "loginName: " + loginName + "already exist!");
		}
	}
	
	public Interview get(){
		Interview iv = interviewService.get(id);
		for( InterviewRound ir : iv.getRounds() )
            if( ir.getType() == InterviewRound.TYPE.EXAM ) 
                for( InterviewRoundExamination ire:ir.getInterviewRoundExaminations() )
                    ire.getExam();
            else
                for( InterviewRoundUser user : ir.getInterviewRoundUsers() ) {
                	  user.getUser().getName();
                    user.getForm().getFormTemplate().getName();
                	  }
		return iv;
	}
	
	public CommonResult delete(){
		Interview iv = interviewService.get( id );
		if( Interview.STATUS.PENDING == iv.getStatus() ) {
			interviewService.delete( iv );
			return CommonResult.createSuccessResult();
		} else {
			return CommonResult.createFailureResult("started", "interview started!");
		}
	}
	
	public CommonResult addRounds(){
		Interview iv = interviewService.get( id );
		try {
			interviewService.addRounds( iv, rounds );
		} catch (JSONException ex) {
			return CommonResult.createFailureResult(null, "json format error!");
        }
		return CommonResult.createSuccessResult();
	}
	
	public CommonResult saveRound(){
		InterviewRound ir = interviewService.getInterviewRoundById( roundId );
		try {
			interviewService.saveRound(ir, round);
		} catch (JSONException e) {
			 return CommonResult.createFailureResult(null, "json format error!");
		}
		return CommonResult.createSuccessResult();
	}
	
	public CommonResult deleteRound(){
		InterviewRound ir = interviewService.getInterviewRoundById( roundId );
		interviewService.delete( ir );
		return CommonResult.createSuccessResult();
	}
	
	public CommonResult cleanConversation(){
		Interview iv = interviewService.get( id );
	   interviewService.cleanConversation( iv );
	   return CommonResult.createSuccessResult();
	}
	
	public CommonResult markGoing(){
		InterviewRoundExamination ire = interviewService.getInterviewRoundExaminationById( id );
		ire.setStartTime( new Date() );
		interviewService.persist( ire );
		return CommonResult.createSuccessResult();
	}
	
	public CommonResult markPending(){
		InterviewRoundExamination ire = interviewService.getInterviewRoundExaminationById( id );
		ire.setStartTime( null );
		interviewService.persist( ire );
		return CommonResult.createSuccessResult();
	}
	
	public CommonResult markAbsent(){
		InterviewRound ir = interviewService.getInterviewRoundById( roundId );
		interviewService.changeInterviewRoundState( ir, InterviewRound.STATE.ABSENT );
		return CommonResult.createSuccessResult();
	}
	
   public InterviewRoundExamination getExaminationAnswer(){
	   InterviewRoundExamination result = interviewService.getInterviewRoundExaminationById(id);
	   Examination exam = result.getExam();
	   //Load lazy data
 		for( ExaminationCatalog ec : exam.getCatalogs() ) { 
 			for( ExaminationCatalogQuestion eq : ec.getQuestions() ) {
 				Question q = eq.getQuestion();
 				for( QuestionChoiceItem qci : q.getChoiceItems() ) {
 					String html = HtmlUtil.transferCommon2HTML( qci.getContent() );
 					html = html.replaceAll("<br/>", "<br/>&nbsp;&nbsp;&nbsp;&nbsp;");
 					qci.setContent( html );
 					interviewService.detach( qci );
 				}
 				String htmlContent = HtmlUtil.transferCommon2HTML( q.getContent() );
 				q.setContent( htmlContent );
 				interviewService.detach( q );
 			}
   		}
 		if( result.getStatus() == InterviewRoundExamination.STATUS.SUBMITTED )
 			result.getExamQuestionAnswers().get(0);
 		return result;
    }
}