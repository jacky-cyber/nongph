com.felix.exam.module.questionType.QuestionTypeGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	
	this.getDataStore().setConfig({
		url: com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+"/questionTypeAction/search",
		id :'id',
		fields : [ { name :'id' }, 
				   { name :'name' }, 
				   { name :'rendener' }]
		});
		
	this.setColumnModelConfig( [
			{ header :TXT.questionType_name,       dataIndex :'name', 		menuDisabled :true,	width :200, align :'left' },
			{ header :TXT.questionType_rendener,   dataIndex :'rendener', menuDisabled :true,	width :200,	align :'left' } ] );
					
	this.setGridConfig({
		title:TXT.questionType_title,
		id:'exam_questionType_grid',
		region:  'center',
		margins: '0 0 4 0',
		cmargins:'0 0 4 0',
		loadMask:true
	});
	
	this.setSelectModel( 0 );				
	
	this.setPagination( false );
}