function taskLayout(){
	// task
	var taskGridPanel = new com.felix.eni.module.task.TaskGridPanel();
	// tabPanel
	var taskNewTabPanel = new com.felix.eni.module.task.TaskNewInfoTabPanel();

	com.felix.nsf.Context.globalView = new com.felix.nsf.GlobalView('task', 1);
	com.felix.nsf.Context.globalView.mainPanel.layout = 'border';
	com.felix.nsf.Context.globalView.addModuleComp( taskGridPanel.getExtGridPanel() );
	com.felix.nsf.Context.globalView.addModuleComp( taskNewTabPanel.getExtTabPanel() );
	com.felix.nsf.Context.globalView.render( TXT.task );
	
	com.felix.nsf.Context.currentComponents.taskGridPanel = taskGridPanel;
	com.felix.nsf.Context.currentComponents.taskMsgGrid = taskNewTabPanel.getItem(0);
	com.felix.nsf.Context.currentComponents.taskCaseGrid = taskNewTabPanel.getItem(1);
	com.felix.nsf.Context.currentComponents.taskNewTabPanel = taskNewTabPanel;
	
	taskGridPanel.load();
	taskNewTabPanel.getItem(0).findNewIncomingMessage();
	var json = {
		'caseId':'',
		'magInstCode':'',
		'creatorBic':'',
		'referenceNum':'',
		'account':'',
		'remittance':'',
		'type':'',
		'currency':'',
		'amountFrom':0,
		'amountTo':0,
		'spendTime':0,
		'valueDateFrom':'',
		'fromValueDate':'',
		'valueDateTo':'',
		'toValueDate':'',
		'createTimeFrom':'',
		'fromCreateTime':'',
		'createTimeTo':'',
		'toCreateTime':'',
		'status':'',
		'internalCode':'',
		'isSubAccount':'',
		'IBPSeqNum':'',
		'realCaseAssignee':''
	};
	taskNewTabPanel.getItem(1).search( {
		'cmd':'case',
		'action':'listAllCaseAsssignment',
		'json':Ext.util.JSON.encode(json)
	});
}
com.felix.nsf.Util.onReady(taskLayout);