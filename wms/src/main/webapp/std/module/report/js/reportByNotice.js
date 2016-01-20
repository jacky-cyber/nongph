/* add by jinlijun 20130128 */
Ecp.components.globalView=new Ecp.GlobalView('report');
function showReportByCaseType(){
	reportGridInitLayout(TXT.reportByNotice, fields4ReportByNotice, columns4ReportByNotice, "statisticsByNotice", showQueryWinowInNotice, clickExcelBtnInNotice);
}
Ecp.onReady(showReportByCaseType);
