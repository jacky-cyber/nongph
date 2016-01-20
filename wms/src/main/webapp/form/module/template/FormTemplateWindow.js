com.felix.form.module.template.FormTemplateWindow = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	
	var form = new com.felix.form.module.template.FormTemplateFormPanel();
	var grid = new com.felix.form.module.template.FormTemplateFieldGridPanel(); 
	
	var clickOkBtn = function() {
		if( form.isValid() ) {
			if( grid.getDataCount()==0 ) {
				com.felix.nsf.MessageBox.alert( "没有添加域!" );
			} else {
				var reqParam = form.getValues();
				var fields = [];
				
				var store = grid.getExtGridPanel().getStore();
				for(var i=0; i<store.getCount(); i++){
					var rd = store.getAt( i );
					fields.push( rd.data );
				}
				
				reqParam.fields =  Ext.encode( fields );
				
				com.felix.nsf.Ajax.requestWithMessageBox("formTemplateAction", 'save',function(result){
					if( result.state=='SUCCESS' ) {
		        		thoj.setCloseConfirm( false );
		     			thoj.close();
		     			thoj.getParent().getParent().reload();
					} else {
						var message = TXT['interview_interview_error_'+result.errorDesc.code];
						if( !message )
							message = result.errorDesc.desc;
						com.felix.nsf.MessageBox.alert( message );
					}
				}, reqParam);
			}
		}
	}
	
	var clickCloseBtn = function() {
		thoj.close();
	}
	
	this.setConfig( { title :TXT.form_template_title_new,
					  width :500,
					  height:540,
					  autoScroll :true,
					  border :false,
					  resizable :true,
					  modal:true,
					  minimizable :false,
					  maximizable :true,
					  shadow: false,
					  closeAction:'close',
					  buttonAlign :'right',
					  layout: 'border'
					} );
	this.setItems( [form, grid] );
	
	this.setButtons( [ { text :TXT.common_btnOK, handler : clickOkBtn }, 
					   { text :TXT.common_btnClose, handler : clickCloseBtn } 
					 ] );
	
	this.edit = function(id){
		com.felix.nsf.Ajax.requestWithMessageBox("formTemplateAction", "get", function(template){
			this.getExtWindow().show( this.getParent().getExtToolBar(), function(){
				form.setValues( template );
				for(var i=0; i<template.fields.length; i++)
					grid.update( template.fields[i] );
			});
		}, {id: id}, this );
	}
}