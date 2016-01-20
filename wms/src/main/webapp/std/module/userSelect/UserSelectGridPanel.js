com.felix.std.module.userSelect.UserSelectGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	
	var thoj = this;
	
        
        
        var topToolbar = new com.felix.std.module.userSelect.UserSelectToolbar();
        
	this.getDataStore().setConfig( { url : com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+"/userAction/search",
                                         root :'items',
                                         totalProperty :'totalCount',
                                         fields:[ {name :'id'}, 
                                                  {name :'name'},
                                                  {name :'code'},
                                                  {name :'loginname'} ]
                                        } );
	
	this.setColumnModelConfig( [ {header :TXT.user_name,               dataIndex :'name',     width :120,menuDisabled :true}, 
				     {header :TXT.std_userSelect_code,     dataIndex :'code',     width :150,menuDisabled :true},
			             {header :TXT.std_userSelect_loginName,dataIndex :'loginname',width :150,menuDisabled :true}
				   ] );
					
	this.setGridConfig( { region:   'center',
                              margins:  '0 0 4 0',
                              cmargins: '0 0 4 0',
                              loadMask: true } );
	
	this.setSelectModel( 2 );	
	
	this.setTopToolBar( topToolbar );
};