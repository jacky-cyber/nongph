com.felix.std.module.user.UserGridPanel = function(){
    com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	
	var thoj = this;
	var selectFirst = function(){
		thoj.selectFirstRow();
	}
	
	var showEditUserWin = function(row, e) {
		var record = thoj.getSelectedRecordData();
		var win = new com.felix.std.module.user.UserWindow();
		win.show();
		win.editUser(record.id);
	}

	this.getDataStore().setConfig( { url : com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+"/userAction/search",
							root :'items',
							totalProperty :'totalCount',
							idProperty:'id',
							fields:[ {name :'id'}, 
									 {name :'name'},
									 {name :'code'},
									 {name :'loginname'}
									 ]
							} );
	
	this.setColumnModelConfig([ {header :TXT.user_name,dataIndex :'name',menuDisabled :true,width :120}, 
								{header :TXT.user_code,dataIndex :'code',width :150,menuDisabled :true},
								{header :TXT.user_loginName,dataIndex :'loginname',width :150,menuDisabled :true}
								]);
	
	this.setGridConfig({
							title:TXT.user_title,
							id:'user_title',
							region :'center',
							stripeRows :true
						});
	this.setSelectModel(0);
	
	this.setTopToolBar( new com.felix.std.module.user.UserToolBar() );
	
	this.setGridEventConfig( {rowdblclick: showEditUserWin } );
	
	this.getDataStore().setEventConfig({load:selectFirst});
}