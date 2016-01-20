package com.felix.msxt.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.felix.exam.model.Examination;
import com.felix.exam.model.ExaminationCatalogQuestionAnswer;
import com.felix.nsf.Need2JSON;

@Entity
@Table(name="interview_round_examination")
public class InterviewRoundExamination {
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
    @Need2JSON
	private String id;
	
	@Version
	private Integer version;
	
	@ManyToOne
	@JoinColumn(name="interview_round_id")
	private InterviewRound interviewRound;
	
	@ManyToOne
	@JoinColumn(name="exam_id")
	@Need2JSON
	private Examination exam;
	
	@OneToMany(mappedBy = "interviewExamination", cascade = CascadeType.REMOVE)
	@Need2JSON
	private List<ExaminationCatalogQuestionAnswer> examQuestionAnswers;
	
	@Column(name="exam_confuse")
	@Need2JSON
	private Integer examConfuse;
	
	@Column(name="exam_score")
    @Need2JSON
	private Float examScore;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="start_time")
    @Need2JSON
	private Date startTime;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="end_time")
    @Need2JSON
	private Date endTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public InterviewRound getInterviewRound() {
		return interviewRound;
	}
	public void setInterviewRound(InterviewRound interviewRound) {
		this.interviewRound = interviewRound;
	}
	public Examination getExam() {
		return exam;
	}
	public void setExam(Examination exam) {
		this.exam = exam;
	}
	
	public Float getExamScore() {
		return examScore;
	}
	
	public void setExamScore(Float examScore) {
		this.examScore = examScore;
	}
	
	public List<ExaminationCatalogQuestionAnswer> getExamQuestionAnswers() {
		return examQuestionAnswers;
	}
	
	public void setExamQuestionAnswers(
			List<ExaminationCatalogQuestionAnswer> examQuestionAnswers) {
		this.examQuestionAnswers = examQuestionAnswers;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getVersion() {
		return version;
	}
	public Integer getExamConfuse() {
		return examConfuse;
	}
	public void setExamConfuse(Integer examConfuse) {
		this.examConfuse = examConfuse;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	public static enum STATUS{
		PENDING, GOING, OVERTIME, SUBMITTED 
	}
	
	@Need2JSON("status")
	public STATUS getStatus(){
		if( getStartTime() == null ) 
			return STATUS.PENDING;
		else { 
			if( getEndTime() == null ) {
				Long span = System.currentTimeMillis() - getStartTime().getTime();
				if( span < (getExam().getTime()+10)*60*1000 )
					return STATUS.GOING;
				else
					return STATUS.OVERTIME;
			} else {
				return STATUS.SUBMITTED;
			}
		}	
	}
}
