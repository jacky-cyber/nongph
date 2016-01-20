Ecp.components.globalView=new Ecp.GlobalView('report');
function caseRemiReportLayout(){
	// 1、initialize json data
	var cmtTreeObj={};
	typeof cmt_caseRemiReport_config=='undefined'?1:cmtTreeObj['cmtTreeColumn']=cmt_caseRemiReport_config;
	typeof cmt_caseRemiReport_columnconfig=='undefined'?1:cmtTreeObj['cmtConfig']=cmt_caseRemiReport_columnconfig;
	typeof cmt_caseRemiReport_eventconfig=='undefined'?1:cmtTreeObj['cmtTreeEvent']=cmt_caseRemiReport_eventconfig;
	
	var toolBarCusObj={};
	typeof cmt_caseRemiReport_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_caseRemiReport_toolBarFn;
	typeof cmt_caseRemiReport_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_caseRemiReport_toolBarConfig;
	
	// 2、initialize widgets
	// initialize toolbar
	Ecp.showWaitingWin();
	var caseRemiReportToolBar = new Ecp.CaseReportToolBarWidget();
	caseRemiReportToolBar.setWidgetEvent({
		queryCaseReport:showQueryCaseRemiReportWin
	});
	caseRemiReportToolBar.customization(toolBarCusObj);
	
	// initialize grid
	var caseRemiReportGrid=new Ecp.CaseReportGridWidget(TXT.reportRemi, 'genRemiReport');
	caseRemiReportGrid.setToolBar(caseRemiReportToolBar);
	
	// Project Customization
	caseRemiReportGrid.customization(cmtTreeObj);
	
	// 3、display main panel
	Ecp.components.globalView.addModuleComp(caseRemiReportGrid.render());
	Ecp.components.globalView.render(TXT.report);

	// register service widgets
	Ecp.components.caseRemiReportGrid=caseRemiReportGrid;
};

Ecp.onReady(caseRemiReportLayout);