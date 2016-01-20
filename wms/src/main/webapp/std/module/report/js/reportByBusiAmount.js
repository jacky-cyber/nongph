//add by jinlijun 2013/1/31
Ecp.components.globalView=new Ecp.GlobalView('report');
function showReportByBusiAmount(){
	reportTreeGridInitLayout(TXT.reportByBusiAmount, column4ReportByBusiAmount, "statisticsByBusiAmount", showQueryWinowInBusiAmount, clickExcelBtnInBusiAmount);
}
Ecp.onReady(showReportByBusiAmount);