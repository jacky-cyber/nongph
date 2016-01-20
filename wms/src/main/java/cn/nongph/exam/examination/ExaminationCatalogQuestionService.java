package com.felix.exam.examination;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.felix.exam.model.ExaminationCatalogQuestion;
import com.felix.nsf.CommonDao;
import com.felix.nsf.CommonService;

@Named
@ApplicationScoped
public class ExaminationCatalogQuestionService extends CommonService<ExaminationCatalogQuestion>{
	
	@Inject
	private ExaminationCatalogQuestionDao examinationCatalogQuestionDao;
	

	@Override
	public CommonDao<ExaminationCatalogQuestion> getDao() {
		return examinationCatalogQuestionDao;
	}
	
}
