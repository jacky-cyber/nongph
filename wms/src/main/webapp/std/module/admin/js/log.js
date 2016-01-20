Ecp.components.globalView=new Ecp.GlobalView('system');
function logLayout(){
	// 1、初始化json数据，该页面需用到的各个组件的配置信息等
	var cmtStoreObj={};
	typeof cmt_log_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_log_dsConfig;
	typeof cmt_log_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_log_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_log_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_log_gridConfig;
	typeof cmt_log_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_log_cmConfig;
	typeof cmt_log_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_log_gridEventConfig;
	
	var toolBarCusObj={};
	typeof cmt_log_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_log_toolBarFn;
	typeof cmt_log_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_log_toolBarConfig;
	
	// 2、初始化各个组件
	// 初始化toolbar
	var logToolBar=new Ecp.LogToolBar();
	logToolBar.setWidgetEvent({
		queryLog:showQueryLogWin
	});
	logToolBar.customization(toolBarCusObj);
	
	// 初始化grid
	var logGrid=new Ecp.LogGrid();
	logGrid.setToolBar(logToolBar.render());
	// 添加grid事件
	logGrid.setWidgetEvent({
		store:{
			load:loadLogData
		}
	});
	// 项目客户化
	logGrid.customization({
		store:cmtStoreObj,
		grid:cmtGridObj
	});
	
	// 3、显示主界面
	Ecp.components.globalView.addModuleComp(logGrid.render());
	Ecp.components.globalView.render(TXT.log);
	logGrid.show();
	
	// register service widgets
	Ecp.components.logGrid=logGrid;
};

Ecp.onReady(logLayout);