/**
 * ProfileInfoTabpanel
 */
com.felix.std.module.login.ProfileInfoTabPanel = function(){
	this.tabPanel=null;
	this.config={
	        activeTab :0,
	        border :true,
	        frame :false,
	        region :'center',
	        defaults : {
	            autoScroll :false
	        }
	};
	this.items=[];

	this.handleWidgetConfig=function(handler){
		handler(this);
	}
	
	this.customization=function(obj)
	{
		if(obj.config!=null)
			this.config=obj.config;
		if(obj.items!=null)
			this.items=obj.items;
	}
	
	this.render=function()
	{
		this.tabPanel=new Ecp.TabPanel();
		this.tabPanel.init();
		var tabPanelObj={};
		tabPanelObj['config']=this.config;
		tabPanelObj['items']=this.items;
		this.tabPanel.customization(tabPanelObj);
		this.tabPanel['ecpOwner']=this;
		return this.tabPanel.render();
	}
	
	this.getItem=function(index){
		return this.items[index]['ecpOwner']['ecpOwner'];
	}
}