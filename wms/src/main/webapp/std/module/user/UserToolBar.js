com.felix.std.module.user.UserToolBar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var showAddUserWin = function(btn, e) {
		var win = new com.felix.std.module.user.UserWindow();
		win.show();
		com.felix.nsf.Context.currentComponents.UserWindow = win;
	};

	var clickDeleteUserBtn = function(btn, e) {
		var userId = com.felix.nsf.Context.currentComponents.UserGridPanel.getSelectedId();
		if( userId ) {
			com.felix.nsf.Ajax.requestWithMessageBox( "userAction", "deleteUser", function(result){
				if( result.state=='SUCCESS' ) {
					com.felix.nsf.Context.currentComponents.UserGridPanel.reload();
				} else {
					com.felix.nsf.MessageBox.alert(result.errorDesc.desc);
				}
			}, {"userId":userId});
		}
	};
	
	var showQueryUserWin = function(btn, e) {
			var win = com.felix.std.module.user.UserSearchWindow.getInstance();
			win.show();
			com.felix.nsf.Context.currentComponents.UserSearchWindow = win;
	};
		
	this.setToolBarConfig({
		id:'publicUserGridToolbar'
	});
	
	this.setToolBarItemsConfig([ {id:'showAddUserWin',    type:'button', text:TXT.commom_btn_add,  handler:'showAddUserWin',    iconCls:'icoAdd'}, 
	                             {id:'clickDeleteUserBtn',type:'button', text:TXT.common_btnDelete,handler:'clickDeleteUserBtn',iconCls:'icoDel'},
								 {id:'showQueryUserWin',  type:'button', text:TXT.common_btnQuery, handler:'showQueryUserWin',  iconCls:'icoFind'}
							    ] );
	
	this.setToolBarEvent({
							showAddUserWin:    showAddUserWin,
							clickDeleteUserBtn:clickDeleteUserBtn,
							showQueryUserWin:  showQueryUserWin
						  });
};