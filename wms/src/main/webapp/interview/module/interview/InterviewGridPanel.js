com.felix.interview.module.interview.InterviewGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	var thoj = this;
	this.getDataStore().setConfig({
		url: com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+"/interviewAction/search",
		root :'items',
		totalProperty :'totalCount',
		id :'id',
		fields : [ 'id', 
				   'candidate.name',
				   'applyPosition.name',
				   'planStartTime',
				   'status' ]
		});
		
	this.setColumnModelConfig( [
			{ header :TXT.interview_interview_interviewer,dataIndex :'candidate.name',  menuDisabled :true,	width :200, align :'left' },
			{ header :TXT.interview_interview_position,   dataIndex :'applyPosition.name',menuDisabled :true,	width :200,	align :'left' },
			{ header :TXT.interview_interview_date,       dataIndex :'planStartTime',             menuDisabled :true,	width :200,	align :'left' },
			{ header :TXT.interview_interview_state,      dataIndex :'status',            menuDisabled :true,	width :200,	align :'left',
			  renderer: function(value){
				  var status = {PENDING: '等待面试', 
					  			GOING: '正在面试',
					  			PASS:  '面试通过',
					  			REJECT:'面试失败',
					  			ABSENT:'缺席面试'}
				  return status[value];
			  } } ] );
					
	this.setGridConfig({
		title:TXT.interview_interview,
		id:'interview_interview_grid',
		region:  'center',
		margins: '0 0 4 0',
		cmargins:'0 0 4 0',
		loadMask:true
	});
	
	this.setSelectModel( 0 );		
	
	var toolbar = new com.felix.interview.module.interview.InterviewToolbar();
	
	this.setTopToolBar( toolbar );
		
	var viewInterview = function( grid, rowIndex ){
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			if( recordData.status!='PENDING' ) {
				com.felix.nsf.Ajax.requestWithMessageBox("interviewAction", "get", function(interview){
					var win = new com.felix.interview.module.interviewView.InterviewWindow( interview );
					win.setParent( thoj );
					win.show();
				}, {id: recordData.id} );
			}
		}
	};
	
	var afterClickRow = function(){
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			if( recordData.status=='PENDING' ) {
				toolbar.getExtToolBar().findById('interview_interview_toolBar_edit').enable();
				toolbar.getExtToolBar().findById('interview_interview_toolBar_dlt').enable();
			} else {
				toolbar.getExtToolBar().findById('interview_interview_toolBar_edit').disable();
				toolbar.getExtToolBar().findById('interview_interview_toolBar_dlt').disable();
			}
		}		
	};
	
	this.setGridEventConfig( {rowclick: afterClickRow,
							  rowdblclick:viewInterview} );
}