com.felix.exam.module.examination.ExaminationGridPanel = function(){
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
			{ header :TXT.exam_examination_name,    dataIndex :'name', 	       menuDisabled :true,	width :300, align :'left' },
			{ header :TXT.exam_examination_time,    dataIndex :'time',         menuDisabled :true,	width :300,	align :'left' },
			{ header :TXT.exam_examination_position,dataIndex :'position.name',menuDisabled :true,	width :300,	align :'left' }] );
					
	this.setGridConfig({
		title:TXT.examination_title,
		id:'exam_examination_grid',
		region:  'center',
		margins: '0 0 4 0',
		cmargins:'0 0 4 0',
		loadMask:true
	});
	
	this.setSelectModel( 0 );		
	
	var toolbar = new com.felix.exam.module.examination.ExaminationToolbar();
	
	this.setTopToolBar( toolbar );
	
	var afterClickRow = function( grid, rowIndex ) {
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			if( recordData.onUsed ) {
				toolbar.getExtToolBar().findById('exam_examination_toolBar_edit').disable();
				toolbar.getExtToolBar().findById('exam_examination_toolBar_copy').enable();
				toolbar.getExtToolBar().findById('exam_examination_toolBar_delete').disable();
			} else {
				toolbar.getExtToolBar().findById('exam_examination_toolBar_edit').enable();
				toolbar.getExtToolBar().findById('exam_examination_toolBar_copy').disable();
				toolbar.getExtToolBar().findById('exam_examination_toolBar_delete').enable();
			}
		}		
	};
	
	var viewExamination = function( grid, rowIndex ){
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithMessageBox("examinationAction", "get4View", function(exam){
				var win = new com.felix.exam.module.examinationView.ExaminationViewWindow(exam);
				win.show();
			}, {id: recordData.id} );
		}
	};
	
	this.setGridEventConfig( {rowclick: afterClickRow,
							  rowdblclick:viewExamination} );
}