package com.felix.msxt.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.felix.form.model.Form;
import com.felix.nsf.Need2JSON;
import com.felix.std.model.User;

import javax.persistence.CascadeType;

@Entity
@Table(name="interview_round_user")
public class InterviewRoundUser {
	public static enum STATE{GOING, PASS, REJECT}
	
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	@Need2JSON
	private String id;
	
	@Version
	private Integer version;
	
   @Need2JSON
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name="interview_round_id")
	@Need2JSON
	private InterviewRound interviewRound;
	
   @Need2JSON
	@ManyToOne(cascade=CascadeType.REMOVE, fetch=FetchType.LAZY)
	@JoinColumn(name="form_id")
	private Form form;
	
	@Enumerated(EnumType.STRING)
	@Column(name="state")
   @Need2JSON
	private STATE state;
	
	@Column(name="start_time")
   @Need2JSON
	private Date  startTime;
	
	@Column(name="finish_time")
   @Need2JSON
	private Date finishTime;
	
	@Column(name="create_on")
	private Date createOn;
	
	@Column(name="create_by")
	private String createBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public InterviewRound getInterviewRound() {
		return interviewRound;
	}

	public void setInterviewRound(InterviewRound interviewRound) {
		this.interviewRound = interviewRound;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public STATE getState() {
		return state;
	}

	public void setState(STATE state) {
		this.state = state;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public Date getCreateOn() {
		return createOn;
	}

	public void setCreateOn(Date createOn) {
		this.createOn = createOn;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
}
