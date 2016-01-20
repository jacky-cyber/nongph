com.felix.interview.module.candidate.CandidateToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var thoj = this;
	
	var currentRowId;
	
	this.setToolBarConfig( { id:'interview_candidate_toolBar'} );
	
	this.setToolBarItemsConfig([ {id:'interview_candidate_toolBar_query', type:'button', text:TXT.common_btnQuery, handler:'queryAction', iconCls:'icoFind'},
	                             {id:'interview_candidate_toolBar_add',   type:'button', text:TXT.commom_btn_add,  handler:'addAction',   iconCls:'icoAdd'},
	                             {id:'interview_candidate_toolBar_edit',  type:'button', text:TXT.commom_btn_edit, handler:'editAction',  iconCls:'icoDetail'},
	                             {id:'interview_candidate_toolBar_delete',type:'button', text:TXT.common_btnDelete,handler:'deleteAction',iconCls:'icoDel'},
	                             {id:'interview_candidate_toolBar_add_iv',type:'button', text:TXT.interview_candidate_add_interview,handler:'addIvAction',iconCls:'icoUser'}
	                           ]);
	
	this.onSelectPosition = function( positionId ) {
		var cName = thoj.getParent().getExtGridPanel().getStore().getById(currentRowId).get("name");
		com.felix.nsf.Ajax.requestWithMessageBox( "positionAction", "get", function(p){
			var win = new com.felix.interview.module.interview.InterviewWindow();
			win.setParent( thoj );
			win.show();
			
			win.getItem(0).setValue("candidateId", currentRowId);
			win.getItem(0).setValue("positionId", positionId);
			win.getItem(0).setValue("candidateName", cName);
			win.getItem(0).setValue("positionName", p.name);
		}, {id: positionId}); 
		
	}
	
	var showQueryWin = function(){
		if( !com.felix.nsf.Context.currentComponents.CandidateSearchWindow ) {
			com.felix.nsf.Context.currentComponents.CandidateSearchWindow = new com.felix.interview.module.candidate.CandidateSearchWindow();
			com.felix.nsf.Context.currentComponents.CandidateSearchWindow.setParent( thoj );
		}
		com.felix.nsf.Context.currentComponents.CandidateSearchWindow.show();
	};
	
	var showAddWin = function(){
		var win = new com.felix.interview.module.candidate.CandidateWindow();
		win.setParent( thoj );
		win.show();
	};
	
	var showEditWin = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithMessageBox("candidateAction", "get", function(candidate){
				var win = new com.felix.interview.module.candidate.CandidateWindow(candidate);
				win.setParent( thoj );
				win.show();
			}, {id: recordData.id} );
		}
	}
	
	var doDelete = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithoutMessageBox("candidateAction", "delete", function(result){
				if( result.state=='SUCCESS' ) {
					thoj.getParent().reload();
				} else {
					var message = TXT['interview_candidate_error_'+result.errorDesc.code];
					if( !message )
						message = result.errorDesc.desc;
					com.felix.nsf.MessageBox.alert(message);
				}
			}, {id:recordData.id});
		}
	};
	
	var addInterview = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			currentRowId = recordData.id;
			var win = new com.felix.exam.module.positionSelect.SelectPositionWindow();
			win.setParent( thoj );
			win.show();
		}
	};
	
	this.setToolBarEvent( { queryAction : showQueryWin,
		                    addAction   : showAddWin,
		                    editAction  : showEditWin, 
		                    deleteAction: doDelete,
		                    addIvAction : addInterview} );
}