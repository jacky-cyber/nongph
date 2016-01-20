/* add by jinlijun 20130128 */
Ecp.components.globalView=new Ecp.GlobalView('report');
function showReportByYear(){
	reportGridInitLayout(TXT.reportByYear, fields4ReportByYear, columns4ReportByYear, "statisticsByYear", showQueryWinowInYear, clickExcelBtnInYear);
}
Ecp.onReady(showReportByYear);
