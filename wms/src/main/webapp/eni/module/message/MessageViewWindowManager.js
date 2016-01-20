com.felix.eni.module.message.MessageViewWindowManager = (function(thoj){
	var singleMessageView = [];
	
	thoj.createMessageViewWindow = function( msgConfig, observers) {
		var tp = new Ecp.MessageViewTabPanel();
		if( msgConfig ) {
			if( msgConfig['fin']!=undefined )
				msgConfig['msgLang'] = 'en';
			else
				msgConfig['msgLang'] = com.felix.nsf.GlobalConstants.MESSAGE_VIEW_LANG;
			tp.setMsgSrc(msgConfig);
		}
		
		// window
		var messageViewWin = new com.felix.eni.module.message.MessageViewWindow(tp, mc);
		if( msgConfig )
			messageViewWin.setMsgConfig( msgConfig );
		if( observers && observers.length>0 ) 
			for( var i=0; i<observers.length; i++ )
				messageViewWin.addObserver( observers[i] );
				
		
		messageViewWin.getExtWindow().addListener( 'hide',function(){
														 	messageViewWin.initMessageBody();
														 });
		messageViewWin.getExtWindow().addListener( 'close',function(){
														 	messageViewWin.initMessageBody();
														 });
			
		return messageViewWin;
	}

	this.createMessageViewByIndexWindow = function(index, obj, msgConfig, handleFun, observers){
		if( !singleMessageView[index] ) {
			Ecp.MessageViewWindow.singleMessageView[index] = Ecp.MessageViewWindow.createMessageViewWindow(obj,msgConfig,function(obj){
				obj.config.modal=true;
				obj.config.closeAction='hide';
				obj.config.onHide=function(){
					this.hide();
				}
				if(handleFun && typeof handleFun=='function')
					handleFun(obj);
			},observers);
		} else {
			if(msgConfig) {
				var messagViewWin=Ecp.MessageViewWindow.singleMessageView[index];
				if(msgConfig.fin==null)
					messagViewWin.window.window.setTitle(messagViewWin.titleJson[msgConfig.tag.substring(0,8)]);
				else
					messagViewWin.window.window.setTitle(msgConfig.tag);
				msgConfig['msgLang']=MESSAGE_VIEW_LANG;
				if(msgConfig['fin']!==undefined)
					msgConfig['msgLang']='en';
				messagViewWin.msgConfig=msgConfig;
				messagViewWin.setIframeSrc(msgConfig);
				if(msgConfig['IBPSeqNum']!==undefined)
					messagViewWin['items'][1]['ecpOwner'].reset();
			}
		}
		return Ecp.MessageViewWindow.singleMessageView[index];
	}

	this.createSingleWindow=function(obj,msgConfig,handleFun,observers){
		return this.createMessageViewByIndexWindow(0,obj,msgConfig,handleFun,observers);
	}
	
	this.createSingelWinToCreateMsg=function(obj,msgConfig,handleFun,observers){
		return this.createMessageViewByIndexWindow(1,obj,msgConfig,handleFun,observers);
	}
	
	this.createSingelWinToReplyMsg=function(obj,msgConfig,handleFun,observers){
		return this.createMessageViewByIndexWindow(2,obj,msgConfig,handleFun,observers);
	}
	
	this.createSingleFinWindow=function(obj,msgConfig,handleFun,observers){
		return this.createMessageViewByIndexWindow(3,obj,msgConfig,handleFun,observers);
	}
	
	this.createSingleFinInCaseWindow=function(obj,msgConfig,handleFun,observers){
		return this.createMessageViewByIndexWindow(4,obj,msgConfig,handleFun,observers);
	}
	
	return thoj;
})({});