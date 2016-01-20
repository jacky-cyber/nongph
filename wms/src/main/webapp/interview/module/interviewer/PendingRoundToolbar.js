com.felix.interview.module.interviewer.PendingRoundToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var thoj = this;

	this.setToolBarConfig( { id:'interview_interviewer_toolBar'} );
	
	this.setToolBarItemsConfig([ {id:"interview_interviewer_toolBar_start",  type:'button', text:TXT.interview_interviewer_start, handler:'startAction', iconCls:'icoDetail'},
	                             {id:"interview_interviewer_toolBar_submit", type:'button', text:TXT.interview_interviewer_submit, handler:'submitAction', iconCls:'icoDetail'}
	                           ]);
	
	var markStart = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			 var reqParam = {id:recordData.id};
		    com.felix.nsf.Ajax.requestWithMessageBox("interviewerAction", 'markStart',function(result){
		              if( result.state==='SUCCESS' ) {
		                  this.getParent().reload();
		              } else {
		                  var message = TXT['interview_interview_error_'+result.errorDesc.code];
		                  if( !message )
		                	  message = result.errorDesc.desc;
		                  com.felix.nsf.MessageBox.alert( message );
		                  }
		          }, reqParam, thoj);
		}
	};
	
	var submitAction = function(){
		var recordData = thoj.getParent().getSelectedRecordData(false);
		if( recordData ) {
			var formWin = new com.felix.interview.module.interviewer.SubmitFormWindow( recordData );
			formWin.setParent(thoj);
			formWin.show();
		}
	};
	
	this.setToolBarEvent( { startAction : markStart,
									submitAction: submitAction} );
}