/**
 * ServiceTabPanel
 */
com.felix.nsf.base.ServiceTabPanel = function() {
	this.tabPanel=null;
	this.items=[];

	this.handleWidgetConfig = function(handler){
		handler(this);
	}
	
	this.render = function(){
		this.tabPanel=new Ecp.TabPanel();
		this.tabPanel.init();
		var tabPanelObj={};
		tabPanelObj['config']=this.config;
		tabPanelObj['items']=this.items;
		this.tabPanel.customization(tabPanelObj);
		this.tabPanel['ecpOwner']=this;
		return this.tabPanel.render();
	}
	
	this.getItem = function(index){
		return this.items[index]['ecpOwner']['ecpOwner'];
	}

}