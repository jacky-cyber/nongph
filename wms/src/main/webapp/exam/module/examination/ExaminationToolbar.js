com.felix.exam.module.examination.ExaminationToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var thoj = this;

	this.setToolBarConfig( { id:'exam_examination_toolBar'} );
	
	this.setToolBarItemsConfig([ {id:'exam_examination_toolBar_query', type:'button', text:TXT.common_btnQuery, handler:'queryAction', iconCls:'icoFind'},
	                             {id:'exam_examination_toolBar_add',   type:'button', text:TXT.commom_btn_add,  handler:'addAction',   iconCls:'icoAdd'},
	                             {id:'exam_examination_toolBar_edit',  type:'button', text:TXT.commom_btn_edit, handler:'editAction',   iconCls:'icoDetail'},
	                             {id:'exam_examination_toolBar_copy',  type:'button', text:TXT.commom_btn_copy, handler:'copyAction',   iconCls:'icoCopy'},
	                             {id:'exam_examination_toolBar_delete',type:'button', text:TXT.common_btnDelete,handler:'deleteAction',iconCls:'icoDel'}
	                           ]);
	
	this.onSelectPosition = function(positionId){
		var ew = new com.felix.exam.module.examination.ExaminationWindow();
		com.felix.nsf.Context.currentComponents.setPositionSyncUnit.setHandler(function(){
			ew.getItem(0).setValue('positionId', positionId);
		});
		ew.setParent( thoj.getParent() );
		ew.setListeners( {'show':com.felix.nsf.Context.currentComponents.setPositionSyncUnit.addEvent()} );
		ew.show();
	}
	
	var showQueryWin = function(){
		if( !com.felix.nsf.Context.currentComponents.ExaminationSearchWindow ) {
			com.felix.nsf.Context.currentComponents.ExaminationSearchWindow = new com.felix.exam.module.examination.ExaminationSearchWindow();
			com.felix.nsf.Context.currentComponents.ExaminationSearchWindow.setParent( thoj );
		}
		com.felix.nsf.Context.currentComponents.ExaminationSearchWindow.show();
	};
	
	var showAddWin = function(){
		var win = new com.felix.exam.module.positionSelect.SelectPositionWindow();
		win.setParent( thoj );
		win.show();
	};
	
	var showEditWin = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			var win = new com.felix.exam.module.examination.ExaminationWindow();
			win.setParent( thoj );
			win.edit( recordData.id );
		}
	}
	
	var showCopyWin = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			var win = new com.felix.exam.module.examination.ExaminationWindow();
			win.setParent( thoj );
			win.copy( recordData.id );
		}
	}
	
	var doDelete = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithoutMessageBox("examinationAction", "delete", function(result){
				if( result.state=='SUCCESS' ) {
					thoj.getParent().reload();
				} else {
					com.felix.nsf.MessageBox.alert(result.errorDesc.desc);
				}
			}, {id:recordData.id});
		}
	};
	
	this.setToolBarEvent( { queryAction : showQueryWin,
		                    addAction   : showAddWin,
		                    editAction  : showEditWin, 
		                    copyAction  : showCopyWin,
		                    deleteAction: doDelete} );
}