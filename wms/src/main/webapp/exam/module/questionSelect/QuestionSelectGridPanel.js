com.felix.exam.module.questionSelect.QuestionSelectGridPanel = function(){
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
			{ header :TXT.exam_question_select_name,   dataIndex :'name', 	  menuDisabled :true,	width :250, align :'left' },
			{ header :TXT.exam_question_select_type,   dataIndex :'questionType.name', menuDisabled :true,	width :150,	align :'left' } ] );
					
	this.setGridConfig({
		title:TXT.question_title,
		region:  'center',
		margins: '0 0 4 0',
		cmargins:'0 0 4 0',
		loadMask:true
	});
	
	this.setSelectModel( 2 );		
	var questionToolbar = new com.felix.exam.module.questionSelect.QuestionSelectToolbar() ;
	this.setTopToolBar( questionToolbar );
		
	var viewQuestion = function( grid, rowIndex ){
		var recordData = thoj.getSelectedRecord(false);
		if( recordData ) {
			var qvWindow = new com.felix.exam.module.questionView.QuestionViewWindow();
			qvWindow.show( recordData.id );
		}
	};
	
	this.setGridEventConfig( {rowdblclick:viewQuestion} );
}