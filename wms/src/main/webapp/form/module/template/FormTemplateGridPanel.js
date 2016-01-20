com.felix.form.module.template.FormTemplateGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	var thoj = this;
	
	this.getDataStore().setConfig({
		url: com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL+"/formTemplateAction/search",
		root :'items',
		totalProperty :'totalCount',
		fields : [ 'id', 
				   'name',
				   'layout',
				   'onUsed' ]
		});
		
	this.setColumnModelConfig( [
			{ header :TXT.form_template_name,  dataIndex :'name',  menuDisabled :true,	width :300, align :'left' },
			{ header :TXT.form_template_layout,dataIndex :'layout',menuDisabled :true,	width :300,	align :'left' }] );
					
	this.setGridConfig({
		title:TXT.form_template,
		id:'form_template_grid',
		region:  'center',
		margins: '0 0 4 0',
		cmargins:'0 0 4 0',
		loadMask:true
	});
	
	this.setSelectModel( 0 );		
	
	var toolbar = new com.felix.form.module.template.FormTemplateToolbar();
	
	this.setTopToolBar( toolbar );
	var afterClickRow = function( grid, rowIndex ) {
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			if( recordData.onUsed ) {
				toolbar.getExtToolBar().findById('form_template_toolBar_edit').disable();
				//toolbar.getExtToolBar().findById('form_template_toolBar_copy').enable();
				toolbar.getExtToolBar().findById('form_template_toolBar_delete').disable();
			} else {
				toolbar.getExtToolBar().findById('form_template_toolBar_edit').enable();
				//toolbar.getExtToolBar().findById('form_template_toolBar_copy').disable();
				toolbar.getExtToolBar().findById('form_template_toolBar_delete').enable();
			}
		}		
	};
	
	var viewTemplate = function( grid, rowIndex ){
		var recordData = thoj.getSelectedRecordData(false);
		if( recordData ) {
			com.felix.nsf.Ajax.requestWithMessageBox("formTemplateAction", "get", function(ft){
				var form = com.felix.form.module.render.FormRenderer.render(ft);
				var win = new Ext.Window( {
					title :ft.name,
					width :500,
					height :450,
					autoScroll :false,
					layout :'border',
					border :false,
					minimizable :false,
					maximizable :false,
					resizable :true,
					shadow:false,
					resizable :false,
					modal :true,
					layoutConfig : {
						animate :false
					},
					items : [ form ],
					buttonAlign :'center'
				} );
				win.show(grid, function(){
                                    form.getForm().clearInvalid();
                                });
			}, {id: recordData.id} );
		}
	};
	
	this.setGridEventConfig( {rowclick: afterClickRow,
							  rowdblclick:viewTemplate} );
}