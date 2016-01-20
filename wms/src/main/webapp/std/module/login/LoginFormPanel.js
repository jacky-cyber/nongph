com.felix.std.module.login.LoginFormPanel = (function(thoj){
	var loginFormPanel = null;
	var config = {
			id: 'loginForm',
			baseCls: 'x-plain',
			height:115,
			labelAlign: 'right',
			defaultType: 'textfield',
			frame: false,
			labelWidth:150,
			region: 'south'
	};
	
	var items = [{
	        fieldLabel: TXT.login_name,
	        id:'username',
	        name: 'username',
	        allowBlank:false,
	        width:200
	    },
	    {
			inputType: 'password',
			fieldLabel: TXT.login_password,
			id:'password',
			name: 'password',
			allowBlank:false,
			width:200
		}/*,
		{
			xtype :'combo',
			fieldLabel :TXT.lang_type,
			id :'lang',
			name :'lang',
			value :'zh',
			store :new Ext.data.SimpleStore({
				fields : [ 'label', 'value' ],
				data : [
						[ '中文','zh' ],
						[ 'English','en' ]
					   ]
			}),
			forceSelection :true,
			displayField :'label',
			valueField :'value',
			typeAhead :true,
			mode :'local',
			triggerAction :'all',
			editable :false,
			selectOnFocus :true,
			width:200
		}*/];
		
	var buttons = [];
	
	thoj.getInstance = function(){
		if( loginFormPanel==null ) {
			loginFormPanel = new com.felix.nsf.wrap.FormPanel();
			loginFormPanel.setConfig( config );
			loginFormPanel.setButtons( buttons );
			loginFormPanel.setItems( items );
		}
		return loginFormPanel;
	}
	
	return thoj;
})({});