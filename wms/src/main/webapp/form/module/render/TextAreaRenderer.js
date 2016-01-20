com.felix.form.module.render.TextAreaRenderer = function() {
	com.felix.nsf.Util.extend(this, com.felix.form.module.render.Renderer);
	
	var regex = null ;
	var regexText = null;
	
	this.constructor = function( field ) {
		this.super.constructor( field );
		
		if( field.regex )
			regex = field.regex;
		if( field.regexText )
			regexText = field.regexText;
	}
	
	this.getExpectedType = function(){
		return "textarea";
	}
	
	this.doRender = function(){
		var config = { id : this.getName(),
					   name :this.getName(),
					   fieldLabel :this.getLabel(),
					   width :300,
					   height :150,
					   readOnly: this.isReadOnly(),
					   allowBlank: !this.isMandatory(),
					   value:this.getValue()
					  };
		
		if (regex != null) {
			if( !Ext.isIE ) {
				regex = regex.replace(/\\r\\n/g,'\\n');	
			}
			config.regex = new RegExp(regex);
			config.regexText=TXT.template_format_error;
		}
		
		var area = new Ext.form.TextArea( config );
		
		return area;
	}
}