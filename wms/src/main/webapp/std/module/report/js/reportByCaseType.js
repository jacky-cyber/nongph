/* add by jinlijun 20130128 */
Ecp.components.globalView=new Ecp.GlobalView('report');
function showReportByCaseType(){
	reportGridInitLayout(TXT.reportByCaseType, fields4ReportByCaseType, columns4ReportByCaseType, "statisticsByCaseType", showQueryWinowInCaseType, clickExcelBtnInCaseType);
}
Ecp.onReady(showReportByCaseType);
