/* add by jinlijun 20130128 */
Ecp.components.globalView=new Ecp.GlobalView('report');
function showReportByUser(){
	reportGridInitLayout(TXT.reportByUser, fields4ReportByUser, columns4ReportByUser, "statisticsByUser", showQueryWinowInUser, clickExcelBtnInUser);
}
Ecp.onReady(showReportByUser);
