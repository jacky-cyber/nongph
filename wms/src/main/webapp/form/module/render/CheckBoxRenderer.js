com.felix.form.module.render.CheckBoxRenderer = function() {
	com.felix.nsf.Util.extend(this, com.felix.form.module.render.Renderer);
	
	var options = null;
	
	this.constructor = function( field ) {
		this.super.constructor( field );
		options = field.options;
	}
	
	this.getExpectedType = function(){
		return "checkBox";
	}
	
	this.doRender = function(){
		var array = new Array();
		for ( var i = 0; i < options.length; i++) {
			var option = options[i];
			array.push( { boxLabel:option.label, name: option.value } );
		}
		
		var config = { id : this.getName(),
					   name :this.getName(),
					   fieldLabel :this.getLabel(),
					   width :300,
					   readOnly: this.isReadOnly(),
					   allowBlank: !this.isMandatory(),
					   value:this.getValue(),
					   columns:3,
                                           items:array
				  	  };
		var cbg = new Ext.form.CheckboxGroup(config);
	
		if( this.getAction() ){
			eval( this.getAction() );
		}
		
		return cbg;
	}
}
