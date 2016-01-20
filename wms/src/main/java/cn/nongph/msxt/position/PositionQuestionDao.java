package com.felix.msxt.position;

import com.felix.msxt.model.PositionQuestion;
import com.felix.nsf.CommonDao;

public class PositionQuestionDao extends CommonDao<PositionQuestion>{

	@Override
	protected Class<PositionQuestion> getModelClass() {
		return PositionQuestion.class;
	}
}
