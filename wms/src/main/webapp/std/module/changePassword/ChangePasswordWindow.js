/**
 * change password window
 */
com.felix.std.module.changePassword.ChangePasswordWindow = function() {
	this.window=null;
	this.config={
			title: TXT.common_profile,
		    height: 277,
		    width: 380,
		    autoScroll :false,
		    layout:'form',
		    border:false,
		    minimizable: false,
		    maximizable: false,
		    resizable: false,
		    closable: false,
		    modal:true,
		    layoutConfig: {animate:false},
	        buttonAlign: 'center'
	};
	this.buttons=[
  				{text:TXT.common_btnOK,
					handler:function(){
  						clickLoginChangePasswordWBtn(this);
					}
				}];
	//this.observers=[];
	this.items=[];

	this.handleWidgetConfig = function(handler){
		handler(this);
	}
	
	this.customization = function(obj) {
		if(obj.config!=null)
			this.config=obj.config;
		if(obj.buttons!=null)
			this.buttons=obj.buttons;
	}
	
	this.render = function(){
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
	
	this.getItem = function(index){
		return this.window.getItem(index);
	}
	
	this.setCloseAction=function(isClose){
		if(!isClose) {
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
}