Ecp.components.globalView=new Ecp.GlobalView('system');
function bicLayout(){
	var cmtStoreObj={};
	typeof cmt_bic_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_bic_dsConfig;
	typeof cmt_bic_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_bic_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_bic_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_bic_gridConfig;
	typeof cmt_bic_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_bic_cmConfig;
	typeof cmt_bic_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_bic_gridEventConfig;
	
	var toolBarCusObj={};
	typeof cmt_bic_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_bic_toolBarFn;
	typeof cmt_bic_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_bic_toolBarConfig;
	
	var bicToolBar=new Ecp.BicToolBarWidget();
	bicToolBar.setWidgetEvent({
		importBic:showImportBicWin,
		addBic:showAddBicWin,
		editBic:showEditBicWin,
		queryBic:showQueryBicWin
	});
	bicToolBar.customization(toolBarCusObj);
	
	var bicGrid=new Ecp.BicGridWidget();
	bicGrid.setToolBar(bicToolBar.render());
	bicGrid.setWidgetEvent({
		grid:{
			dblclick:showEditBicWin
		},
		store:{
			load:loadBicData
		}
	});
	bicGrid.customization({
		store:cmtStoreObj,
		grid:cmtGridObj
	});
	
	Ecp.components.globalView.addModuleComp(bicGrid.render());
	Ecp.components.globalView.render(TXT.bic);
	bicGrid.show();
	
	// register service widgets
	//Ecp.components.bicToolBar=bicToolBar;
	Ecp.components.bicGrid=bicGrid;
};

Ecp.onReady(bicLayout);