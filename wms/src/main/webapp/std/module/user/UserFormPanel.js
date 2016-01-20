com.felix.std.module.user.UserFormPanel = function(){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.FormPanel );
	
	this.setConfig( {
			id: 'userForm',
			baseCls: 'x-plain',
			height:155,
			labelAlign: 'left',
			defaultType: 'textfield',
			frame: false,
			labelWidth:150,
			layout:'form',
			region: 'north'
	} );
	
	this.setItems( [ { id:'user.id',
					   name: 'user.id',
					   inputType:"hidden"
					 },
	                 { fieldLabel: TXT.user_name,
	        		   id:'user.name',
	        		   name: 'user.name',
	        		   allowBlank:false,
	        		   width:200
	    			 },
	    			 { fieldLabel: TXT.user_loginName,
	    			   id: 'user.loginname',
	    			   name: 'user.loginname',
	    			   width:200
	    			 },
	    			 { fieldLabel: TXT.user_password,
	    			   inputType: 'password',
	    			   id:'user.password',
	    			   name: 'user.password',
	    			   allowBlank:false,
	    			   width:200
	    			 },
	    			 { fieldLabel: TXT.user_confirm_password,
	    			   inputType: 'password',
	    			   id: 'confirmPwd',
	    			   name: 'user.password',
	    			   allowBlank:false,
	    			   vtype: 'password',
	    			   initialPassField: 'user.password' ,
	    			   width:200
	    			 },
	    			 { fieldLabel: TXT.user_code,
	    			   id: 'user.code',
	    			   name: 'user.code',
	    			   width:200
	    			 }
	    		  ] );
	this.loadValue = function(user){
		this.setValue("user.id", user.id);
		this.setValue("user.name", user.name);
		this.setValue("user.loginname", user.loginname);
		this.setValue("user.password", user.password);
		this.setValue("confirmPwd", user.password);
		this.setValue("user.code", user.code);
	}
};