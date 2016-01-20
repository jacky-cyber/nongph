/**
 * PasswordForm
 */
com.felix.std.module.login.PasswordForm = function(){
	this.form=null;
	this.config={
		title :TXT.user_password_change,
		labelAlign: 'right',
		labelWidth:100,
	    height: 200,
	    width: 380,
	    margins:'0 0 2 0',
		cmargins:'0 0 2 0',
	    frame:true,
	    defaults: {width: 180},
		defaultType: 'textfield',
		buttonAlign: 'center'
	};
	this.items=[
			{
              	fieldLabel: TXT.user_newpassword,
              	inputType: 'password',
              	id: 'password',
              	name: 'password',
				allowBlank:false
            },
           {
              	fieldLabel: TXT.user_confirm_pwd,
              	inputType: 'password',
              	id: 'confirmPwd',
              	name: 'password',
				allowBlank:false,
				vtype: 'password'					
            }
	];
	this.buttons=[{
			text :TXT.common_save,
				handler : function() {
					clickChangeInfoBtn(this);
				}
			},
			{
				text :TXT.task_action_cancel,
				handler : function() {
					if(Ecp.ProfileInfoWindow.singleInfoWinows!=null)
						Ecp.ProfileInfoWindow.singleInfoWinows.window.hide();
				}
			}];


	this.handleWidgetConfig = function(handler){
		handler(this);
	}
	
	this.customization = function(obj)
	{
		if(obj.config!=null)
			this.config=obj.config;
		if(obj.buttons!=null)
			this.buttons=obj.buttons;
		if(obj.items!=null)
			this.items=obj.items;
	}
	
	this.render = function(){
		this.form=new Ecp.FormPanel();
		this.form.init();
		var obj={};
		obj['config']=this.config;
		obj['buttons']=this.buttons;
		obj['items']=this.items;
		this.form.customization(obj);
		return this.form.render();
	}
}