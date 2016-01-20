/**
 * ProfileInfoWindow
 */
com.felix.std.module.login.ProfileInfoWindow = function(){
	this.window=null;
	this.config={
		    title: TXT.common_profile,
	        width:350,
	        height:200,
	        autoScroll :false,
	        layout:'border',
	        border:false,
	        minimizable: false,
	        maximizable: false,
	        resizable: false,
	        modal:true,
			layoutConfig: {animate:false}
	};
	this.buttons=[];
	this.observers=[];
	this.items=[];

	this.handleWidgetConfig = function(handler){
		handler(this);
	}
	
	this.customization=function(obj) {
		if(obj.config!=null)
			this.config=obj.config;
		if(obj.buttons!=null)
			this.buttons=obj.buttons;
	}
	
	this.setItems=function(items) {
		this.items=items;
	}
	
	this.render=function(){
		this.window=new Ecp.Window();
		this.window.init();
		var winObj={};
		winObj['config']=this.config;
		winObj['buttons']=this.buttons;
		winObj['items']=this.items;
		this.window.customization(winObj);
		this.window['ecpOwner']=this;
		return this.window.render();
	}
	
	this.addObserver=function(observer)
	{
		this.observers.push(observer);
	}
	
	/**
	 * execute every observer's update method
	 */
	this.notifyAll=function(eventName)
	{
		for(var i=0;i<this.observers.length;i++)
			this.observers[i].update(this.window.window,eventName);
	}
	
	this.getItem=function(index)
	{
		return this.window.getItem(index)['ecpOwner'];
	}
	
	this.show=function(){
		this.window.window.show();
	}
	
	this.setCloseAction=function(isClose){
		if(!isClose)
		{
			this.config.closeAction='hide';
			this.config.onHide=function(){
				this.hide();
			}
		}else{
			this.config.closeAction='close';
			this.config.onHide=function(){
				this.close();
			}
		}
	}
	
	Ecp.ProfileInfoWindow.createSingleEditWinow=function(windowObj){
		if(!Ecp.ProfileInfoWindow.singleInfoWinows)
		{
			var informationForm=new Ecp.InformationForm();
			var passwordForm=new Ecp.PasswordForm();
			
			var profileInfoTabPanel=new Ecp.ProfileInfoTabPanel();
			
			if(windowObj['items'] && windowObj['items'][0])
			{
				profileInfoTabPanel.customization(windowObj['items'][0]);
				
				if(windowObj['items'][0]['items'] && windowObj['items'][0]['items'][0])
					informationForm.customization(windowObj['items'][0]['items'][0]);
				if(windowObj['items'][0]['items'] && windowObj['items'][0]['items'][1])
					passwordForm.customization(windowObj['items'][0]['items'][1]);
			}
			
			profileInfoTabPanel.items=[informationForm.render(),passwordForm.render()];
			
			// window
			var profileInfoWindow = new Ecp.ProfileInfoWindow();
			profileInfoWindow.handleWidgetConfig(function(obj){
				obj.setCloseAction(false);
				obj.items=[profileInfoTabPanel.render()];
			});
			profileInfoWindow.customization(windowObj);
	
			profileInfoWindow.render();
			Ecp.ProfileInfoWindow.singleInfoWinows=profileInfoWindow.window;
		}
		return Ecp.ProfileInfoWindow.singleInfoWinows;
	}
}