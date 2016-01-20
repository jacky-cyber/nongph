com.felix.exam.module.position.PositionGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	
	this.getDataStore().setConfig({
		url: com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+"/positionAction/search",
		root :'items',
		totalProperty :'totalCount',
		id :'id',
		fields : [ { name :'id' }, 
				   { name :'name' }, 
				   { name :'nextPosition.name' }]
		});
		
	this.setColumnModelConfig( [
			{ header :TXT.position_name,       dataIndex :'name', 			 menuDisabled :true,	width :200, align :'left'	},
			{ header :TXT.position_next_name,	 dataIndex :'nextPosition.name', menuDisabled :true,	width :200,	align :'left'	}] );
					
	this.setGridConfig({
		title:TXT.position_title,
		id:'interview_postion_grid',
		region:  'center',
		margins: '0 0 4 0',
		cmargins:'0 0 4 0',
		loadMask:true
	});
	
	this.setSelectModel( 0 );				
}