com.felix.interview.module.interviewer.PendingRoundGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	var thoj = this;
	this.getDataStore().setConfig({
		url: com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+"/interviewerAction/getPendingInterviewRounds",
		fields : ['id', 
		          'interviewRound.interview.id',
				    'interviewRound.planStartTime',
				    'interviewRound.interview.candidate.name',
				    'interviewRound.interview.applyPosition.name',
				    'interviewRound.name',
				    'state',
				    'form.formTemplate.id']
		});
		
	this.setColumnModelConfig( [
			{ header :TXT.interview_interviewer_planStartTime,dataIndex :'interviewRound.planStartTime',               menuDisabled :true,	width :200, align :'left' },
			{ header :TXT.interview_interviewer_candidate,    dataIndex :'interviewRound.interview.candidate.name',    menuDisabled :true,	width :200,	align :'left' },
			{ header :TXT.interview_interviewer_position,     dataIndex :'interviewRound.interview.applyPosition.name',menuDisabled :true,	width :200,	align :'left' },
			{ header :TXT.interview_interviewer_round,        dataIndex :'interviewRound.name',     				        menuDisabled :true,	width :200,	align :'left' },
			{ header :TXT.interview_interviewer_state,        dataIndex :'state',            						        menuDisabled :true,	width :200,	align :'left',
			  renderer: function(value){
				  if( value=="GOING" )
					  return "已开始";
				  else
					  return "等待开始";
			  } } ] );
					
	this.setGridConfig({
		title:TXT.interview_interviewer,
		region:  'center',
		margins: '0 0 4 0',
		cmargins:'0 0 4 0',
		loadMask:true
	});
	
	this.setSelectModel( 0 );		
	
	var toolbar = new com.felix.interview.module.interviewer.PendingRoundToolbar();
	
	this.setTopToolBar( toolbar );
		
	this.setPagination( false );
	
	var viewInterview = function( grid, rowIndex ){
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithMessageBox("interviewAction", "get", function(interview){
				var win = new com.felix.interview.module.interviewer.InterviewInfoWindow( interview );
				win.show();
			}, {id: recordData['interviewRound.interview.id']} );
		}
	};
	
	var afterClickRow = function( grid, rowIndex ) {
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			if( recordData.state == "GOING" ) {
				toolbar.getExtToolBar().findById('interview_interviewer_toolBar_start').disable();
				toolbar.getExtToolBar().findById('interview_interviewer_toolBar_submit').enable();
			} else {
				toolbar.getExtToolBar().findById('interview_interviewer_toolBar_start').enable();
				toolbar.getExtToolBar().findById('interview_interviewer_toolBar_submit').disable();
			}
		}		
	};
	this.setGridEventConfig( { rowclick: afterClickRow,
		                        rowdblclick: viewInterview} );
}

