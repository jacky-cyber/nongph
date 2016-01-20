com.felix.form.module.render.HiddenRenderer = function() {
	com.felix.nsf.Util.extend(this, com.felix.form.module.render.Renderer);
	
	this.getExpectedType = function(){
		return "hidden";
	}
	
	this.doRender = function(){
		var config = { id : this.getName(),
					   name :this.getName(),
					   value:this.getValue()
					  };
		
		return new Ext.form.Hidden(config);
	}
}