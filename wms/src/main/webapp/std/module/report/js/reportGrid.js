
/* add by jinlijun 20130128 */
var reportGridInitLayout = function (title, fields, columns, action, showQueryWindow, exportExcelHandler) {
	var reportGrid = new Ecp.ReportGridWidget(title, fields, columns, action);
	var reportGridToolbar = new Ecp.ReportGridToolbar();
	reportGridToolbar.setWidgetEvent({queryCaseReport:showQueryWindow, exportCaseReport:exportExcelHandler});
	reportGrid.setToolBar(reportGridToolbar.render());
	Ecp.components.globalView.addModuleComp(reportGrid.render());
	Ecp.components.globalView.render(TXT.reportByCaseType);
	reportGrid.show();
	Ecp.components.reportGridToolbar = reportGridToolbar;
	Ecp.components.reportGrid = reportGrid;
};

