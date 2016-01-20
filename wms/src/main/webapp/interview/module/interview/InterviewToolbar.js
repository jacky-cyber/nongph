com.felix.interview.module.interview.InterviewToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var thoj = this;

	this.setToolBarConfig( { id:'interview_interview_toolBar'} );
	
	this.setToolBarItemsConfig([ {id:'interview_interview_toolBar_query', type:'button', text:TXT.common_btnQuery, handler:'queryAction', iconCls:'icoFind'},
	                             {id:'interview_interview_toolBar_edit',  type:'button', text:TXT.commom_btn_edit, handler:'editAction',  iconCls:'icoDetail'},
	                             {id:'interview_interview_toolBar_dlt',   type:'button', text:TXT.common_btnDelete,handler:'deleteAction',iconCls:'icoDel'},
	                           ]);
	
	var showQueryWin = function(){
		if( !com.felix.nsf.Context.currentComponents.InterviewSearchWindow ) {
			com.felix.nsf.Context.currentComponents.InterviewSearchWindow = new com.felix.interview.module.interview.InterviewSearchWindow();
			com.felix.nsf.Context.currentComponents.InterviewSearchWindow.setParent( thoj );
		}
		com.felix.nsf.Context.currentComponents.InterviewSearchWindow.show();
	};
	
	var showEditWin = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			var win = new com.felix.interview.module.interview.InterviewWindow();
			win.setParent( thoj );
			win.edit( recordData.id );
		}
	};
	
	var doDelete = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithoutMessageBox("interviewAction", "delete", function(result){
				if( result.state==='SUCCESS' ) {
					thoj.getParent().reload();
				} else {
					var message = TXT['interview_interview_error_'+result.errorDesc.code];
					if( !message )
						message = result.errorDesc.desc;
					com.felix.nsf.MessageBox.alert(message);
				}
			}, {id:recordData.id});
		}
	};
	

	
	this.setToolBarEvent( { queryAction : showQueryWin,
		                    editAction  : showEditWin, 
		                    deleteAction: doDelete} );
}