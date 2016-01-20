package com.felix.std.model;

import java.io.Serializable;
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
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.felix.nsf.Need2JSON;

@Entity
@Table(name="user")
public class User implements Serializable{
	private static final long serialVersionUID = -5675392994476570463L;
	
	@Need2JSON
	@Id
	@Column(name="ID")
	@GenericGenerator(name="uuidGG",strategy="uuid")
	@GeneratedValue(generator="uuidGG")
	private String id;
	
	@Version
	private Integer version;
	
	@Column(name="name")
	@Need2JSON
	private String name;
	
	@Column(name="code")
	@Need2JSON
	private String code;
	
	@Need2JSON
	@Column(name="password")
	private String password;
	
	@Column(name="loginname")
	@Need2JSON
	private String loginname;
	
	@Column(name="is_deleted")
	private Boolean isDelete;
	
	@Need2JSON
	@ManyToMany(targetEntity = Role.class)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	@LazyCollection(LazyCollectionOption.TRUE)
	private Set<Role> roles;
	
	public String getId() {
		return id;
	}
	
    public String getKey() {
        return getId();
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
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLoginname() {
		return loginname;
	}
	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}
	public Set<Role> getRoles() {
		if( roles==null ) {
			roles = new HashSet<Role>();
		}
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	public Integer getVersion() {
		return version;
	}
	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (o == null || getClass() != o.getClass()) {
         return false;
      }

      User that = (User)o;

      if (id != null ? !id.equals(that.id) : that.id != null) {
         return false;
      }

      return true;
   }  
   @Transient
   @Need2JSON("privileges")
   private Set<String> privileges = new HashSet<String>();
   public Set<String> getPrivileges(){
	   return privileges;
   }
   public void setPrivileges(Set<String> privileges){
	   this.privileges = privileges;
   }
   @Override
   public int hashCode() {
      return id != null ? id.hashCode() : 0;
   }

   @Override
   public String toString() {
      return "User{" +
         "id='" + id + '\'' +
         '}';
   }
}
