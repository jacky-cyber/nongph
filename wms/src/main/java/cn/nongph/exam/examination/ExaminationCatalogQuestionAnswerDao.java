package com.felix.exam.examination;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.felix.exam.model.ExaminationCatalogQuestionAnswer;
import com.felix.nsf.CommonDao;

@Named
@ApplicationScoped
public class ExaminationCatalogQuestionAnswerDao extends CommonDao<ExaminationCatalogQuestionAnswer>{

	@Override
	protected Class<ExaminationCatalogQuestionAnswer> getModelClass() {
		return ExaminationCatalogQuestionAnswer.class;
	}

}
