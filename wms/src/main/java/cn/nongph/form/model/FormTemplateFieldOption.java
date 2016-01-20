package com.felix.form.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

import com.felix.nsf.Need2JSON;

@Entity
@Table(name="form_template_field_option")
public class FormTemplateFieldOption {
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	@Need2JSON
	private String id;
	
	@Version
	private Integer version;
	
	@Column(name="label")
	@Need2JSON
	private String label;
	
	@Column(name="value")
	@Need2JSON
	private String value;
	 
	@ManyToOne
	@JoinColumn(name="form_template_field_id")
	private FormTemplateField field;

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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public FormTemplateField getField() {
		return field;
	}

	public void setField(FormTemplateField field) {
		this.field = field;
	}
}
