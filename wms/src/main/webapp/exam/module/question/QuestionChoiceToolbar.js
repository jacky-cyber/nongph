com.felix.exam.module.question.QuestionChoiceToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var thoj = this;
	
	var doAdd = function(){
		var store = thoj.getParent().addChoiceItem();
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
	
	this.setToolBarConfig( { id:'exam_question_choice_toolBar'} );

	this.setToolBarItemsConfig([ {id:'exam_question_choice_toolBar_add',   type:'button', text:TXT.commom_btn_add,  handler:'addAction',   iconCls:'icoAdd'},
	                             {id:'exam_question_choice_toolBar_delete',type:'button', text:TXT.common_btnDelete,handler:'deleteAction',iconCls:'icoDel'}
	                           ]);
	
	this.setToolBarEvent( { addAction   : doAdd,
		                    deleteAction: doDelete} );
}