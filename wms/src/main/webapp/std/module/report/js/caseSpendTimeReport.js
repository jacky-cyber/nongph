Ecp.components.globalView=new Ecp.GlobalView('report');
function caseSpendTimeReportLayout(){
	// 1、initialize json data
	var cmtTreeObj={};
	typeof cmt_caseSpendTimeReport_config=='undefined'?1:cmtTreeObj['cmtTreeColumn']=cmt_caseSpendTimeReport_config;
	typeof cmt_caseSpendTimeReport_columnconfig=='undefined'?1:cmtTreeObj['cmtConfig']=cmt_caseSpendTimeReport_columnconfig;
	typeof cmt_caseSpendTimeReport_eventconfig=='undefined'?1:cmtTreeObj['cmtTreeEvent']=cmt_caseSpendTimeReport_eventconfig;
	
	var toolBarCusObj={};
	typeof cmt_caseSpendTimeReport_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_caseSpendTimeReport_toolBarFn;
	typeof cmt_caseSpendTimeReport_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_caseSpendTimeReport_toolBarConfig;
	
	// 2、initialize widgets
	// initialize toolbar
	Ecp.showWaitingWin();
	var caseSpendTimeReportToolBar = new Ecp.CaseReportToolBarWidget();
	caseSpendTimeReportToolBar.setWidgetEvent({
		queryCaseReport:showQueryCaseSpendTimeReportWin
	});
	caseSpendTimeReportToolBar.customization(toolBarCusObj);
	
	// initialize grid
	var caseSpendTimeReportGrid=new Ecp.CaseReportGridWidget(TXT.reportTime, 'genSpendTimeReport');
	caseSpendTimeReportGrid.setToolBar(caseSpendTimeReportToolBar);
	
	// Project Customization
	caseSpendTimeReportGrid.customization(cmtTreeObj);
	
	// 3、display main panel
	Ecp.components.globalView.addModuleComp(caseSpendTimeReportGrid.render());
	Ecp.components.globalView.render(TXT.report);

	// register service widgets
	Ecp.components.caseSpendTimeReportGrid=caseSpendTimeReportGrid;
};

Ecp.onReady(caseSpendTimeReportLayout);