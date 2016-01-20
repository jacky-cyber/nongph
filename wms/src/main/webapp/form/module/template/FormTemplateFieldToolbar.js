com.felix.form.module.template.FormTemplateFieldToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var thoj = this;
	
	var doAdd = function(){
		var qsw = new com.felix.form.module.template.FormTemplateFieldWindow();
		qsw.setParent( thoj );
		qsw.show();
	};
	
	var showEditWin = function(){
		var record = thoj.getParent().getSelected(true);
		if( record ) {
			var recordData = record.data;
			var rowIndex = thoj.getParent().getExtGridPanel().getStore().indexOf( record );
			
			var qsw = new com.felix.form.module.template.FormTemplateFieldWindow();
			qsw.setParent( thoj );
			qsw.edit( recordData, rowIndex );
		}
	}
	
	var doDelete = function(){
		var records = thoj.getParent().getSelections(true);
		if( records ) {
			var store = thoj.getParent().getExtGridPanel().getStore();
			for( var i=0; i<records.length; i++ )
				store.remove( records[i] );
		}
		thoj.getParent().getExtGridPanel().getView().refresh();
	};
	
	this.setToolBarConfig( {} );
	
	this.setToolBarItemsConfig([ {type:'button', text:TXT.form_template_field_add, handler:'addAction',   iconCls:'icoAdd'},
	                             {type:'button', text:TXT.form_template_field_edit,handler:'editAction',  iconCls:'icoDetail'},
	                             {type:'button', text:TXT.form_template_field_dlt, handler:'deleteAction',iconCls:'icoDel'}
	                           ]);
	
	this.setToolBarEvent( { addAction   : doAdd,
							editAction  : showEditWin, 
		                    deleteAction: doDelete} );
}