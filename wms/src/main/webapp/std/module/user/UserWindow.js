com.felix.std.module.user.UserWindow = function(){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.Window);
	
	var thoj = this;
	
	var clickSaveUserBtn = function(){
		if( userFormPanel.isValid() ) {
			var roleids = roleGridPanel.getSelectedIds(true);
			if( roleids ) {
				var values = userFormPanel.getValues();
				values.roleIds = Ext.encode(roleids);
				
				com.felix.nsf.Ajax.requestWithMessageBox( "userAction", "save", function(result){
					if( result.state=='SUCCESS' ) {
						thoj.setCloseConfirm(false);
						thoj.close();
						com.felix.nsf.Context.currentComponents.UserGridPanel.reload();
					} else {
						com.felix.nsf.MessageBox.alert(result.errorDesc.desc);
					}
				}, values);
			}
		}
	};
	
	var clickCloseBtn = function(){
		thoj.close();
	};
	
	this.setConfig({
		    title:TXT.user_info,
	        width:700,
	        height:450,
	        autoScroll :false,
	        layout:'border',
	        closeAction:'close',
	        border:false,
	        minimizable: false,
	        maximizable: false,
	        resizable: false,
	        modal:true,
			layoutConfig: {animate:false},
			buttonAlign: 'center'
	});
	
	this.setButtons( [ { text :TXT.common_btnOK,   handler : clickSaveUserBtn },
	                   { text :TXT.common_btnClose,handler : clickCloseBtn }
					 ] );
	
	var userFormPanel = new com.felix.std.module.user.UserFormPanel();
	var roleGridPanel = new com.felix.std.module.role.RoleGridPanel4Select();
	roleGridPanel.load();

	this.setItems( [userFormPanel, roleGridPanel ] );	
	
	this.editUser = function(userId){
		com.felix.nsf.Ajax.requestWithMessageBox( "userAction", "findUser", function(result){
			userFormPanel.loadValue(result);
			for(var i=0; i<result.roles.length; i++)
				roleGridPanel.selectRowById(result.roles[i].id);
		}, {"userId":userId});
	}
};