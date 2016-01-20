package com.felix.exam.examination;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;

import com.felix.exam.model.Examination;
import com.felix.exam.model.ExaminationCatalog;
import com.felix.nsf.CommonDao;
import com.felix.nsf.CommonService;
import com.felix.nsf.Pagination;

@Named
@ApplicationScoped
public class ExaminationService extends CommonService<Examination> {
	@Inject
	private ExaminationDao examinationDao;
	
	@Inject
	private CatalogParser cp;
	
	@Override
	public CommonDao<Examination> getDao() {
		return examinationDao;
	}
	
	public void search(Pagination pagination, String positionId, String pattern){
		examinationDao.search(pagination, positionId, pattern);
	}
	
	public void saveExamination( Examination exam, String catalogsXml) throws XMLStreamException{
		if( !StringUtils.isEmpty( exam.getId() ) ) {
			for( ExaminationCatalog ec : exam.getCatalogs() )
				examinationDao.deleteCatalog( ec );
			exam.getCatalogs().clear();
		}
		List<ExaminationCatalog> ecs = cp.parse( catalogsXml );
		for( ExaminationCatalog ec : ecs ) {
			ec.setExam( exam );
			exam.getCatalogs().add( ec );
		}
		examinationDao.persist( exam );
	}
}
