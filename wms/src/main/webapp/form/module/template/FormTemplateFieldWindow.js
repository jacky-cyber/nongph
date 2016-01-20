com.felix.form.module.template.FormTemplateFieldWindow = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	
	var form = new com.felix.form.module.template.FormTemplateFieldFormPanel();
	var grid = new com.felix.form.module.template.FormTemplateFieldOptionGridPanel(); 
	
	var clickOkBtn = function() {
		if( form.isValid() ) {
			var vs = form.getValues();
			if( vs.type=='comboBox'|| vs.type=='checkBox' ) {
				if( grid.getDataCount()==0 ) {
					com.felix.nsf.MessageBox.alert( '没有设置选项!' );
					return;
				}
			}
						
			if( vs.type!='text' ) {
				vs.maxSize = '';
				vs.regex = '';
				vs.regexText = '';
			}
			
			var options = [];
			if( vs.type=='comboBox'|| vs.type=='checkBox' ) {
				var store = grid.getExtGridPanel().getStore();
				for(var i=0; i<store.getCount(); i++){
					var rd = store.getAt( i );
					options.push( {value:rd.get('value'), label:rd.get('label')} );
				}
			}
			vs.options =  options;
			
			var actions = [];
			actions.push( {onEvent:'init', scriptType:'JS', script:vs.action} )
			vs.actions = actions;
			
			var rowIndex = vs.rowIndex;
			delete vs.rowIndex;
			if( rowIndex=='' )
				thoj.getParent().getParent().update( vs );
			else
				thoj.getParent().getParent().update( vs, rowIndex );
			thoj.setCloseConfirm( false );
 			thoj.close();
		}
	}
	
	var clickCloseBtn = function() {
		thoj.close();
	}
	
	this.setConfig( { title :TXT.form_template_field_tile_new,
					  width :450,
					  height:550,
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
	
	this.edit = function( data, rowIndex ) {
		this.getExtWindow().show( this.getParent().getExtToolBar(), function(){
			if( data.actions && data.actions.length>0 )
				form.setValue( "action", data.actions[0].script );
			form.setValue("rowIndex", rowIndex);
			form.setValues( data );
			
			for(var i=0; i<data.options.length; i++){
				var o = data.options[i];
				grid.addOption(o.value, o.label);
			}
		});
	}
}