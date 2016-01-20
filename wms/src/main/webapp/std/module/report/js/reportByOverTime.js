//add by jinlijun 2013/1/31
Ecp.components.globalView = new Ecp.GlobalView("report");
function showReportByOverTime() {
	reportTreeGridInitLayout(TXT.reportByOverTime, column4ReportByOverTime, "statisticsByOverTime", showQueryWinowInOverTime, clickExcelBtnInOverTime);
}
Ecp.onReady(showReportByOverTime);

