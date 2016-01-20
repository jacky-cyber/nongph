com.felix.std.module.userSelect.UserSelectToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	var thoj = this;
	
	var doSelect = function(){
            var rds = thoj.getParent().getSelectedRecordDatas(true);
            if( rds ) {
                thoj.getParent().getParent().getSelectUserCallBack()( rds );
            }
	};
	
	this.setToolBarConfig( {} );

	this.setToolBarItemsConfig([ 
	                             {type:'button', text:TXT.std_userSelect_add,  handler:'addAction',   iconCls:'icoAdd'}
	                           ]);
	
	this.setToolBarEvent( { addAction: doSelect } );
}