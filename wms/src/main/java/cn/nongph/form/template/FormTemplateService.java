package com.felix.form.template;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.felix.form.model.FormTemplate;
import com.felix.nsf.CommonDao;
import com.felix.nsf.CommonService;
import com.felix.nsf.Pagination;

@ApplicationScoped
@Named
public class FormTemplateService extends CommonService<FormTemplate>{
	
	@Inject
	private FormTemplateDao dao;
	
	public void search(Pagination pagination, String pattern){
		dao.search(pagination, pattern);
	}
	
	@Override
	public CommonDao<FormTemplate> getDao() {
		return dao;
	}

}
