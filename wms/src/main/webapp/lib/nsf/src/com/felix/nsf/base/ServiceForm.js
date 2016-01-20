// base form
com.felix.nsf.base.ServiceForm = function(){
	this.form = null;
	
	this.config = {	labelAlign: 'left',
					region: 'center',
					frame:true,
					defaultType: 'textfield'
					};
	this.items=[];
	this.buttons=[];

	this.handleWidgetConfig=function(handler){
		handler(this);
	}
	
	this.render = function(){
		this.form = new com.felix.nsf.wrap.FormPanel();
		this.form.init();
		var obj={};
		obj['config']=this.config;
		obj['buttons']=this.buttons;
		obj['items']=this.items;
		this.form.customization(obj);
		return this.form.render();
	}
}