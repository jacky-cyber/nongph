com.felix.form.module.template.FormTemplateFieldGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	var thoj = this;
	
	this.getDataStore().setConfig({
		fields: ['id', 'name', 'label', 'type', 'readOnly', 'mandatory', 'initValue', 'maxSize', 'regex', 'regexText', 'actions', 'options' ]
	});
	
	
	this.setColumnModelConfig( [{ header :TXT.form_template_field_name,  dataIndex :'name', menuDisabled :true,	width :150,  align :'left'},
	                            { header :TXT.form_template_field_label, dataIndex :'label',menuDisabled :true,	width :150,  align :'left'},
	                			{ header :TXT.form_template_field_type,  dataIndex :'type', menuDisabled :true,	width :70,	 align :'left'} ] );
	
	this.setGridConfig({
		region:  'south',
		border :true,
		margins: '0 0 0 0',
		cmargins:'0 0 4 0',
		height:400,
		loadMask:true
	});
	
	this.setEditable( true );
	this.setPagination( false );
	this.setTopToolBar( new com.felix.form.module.template.FormTemplateFieldToolbar() );
	this.setSelectModel( 2 );
	
	this.update = function( data, rowIndex ) {
		var store = this.getExtGridPanel().getStore();
		if( rowIndex ) {
			store.removeAt( rowIndex );
			store.insert( rowIndex, new store.recordType( data ) );
		} else {
			store.add( new store.recordType( data ) );
		}
	    
	    this.getExtGridPanel().getView().refresh();
	}
}
	