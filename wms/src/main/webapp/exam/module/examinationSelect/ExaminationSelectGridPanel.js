com.felix.exam.module.examinationSelect.ExaminationSelectGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	
	var thoj = this;
	
	this.getDataStore().setConfig({
		url: com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+"/examinationAction/search",
		root :'items',
		totalProperty :'totalCount',
		id :'id',
		fields : [ 'id', 
				   'name',
				   'time',
				   'position.name',
				   'onUsed' ]
		});
		
	this.setColumnModelConfig( [
			{ header :TXT.exam_examination_name,    dataIndex :'name', 	       menuDisabled :true,	width :200, align :'left' },
			{ header :TXT.exam_examination_time,    dataIndex :'time',         menuDisabled :true,	width :70,	align :'left' },
			{ header :TXT.exam_examination_position,dataIndex :'position.name',menuDisabled :true,	width :200,	align :'left' } ] );
					
	this.setGridConfig({
		//title:TXT.question_title,
		region:  'center',
		margins: '0 0 4 0',
		cmargins:'0 0 4 0',
		loadMask:true
	});
	
	this.setSelectModel( 2 );		
	var topToolbar = new com.felix.exam.module.examinationSelect.ExaminationSelectToolbar() ;
	this.setTopToolBar( topToolbar );
		
	var viewExamination = function( grid, rowIndex ){
		var recordData = thoj.getSelectedRecord(false);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithMessageBox("examinationAction", "get4View", function(exam){
				var win = new com.felix.exam.module.examinationView.ExaminationViewWindow(exam);
				win.show();
			}, {id: recordData.id} );
		}
	};
	
	this.setGridEventConfig( {rowdblclick:viewExamination} );
}