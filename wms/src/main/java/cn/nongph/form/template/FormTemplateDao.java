package com.felix.form.template;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.felix.form.model.FormTemplate;
import com.felix.form.model.FormTemplate_;
import com.felix.nsf.CommonDao;
import com.felix.nsf.Pagination;

@ApplicationScoped
@Named
public class FormTemplateDao extends CommonDao<FormTemplate>{

	@Override
	protected Class<FormTemplate> getModelClass() {
		return FormTemplate.class;
	}
	
	public void search(Pagination pagination, String pattern){
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		
		CriteriaQuery<FormTemplate> cquery = builder.createQuery(FormTemplate.class);
		
		Root<FormTemplate> rp = cquery.from(FormTemplate.class);
		
		Predicate p1 = builder.like( builder.lower( rp.get(FormTemplate_.name) ), pattern );
	    
		cquery.select( rp ).where( p1 );  
		
		searchByPagination( cquery, pagination );
		
		List<FormTemplate> ftList = (List<FormTemplate>)pagination.getItems();
        for( FormTemplate ft : ftList ) {
        	ft.setOnUsed( isOnUsed(ft) );
        }
	}
	
	public boolean isOnUsed( FormTemplate ft ) {
		Object result = entityManager.createQuery("select COUNT(f) from Form f where f.formTemplate=:ft").setParameter("ft", ft).getSingleResult();
		return (Long)result>0;
	}
}
