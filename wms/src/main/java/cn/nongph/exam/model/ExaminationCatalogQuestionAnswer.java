package com.felix.exam.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.felix.msxt.model.InterviewRoundExamination;
import com.felix.nsf.Need2JSON;

@Entity
@Table(name="examination_catalog_question_answer")
public class ExaminationCatalogQuestionAnswer {
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	private String id;
	
	@Version
	private Integer version;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="interview_round_exam_id")
	private InterviewRoundExamination interviewExamination;
	
	@ManyToOne
	@JoinColumn(name="exam_question_id")
	@Need2JSON
	private ExaminationCatalogQuestion examQuestion;
	
	@Column(name="answer")
	@Need2JSON
	private String answer;
	
	@Column(name="result")
	@Need2JSON
	private float result;
	
	@Column(name="score")
	private float score;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public InterviewRoundExamination getInterviewExamination() {
		return interviewExamination;
	}
	
	public void setInterviewExamination(InterviewRoundExamination interviewExamination) {
		this.interviewExamination = interviewExamination;
	}
	
	public ExaminationCatalogQuestion getExamQuestion() {
		return examQuestion;
	}
	
	public void setExamQuestion(ExaminationCatalogQuestion examQuestion) {
		this.examQuestion = examQuestion;
	}
	
	public String getAnswer() {
		return answer;
	}
	
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
	public float getResult() {
		return result;
	}
	
	public void setResult(float result) {
		this.result = result;
	}
	
	public float getScore() {
		return score;
	}
	
	public void setScore(float score) {
		this.score = score;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getVersion() {
		return version;
	}
}
