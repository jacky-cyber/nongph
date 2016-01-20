com.felix.exam.module.question.QuestionGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	
	var thoj = this;
	
	this.getDataStore().setConfig({
		url: com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+"/questionAction/search",
		root :'items',
		totalProperty :'totalCount',
		id :'id',
		fields : [ { name :'id' }, 
				   { name :'name' }, 
				   { name :'questionType.name' },
				   { name :'onUsed' }]
		});
		
	this.setColumnModelConfig( [
			{ header :TXT.question_name,   dataIndex :'name', 	  menuDisabled :true,	width :300, align :'left' },
			{ header :TXT.question_type,   dataIndex :'questionType.name', menuDisabled :true,	width :300,	align :'left' } ] );
					
	this.setGridConfig({
		title:TXT.question_title,
		id:'exam_question_grid',
		region:  'center',
		margins: '0 0 4 0',
		cmargins:'0 0 4 0',
		loadMask:true
	});
	
	this.setSelectModel( 0 );		
	var questionToolbar = new com.felix.exam.module.question.QuestionToolbar() ;
	this.setTopToolBar( questionToolbar );
	
	var afterClickRow = function( grid, rowIndex ) {
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			if( recordData.onUsed ) {
				questionToolbar.getExtToolBar().findById('exam_question_toolBar_edit').disable();
				questionToolbar.getExtToolBar().findById('exam_question_toolBar_copy').enable();
				questionToolbar.getExtToolBar().findById('exam_question_toolBar_delete').disable();
			} else {
				questionToolbar.getExtToolBar().findById('exam_question_toolBar_edit').enable();
				questionToolbar.getExtToolBar().findById('exam_question_toolBar_copy').disable();
				questionToolbar.getExtToolBar().findById('exam_question_toolBar_delete').enable();
			}
		}		
	};
	
	var viewQuestion = function( grid, rowIndex ){
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			var qvWindow = new com.felix.exam.module.questionView.QuestionViewWindow();
			qvWindow.show( recordData.id );
		}
	};
	
	this.setGridEventConfig( {rowclick: afterClickRow,
							  rowdblclick:viewQuestion} );
}