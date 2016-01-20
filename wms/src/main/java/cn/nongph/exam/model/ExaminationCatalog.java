package com.felix.exam.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.felix.nsf.Need2JSON;

@Entity
@Table(name="examination_catalog")
public class ExaminationCatalog {
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	@Need2JSON
	private String id;
	
	@Version
	private Integer version;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="exam_id")
	private Examination exam;
	
	@Column(name="name")
	@Need2JSON
	private String name;
	
	@Column(name="description")
	@Need2JSON
	private String description;
	
	@Column(name="idx")
	private int index;
	
	@OneToMany(mappedBy = "catalog", cascade = {CascadeType.ALL})
	@Need2JSON
	private List<ExaminationCatalogQuestion> questions;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Examination getExam() {
		return exam;
	}
	public void setExam(Examination exam) {
		this.exam = exam;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
	public List<ExaminationCatalogQuestion> getQuestions() {
		if( questions==null )
			questions = new ArrayList<ExaminationCatalogQuestion>();
		return questions;
	}
	public void setQuestions(List<ExaminationCatalogQuestion> questions) {
		this.questions = questions;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getVersion() {
		return version;
	}
}
