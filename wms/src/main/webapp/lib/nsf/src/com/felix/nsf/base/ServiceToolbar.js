com.felix.nsf.ServiceToolbar = function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[];
	this.defaultToolBarConfig={};

	this.handleWidgetConfig=function(handler){
		handler(this);	
	}
	
	this.setWidgetEvent=function(toolBarEventConfig){
		this.toolBar.setToolBarEvent(toolBarEventConfig);
	}
	
	this.setPremission=function(premission){
		this.toolBar.setToolBarPremission(premission);
	}
	
	this.render=function(){
		this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
		this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
		//toolBar.setOwner(this);
		this.toolBar.init();
		return this.toolBar.render();
	}
}