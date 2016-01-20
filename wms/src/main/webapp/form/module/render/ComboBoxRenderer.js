com.felix.form.module.render.ComboBoxRenderer = function() {
	com.felix.nsf.Util.extend(this, com.felix.form.module.render.Renderer);

	var options = null;
	
	this.constructor = function( field ) {
		this.super.constructor( field );
		options = field.options;
	}
	
	this.getExpectedType = function(){
		return "comboBox";
	}
	
	this.doRender = function(){
		var array = new Array();
		for ( var i = 0; i < options.length; i++) {
			var option = options[i];
			var subArray = new Array();
			subArray[0] = option.label;
			subArray[1] = option.value;
			
			array.push( subArray );
		}
		
		var store = new Ext.data.SimpleStore( { fields : [ 'label', 'value' ],
												data :array
											   } );
		
		var config = { id : this.getName(),
					   name :this.getName(),
					   fieldLabel :this.getLabel(),
					   width :300,
					   readOnly: this.isReadOnly(),
					   allowBlank: !this.isMandatory(),
					   value:this.getValue(),
					   tpl :'<tpl for="."><div ext:qtip="{label}" class="x-combo-list-item">{label}</div></tpl>',
					   store :store,
					   displayField :'label',
					   valueField :'value',
					   mode :'local',
					   triggerAction :'all',
					   forceSelection :true,
					   selectOnFocus :true
				  };
		var combo = new Ext.form.ComboBox(config);
	
		if( this.getAction() ){
			eval( this.getAction() );
		}
		
		return combo;
	}
}