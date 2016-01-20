Ecp.components.globalView=new Ecp.GlobalView('report');
function caseTypeReportLayout(){
	// 1、initialize json data
	var cmtTreeObj={};
	typeof cmt_caseTypeReport_config=='undefined'?1:cmtTreeObj['cmtTreeColumn']=cmt_caseTypeReport_config;
	typeof cmt_caseTypeReport_columnconfig=='undefined'?1:cmtTreeObj['cmtConfig']=cmt_caseTypeReport_columnconfig;
	typeof cmt_caseTypeReport_eventconfig=='undefined'?1:cmtTreeObj['cmtTreeEvent']=cmt_caseTypeReport_eventconfig;
	
	var toolBarCusObj={};
	typeof cmt_caseTypeReport_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_caseTypeReport_toolBarFn;
	typeof cmt_caseTypeReport_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_caseTypeReport_toolBarConfig;
	
	// 2、initialize widgets
	// initialize toolbar
	Ecp.showWaitingWin();
	var caseTypeReportToolBar = new Ecp.CaseReportToolBarWidget();
	caseTypeReportToolBar.setWidgetEvent({
		queryCaseReport:showQueryCaseTypeReportWin
	});
	caseTypeReportToolBar.customization(toolBarCusObj);
	
	// initialize grid
	var caseTypeReportGrid=new Ecp.CaseReportGridWidget(TXT.reportType, 'genTypeReport');
	caseTypeReportGrid.setToolBar(caseTypeReportToolBar);
	
	// Project Customization
	caseTypeReportGrid.customization(cmtTreeObj);
	
	// 3、display main panel
	Ecp.components.globalView.addModuleComp(caseTypeReportGrid.render());
	Ecp.components.globalView.render(TXT.report);

	// register service widgets
	Ecp.components.caseTypeReportGrid=caseTypeReportGrid;
};

Ecp.onReady(caseTypeReportLayout);