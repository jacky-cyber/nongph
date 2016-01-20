/**
 * change password form
 */
com.felix.std.module.changePassword.ChangePasswordFormPanel=function() {
	this.form=null;
	this.buttons=[];
	this.config={
			labelAlign: 'right',
		    labelWidth:150,
		    //width: 370,
		    margins:'0 0 2 0',
			cmargins:'0 0 2 0',
		    frame:true,
		    defaults: {width: 180},
		    defaultType:'textfield'
	};
	this.items=[{
	            	xtype:'hidden',
	            	id: 'lang',
	            	name: 'lang'
	            },
	            {
	            	fieldLabel: TXT.user_fullName,
	            	id: 'fullname',
	            	name: 'fullname',
	            	readOnly: true
	            },
	            {
	            	fieldLabel: TXT.user_branch,
	            	id: 'institution',
	            	name: 'institution',
	            	readOnly: true
	            },
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
	            },
	            {
	            	fieldLabel: TXT.user_tel,
	            	id: 'tel',
	            	name: 'tel',
	            	allowBlank:false
	            },
	            {
	              	fieldLabel: TXT.user_email,
	              	id: 'email',
	              	name: 'email',
	              	vtype: 'email',
					allowBlank:false
	            },
	            {
					xtype:'combo',
		                fieldLabel: TXT.user_receiveEmail,
						id: 'receiveEmail',
						name: 'receiveEmail',
						value:true,
						store: new Ext.data.SimpleStore({
					fields: ['label','value'],
					data: [
					[TXT.user_receive,true],
					[TXT.user_notReceive,false]
					]
					}),
					forceSelection:true,
					displayField:'label',
					valueField: 'value',					                        			
					typeAhead: true,
					mode: 'local',
					//hideTrigger:false,
					triggerAction: 'all',                        			
					selectOnFocus:true,
					editable:false
				}];

	this.handleWidgetConfig = function(handler){
		handler(this);
	}
	
	this.customization = function(obj){
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
		this.form['ecpOwner']=this;
		return this.form.render();
	}
}