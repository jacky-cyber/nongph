com.felix.interview.module.interviewView.ExamGridPanel = function(ir){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.GridPanel);
	var thoj = this;
	var toolbar = null;
 
	this.getDataStore().setConfig({
            fields: ['id', 'examinationName', 'positionName', ' confuse', 'status', 'startTime', 'endTime']
	});
	
	this.setColumnModelConfig( [{ header :TXT.interview_interviewView_examinationName, dataIndex :'examinationName',menuDisabled :true,	width :150,  align :'left'},
	                            { header :TXT.interview_interviewView_position,        dataIndex :'positionName',   menuDisabled :true,	width :150,  align :'left'},
	                			{ header :TXT.interview_interviewView_confuse,         dataIndex :'confuse',        menuDisabled :true,	width :70,	 align :'left',
									   renderer:function(value){
										  return value==0?'NO':'YES';
									   }
								},
								{ header :TXT.interview_interviewView_status,          dataIndex :'status',        menuDisabled :true,	width :70,	 align :'left',
										   renderer : function( value ) {
											  var status = { PENDING:'等待考试', 
													         GOING:'正在考试', 
													         OVERTIME:'考试超时', 
													         SUBMITTED:'考试完成' };
											  return status[value];
										   }
								},
							    { header :TXT.interview_interviewView_start_time,  dataIndex :'startTime',   menuDisabled :true,	width :150,  align :'left'},
							    { header :TXT.interview_interviewView_end_time,    dataIndex :'endTime',     menuDisabled :true,	width :150,  align :'left'}] );
	
	this.setGridConfig({
		border :true,
		margins: '0 0 0 0',
		cmargins:'0 0 4 0',
		height:150,
		loadMask:true
	});
	
	this.setPagination( false );
	if( ir.state=="GOING" ) {
		toolbar = new com.felix.interview.module.interviewView.ExamToolbar();
		this.setTopToolBar( toolbar );
	}		
	this.setSelectModel( 0 );
	
	this.addExamination = function( id, name, positioName, confuse, status, startTime, endTime ){
		var store = this.getExtGridPanel().getStore();
		var Choice = store.recordType;
		var c = new Choice( {id:id, examinationName:name, positionName:positioName, confuse: confuse?confuse:0, status: status, startTime: startTime, endTime: endTime} );
		store.insert( store.getCount(), c );
	}
	
	var afterClickRow = function( grid, rowIndex ) {
		if( toolbar!=null ) {
			var recordData = thoj.getSelectedRecordData(false);
			if( recordData ) {
				if( recordData.status=='GOING' ) {
					toolbar.getExtToolBar().findById('interview_interviewView_exam_tbar_mp').enable();
				} else {
					toolbar.getExtToolBar().findById('interview_interviewView_exam_tbar_mp').disable();
				}
					
				if( recordData.status=='OVERTIME' ) {
					toolbar.getExtToolBar().findById('interview_interviewView_exam_tbar_mg').enable();
				} else {
					toolbar.getExtToolBar().findById('interview_interviewView_exam_tbar_mg').disable();
				}
			}		
		}
	};
	
	var viewExamination = function( grid, rowIndex ){
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithMessageBox("interviewAction", "getExaminationAnswer", function(ire){
				var win = new com.felix.exam.module.examinationView.ExaminationViewWindow(ire.exam, ire.examQuestionAnswers);
				win.show();
			}, {id: recordData.id} );
		}
	};

	this.setGridEventConfig( { rowclick: afterClickRow,
							         rowdblclick: viewExamination} );
}