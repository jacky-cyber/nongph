package com.felix.exam.examination;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.felix.exam.model.ExaminationCatalogQuestion;
import com.felix.nsf.CommonDao;

@Named
@ApplicationScoped
public class ExaminationCatalogQuestionDao extends CommonDao<ExaminationCatalogQuestion>{

	@Override
	protected Class<ExaminationCatalogQuestion> getModelClass() {
		return ExaminationCatalogQuestion.class;
	}

}
