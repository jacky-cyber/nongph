package com.felix.std.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="system_parameter")
public class SystemParameter {
    public static final String PN_VIEW_ROUND_TO_NEXT_ROUND = "VIEW_ROUND_TO_NEXT_ROUND"; 
    public static final String PN_VIEW_ROUND_PASS = "VIEW_ROUND_PASS";
    
    public static enum VIEW_ROUND_TO_NEXT_ROUND{DIRECT, PASS}
    public static enum VIEW_ROUND_PASS{ONE_PASS, MAJORITY_PASS, ALL_PASS}
    
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	private String id;
	
	@Version
	private Integer version;
	
	@Column(name="param_name")
	private String name;
	
	@Column(name="param_value")
	private String value;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
