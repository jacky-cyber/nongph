Ecp.components.globalView=new Ecp.GlobalView('report');
function caseStatusReportLayout(){
	// 1、initialize json data
	var cmtTreeObj={};
	typeof cmt_caseStatusReport_config=='undefined'?1:cmtTreeObj['cmtTreeColumn']=cmt_caseStatusReport_config;
	typeof cmt_caseStatusReport_columnconfig=='undefined'?1:cmtTreeObj['cmtConfig']=cmt_caseStatusReport_columnconfig;
	typeof cmt_caseStatusReport_eventconfig=='undefined'?1:cmtTreeObj['cmtTreeEvent']=cmt_caseStatusReport_eventconfig;
	
	var toolBarCusObj={};
	typeof cmt_caseStatusReport_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_caseStatusReport_toolBarFn;
	typeof cmt_caseStatusReport_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_caseStatusReport_toolBarConfig;
	
	// 2、initialize widgets
	// initialize toolbar
	Ecp.showWaitingWin();
	var caseStatusReportToolBar = new Ecp.CaseReportToolBarWidget();
	caseStatusReportToolBar.setWidgetEvent({
		queryCaseReport:showQueryCaseStatusReportWin
	});
	caseStatusReportToolBar.customization(toolBarCusObj);
	
	// initialize grid
	var caseStatusReportGrid=new Ecp.CaseReportGridWidget(TXT.reportSta, 'genStatusReport');
	caseStatusReportGrid.setToolBar(caseStatusReportToolBar);
	
	// Project Customization
	caseStatusReportGrid.customization(cmtTreeObj);
	
	// 3、display main panel
	Ecp.components.globalView.addModuleComp(caseStatusReportGrid.render());
	Ecp.components.globalView.render(TXT.report);

	// register service widgets
	Ecp.components.caseStatusReportGrid=caseStatusReportGrid;
};

Ecp.onReady(caseStatusReportLayout);