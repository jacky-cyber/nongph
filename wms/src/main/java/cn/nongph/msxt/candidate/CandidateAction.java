package com.felix.msxt.candidate;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;

import com.felix.msxt.model.Candidate;
import com.felix.nsf.Pagination;
import com.felix.nsf.response.CommonResult;

@RequestScoped
@Named
public class CandidateAction {
	@Inject
	private CandidateService interviewerService;
	private int start;
	private int limit;
	private String pattern;
	
	private String id;
	private String name;
	private String idCode;
	private String phone;
	private Integer age;
	private byte[] resume;
	private String resumeName;
	
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
	public String getIdCode() {
		return idCode;
	}
	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	public byte[] getResume() {
		return resume;
	}
	public void setResume(byte[] resume) {
		this.resume = resume;
	}
	
	public String getResumeName() {
		return resumeName;
	}
	public void setResumeName(String resumeName) {
		this.resumeName = resumeName;
	}
	
	public Pagination search(){
		Pagination page = new Pagination();
		page.setStart( start );
		page.setLimit( limit );
		if( pattern==null ) {
			pattern = "%";
		} else {
			pattern = "%" + pattern + "%";
		}
		interviewerService.search( page, pattern );
		return page;
	}
	
	public Candidate get(){
		return interviewerService.get(id);
	}
	
	public CommonResult saveInterviewer() {
		Candidate it;
		if( StringUtils.isEmpty( id ) ) {
			it = new Candidate();
		} else {
			it = interviewerService.get(id);
		}
		it.setName( name );
		it.setIdCode( idCode );
		it.setPhone( phone );
		it.setAge( age );
		if( resume!=null ) {
			it.setResume( resume );
			it.setResumeName( resumeName );
		}
		if( interviewerService.verifyIdCodeIsAvailable(it) ) {
			interviewerService.persist( it );
		} else {
			return CommonResult.createFailureResult("idcode_exist", "ID already exist");
		}
		
		return CommonResult.createSuccessResult();
	}
	
	public CommonResult delete(){
		Candidate itr = interviewerService.get( id );
		
		if( interviewerService.checkExistInterview( itr ) ) {
			return CommonResult.createFailureResult("exist_interview", "exist interview, so can't delete this candidate");
		} else {
			interviewerService.delete( itr );
			return CommonResult.createSuccessResult();
		}
	}
	
	public void downloadResume(){
		
	}
}
