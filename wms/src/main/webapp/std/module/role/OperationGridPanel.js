com.felix.std.module.role.OperationGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	
	this.getDataStore().setConfig({
			url: com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+"/operationAction/getAllOperations",
			id :'id',
			fields : [ {name: 'id'},
				       {name: 'description'} ]
		});
	
	this.setColumnModelConfig( [ { header: TXT.role_operation_code, dataIndex: 'description', menuDisabled: true, width:300 } ] );
					
	this.setGridConfig( { title:TXT.role_operation_title,
						  id:'operate_grid',
						  region:'center',
						  margins: '0 0 4 0',
						  cmargins:'0 0 4 0',
						  loadMask:true
						} );
	this.setSelectModel( 2 );		
	this.setPagination( false );
}