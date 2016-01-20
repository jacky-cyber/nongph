package com.felix.msxt.model;

import java.io.Serializable;
import java.util.List;

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

import com.felix.exam.model.Examination;
import com.felix.nsf.Need2JSON;

@Entity
@Table(name="position")
public class Position implements Serializable{
	private static final long serialVersionUID = 8350172956635419908L;

	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	@Need2JSON
	private String   id;
	
	@Version
	private Integer version;
	
	@Column(name="name")
	@Need2JSON
	private String name;  
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="next_position")
	@Need2JSON
	private Position nextPosition; //高一级的职位
	
	@OneToMany(mappedBy="position", fetch=FetchType.LAZY)
	@Need2JSON
	private List<Examination> exams;
	
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
	public Position getNextPosition() {
		return nextPosition;
	}
	public void setNextPosition(Position nextPosition) {
		this.nextPosition = nextPosition;
	}
	public List<Examination> getExams() {
		return exams;
	}
	public void setExams(List<Examination> exams) {
		this.exams = exams;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getVersion() {
		return version;
	}
}
