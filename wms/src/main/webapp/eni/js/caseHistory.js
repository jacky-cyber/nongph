Ecp.components.globalView=new Ecp.GlobalView('caseBackUpMenu');
function caseLayout(){
	var cmtStoreObj={};
	typeof cmt_case_history_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_case_history_dsConfig;
	typeof cmt_case_history_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_case_history_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_case_history_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_case_history_gridConfig;
	typeof cmt_case_history_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_case_history_cmConfig;
	typeof cmt_case_history_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_case_history_gridEventConfig;
	
	var toolBarCusObj={};
	typeof cmt_case_history_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_case_history_toolBarFn;
	typeof cmt_case_history_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_case_history_toolBarConfig;
	
	var caseHistoryToolBar=new Ecp.CaseHistoryToolBar();
	caseHistoryToolBar.setWidgetEvent({
		queryCase:showBackUpSearchWin,
		resumeCase:resumeCase
	});
	caseHistoryToolBar.customization(toolBarCusObj);
	
	var caseGrid=new Ecp.CaseGrid();
	caseGrid.handleWidgetConfig(function(obj){
		obj.defaultGridConfig['title']=TXT.case_backup_record;
		obj.defaultGridConfig['id']='caseHistoryGrid';
	});
	caseGrid.setToolBar(caseHistoryToolBar.render());
	caseGrid.setWidgetEvent({
		/*grid:{
			dblclick:showCaseDetailWindow
		},*/
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
	Ecp.components.globalView.render(TXT.caseRestore);
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
		'IBPSeqNum':''
	};
	
	caseGrid.search({
		'cmd':'caseBackup',
		'action':'find',
		json:Ext.util.JSON.encode(json)
	});
	Ecp.components.caseGrid=caseGrid;
};

Ecp.onReady(caseLayout);