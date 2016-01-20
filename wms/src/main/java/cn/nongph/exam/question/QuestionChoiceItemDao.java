package com.felix.exam.question;

import com.felix.exam.model.QuestionChoiceItem;
import com.felix.nsf.CommonDao;

public class QuestionChoiceItemDao extends CommonDao<QuestionChoiceItem> {

	@Override
	protected Class<QuestionChoiceItem> getModelClass() {
		return QuestionChoiceItem.class;
	}

}
