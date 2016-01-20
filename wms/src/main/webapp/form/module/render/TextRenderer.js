com.felix.form.module.render.TextRenderer = function() {
	com.felix.nsf.Util.extend(this, com.felix.form.module.render.Renderer);
	
	var maxLength;
	var regex = null ;
	var regexText = null;
	
	this.constructor = function( field ) {
		this.super.constructor( field );
		
		if( field.maxLength )
			maxLength = field.maxLength;
		if( field.regex )
			regex = field.regex;
		if( field.regexText )
			regexText = field.regexText;
	}
	
	this.getExpectedType = function(){
		return "text";
	}
	
	this.doRender = function(){
		var config = { id : this.getName(),
					   name :this.getName(),
					   fieldLabel :this.getLabel(),
					   maxLength :maxLength,
					   width :300,
					   readOnly: this.isReadOnly(),
					   allowBlank: !this.isMandatory(),
					   value:this.getValue()
					  };
		if(regex != null) {
			config.regex = new RegExp(regex);
			config.regexText = regexText;
		}
		
		var text = new Ext.form.TextField( config );
		
		if( this.getAction() ){
			eval( this.getAction() );
		}
		
		return text;
	}	
}