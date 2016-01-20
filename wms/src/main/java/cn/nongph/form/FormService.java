package com.felix.form;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.felix.form.model.Form;
import com.felix.form.model.FormField;
import com.felix.nsf.CommonDao;
import com.felix.nsf.CommonService;

@ApplicationScoped
@Named
public class FormService extends CommonService<Form> {
	
	@Inject
	private FormDao formDao;
	
	@Override
	public CommonDao<Form> getDao() {
		return formDao;
	}
	
	public void saveForm(Form form, Map<String, String> fields){
		List<FormField> existFields = form.getFields();
		for( Map.Entry<String, String> entry : fields.entrySet() ) {
			boolean exist = false;
			for( FormField ff: existFields ) {
				if( ff.getName().equals( entry.getKey() ) ){
					ff.setValue( entry.getValue() );
					formDao.persist( ff );
					exist = true;
					break;
				}
			}
			if( !exist ) {
				FormField ff = new FormField();
				ff.setForm( form );
				ff.setName( entry.getKey() );
				ff.setValue( entry.getValue() );
				formDao.persist( ff );
			}
		}
		
		form.setFinishTime( new Date() );
		form.setState( Form.STATE.FINISHED );
		formDao.persist(form);
	}
}
