com.felix.exam.module.question.QuestionChoiceGridPanel = function(){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.GridPanel);
	
	this.getDataStore().setConfig({
		autoDestroy: true,
		idIndex:0,
		remote:false,
		fields: ['id', 'labelName', 'content']
	});
	
	this.setColumnModelConfig( [{ header :TXT.exam_question_choice_label,   dataIndex :'labelName',menuDisabled :true,	width :80,  align :'left',
								  editor : new Ext.form.TextField( {allowBlank:false} ) },
	                			{ header :TXT.exam_question_choice_content, dataIndex :'content',  menuDisabled :true,	width :500,	align :'left',
	                			  editor : new Ext.form.TextArea( {allowBlank:false} )  } ] );
	
	this.setGridConfig({
		title:TXT.exam_question_choice,
		id:'exam_question_choice_grid',
		region:  'center',
		margins: '0 0 4 0',
		cmargins:'0 0 4 0',
		height:200,
		loadMask:true
	});
	
	this.setEditable( true );
	this.setPagination( false );
	this.setTopToolBar( new com.felix.exam.module.question.QuestionChoiceToolbar() );
	this.setSelectModel( 2 );
	
	this.addChoiceItem = function( label, content ){
		var store = this.getExtGridPanel().getStore();
		if( !label ) {
			label = '';
			if( store.getCount()>0 ) {
				var lastData = store.getAt( store.getCount()-1 ).data;
				if( lastData.labelName!='' ) {
					label = String.fromCharCode( lastData.labelName.charCodeAt() + 1 );
				}
			}
		}
		if( !content )
			content = '';
		var Choice = store.recordType;
		var c = new Choice( {id:'', labelName:label, content:content} );
		store.insert( store.getCount(), c );
	}
}