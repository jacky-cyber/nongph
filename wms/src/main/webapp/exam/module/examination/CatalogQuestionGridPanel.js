com.felix.exam.module.examination.CatalogQuestionGridPanel = function(){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.GridPanel);
	var thoj = this;
	
	this.getDataStore().setConfig({
		autoDestroy: true,
		idIndex:id,
		remote:false,
		fields: ['id', 'questionName', 'questionType', 'score']
	});
	
	this.setColumnModelConfig( [{ header :TXT.exam_examination_catalog_question_name, dataIndex :'questionName',menuDisabled :true,	width :300,  align :'left'},
	                            { header :TXT.exam_examination_catalog_question_type, dataIndex :'questionType',menuDisabled :true,	width :150,  align :'left'},
	                			{ header :TXT.exam_examination_catalog_question_score,dataIndex :'score',       menuDisabled :true,	width :100,	align :'left',
	                			  editor : new Ext.form.NumberField( {allowBlank:false, allowNegative:false, maxValue:100} )  } ] );
	
	this.setGridConfig({
		title:TXT.exam_examination_catalog_question,
		region:  'center',
		border :true,
		margins: '0 0 4 0',
		cmargins:'0 0 4 0',
		height:300,
		loadMask:true
	});
	
	this.setEditable( true );
	this.setPagination( false );
	this.setTopToolBar( new com.felix.exam.module.examination.CatalogQuestionToolbar() );
	this.setSelectModel( 2 );
	
	this.addQuestion = function( id, name, typeName, score ){
		if( !this.getParent().getParent().isQuestionExist(id) ) {
			if( !score )
				score = '';			
			var store = this.getExtGridPanel().getStore();
			var Choice = store.recordType;
			var c = new Choice( {id:id, questionName:name, questionType:typeName, score:score} );
			store.insert( store.getCount(), c );
		}
	}
	
	this.isValid = function(){
		var store = this.getExtGridPanel().getStore();
		for(var i=0; i<store.getCount(); i++){
			var score = store.getAt( i ).data.score;
			if( score==undefined || score=='') {
				return false;
			}
		}
		return true;
	}
	
	var viewQuestion = function( grid, rowIndex ){
		var recordData = thoj.getSelectedRecord(false);
		if( recordData ) {
			var qvWindow = new com.felix.exam.module.questionView.QuestionViewWindow();
			qvWindow.show( recordData.id );
		}
	};
	
	this.setGridEventConfig( {rowdblclick:viewQuestion} );
}