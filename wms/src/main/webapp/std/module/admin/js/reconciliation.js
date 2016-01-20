Ecp.components.globalView=new Ecp.GlobalView('system');
function reconciliationLayout(){
	var cmtStoreObj={};
	typeof cmt_reconciliation_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_reconciliation_dsConfig;
	typeof cmt_reconciliation_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_reconciliation_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_reconciliation_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_reconciliation_gridConfig;
	typeof cmt_reconciliation_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_reconciliation_cmConfig;
	typeof cmt_reconciliation_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_reconciliation_gridEventConfig;
	
	var toolBarCusObj={};
	typeof cmt_reconciliation_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_reconciliation_toolBarFn;
	typeof cmt_reconciliation_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_reconciliation_toolBarConfig;
	
	var reconciliationToolBar=new Ecp.ReconciliationToolBar();
	reconciliationToolBar.setWidgetEvent({
		importReconciliationFile:showImportReconciliationFileWin
	});
	
	var reconciliationGrid=new Ecp.ReconciliationGrid();
	reconciliationGrid.setToolBar(reconciliationToolBar.render());
	/*reconciliationGrid.setWidgetEvent({
		store:{
			load:loadLogData
		}
	});*/
	reconciliationGrid.customization({
		store:cmtStoreObj,
		grid:cmtGridObj
	});
	
	Ecp.components.globalView.addModuleComp(reconciliationGrid.render());
	Ecp.components.globalView.render(TXT.reconciliation);
	//reconciliationGrid.show();
	
	Ecp.components.reconciliationGrid=reconciliationGrid;
};

Ecp.onReady(reconciliationLayout);