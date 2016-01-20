com.felix.std.module.role.RoleWindow = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.Window );
	var thoj = this;
	
	this.setConfig({
			   		title:TXT.role_info,
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
					} );

	var operationGridPanel = new com.felix.std.module.role.OperationGridPanel();
	operationGridPanel.load();
	var roleFormPanel = new com.felix.std.module.role.RoleFormPanel();
	this.setItems( [ roleFormPanel, operationGridPanel ] );
	
	var clickSaveRoleBtn = function(){
		if( roleFormPanel.isValid() ) {
			var operationIds = operationGridPanel.getSelectedIds(true);
			if( operationIds ) {
				var values = roleFormPanel.getValues();
				values.operationIds = Ext.encode(operationIds);
				
				com.felix.nsf.Ajax.requestWithMessageBox( "roleAction", "save", function(result){
					if( result.state=='SUCCESS' ) {
						thoj.setCloseConfirm( false );
						thoj.close();
						com.felix.nsf.Context.currentComponents.roleGridPanel.reload();
					} else {
						com.felix.nsf.MessageBox.alert(result.errorDesc.desc);
					}
				}, values);
			}
		}
	}
	
	var clickCloseBtn = function(){
		thoj.close();
	};
	
	this.setButtons( [ { text :TXT.common_btnOK,   handler : clickSaveRoleBtn },
	                   { text :TXT.common_btnClose,handler : clickCloseBtn } ] );
	
	this.editRole = function( roleId ) {
		com.felix.nsf.Ajax.requestWithMessageBox("roleAction","get", function(result){
			roleFormPanel.setValues( result );
			for( var i=0; i<result.permisions.length; i++)
				operationGridPanel.selectRowById(result.permisions[i].id);
		}, {id:roleId} );
	}
};