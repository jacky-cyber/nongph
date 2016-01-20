package com.felix.msxt.interview;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.felix.exam.examination.ExaminationCatalogQuestionAnswerService;
import com.felix.exam.examination.ExaminationCatalogQuestionService;
import com.felix.exam.model.ExaminationCatalog;
import com.felix.exam.model.ExaminationCatalogQuestion;
import com.felix.exam.model.ExaminationCatalogQuestionAnswer;
import com.felix.exam.model.Question;
import com.felix.exam.model.QuestionChoiceItem;
import com.felix.msxt.common.DateUtil;
import com.felix.msxt.model.Interview;
import com.felix.msxt.model.InterviewRound;
import com.felix.msxt.model.InterviewRoundExamination;
import java.util.List;
import java.util.UUID;

@Named
@RequestScoped
public class InterviewExamRunAction {
	@Inject
	private ExaminationCatalogQuestionService examinationCatalogQuestionService;
	@Inject
	private ExaminationCatalogQuestionAnswerService examinationCatalogQuestionAnswerService;
    @Inject
	private InterviewService interviewService; 
    
	private HttpServletRequest request;
    private String loginName;
	private String loginPassword;
	private String conversation;
	private String examId;
	
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
    
	public String getConversation() {
		return conversation;
	}
	public void setConversation(String conversation) {
		this.conversation = conversation;
	}

	public String getExamId() {
		return examId;
	}
	public void setExamId(String examId) {
		this.examId = examId;
	}
    
    public String candidateLogin() throws UnsupportedEncodingException {
	     Interview iv = interviewService.getInterviewByLoginNameAndPassword( loginName, loginPassword );
    	  String errorCode = checkError4CandidateLogin( iv );
        if( errorCode!=null ) {
            return "<login><status>failed</status><desc>"+errorCode+"</desc></login>";
          }
        
        InterviewRound currentRound = iv.getCurrentRound();
        String newConversation = UUID.randomUUID().toString();
        StringBuilder sb = new StringBuilder();
        sb.append( "<login>" );
        sb.append( "<status>success</status>" );
        sb.append( "<conversation>" ).append( newConversation ).append( "</conversation>" );
        sb.append( "<interviewer>" ).append( iv.getCandidate().getName() ).append( "</interviewer>" ); 
        sb.append( "<jobtitle>" ).append( iv.getApplyPosition().getName() ).append( "</jobtitle>" );
        sb.append( "<round>" ).append( currentRound.getName() ).append("</round>");
        sb.append( "<examinations>" );
        
        for( InterviewRoundExamination ie : currentRound.getInterviewRoundExaminations() ) {
            if( ie.getStatus() == InterviewRoundExamination.STATUS.SUBMITTED )
                continue;

            sb.append( "<examination>" );
            sb.append( "<id>").append( ie.getId() ).append( "</id>" );
            sb.append( "<name>").append( ie.getExam().getName() ).append( "</name>" );

            int totalScore = 0;
            int totalQuestion = 0;
            for( ExaminationCatalog ec : ie.getExam().getCatalogs() ) {
                for( ExaminationCatalogQuestion eq : ec.getQuestions() ) {
                    totalScore += eq.getScore();
                    totalQuestion++;
                }
            }
            sb.append( "<desc><![CDATA[Catalog count:").append( ie.getExam().getCatalogs().size() )
              .append( " Total question count : " ).append( totalQuestion )
              .append( " Total score : ").append( totalScore )
              .append( "]]></desc>" );
            sb.append( "</examination>" );
        }		
        sb.append( "</examinations>" );
        sb.append( "</login>" );

        if( currentRound.getState() == InterviewRound.STATE.PENDING ) {
            interviewService.changeInterviewRoundState( currentRound, InterviewRound.STATE.GOING );
           }

        iv.setConversationId( newConversation );
        interviewService.persist( iv );

        return sb.toString();
	}
    
    private String checkError4CandidateLogin(Interview iv){
		if( iv==null ) 
            return "login.LE001";
        
        if( iv.getStatus()==Interview.STATUS.PASS
            || iv.getStatus()==Interview.STATUS.REJECT ) 
            return "login.LE005";
			
        if( iv.getStatus()==Interview.STATUS.ABSENT )
				return "login.LE007";
         
        if( iv.getConversationId()!=null )
            return "login.LE004";
        	 
        InterviewRound currentRound = iv.getCurrentRound();
        
        if( currentRound==null || currentRound.getType()!= InterviewRound.TYPE.EXAM )
            return "login.LE006";
            
        if( currentRound.getPlanStartTime().compareTo( DateUtil.getTodayStart() ) == -1 )
            return "login.LE003";
        
        if ( currentRound.getPlanStartTime().compareTo( DateUtil.getTodayEnd() ) >-1 )//limit to today
            return "login.LE002";
        
        return null;
    }
        
