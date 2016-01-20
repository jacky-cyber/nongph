Ecp.components.globalView=new Ecp.GlobalView('report');
function caseCurrencyReportLayout(){
	// 1、initialize json data
	var cmtTreeObj={};
	typeof cmt_caseCurrencyReport_config=='undefined'?1:cmtTreeObj['cmtTreeColumn']=cmt_caseCurrencyReport_config;
	typeof cmt_caseCurrencyReport_columnconfig=='undefined'?1:cmtTreeObj['cmtConfig']=cmt_caseCurrencyReport_columnconfig;
	typeof cmt_caseCurrencyReport_eventconfig=='undefined'?1:cmtTreeObj['cmtTreeEvent']=cmt_caseCurrencyReport_eventconfig;
	
	var toolBarCusObj={};
	typeof cmt_caseCurrencyReport_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_caseCurrencyReport_toolBarFn;
	typeof cmt_caseCurrencyReport_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_caseCurrencyReport_toolBarConfig;
	
	// 2、initialize widgets
	// initialize toolbar
	Ecp.showWaitingWin();
	var caseCurrencyReportToolBar = new Ecp.CaseReportToolBarWidget();
	caseCurrencyReportToolBar.setWidgetEvent({
		queryCaseReport:showQueryCaseCurrencyReportWin
	});
	caseCurrencyReportToolBar.customization(toolBarCusObj);
	
	// initialize grid
	var caseCurrencyReportGrid=new Ecp.CaseReportGridWidget(TXT.reportCurr, 'genCurrencyReport');
	caseCurrencyReportGrid.setToolBar(caseCurrencyReportToolBar);
	
	// Product Customization
	caseCurrencyReportGrid.customization(cmtTreeObj);
	
	// 3、display main panel
	Ecp.components.globalView.addModuleComp(caseCurrencyReportGrid.render());
	Ecp.components.globalView.render(TXT.report);

	// register service widgets
	Ecp.components.caseCurrencyReportGrid=caseCurrencyReportGrid;
};

Ecp.onReady(caseCurrencyReportLayout);