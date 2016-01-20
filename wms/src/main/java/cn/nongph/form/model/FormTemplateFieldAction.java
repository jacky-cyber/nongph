package com.felix.form.model;

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

import com.felix.nsf.Need2JSON;

@Entity
@Table(name="form_template_field_action")
public class FormTemplateFieldAction {
	public static enum SCRIPT_TYPE{ javascript } 
	public static enum ON_EVENT{ON_RENDERER}
	
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	@Need2JSON
	private String id;
	
	@Version
	private Integer version;
	
	@Column(name="on_event")
	@Need2JSON
	private String onEvent;
	
	@Column(name="script_type")
	@Need2JSON
	private String scriptType;
	
	@Column(name="script")
	@Need2JSON
	private String script;
	
	@ManyToOne(fetch=FetchType.LAZY)
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
	public String getOnEvent() {
		return onEvent;
	}
	public void setOnEvent(String onEvent) {
		this.onEvent = onEvent;
	}
	public String getScriptType() {
		return scriptType;
	}
	public void setScriptType(String scriptType) {
		this.scriptType = scriptType;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public FormTemplateField getField() {
		return field;
	}
	public void setField(FormTemplateField field) {
		this.field = field;
	}
}