	public String getExam() {
		InterviewRoundExamination ie = interviewService.getInterviewRoundExaminationById( examId );
		
		String csId = ie.getInterviewRound().getInterview().getConversationId();
		if( csId==null || csId.isEmpty() || !csId.equals(conversation) ) 
			return "<error><code>201</code><desc>Invalid Conversation</desc></error>";
					
		StringBuilder sb = new StringBuilder();
		sb.append( "<examination>" ); 
		sb.append( "<examinationid>" ).append( ie.getId() ).append( "</examinationid>" );
		sb.append( "<name>" ).append( ie.getExam().getName() ).append( "</name>" );
		sb.append( "<time>" ).append( ie.getExam().getTime() ).append( "</time>" );
		sb.append( "<confuse>" ).append( ie.getExamConfuse()==1 ? true : false ).append( "</confuse>" );
		sb.append( "<catalogs>" );
		for( ExaminationCatalog ec : ie.getExam().getCatalogs() ) {
			sb.append( "<catalog>");  
			sb.append( "<index>" ).append( ec.getIndex() ).append("</index>");
			sb.append( "<name>" ).append( ec.getName() ).append("</name>");
			sb.append( "<catalogdesc><![CDATA[" ).append( ec.getDescription() ).append("]]></catalogdesc>");
			sb.append( "<questions>" ); 
			for( ExaminationCatalogQuestion ecq : ec.getQuestions() ) {
				sb.append( "<question>" );
				sb.append( "<index>" ).append( ecq.getIndex() ).append( "</index>" );
				sb.append( "<name>" ).append( ecq.getQuestion().getName() ).append("</name>");
				sb.append( "<questionid>" ).append( ecq.getId() ).append( "</questionid>" );
				sb.append( "<type>" ).append( ecq.getQuestion().getQuestionType().getId() ).append( "</type>" );
				sb.append( "<score>" ).append( ecq.getScore() ).append( "</score>" );
				sb.append( "<content><![CDATA[" ).append( ecq.getQuestion().getContent() ).append( "]]></content>");	
				if( ecq.getQuestion().getQuestionType().getId().equals("1") || ecq.getQuestion().getQuestionType().getId().equals("2") )
					sb.append( "<choices>" );
					for( QuestionChoiceItem qci : ecq.getQuestion().getChoiceItems() ) {
						sb.append( "<choice>" );    
						sb.append( "<index>" ).append( qci.getIndex() ).append( "</index>" );
						sb.append( "<label>" ).append( qci.getLabelName() ).append( "</label>" );
						sb.append( "<content>" ).append( qci.getContent() ).append( "</content>" );
						sb.append( "</choice>" );
					}
					sb.append( "</choices>" );
				sb.append( "</question>" );	
			}
			sb.append( "</questions>" );
			sb.append( "</catalog>" );
		}
		sb.append( "</catalogs>" );
		sb.append("</examination>" );
		
		ie.setStartTime( new Date() );
		interviewService.persist( ie );
		return sb.toString();
	}
	
