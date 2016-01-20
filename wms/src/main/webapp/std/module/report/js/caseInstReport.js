Ecp.components.globalView=new Ecp.GlobalView('report');
function caseInstReportLayout(){
	// 1、initialize json data
	var cmtTreeObj={};
	typeof cmt_caseInstReport_config=='undefined'?1:cmtTreeObj['cmtTreeColumn']=cmt_caseInstReport_config;
	typeof cmt_caseInstReport_columnconfig=='undefined'?1:cmtTreeObj['cmtConfig']=cmt_caseInstReport_columnconfig;
	typeof cmt_caseInstReport_eventconfig=='undefined'?1:cmtTreeObj['cmtTreeEvent']=cmt_caseInstReport_eventconfig;
	
	var toolBarCusObj={};
	typeof cmt_caseInstReport_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_caseInstReport_toolBarFn;
	typeof cmt_caseInstReport_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_caseInstReport_toolBarConfig;
	
	// 2、initialize widgets
	// initialize toolbar
	Ecp.showWaitingWin();
	var caseInstReportToolBar = new Ecp.CaseReportToolBarWidget();
	caseInstReportToolBar.setWidgetEvent({
		queryCaseReport:showQueryCaseInstReportWin
	});
	caseInstReportToolBar.customization(toolBarCusObj);
	
	// initialize grid
	var caseInstReportGrid=new Ecp.CaseReportGridWidget(TXT.reportInst, 'genInstReport');
	caseInstReportGrid.setToolBar(caseInstReportToolBar);
	
	// project customization
	caseInstReportGrid.customization(cmtTreeObj);
	
	// 3、display main panel
	Ecp.components.globalView.addModuleComp(caseInstReportGrid.render());
	Ecp.components.globalView.render(TXT.report);

	// register service widgets
	Ecp.components.caseInstReportGrid=caseInstReportGrid;
};

Ecp.onReady(caseInstReportLayout);