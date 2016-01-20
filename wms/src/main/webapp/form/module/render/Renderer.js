com.felix.form.module.render.Renderer = function(){
	var type;
	var name;
	var label; 
	var value = null; 
	var readOnly = false;
	var mandatory = false;
	var action = null;
	
	this.getType = function(){
		return type;
	}

	this.getName = function(){
		return name;
	}
	
	this.getLabel = function(){
		return label;
	}
	
	this.getValue = function(){
		return value;
	}

	this.isReadOnly = function(){
		return readOnly;
	}
	
	this.isMandatory = function(){
		return mandatory;
	}
	
	this.getAction = function(){
		return action;
	}
	
	this.getExpectedType = function(){}
	
	this.doRender = function(){}
	
	this.render = function(){
		if( this.getExpectedType()!= type )
			return false;
		return this.doRender();
	}
	
	this.constructor = function( field ) {
		type = field.type;
		name = field.name;
		label = field.label;
		if( field.value ) 
			value = field.value;
		if( field.readOnly )
			readOnly = field.readOnly;
		if( field.mandatory )
			mandatory = field.mandatory;
		if( field.action ) 
			action = field.action;
	}
}
com.felix.form.module.render.Renderer.rendererMap = {
		checkBox: com.felix.form.module.render.CheckBoxRenderer,
		comboBox: com.felix.form.module.render.ComboBoxRenderer,
		date:     com.felix.form.module.render.DateRenderer,
		hidden:   com.felix.form.module.render.HiddenRenderer,
		number:   com.felix.form.module.render.NumberRenderer,
		textarea: com.felix.form.module.render.TextAreaRenderer,
		text:     com.felix.form.module.render.TextRenderer };
com.felix.form.module.render.Renderer.getRenderer = function(type){
	return com.felix.form.module.render.Renderer.rendererMap[type];
}