Ecp.components.globalView=new Ecp.GlobalView('eiCase');
function caseLayout(){
	var cmtStoreObj={};
	typeof cmt_case_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_case_dsConfig;
	typeof cmt_case_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_case_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_case_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_case_gridConfig;
	typeof cmt_case_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_case_cmConfig;
	typeof cmt_case_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_case_gridEventConfig;
	
	var toolBarCusObj={};
	typeof cmt_case_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_case_toolBarFn;
	typeof cmt_case_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_case_toolBarConfig;
	
	var caseToolBar=new Ecp.CaseToolBarWidget();
	caseToolBar.setWidgetEvent({
		queryCase:showQueryCaseWin,
		assignCase:showAssignCaseWin,
		createCaseWith192:showCreateCaseWith192InCaseListWin,
		createCaseWith195:showCreateCaseWith195InCaseListWin,
		createCaseWith196:showCreateCaseWith196InCaseListWin,
		createCaseWith292:showCreateCaseWith292InCaseListWin,
		createCaseWith295:showCreateCaseWith295InCaseListWin,
		createCaseWith296:showCreateCaseWith296InCaseListWin,
		createCaseWithFinTemplate:showMsgTmpWinWithCaseList,
		createCaseWithDraft:showPrivateMsgTmpWinWithCaseList,
		createCaseWith007:showCreateCaseWith007InCaseListWin,
		createCaseWith008:showCreateCaseWith008InCaseListWin,
		createCaseWith026:showCreateCaseWith026InCaseListWin,
		createCaseWith027:showCreateCaseWith027InCaseListWin,
		createCaseWith028:showCreateCaseWith028InCaseListWin,
		createCaseWith037:showCreateCaseWith037InCaseListWin,
		listGeCloseTimeCases:listGeCloseTimeCases
	});
	caseToolBar.customization(toolBarCusObj);
	
	var caseGrid=new Ecp.CaseGrid();
	caseGrid.setToolBar(caseToolBar.render());
	caseGrid.setWidgetEvent({
		grid:{
			dblclick:showCaseDetailWindow
		},
		store:{
			load:loadCaseData
		}
	});
	caseGrid.customization({
		store:cmtStoreObj,
		grid:cmtGridObj
	});
	
	//grid.height=500;
	Ecp.components.globalView.addModuleComp(caseGrid.render());
	Ecp.components.globalView.render(TXT.eiCase);
	var json={
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
	
	caseGrid.search({
		'cmd':'case',
		'action':'find',
		json:Ext.util.JSON.encode(json)
	});
	Ecp.components.caseGrid=caseGrid;
};

Ecp.onReady(caseLayout);