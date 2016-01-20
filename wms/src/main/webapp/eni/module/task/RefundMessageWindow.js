com.felix.eni.module.task.RefundMessageWindow = function(autoForm){
	com.felix.nsf.Util.extend(this, com.felix.nsf.base.ServiceWindow);
	
	var tcf = new com.felix.eni.module.task.TaskCommentForm();
	
	this.setConfig( {
				 	title:TXT.eni_message_fin,
					width :580,
					height :560,
			        autoScroll :false,
			      	layout :'border',
			        border:false,
			        modal:true,
			        minimizable: false,
			        maximizable: false,
					layoutConfig: {animate:false},
					resizable: false,
					shadow:false,
					buttonAlign: 'center'
					} );
	this.setButtons( [{ text:TXT.task_modify,    handler: clickTaskModifyForRefundBtn },
					  {	text:TXT.task_cancel,    handler: clickTaskCancelBtn},
					  { text:TXT.task_detail,    handler: showTaskDetailWin},
					  {	text:TXT.task_findCase,  handler: showCaseInTaskDetailWin},
					  {	text:TXT.common_btnClose,handler: function(){ this['ecpOwner'].window.close();}	}
					 ] );
	
	this.setItems([autoform, tcf.getExtFormPanel()]) 				 
					 
}