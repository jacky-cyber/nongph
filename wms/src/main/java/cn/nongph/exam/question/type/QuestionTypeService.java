package com.felix.exam.question.type;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.felix.exam.model.QuestionType;
import com.felix.nsf.CommonDao;
import com.felix.nsf.CommonService;

/**
 * 
 * @author Felix
 *
 */
@ApplicationScoped
@Named
public class QuestionTypeService extends CommonService<QuestionType>{
	
	@Inject
	private QuestionTypeDao questionTypeDao;
	
	public List<QuestionType> search(){
		return questionTypeDao.getAll();
	}

	@Override
	public CommonDao<QuestionType> getDao() {
		return questionTypeDao;
	}
}
