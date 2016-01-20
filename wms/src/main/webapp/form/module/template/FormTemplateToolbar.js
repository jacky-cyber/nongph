com.felix.form.module.template.FormTemplateToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var thoj = this;

	this.setToolBarConfig( { id:'form_template_toolBar'} );
	
	this.setToolBarItemsConfig([ {id:'form_template_toolBar_query', type:'button', text:TXT.common_btnQuery, handler:'queryAction', iconCls:'icoFind'},
	                             {id:'form_template_toolBar_add',   type:'button', text:TXT.commom_btn_add,  handler:'addAction',   iconCls:'icoAdd'},
	                             {id:'form_template_toolBar_edit',  type:'button', text:TXT.commom_btn_edit, handler:'editAction',  iconCls:'icoDetail'},
	                             {id:'form_template_toolBar_delete',type:'button', text:TXT.common_btnDelete,handler:'deleteAction',iconCls:'icoDel'}
	                           ]);
	

	
	var showQueryWin = function(){
		if( !com.felix.nsf.Context.currentComponents.ExaminationSearchWindow ) {
			com.felix.nsf.Context.currentComponents.ExaminationSearchWindow = new com.felix.exam.module.examination.ExaminationSearchWindow();
			com.felix.nsf.Context.currentComponents.ExaminationSearchWindow.setParent( thoj );
		}
		com.felix.nsf.Context.currentComponents.ExaminationSearchWindow.show();
	};
	
	var showAddWin = function(){
		var win = new com.felix.form.module.template.FormTemplateWindow();
		win.setParent( thoj );
		win.show();
	};
	
	var showEditWin = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			var win = new com.felix.form.module.template.FormTemplateWindow();
			win.setParent( thoj );
			win.edit( recordData.id );
		}
	}
		
	var doDelete = function(){
		var recordData = thoj.getParent().getSelectedRecordData(true);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithoutMessageBox("formTemplateAction", "delete", function(result){
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
		                    deleteAction: doDelete} );
}