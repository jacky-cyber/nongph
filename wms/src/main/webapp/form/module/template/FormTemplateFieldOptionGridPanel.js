com.felix.form.module.template.FormTemplateFieldOptionGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	var thoj = this;
	
	var editor = new Ext.ux.grid.RowEditor({
        saveText: TXT.common_btnOK
    });
	
	this.getDataStore().setConfig({
		fields: ['id', 'value', 'label']
	});
	
	
	this.setColumnModelConfig( [{ header :TXT.form_template_field_name,  dataIndex :'value', menuDisabled :true,	width :150,  align :'left',
								  editor: { xtype: 'textfield', allowBlank: false} },
	                            { header :TXT.form_template_field_label, dataIndex :'label', menuDisabled :true,	width :150,  align :'left',
	                              editor: { xtype: 'textfield', allowBlank: false} } ] );
	
	this.setGridConfig({
		region:  'south',
		border :true,
		margins: '0 0 0 0',
		cmargins:'0 0 4 0',
		height:150,
		loadMask:true,
		plugins: [editor]
	});
	
	this.setEditable( true );
	this.setPagination( false );
	this.setTopToolBar( new com.felix.form.module.template.FormTemplateFieldOptionToolbar() );
	this.setSelectModel( 2 );
	this.addOption = function(value, label){
		if(!value)
		  value = 'new value';
		if(!label)
		  label='new label';
		
		editor.stopEditing();
		
		var store = this.getExtGridPanel().getStore();
	    store.insert( store.getCount(), new store.recordType({id:'', value:value, label:label} ) );
	    this.getExtGridPanel().getView().refresh();
	    
	    //this.getExtGridPanel().getSelectionModel().selectRow(0);
	    //editor.startEditing(0);
	}
	
	this.removeSelectedOptions = function(){
		editor.stopEditing();
		
		var records = this.getSelections(true);
		if( records ) {
			var store = this.getExtGridPanel().getStore();
			for( var i=0; i<records.length; i++ )
				store.remove( records[i] );
		}
		this.getExtGridPanel().getView().refresh();
	}
}
	