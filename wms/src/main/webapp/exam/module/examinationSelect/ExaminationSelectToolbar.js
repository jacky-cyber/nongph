com.felix.exam.module.examinationSelect.ExaminationSelectToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	var thoj = this;
	
	var doSelect = function(){
		var rds = thoj.getParent().getSelectedRecordDatas(true);
		if( rds ) {
			for( var i=0; i<rds.length; i++ ) {
				var rd = rds[i];
				thoj.getParent().getParent().getParent().getParent().addExamination(rd.id, rd.name, rd['position.name']);
			}
		}
	};
	
	this.setToolBarConfig( {} );

	this.setToolBarItemsConfig([ 
	                             {type:'button', text:TXT.exam_examination_select_add,  handler:'addAction',   iconCls:'icoAdd'}
	                           ]);
	
	this.setToolBarEvent( { addAction: doSelect } );
}