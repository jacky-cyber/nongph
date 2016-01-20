/**
 * TaskMessageViewWindow
 */
com.felix.eni.module.task.TaskMessageViewWindow = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var prefix = new Date().getTime();
	
	this.setConfig( { id:'xmlMsgInTaskList' + this.prefix,
					  width :700,
					  height :500,
					  autoScroll :false,
					  layout :'border',
					  border :false,
					  modal:true,
					  minimizable :false,
					  maximizable :true,
					  layoutConfig : { animate :false },
					  resizable :false,
					  buttonAlign :'center'
					} );
	this.buttons=null;
	this.approveButtons=[{text: TXT.task_approval,  handler : function() { clickTaskApproveBtn(this); } },
						 {text: TXT.task_reject,    handler : function() { clickTaskRejectBtn(this);	} },
						 {text: TXT.task_detail,    handler : function() { showTaskDetailWin(this); } },
						 {text: TXT.task_findCase,  handler : function() { showCaseInTaskDetailWin(this); } },
						 {text: TXT.common_btnClose,handler : function() { win.window.close(); } } ];
	this.modifyButtons =[{text: TXT.task_modify,    handler : function() { clickTaskModifyBtn(this); } },
						 {text: TXT.task_cancel,    handler : function() { clickTaskCancelBtn(this); } },
						 {text: TXT.task_detail,	handler : function() { showTaskDetailWin(this);	} },
						 {text: TXT.task_findCase,  handler : function() { showCaseInTaskDetailWin(this); } },
						 {text: TXT.common_btnClose,handler : function() { win.window.close(); } } ];
						 
	this.getMessageBody=function(){
		return this.getItem(0)['ecpOwner'].getMessageBody();
	}
	
	this.validate = function(tag,validateSuccessFun){
		return this.getItem(0)['ecpOwner'].validate(tag,validateSuccessFun);
	}
	
	this.validateFin=function(tag,sender,receiver,validateSuccessFun){
		return this.getItem(0)['ecpOwner'].validateFin(tag,sender,receiver,validateSuccessFun);
	}
}

com.felix.eni.module.task.TaskMessageViewWindow.createWindow=function(obj, msgConfig, observers){
	// tabpanel
	var tp = new Ecp.MessageViewTabPanel();
	tp.handleWidgetConfig(function(o){
		if(msgConfig['status']=='W')
			msgConfig['editMode']='readOnly';
		else
			msgConfig['editMode']='editable';
		msgConfig['msgLang']=MESSAGE_VIEW_LANG;
		o.setMsgSrc(msgConfig);
	});
	if(obj['items'] && obj['items'][0])
		tp.customization(obj['items'][0]);
	
	// form
	var taskCommentForm = new Ecp.TaskCommentForm();
	if(obj['items'] && obj['items'][1])
		taskCommentForm.customization(obj['items'][1]);
		
	// window
	var win = new Ecp.TaskMessageViewWindow();
	win.handleWidgetConfig(function(w){
		w.config.closeAction='close';
		w.items=[tp.render(),taskCommentForm.render()];
		win.msgConfig=msgConfig;
	});
	
	win.customization(obj);
	if(observers && observers.length>0)
		win.observers=observers;
	win.render();
	return win.window;
}
