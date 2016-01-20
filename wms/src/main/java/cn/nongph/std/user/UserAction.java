package com.felix.std.user;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.json.JSONArray;
import org.json.JSONException;

import com.felix.std.model.User;
import com.felix.nsf.Pagination;
import com.felix.nsf.exception.SessionTimeoutException;
import com.felix.nsf.response.CommonResult;

@RequestScoped
@Named("userAction")
public class UserAction {
	private static String CURRENT_USR_KEY = "CURRENT_USR";
	
	private HttpServletRequest request;
	
	private String userId;
	
	private String username;
	private String password;
	
	private User user = new User();
	private String roleIds; 

	private int start;
	private int limit;
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String userName) {
		this.username = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Inject
	private UserService userService;
    
	public CommonResult login() {    	
    	
        if ( username == null || username.isEmpty() )
        	return CommonResult.createFailureResult("username", "username is empty");
        
        if( password == null || password.isEmpty() ) 
        	return CommonResult.createFailureResult("password", "password is empty");
        
        User user = userService.checkUser(username, password);
        if ( user != null ) {
    		if( user.getPassword().equals( password ) ) {
    			userService.loadPermisions(user);
    			request.getSession().setAttribute( CURRENT_USR_KEY,  user);
	            return CommonResult.createSuccessResult();
    		}
    		return CommonResult.createFailureResult("errorPassword", "password error");
        } 
        return CommonResult.createFailureResult("notExist", "username not exist");
    }
	
	public User getCurrentUser(){
		User usr = (User)request.getSession().getAttribute( CURRENT_USR_KEY );
		if( usr==null )
			throw new SessionTimeoutException();
		return usr;
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
		Map<String,String> param = new HashMap<String,String>();
		param.put("name", name);
		userService.find( page, param );
		return page;
	}
	
	public User findUser(){
		return userService.get( userId );
	}
	
	public CommonResult deleteUser(){
		userService.delete( userId );
		return CommonResult.createSuccessResult();
	}
	
	public CommonResult save() {
		List<String> roleIdList = new ArrayList<String>();
		try {
			JSONArray ja = new JSONArray( roleIds );
			for( int i=0; i<ja.length(); i++ ) 
				roleIdList.add( ja.getString(i) ); 
			
			if( !user.getId().isEmpty() ) {
				User editedUser = userService.get( user.getId() );
				editedUser.setName( user.getName() );
				editedUser.setLoginname( user.getLoginname() );
				editedUser.setPassword( user.getPassword() );
				editedUser.setCode( user.getCode() );
				user = editedUser;
			}
			
			userService.save(user, roleIdList);
			return CommonResult.createSuccessResult();
		} catch (JSONException e) {
			e.printStackTrace();
			return CommonResult.createFailureResult();
		}
	}
	
}
