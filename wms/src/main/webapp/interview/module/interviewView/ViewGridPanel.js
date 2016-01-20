com.felix.interview.module.interviewView.ViewGridPanel = function( ir ){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.GridPanel);
	var thoj = this;
	var toolbar = new com.felix.interview.module.interviewView.ViewToolbar();
  
	this.getDataStore().setConfig({
		fields: ['id', 'name', 'formTemplate', 'state', 'startTime', 'finishTime', 'formTemplateId']
	});
	
	this.setColumnModelConfig( [{ header :TXT.interview_interviewView_interviewer,dataIndex :'name',        menuDisabled :true,	width :150,  align :'left'},
	                	          { header :TXT.interview_interviewView_template,   dataIndex :'formTemplate',menuDisabled :true,	width :200,  align :'left'},
                               { header :TXT.interview_interviewView_status,     dataIndex :'state',       menuDisabled :true,	width :200,  align :'left'},
                               { header :TXT.interview_interviewView_start_time, dataIndex :'startTime',   menuDisabled :true,	width :200,  align :'left'},	 
                               { header :TXT.interview_interviewView_finish_time,dataIndex :'finishTime',  menuDisabled :true,	width :200,  align :'left'}
                                        ] );
	
	this.setGridConfig({
		border :true,
		margins: '0 0 0 0',
		cmargins:'0 0 4 0',
		height:150,
		loadMask:true
	});
	this.setPagination( false );
	this.setTopToolBar( toolbar );
	this.setSelectModel( 2 );
        
	this.addInterviewer = function( id, name, template, state, startTime, finishTime, templateId ){
		var store = this.getExtGridPanel().getStore();
		var Choice = store.recordType;
		var c = new Choice( {id:id, name:name, formTemplate: template, state:state, startTime:startTime, finishTime:finishTime, formTemplateId:templateId} );
		store.insert( store.getCount(), c );
	};
        
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
	
	var afterClickRow = function( grid, rowIndex ) {
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			if( ir.state=='GOING' && recordData.state=='' ) {
				toolbar.getExtToolBar().items.items[0].enable();
			} else {
				toolbar.getExtToolBar().items.items[0].disable();
			}
		}		
	};
	
	this.setGridEventConfig( { rowclick: afterClickRow,
		   					   rowdblclick: viewInterviewForm } );
}