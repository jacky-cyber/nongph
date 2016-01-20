package com.felix.form.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.felix.nsf.Need2JSON;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="form_template")
public class FormTemplate {
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	@Need2JSON
	private String id;
	
	@Version
	private Integer version;
	
	@Column(name="name")
	@Need2JSON
	private String name;
	
	@Column(name="layout")
	@Need2JSON
	private String layout;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="formTemplate",cascade=CascadeType.ALL)
	@Need2JSON
    private List<FormTemplateField> fields;
	
	@Transient
	@Need2JSON
	private boolean onUsed = false;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLayout() {
		return layout;
	}
	public void setLayout(String layout) {
		this.layout = layout;
	}
	public List<FormTemplateField> getFields() {
		if( fields==null )
			fields = new ArrayList<FormTemplateField>();
		return fields;
	}
	public void setFields(List<FormTemplateField> fields) {
		this.fields = fields;
	}
	
	public boolean isOnUsed() {
		return onUsed;
	}
	public void setOnUsed(boolean onUsed) {
		this.onUsed = onUsed;
	}
}
