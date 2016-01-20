//add by jinlijun 2013/1/31
function reportTreeGridInitLayout(title, columns, action, showQueryWindow, exportExcelHandler){
	// 1、initialize json data
	var cmtTreeObj={};
	typeof cmt_caseTraceReport_config=='undefined'?1:cmtTreeObj['cmtTreeColumn']=cmt_caseTraceReport_config;
	typeof cmt_caseTraceReport_columnconfig=='undefined'?1:cmtTreeObj['cmtConfig']=cmt_caseTraceReport_columnconfig;
	typeof cmt_caseTraceReport_eventconfig=='undefined'?1:cmtTreeObj['cmtTreeEvent']=cmt_caseTraceReport_eventconfig;
	
	var toolBarCusObj={};
	typeof cmt_caseTraceReport_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_caseTraceReport_toolBarFn;
	typeof cmt_caseTraceReport_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_caseTraceReport_toolBarConfig;
	
	// 2、initialize widgets
	// initialize toolbar
	Ecp.showWaitingWin();
	var caseTraceReportToolBar = new Ecp.CaseReportToolBarWidget();
	caseTraceReportToolBar.setWidgetEvent({
		queryCaseReport:showQueryWindow,
		exportCaseReport:exportExcelHandler
	});
	caseTraceReportToolBar.customization(toolBarCusObj);
	
	// initialize grid
	var caseTraceReportGrid=new Ecp.CaseReportGridWidget(title, columns, action);
	caseTraceReportGrid.setToolBar(caseTraceReportToolBar);
	
	// Project Customization
	caseTraceReportGrid.customization(cmtTreeObj);
	
	// 3、display main panel
	Ecp.components.globalView.addModuleComp(caseTraceReportGrid.render());
	Ecp.components.globalView.render(TXT.report);
	
	//Ecp.components.caseTraceReportToolBar = caseTraceReportToolBar;

	// register service widgets
	Ecp.components.caseTraceReportGrid=caseTraceReportGrid;
};
