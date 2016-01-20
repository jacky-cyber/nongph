com.felix.std.module.role.RoleGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.std.module.role.RoleGridPanel4Select );
	
	var thoj = this;

	this.getGridConfig().title = TXT.role_title; 
	
	var selectFirst = function(){
		thoj.selectFirstRow();
	}
	
	var showEditRoleWin = function() {
		var win = new com.felix.std.module.role.RoleWindow();
		win.show();
		win.editRole( thoj.getSelectedId(true) );
	}
	
	this.getDataStore().setEventConfig( {load:selectFirst} );
	
	this.setGridEventConfig( {rowdblclick:showEditRoleWin } );
	
	this.setSelectModel( 0 );		
	
	this.setTopToolBar( new com.felix.std.module.role.RoleToolBar() );
}