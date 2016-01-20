package com.felix.form.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="form_template_field")
public class FormTemplateField {
	public static enum TYPE{ text, textarea, number, hidden, date, comboBox, checkBox}
	
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
	
	@Column(name="label")
	@Need2JSON
	private String label;
	
	@Enumerated(EnumType.STRING)
	@Column(name="type")
	@Need2JSON
	private TYPE type;
	
	@Column(name="readonly")
	@Need2JSON
	private Boolean readOnly;

	@Column(name="mandatory")
	@Need2JSON
	private Boolean mandatory;
	
	@Column(name="init_value")
	@Need2JSON
	private String initValue;
	
	@Column(name="max_size")
	@Need2JSON
	private Integer maxSize;
	
	@Column(name="regex")
	@Need2JSON
	private String regex;
	
	@Column(name="regex_text")
	@Need2JSON
	private String regexText;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="form_template_id")
	private FormTemplate formTemplate;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="field")
	@Need2JSON
	private List<FormTemplateFieldAction> actions;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="field")
	@Need2JSON
	private List<FormTemplateFieldOption> options;
	
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

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	public Boolean getReadOnly() {
		return readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getRegexText() {
		return regexText;
	}

	public void setRegexText(String regexText) {
		this.regexText = regexText;
	}

	public Integer getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	public String getInitValue() {
		return initValue;
	}

	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}

	public FormTemplate getFormTemplate() {
		return formTemplate;
	}

	public void setFormTemplate(FormTemplate formTemplate) {
		this.formTemplate = formTemplate;
	}

	public List<FormTemplateFieldAction> getActions() {
		if( actions==null )
			actions = new ArrayList<FormTemplateFieldAction>();
		return actions;
	}

	public void setActions(List<FormTemplateFieldAction> actions) {
		this.actions = actions;
	}

	public List<FormTemplateFieldOption> getOptions() {
		if( options==null )
			options = new ArrayList<FormTemplateFieldOption>();
		return options;
	}

	public void setOptions(List<FormTemplateFieldOption> options) {
		this.options = options;
	}
	
}
