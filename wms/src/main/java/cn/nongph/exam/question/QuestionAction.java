package com.felix.exam.question;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.felix.exam.model.Question;
import com.felix.exam.model.QuestionChoiceItem;
import com.felix.msxt.model.Position;
import com.felix.msxt.position.PositionService;
import com.felix.nsf.Pagination;
import com.felix.nsf.response.CommonResult;

/**
 * 
 * @author Felix
 *
 */
@RequestScoped
@Named
public class QuestionAction {
	private int start;
	private int limit;
	private String questionTypeId;
	private String positionId;
	private String pattern;
	
	private String id;
	private String name;
	private String content;
	private String rightAnswer;
	private String choiceItems;
	
	@Inject
	private QuestionService questionService;
	
	@Inject
	private PositionService positionService;
	
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
	public String getQuestionTypeId() {
		return questionTypeId;
	}
	public void setQuestionTypeId(String questionTypeId) {
		this.questionTypeId = questionTypeId;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRightAnswer() {
		return rightAnswer;
	}
	public void setRightAnswer(String rightAnswer) {
		this.rightAnswer = rightAnswer;
	}
	public String getChoiceItems() {
		return choiceItems;
	}
	public void setChoiceItems(String choiceItems) {
		this.choiceItems = choiceItems;
	}
	
	public Pagination search(){
		Pagination page = new Pagination();
		page.setStart( start );
		page.setLimit( limit );
		if( pattern==null ) {
			pattern = "%";
		} else {
			pattern = "%" + pattern + "%";
		}
		questionService.searchAll( page, questionTypeId, positionId, pattern );
		return page;
	}
	
	
	public CommonResult save(){
		Question q;
		if( StringUtils.isEmpty(id) ) {
			q = new Question();
		} else {
			q = questionService.get(id);
		}
		q.setName( name );
		q.setContent( content );
		q.setRightAnswer( rightAnswer );
		
		List<String> positionIdList = new ArrayList<String>();
		List<QuestionChoiceItem> choiceItemList = new ArrayList<QuestionChoiceItem>();
		try {
			JSONArray ja = new JSONArray( positionId );
			for( int i=0; i<ja.length(); i++ ) 
				positionIdList.add( ja.getString(i) ); 
			
			ja = new JSONArray( choiceItems );
			for( int i=0; i<ja.length(); i++ ) {
				JSONObject jo = ja.getJSONObject(i); 
				QuestionChoiceItem ci = new QuestionChoiceItem();
				ci.setLabelName( jo.getString( "labelName" ) );
				ci.setContent( jo.getString( "content" ) );
				ci.setIndex( i+1 );
				ci.setQuestion( q );
				choiceItemList.add( ci );
			}	
		} catch (JSONException e) {
			e.printStackTrace();
			return CommonResult.createFailureResult();
		}
		questionService.saveQuestion(q, questionTypeId, positionIdList, choiceItemList);
		return CommonResult.createSuccessResult();
	}
	
	public Question get(){
		Question q = questionService.get( id );
		if( q.getChoiceItems().size()>0 )
			q.getChoiceItems().get(0);
		q.getQuestionType();
		return q;
	}
	
	public List<Position> getQuestionPosition() {
		Question q = questionService.get( id );
		return positionService.getQuestionPosition( q );
	}
	
	public CommonResult delete(){
		Question q = questionService.get( id );
		if( questionService.getExaminationCount( q )>0 ) {
			return CommonResult.createFailureResult( "E_Q_D_1", "Examiniation is usring this question!" );
		} else {
			questionService.delete(q);
			return CommonResult.createSuccessResult();
		}
	}
}
