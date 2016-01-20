package com.felix.exam.examination;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang.StringUtils;

import com.felix.exam.model.Examination;
import com.felix.exam.model.ExaminationCatalog;
import com.felix.exam.model.ExaminationCatalogQuestion;
import com.felix.exam.model.Question;
import com.felix.exam.model.QuestionChoiceItem;
import com.felix.msxt.common.HtmlUtil;
import com.felix.msxt.position.PositionService;
import com.felix.nsf.Pagination;
import com.felix.nsf.response.CommonResult;

@RequestScoped
@Named
public class ExaminationAction {
	private int start;
	private int limit;
	private String pattern;
	private String positionId;
	private String id;
	
	private String name;
	private Integer time;
	private String catalogsXML;
	
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getPositionId() {
		return positionId;
	}
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getTime() {
		return time;
	}
	public void setTime(Integer time) {
		this.time = time;
	}
	public String getCatalogsXML() {
		return catalogsXML;
	}
	public void setCatalogsXML(String catalogsXML) {
		this.catalogsXML = catalogsXML;
	}

	@Inject
	private ExaminationService examinationService;
	
	@Inject
	private PositionService positionService;
	
	public Pagination search(){
		Pagination page = new Pagination();
		page.setStart( start );
		page.setLimit( limit );
		if( pattern==null ) {
			pattern = "%";
		} else {
			pattern = "%" + pattern + "%";
		}
		examinationService.search( page, positionId, pattern );
		return page;
	}
	
	public CommonResult saveExamination(){
		Examination exam;
		if( StringUtils.isEmpty(id) ) {
			exam = new Examination();
			exam.setPosition( positionService.get( positionId ) );
		} else {
			exam = examinationService.get(id);
		} 
		exam.setName( name );
		exam.setTime( time );
		try {
			examinationService.saveExamination( exam, catalogsXML );
			return CommonResult.createSuccessResult();
		} catch (XMLStreamException e) {
			e.printStackTrace();
			return CommonResult.createFailureResult("", "bad xml file format");
		}
	}
	
	public Examination get(){
		Examination exam = examinationService.get(id);
		for( ExaminationCatalog ec : exam.getCatalogs() ) {
			for( ExaminationCatalogQuestion ecq : ec.getQuestions() ) {
				if( ecq.getQuestion().getChoiceItems().size()>0 ) {
					ecq.getQuestion().getChoiceItems().get(0);
				}
			}
		}
		return exam;
	}
	
	public Examination get4View(){
		Examination exam = examinationService.get(id);
		//Load lazy data
		for( ExaminationCatalog ec : exam.getCatalogs() ) 
			for( ExaminationCatalogQuestion eq : ec.getQuestions() ) {
				Question q = eq.getQuestion();
				for( QuestionChoiceItem qci : q.getChoiceItems() ) {
					String html = HtmlUtil.transferCommon2HTML( qci.getContent() );
					html = html.replaceAll("<br/>", "<br/>&nbsp;&nbsp;&nbsp;&nbsp;");
					qci.setContent( html );
					examinationService.detach( qci );
				}
				String htmlContent = HtmlUtil.transferCommon2HTML( q.getContent() );
				q.setContent( htmlContent );
				examinationService.detach( q );
			}
		return exam;
	}
	
	public CommonResult delete(){
		Examination exam = examinationService.get( id );
		examinationService.delete( exam );
		return CommonResult.createSuccessResult();
	}
}