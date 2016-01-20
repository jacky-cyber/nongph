com.felix.std.module.role.RoleGridPanel4Select = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	
	this.getDataStore().setConfig({
		url: com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+"/roleAction/search",
		root :'items',
		totalProperty :'totalCount',
		id :'id',
		fields : [ {name: 'id'},
		           {name: 'name'},
		           {name: 'description'} ]
		});
	
	this.setColumnModelConfig( [ { header :TXT.role_name, dataIndex :'name', 	   menuDisabled :true,	width :200, align :'left' },
	                             { header :TXT.role_desc, dataIndex :'description',menuDisabled :true,	width :200,	align :'left' } ] );
					
	this.setGridConfig( { title:TXT.user_role,
						  id:'role_grid',
						  region:'center',
						  margins: '0 0 4 0',
						  cmargins:'0 0 4 0',
						  loadMask:true
						} );
		
	this.setSelectModel( 2 );		
}