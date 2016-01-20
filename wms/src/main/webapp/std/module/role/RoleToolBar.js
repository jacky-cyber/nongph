com.felix.std.module.role.RoleToolBar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var showAddRoleWin = function(btn, e) {
		var win = new com.felix.std.module.role.RoleWindow();
		win.show();
	}

	var clickDeleteRoleBtn = function() {
		var roleId = com.felix.nsf.Context.currentComponents.roleGridPanel.getSelectedId(true);
		if( roleId ) {
			com.felix.nsf.Ajax.requestWithMessageBox( "roleAction", "delete", function(result){
				if( result.state=='SUCCESS' ) {
					com.felix.nsf.Context.currentComponents.roleGridPanel.reload();
				} else {
					com.felix.nsf.MessageBox.alert(result.errorDesc.desc);
				}
			}, {"id":roleId});
		}
	}

	this.setToolBarConfig({
		id:'publicRoleGridToolbar'
	});
	
	this.setToolBarItemsConfig([ {id:'showAddRoleWin',    type:'button', text:TXT.commom_btn_add,  handler:'showAddRoleWin',    iconCls:'icoAdd'}, 
	                             {id:'clickDeleteRoleBtn',type:'button', text:TXT.common_btnDelete,handler:'clickDeleteRoleBtn',iconCls:'icoDel'}
							   ] );
	
	this.setToolBarEvent( {	showAddRoleWin:    showAddRoleWin,
							clickDeleteRoleBtn:clickDeleteRoleBtn
						  } );
}