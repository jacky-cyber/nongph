package com.felix.std.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.felix.nsf.Need2JSON;

@Entity
@Table(name = "role")
public class Role {
	@Id
	@Column(name = "ID")
	@GenericGenerator(name = "uuidGG", strategy = "uuid")
	@GeneratedValue(generator = "uuidGG")
	@Need2JSON
	private String id;

	@Version
	private Integer version;

	@Column(name = "name")
	@Need2JSON
	private String name;
	@Column(name = "description")
	@Need2JSON
	private String description;
	
	@ManyToMany(targetEntity=Operation.class)
	@JoinTable(name="role_operation",joinColumns=@JoinColumn(name="role_id"), inverseJoinColumns=@JoinColumn(name="operation_id"))
	@LazyCollection(LazyCollectionOption.FALSE)
	@Need2JSON
	private Set<Operation> permisions = new HashSet<Operation>();
	
	@ManyToMany(targetEntity = User.class,mappedBy="roles")
	@LazyCollection(LazyCollectionOption.FALSE)
	private Set<User> users = new HashSet<User>();

	/**
	 * getter
	 * @return users
	 */
	public Set<User> getUsers() {
		return users;
	}
	/**
	 * setter
	 * @param users Set
	 */
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	public Set<Operation> getPermisions() {
		return permisions;
	}
	public void setPermisions(Set<Operation> permisions) {
		this.permisions = permisions;
	}
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getVersion() {
		return version;
	}
}
