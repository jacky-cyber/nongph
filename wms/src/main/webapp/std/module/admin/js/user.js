Ecp.components.globalView=new Ecp.GlobalView('system');
function userLayout(){
	// 1、initialize json data
	var cmtStoreObj={};
	typeof cmt_user_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_user_dsConfig;
	typeof cmt_user_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_user_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_user_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_user_gridConfig;
	typeof cmt_user_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_user_cmConfig;
	typeof cmt_user_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_user_gridEventConfig;
	
	var toolBarCusObj={};
	typeof cmt_user_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_user_toolBarFn;
	typeof cmt_user_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_user_toolBarConfig;
	
	// 2、initialize widget
	// initialize toolbar
	var userToolBar=new Ecp.UserToolBarWidget();
	userToolBar.setWidgetEvent({
		//showAddUserWin:showAddUserWin,
		//showEditUserWin:showEditUserWin,
		//clickDeleteUserBtn:clickDeleteUserBtn,
		showQueryUserWin:showQueryUserWin
	});
	userToolBar.customization(toolBarCusObj);
	
	//  initialize grid
	var userGrid=new Ecp.UserGridWidget();
	userGrid.setToolBar(userToolBar.render());
	// add grid event
	userGrid.setWidgetEvent({
		grid:{
			// double click event
			dblclick:showEditUserWin
		},
		store:{
			load:loadUserData
		}
	});
	// project custmization
	userGrid.customization({
		store:cmtStoreObj,
		grid:cmtGridObj
	});
	
	//grid.height=500;
	// 3、diaplay main panel
	Ecp.components.globalView.addModuleComp(userGrid.render());
	Ecp.components.globalView.render(TXT.user);	
	userGrid.show();
	
	// register service widgets
	//Ecp.components.userToolBar=userToolBar;
	Ecp.components.userGrid=userGrid;
};

Ecp.onReady(userLayout);