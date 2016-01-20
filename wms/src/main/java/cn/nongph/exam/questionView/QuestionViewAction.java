package com.felix.exam.questionView;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.felix.exam.model.Question;
import com.felix.exam.model.QuestionChoiceItem;
import com.felix.exam.question.QuestionService;
import com.felix.msxt.model.Position;
import com.felix.msxt.position.PositionService;

/**
 * 
 * @author Felix
 *
 */
@RequestScoped
@Named
public class QuestionViewAction {
	
	private String questionId;
	
	@Inject
	private QuestionService questionService;
	
	@Inject
	private PositionService positionService;
	
	public String getQuestionId() {
		return questionId;
	}

	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public QuestionViewTO getQuestionInfo(){
		QuestionViewTO to = new QuestionViewTO();
		Question q = questionService.get( questionId );
		
		to.setQuestionType( q.getQuestionType().getName() );
		
		List<Position> positions = positionService.getQuestionPosition( q );
		StringBuffer sb = new StringBuffer();
		for( Position p : positions ) {
			sb.append( p.getName() ).append(", ");
		}
		to.setPosition( sb.substring(0, sb.length()-2) );
		
		to.setName( q.getName() );
		to.setContent( q.getContent() );
		to.setRightAnswer( q.getRightAnswer() );
		
		if( q.getQuestionType().getId().equals("1") 
				|| q.getQuestionType().getId().equals("2") ) {
			StringBuffer sb2 = new StringBuffer();
			for( QuestionChoiceItem ci : q.getChoiceItems() ) {
				sb2.append( ci.getLabelName() ).append( ". " ).append( ci.getContent().replaceAll("\n", "\n    ") ).append("\n\n");
			}
			
			to.setChoiceItems( sb2.substring( 0, sb2.length()-2 ) );
 		}
		
		return to;
	}
}
