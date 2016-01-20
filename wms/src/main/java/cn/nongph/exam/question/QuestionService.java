package com.felix.exam.question;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.felix.exam.model.Question;
import com.felix.exam.model.QuestionChoiceItem;
import com.felix.exam.question.type.QuestionTypeDao;
import com.felix.msxt.model.Position;
import com.felix.msxt.model.PositionQuestion;
import com.felix.msxt.position.PositionDao;
import com.felix.msxt.position.PositionQuestionDao;
import com.felix.nsf.CommonDao;
import com.felix.nsf.CommonService;
import com.felix.nsf.Pagination;

/**
 * 
 * @author Felix
 *
 */
@ApplicationScoped
@Named
public class QuestionService extends CommonService<Question>{
	
	@Inject
	private QuestionDao questionDao;
	@Inject
	private QuestionTypeDao questionTypeDao;
	@Inject
	private PositionDao positionDao;
	@Inject
	private PositionQuestionDao positionQuestionDao;
	@Inject
	private QuestionChoiceItemDao questionChoiceItemDao;
	
	public void searchAll(Pagination pagination, String typeId, String positionId, String pattern){
		questionDao.searchAll(pagination, typeId, positionId, pattern);
	}

	public void saveQuestion( Question q, String typeId, List<String> positions, List<QuestionChoiceItem> choiceItems ){
		if( q.getId()!=null ) {
			positionDao.cleanPositionQuestion( q );
			String oriTypeId = q.getQuestionType().getId();
			if( oriTypeId.equals("1") || oriTypeId.equals("2") )
				questionDao.cleanQuestionChoiceItem( q );
		}
		
		q.setQuestionType( questionTypeDao.get( typeId ) );
		questionDao.persist( q );
		for( String pid : positions ) {
			Position p = positionDao.get( pid );
			
			PositionQuestion pq = new PositionQuestion();
			pq.setPosition( p );
			pq.setQuestion( q );
			
			positionQuestionDao.persist( pq );
		}
		
		for( QuestionChoiceItem ci : choiceItems )
			questionChoiceItemDao.persist( ci );
			
	}
	@Override
	public CommonDao<Question> getDao() {
		return questionDao;
	}
	
	public int getExaminationCount( Question q ) {
		return questionDao.getExaminationCount(q);
	}
	
	public void delete( Question q ) {
		positionDao.cleanPositionQuestion( q );
		super.delete( q );
	}
}
