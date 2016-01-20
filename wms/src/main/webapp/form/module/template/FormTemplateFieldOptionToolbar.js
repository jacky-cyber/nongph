com.felix.form.module.template.FormTemplateFieldOptionToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var thoj = this;
	
	var doAdd = function(){
		 thoj.getParent().addOption();
	};
	
	var doDelete = function(){
		thoj.getParent().removeSelectedOptions();
	};
	
	this.setToolBarConfig( {} );
	
	this.setToolBarItemsConfig([ {type:'button', text:TXT.form_template_field_option_add, handler:'addAction',   iconCls:'icoAdd'},
	                             {type:'button', text:TXT.form_template_field_option_dlt, handler:'deleteAction',iconCls:'icoDel'}
	                           ]);
	
	this.setToolBarEvent( { addAction   : doAdd,
		                    deleteAction: doDelete} );
}