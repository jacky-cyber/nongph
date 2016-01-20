com.felix.exam.module.question.QuestionToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	var thoj = this;
	
	var showQueryWin = function(){
		if( !com.felix.nsf.Context.currentComponents.QuestionSearchWindow ) {
			com.felix.nsf.Context.currentComponents.QuestionSearchWindow = new com.felix.exam.module.question.QuestionSearchWindow();
			com.felix.nsf.Context.currentComponents.QuestionSearchWindow.setParent( thoj );
		}
		
		com.felix.nsf.Context.currentComponents.QuestionSearchWindow.show();
	};
	
	var showAddWin = function(){
		var questionWindow = new com.felix.exam.module.question.QuestionWindow();
		questionWindow.setParent( thoj );
		questionWindow.show();
	};
	
	var showEditWin = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			var questionWindow = new com.felix.exam.module.question.QuestionWindow();
			questionWindow.setParent( thoj );
			questionWindow.edit( recordData.id );
		}
	};
	
	var showCopyWin = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			var questionWindow = new com.felix.exam.module.question.QuestionWindow();
			questionWindow.setParent( thoj );
			questionWindow.copy( recordData.id );
		}
	};
	
	var doDelete = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithoutMessageBox("questionAction", "delete", function(result){
				if( result.state=='SUCCESS' ) {
					thoj.getParent().reload();
				} else {
					com.felix.nsf.MessageBox.alert(result.errorDesc.desc);
				}
			}, {id:recordData.id});
		}
	};
	
	this.setToolBarConfig( { id:'exam_question_toolBar'} );

	this.setToolBarItemsConfig([ {id:'exam_question_toolBar_query', type:'button', text:TXT.common_btnQuery, handler:'queryAction', iconCls:'icoFind'},
	                             {id:'exam_question_toolBar_add',   type:'button', text:TXT.commom_btn_add,  handler:'addAction',   iconCls:'icoAdd'},
	                             {id:'exam_question_toolBar_edit',  type:'button', text:TXT.commom_btn_edit, handler:'editAction',  iconCls:'icoDetail'},
	                             {id:'exam_question_toolBar_copy',  type:'button', text:TXT.commom_btn_copy, handler:'copyAction',  iconCls:'icoCopy'},
	                             {id:'exam_question_toolBar_delete',type:'button', text:TXT.common_btnDelete,handler:'deleteAction',iconCls:'icoDel'}
	                           ]);
	
	this.setToolBarEvent( { queryAction : showQueryWin,
		                    addAction   : showAddWin,
		                    editAction  : showEditWin,
		                    copyAction  : showCopyWin,
		                    deleteAction: doDelete} );
}