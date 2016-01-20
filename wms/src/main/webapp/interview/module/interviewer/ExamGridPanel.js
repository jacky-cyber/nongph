com.felix.interview.module.interviewer.ExamGridPanel = function(ir){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.GridPanel);
	var thoj = this;
	
	this.getDataStore().setConfig({
         fields: ['id', 'exam.name', 'exam.position.name', 'startTime', 'endTime', 'examScore', 'status'],
         data: ir.interviewRoundExaminations
	});
	
	this.setColumnModelConfig( [{ header :TXT.interview_interviewer_examinationName, dataIndex :'exam.name',         menuDisabled :true,	width :150,  align :'left'},
	                            { header :TXT.interview_interviewer_position,        dataIndex :'exam.position.name',menuDisabled :true,	width :150,  align :'left'},
							          { header :TXT.interview_interviewer_start_time,      dataIndex :'startTime',         menuDisabled :true,	width :150,  align :'left'},
							          { header :TXT.interview_interviewer_end_time,        dataIndex :'endTime',           menuDisabled :true,	width :150,  align :'left'},
							          { header :TXT.interview_interviewer_score,        dataIndex :'examScore',         menuDisabled :true,	width :150,  align :'left'}] );
	
	this.setGridConfig({
		title  :ir.name,
		border :true,
		margins: '0 0 0 0',
		cmargins:'0 0 4 0',
		height:150,
		loadMask:true
	});
	
	this.setPagination( false );
	this.setSelectModel( 0 );

	var viewExamination = function( grid, rowIndex ){
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithMessageBox("interviewAction", "getExaminationAnswer", function(ire){
				var win = new com.felix.exam.module.examinationView.ExaminationViewWindow(ire.exam, ire.examQuestionAnswers);
				win.show();
			}, {id: recordData.id} );
		}
	};

	this.setGridEventConfig( { rowdblclick: viewExamination } );
}