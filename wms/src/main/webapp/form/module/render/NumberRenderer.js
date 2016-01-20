com.felix.form.module.render.NumberRenderer = function() {
	com.felix.nsf.Util.extend(this, com.felix.form.module.render.Renderer);
	
	this.getExpectedType = function(){
		return "number";
	}
	
	this.doRender = function(){
		var config = { 	id : this.getName(),
						name :this.getName(),
						fieldLabel :this.getLabel(),
						width :300,
						readOnly: this.isReadOnly(),
						allowBlank: !this.isMandatory(),
						value:this.getValue(),
						allowDecimals:true,
						decimalPrecision :2
					  };
		var number = new Ext.form.NumberField( config )
		if( this.getAction() ){
			eval( this.getAction() );
		}
		return number;
	}	
}