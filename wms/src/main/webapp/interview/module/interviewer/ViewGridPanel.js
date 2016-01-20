com.felix.interview.module.interviewer.ViewGridPanel = function(ir){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.GridPanel);
	var thoj = this;

	this.getDataStore().setConfig({
		fields: ['id', 'user.name', 'state', 'startTime', 'finishTime'],
		data: ir.interviewRoundUsers
	});
	
	this.setColumnModelConfig( [ { header :TXT.interview_interviewer_interviewer, dataIndex :'user.name',          menuDisabled :true,	width :150,  align :'left'},
                                { header :TXT.interview_interviewer_status,      dataIndex :'state',     menuDisabled :true,	width :200,   align :'left'},
                                { header :TXT.interview_interviewer_start_time,  dataIndex :'startTime', menuDisabled :true,	width :200,   align :'left'},	 
                                { header :TXT.interview_interviewer_finish_time, dataIndex :'finishTime',menuDisabled :true,	width :200,   align :'left'}
                                        ] );
	
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

	var viewInterviewForm = function( grid, rowIndex ){
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			var recordData = thoj.getSelectedRecordData(false);
			if( recordData ) {
				com.felix.nsf.Ajax.requestWithMessageBox("formTemplateAction", "get", function(ft){
					var form = com.felix.form.module.render.FormRenderer.render(ft); 
					var win = new Ext.Window( { title :ft.name,
														 width :500,
														 height :450,
														 autoScroll :false,
														 layout :'border',
														 border :false,
														 minimizable :false,
														 maximizable :false,
														 resizable :true,
														 shadow:false,
														 resizable :false,
														 modal :true,
														 layoutConfig : {
															animate :false
														 },
														 items:[form],
														buttonAlign :'right'
													} );
					
					if( recordData.state=='PASS' || recordData.state=='REJECT' ) {
						com.felix.nsf.Ajax.requestWithMessageBox("interviewerAction", "getFormValues", function(fvs){
							form.getForm().setValues( fvs );
							win.show( thoj );
					   }, {id: recordData.id} );
					} else {
						win.show( thoj, function(){
							 form.getForm().clearInvalid();
						} );
					}
				}, {id: recordData.formTemplateId} );
			}
		}
	};
	
	this.setGridEventConfig( { rowdblclick: viewInterviewForm } );
}