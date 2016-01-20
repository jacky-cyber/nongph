package com.felix.exam.examination;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.felix.exam.model.ExaminationCatalogQuestionAnswer;
import com.felix.nsf.CommonDao;
import com.felix.nsf.CommonService;

@Named
@ApplicationScoped
public class ExaminationCatalogQuestionAnswerService extends CommonService<ExaminationCatalogQuestionAnswer>{
	
	@Inject
	private ExaminationCatalogQuestionAnswerDao examinationCatalogQuestionAnswerDao;
	
	@Override
	public CommonDao<ExaminationCatalogQuestionAnswer> getDao() {
		return examinationCatalogQuestionAnswerDao;
	}

}