	public String submitAnswer() {
		try {
			request.setCharacterEncoding( "utf-8" );
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		StringBuilder answerXML = new StringBuilder();
		BufferedReader br = null;
		try {
			br = new BufferedReader( new InputStreamReader( request.getInputStream() ) );
			String line;
			while( (line = br.readLine())!=null ) {
				answerXML.append( line );
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if( br!=null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		String examinationId = null;
        String status = "";
        String desc = "";
        String score = "";
        
        try{
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
	        ByteArrayInputStream is = new ByteArrayInputStream( answerXML.toString().getBytes() );
	        Document doc = db.parse( is );
	        is.close();
        
	        Element root = doc.getDocumentElement();
	        examinationId = getExaminationId( root );
	        desc = validate( root );
	        if( desc==null ) {
	        	InterviewRoundExamination ie = interviewService.getInterviewRoundExaminationById( examinationId );
	        	
	        	Element answersE = (Element)root.getElementsByTagName( "answers" ).item(0);
	     		saveAnswer( ie, answersE );
	     		status = "success";
	     		score = ie.getExamScore().toString();
                
                boolean allSubmit = true;
                List<InterviewRoundExamination> allExams = ie.getInterviewRound().getInterviewRoundExaminations();
                for( InterviewRoundExamination ire : allExams ) {
                    if( ire.getStatus() != InterviewRoundExamination.STATUS.SUBMITTED ) {
                        allSubmit = false;
                    }
                }
                if( allSubmit ) 
                   interviewService.changeInterviewRoundState(ie.getInterviewRound(), InterviewRound.STATE.PASS );
	        } else {
	        	if( examinationId==null ) 
	        		examinationId = "empty";
	        	status = "fail";
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        	status = "fail";
        	desc = "system exception";
		}
        StringBuilder sb = new StringBuilder();
        sb.append( "<submitresult>" );
        sb.append( "<examinationid>" ).append(examinationId).append("</examinationid>" );
    	sb.append( "<status>" ).append( status ).append( "</status>" );
    	if( status.equals("fail") )
    		sb.append("<desc>").append(desc).append("</desc>");
    	else
    		sb.append("<score>").append(score).append("</score>");
        sb.append("</submitresult>");
        
        return sb.toString();
	}
	
	private String getExaminationId( Element examanswerE ) {
		String result;
		NodeList nl = examanswerE.getElementsByTagName( "examinationid" );
        if( nl.getLength()==0 )
        	result = null;  
        else {
        	result = nl.item(0).getTextContent();
        	result = result.trim().isEmpty() ? null : result.trim();
        }
        return result;
	}
	
	private String validate( Element examanswerE ) {
		String examinationId = getExaminationId( examanswerE );
       
        if( examinationId == null )
        	return "examinationid is empty";  
        
    	InterviewRoundExamination ie = interviewService.getInterviewRoundExaminationById(examinationId );
    	if( ie==null )
        	return "can't find examination";  
    	
		if( Interview.STATUS.GOING != ie.getInterviewRound().getInterview().getStatus()  ) 
			return "This interview is not going";
		
		if( ie.getStatus() == InterviewRoundExamination.STATUS.SUBMITTED )
			return "examination is submitted, can't resubmit";
		
		if( ie.getStatus() == InterviewRoundExamination.STATUS.OVERTIME )
			return "examination is overtime, if you really need to submit please request administrator reset this examination";
			
		NodeList nl = examanswerE.getElementsByTagName( "conversation" );
        if( nl.getLength()==0 )
        	return "can't set conversation value";  
        
    	String conversationId = nl.item(0).getTextContent();
    	if( !ie.getInterviewRound().getInterview().getConversationId().equals( conversationId ) ) 
    		return "conversation id value is invalid";
    	
    	return null;
	}
	
	private void saveAnswer( InterviewRoundExamination ie, Element answersE ) {
		float tatalScore = 0;
		NodeList nl = answersE.getChildNodes();
		for( int i=0; i<nl.getLength(); i++ ) {
			Element answerE = (Element)nl.item(i);
			String questionid = answerE.getElementsByTagName("questionid").item(0).getTextContent();
			String content = answerE.getElementsByTagName("content").item(0).getTextContent();
			
			ExaminationCatalogQuestion  ecq = examinationCatalogQuestionService.get( questionid );
			ExaminationCatalogQuestionAnswer ecqa = new ExaminationCatalogQuestionAnswer();
			ecqa.setInterviewExamination( ie );
			ecqa.setExamQuestion( ecq );
			ecqa.setAnswer( content.isEmpty() ? null : content );
			
			Question q = ecq.getQuestion();
			String questionTypeId = q.getQuestionType().getId();
			if( questionTypeId.equals("1") || questionTypeId.equals("2") ) {
				if( q.getRightAnswer().equals( content ) ) {
					ecqa.setResult( 1 );
					ecqa.setScore( ecq.getScore() );
				} else {
					ecqa.setResult( 0 );
					ecqa.setScore( 0 );
				}
				
				tatalScore += ecqa.getScore();
			}
			
			examinationCatalogQuestionAnswerService.persist( ecqa );
		}
		
		ie.setExamScore( tatalScore );
		ie.setEndTime( new Date() );
		interviewService.persist( ie );
	}
}
