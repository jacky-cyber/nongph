com.felix.exam.module.examination.CatalogQuestionToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var thoj = this;
	
	var doAdd = function(){
		var qsw = new com.felix.exam.module.questionSelect.QuestionSelectWindow();
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
	
	this.setToolBarItemsConfig([ {type:'button', text:TXT.exam_examination_catalog_add_question,   handler:'addAction',   iconCls:'icoAdd'},
	                             {type:'button', text:TXT.exam_examination_catalog_delete_question,handler:'deleteAction',iconCls:'icoDel'}
	                           ]);
	
	this.setToolBarEvent( { addAction   : doAdd,
		                    deleteAction: doDelete} );
}