package com.felix.std.role;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.json.JSONArray;
import org.json.JSONException;

import com.felix.nsf.Pagination;
import com.felix.nsf.response.CommonResult;
import com.felix.std.model.Role;
import com.felix.std.model.User;

@RequestScoped
@Named
public class RoleAction {
	
	@Inject
	private RoleService roleService;

	private int start;	
	private int limit;
	
	private String id;
	private String name;
	private String description;
	private String operationIds;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
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

	public String getOperationIds() {
		return operationIds;
	}

	public void setOperationIds(String operationIds) {
		this.operationIds = operationIds;
	}

	public Pagination search(){
		Pagination page = new Pagination();
		page.setStart( start );
		page.setLimit( limit );
		if( name==null || name.isEmpty() ) {
			name = "%";
		} else {
			name = "%" + name + "%";
		}
		roleService.search( page, name );
		return page;
	}
	
	public List<Role> getAll(){
		return roleService.getAll();
	}
	
	public CommonResult save(){
		Role role;
		if( id==null || id.isEmpty() )
			role = new Role();
		else {
			role = roleService.get( id );
			role.getPermisions().clear();
		}			
		role.setName( name );
		role.setDescription( description );
		
		List<String> operationIdList = new ArrayList<String>();
		try {
			JSONArray ja = new JSONArray( operationIds );
			for( int i=0; i<ja.length(); i++ ) 
				operationIdList.add( ja.getString(i) ); 
			roleService.save( role, operationIdList );
			return CommonResult.createSuccessResult();
		} catch (JSONException e) {
			e.printStackTrace();
			return CommonResult.createFailureResult();
		}
	}
	
	public CommonResult delete(){
		Role role = roleService.get(id);
		for( User u : role.getUsers() ) {
			if( !u.isDelete() )
				return CommonResult.createFailureResult("role_on_using", "Role is on using");
		}
		roleService.delete( role );
		return CommonResult.createSuccessResult();
	}

	public Role get(){
		return roleService.get(id);
	}
}
