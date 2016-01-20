package com.felix.msxt.interview;

import java.util.Date;

import com.felix.nsf.converter.DateTime;

public class InterviewSearchCriteria {
	private String pattern;
	private String positionId;
	
	@DateTime
	private Date startBegin;
	@DateTime
	private Date startEnd;
	
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
	public Date getStartBegin() {
		return startBegin;
	}
	public void setStartBegin(Date startBegin) {
		this.startBegin = startBegin;
	}
	public Date getStartEnd() {
		return startEnd;
	}
	public void setStartEnd(Date startEnd) {
		this.startEnd = startEnd;
	}
}
