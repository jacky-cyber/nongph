Ecp.components.globalView=new Ecp.GlobalView('system');
function currencyLayout(){
	// 1、初始化json数据，该页面需用到的各个组件的配置信息等
	var cmtStoreObj={};
	typeof cmt_currency_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_currency_dsConfig;
	typeof cmt_currency_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_currency_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_currency_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_currency_gridConfig;
	typeof cmt_currency_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_currency_cmConfig;
	typeof cmt_currency_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_currency_gridEventConfig;
	
	var toolBarCusObj={};
	typeof cmt_currency_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_currency_toolBarFn;
	typeof cmt_currency_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_currency_toolBarConfig;
	
	// 2、初始化各个组件
	// 初始化toolbar
	var currencyToolBar=new Ecp.CurrencyToolBarWidget();
	currencyToolBar.setWidgetEvent({
		addCurrency:showAddCurrencyWin,
		deleteCurrency:clickDeleteCurrencyBtn,
		editCurrency:showEditCurrencyWin,
		queryCurrency:showQueryCurrencyWin
	});
	currencyToolBar.customization(toolBarCusObj);
	
	// 初始化grid
	var currencyGrid=new Ecp.CurrencyGridWidget();
	currencyGrid.setToolBar(currencyToolBar.render());
	// 添加grid事件
	currencyGrid.setWidgetEvent({
		grid:{
			// 双击事件
			dblclick:showEditCurrencyWin
		},
		store:{
			load:loadCurrencyData
		}
	});
	// 项目客户化
	currencyGrid.customization({
		store:cmtStoreObj,
		grid:cmtGridObj
	});
	
	//grid.height=500;
	// 3、显示主界面
	Ecp.components.globalView.addModuleComp(currencyGrid.render());
	Ecp.components.globalView.render(TXT.curr);
	
	currencyGrid.show();
	
	// register service widgets
	//Ecp.components.currencyToolBar=currencyToolBar;
	Ecp.components.currencyGrid=currencyGrid;
};

Ecp.onReady(currencyLayout);