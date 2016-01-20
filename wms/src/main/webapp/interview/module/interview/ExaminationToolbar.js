com.felix.interview.module.interview.ExaminationToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var thoj = this;
	
	var doAdd = function(){
		var qsw = new com.felix.exam.module.examinationSelect.ExaminationSelectWindow();
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
	
	this.setToolBarItemsConfig([ {type:'button', text:TXT.interview_interview_add_exam, handler:'addAction',   iconCls:'icoAdd'},
	                             {type:'button', text:TXT.interview_interview_dlt_exam, handler:'deleteAction',iconCls:'icoDel'}
	                           ]);
	
	this.setToolBarEvent( { addAction   : doAdd,
		                    deleteAction: doDelete} );
}