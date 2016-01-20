com.felix.form.module.render.DateRenderer = function() {
	com.felix.nsf.Util.extend(this, com.felix.form.module.render.Renderer);
	
	this.getExpectedType = function(){
		return "date";
	}
	
	this.doRender = function(){
		var config = { id : this.getName(),
					   name :this.getName(),
					   fieldLabel :this.getLabel(),
					   width :300,
					   readOnly: this.isReadOnly(),
					   allowBlank: !this.isMandatory(),
					   value:this.getValue(),
					   format :'Y-m-d'
					 };
		return new Ext.form.DateField( config );
	}
}

