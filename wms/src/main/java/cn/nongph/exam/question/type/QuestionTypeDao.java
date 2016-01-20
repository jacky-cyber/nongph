package com.felix.exam.question.type;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.felix.exam.model.QuestionType;
import com.felix.nsf.CommonDao;

/**
 * 
 * @author Felix
 *
 */
@ApplicationScoped
@Named
public class QuestionTypeDao extends CommonDao<QuestionType> {
	
	@Override
	protected Class<QuestionType> getModelClass() {
		return QuestionType.class;
	}
	
}
