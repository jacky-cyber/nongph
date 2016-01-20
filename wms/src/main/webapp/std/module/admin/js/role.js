Ecp.components.globalView=new Ecp.GlobalView('system');
function roleLayout(){
	// 1、initialize json data
	var cmtStoreObj={};
	typeof cmt_role_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_role_dsConfig;
	typeof cmt_role_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_role_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_role_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_role_gridConfig;
	typeof cmt_role_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_role_cmConfig;
	typeof cmt_role_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_role_gridEventConfig;
	
	var toolBarCusObj={};
	typeof cmt_role_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_role_toolBarFn;
	typeof cmt_role_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_role_toolBarConfig;
	
	// 2、initialize widget
	// initialize toolbar
	var roleToolBar=new Ecp.RoleToolBarWidget();
	roleToolBar.setWidgetEvent({
		showAddRoleWin:showAddRoleWin,
		showEditRoleWin:showEditRoleWin,
		clickDeleteRoleBtn:clickDeleteRoleBtn
	});
	roleToolBar.customization(toolBarCusObj);
	
	// initialize grid
	var roleGrid=new Ecp.RoleGridWidget();
	roleGrid.setToolBar(roleToolBar.render());
	// add grid event
	roleGrid.setWidgetEvent({
		grid:{
			// double click event
			dblclick:showEditRoleWin
		},
		store:{
			load:loadRoleData
		}
	});
	// project custmization
	roleGrid.customization({
		store:cmtStoreObj,
		grid:cmtGridObj
	});
	
	// 3、diaplay main panel
	Ecp.components.globalView.addModuleComp(roleGrid.render());
	Ecp.components.globalView.render(TXT.role_manager);
	roleGrid.show();
	
	// register service widgets
	//Ecp.components.roleToolBar=roleToolBar;
	Ecp.components.roleGrid=roleGrid;
};

Ecp.onReady(roleLayout);