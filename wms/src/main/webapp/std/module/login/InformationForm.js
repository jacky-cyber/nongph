/**
 * InformationForm
 */
com.felix.std.module.login.InformationForm=function(){
	this.form=null;
	this.config={
		title :TXT.user_basic_info,
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
	                  	fieldLabel: TXT.user_tel,
	                  	id: 'tel',
	                  	name: 'tel',
						allowBlank:false
	                },
	                {
	                	fieldLabel: TXT.user_e_mail,
	                	id: 'email',
	                	name: 'email',
	                	allowBlank: false,
	                	vtype: 'email'
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
						triggerAction: 'all',                        			
						selectOnFocus:true,
						editable:false
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
			
	this.handleWidgetConfig=function(handler){
		handler(this);
	}
	
	this.customization=function(obj){
		if(obj.config!=null)
			this.config=obj.config;
		if(obj.buttons!=null)
			this.buttons=obj.buttons;
		if(obj.items!=null)
			this.items=obj.items;
	}
	
	this.render=function(){
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