Ecp.components.globalView=new Ecp.GlobalView('system');
function swiftServiceLayout(){
	var cmtStoreObj={};
	typeof cmt_swift_service_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_swift_service_dsConfig;
	typeof cmt_swift_service_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_swift_service_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_swift_service_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_swift_service_gridConfig;
	typeof cmt_swift_service_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_swift_service_cmConfig;
	typeof cmt_swift_service_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_swift_service_gridEventConfig;
	
	var toolBarCusObj={};
	typeof cmt_swift_service_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_swift_service_toolBarFn;
	typeof cmt_swift_service_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_swift_service_toolBarConfig;

	var swiftServiceToolBar=new Ecp.SwiftServiceToolBar();
	swiftServiceToolBar.setWidgetEvent({
		importSwiftService:showImportSwiftServiceWin,
		addSwiftService:showAddSwiftServiceWin,
		editSwiftService:showEditSwiftServiceWin,
		deleteSwiftService:showDeleteSwiftServiceWin,
		querySwiftService:showQuerySwiftServiceWin
	});
	swiftServiceToolBar.customization(toolBarCusObj);
	
	var swiftServiceGrid=new Ecp.SwiftServiceGrid();
	swiftServiceGrid.setToolBar(swiftServiceToolBar.render());

	swiftServiceGrid.setWidgetEvent({
		grid:{
			dblclick:showEditSwiftServiceWin
		},
		store:{
			load:loadSwiftServiceData
		}
	});
	
	swiftServiceGrid.customization({
		store:cmtStoreObj,
		grid:cmtGridObj
	});
	
	swiftServiceGrid.customization({
		store:cmtStoreObj,
		grid:cmtGridObj
	});
	
	Ecp.components.globalView.addModuleComp(swiftServiceGrid.render());
	Ecp.components.globalView.render(TXT.sys);
	swiftServiceGrid.show();
	
	// register service widgets
	//Ecp.components.swiftServiceToolBar=swiftServiceToolBar;
	Ecp.components.swiftServiceGrid=swiftServiceGrid;
};

Ecp.onReady(swiftServiceLayout);