package com.felix.form;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.felix.form.model.Form;
import com.felix.nsf.CommonDao;

@ApplicationScoped
@Named
public class FormDao extends CommonDao<Form>{

	@Override
	protected Class<Form> getModelClass() {
		return Form.class;
	}

}
