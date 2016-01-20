com.felix.std.module.user.UserSearchFormPanel = (function(thoj){
	var userSearchFormPanel = null;
	var config = {
			id: 'userSearchForm',
			baseCls: 'x-plain',
			labelAlign: 'left',
			defaultType: 'textfield',
			frame: false,
			labelWidth:80,
			layout:'form',
			region: 'center'
	};
	
	var items=[
		       {
	        fieldLabel: TXT.user_name,
	        id:'name',
	        name: 'name',
	        allowBlank:false,
	        width:100
	    },
	    {
			fieldLabel: TXT.user_loginName,
			id: 'loginname',
			name: 'loginname',
			width:100
		},
	    {
			fieldLabel: TXT.user_code,
			id: 'code',
			name: 'code',
			width:100
		}
	];
	thoj.getInstance = function(){
		if( userSearchFormPanel==null ) {
			userSearchFormPanel = new com.felix.nsf.wrap.FormPanel();
			userSearchFormPanel.setConfig( config );
			userSearchFormPanel.setItems( items );
		}
		return userSearchFormPanel;
	}
	return thoj;
})({});