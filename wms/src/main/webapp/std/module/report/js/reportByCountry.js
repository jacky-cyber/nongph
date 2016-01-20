/* add by jinlijun 20130128 */
Ecp.components.globalView=new Ecp.GlobalView('report');
function showReportByCountry(){
	reportGridInitLayout(TXT.reportByCountry, fields4ReportByCountry, columns4ReportByCountry, "statisticsByCountry", showQueryWinowInCountry, clickExcelBtnInCountry);
}
Ecp.onReady(showReportByCountry);
