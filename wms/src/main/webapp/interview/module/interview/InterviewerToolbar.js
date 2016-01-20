com.felix.interview.module.interview.InterviewerToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var thoj = this;
	
	var doAdd = function(){
		var qsw = new com.felix.std.module.userSelect.UserSelectWindow();
                qsw.setSelectUserCallBack( function( users ) {
                                                for( var i=0; i<users.length; i++) {
                                                    var user = users[i];
                                                    thoj.getParent().addInterviewer(  user.id, user.name );
                                                }
                                           } );
		qsw.setParent( thoj );
		qsw.show();
	};
	
	var doDelete = function(){
		var records = thoj.getParent().getSelections(true);
		if( records ) {
			var store = thoj.getParent().getExtGridPanel().getStore();
			for( var i=0; i<records.length; i++ )
				store.remove( records[i] );
		}
		thoj.getParent().getExtGridPanel().getView().refresh();
	};
	
	this.setToolBarConfig( {} );
	
	this.setToolBarItemsConfig([ {type:'button', text:TXT.interview_interview_add_interviewer, handler:'addAction',   iconCls:'icoAdd'},
	                             {type:'button', text:TXT.interview_interview_dlt_interviewer, handler:'deleteAction',iconCls:'icoDel'}
	                           ]);
	
	this.setToolBarEvent( { addAction   : doAdd,
		                    deleteAction: doDelete} );
}