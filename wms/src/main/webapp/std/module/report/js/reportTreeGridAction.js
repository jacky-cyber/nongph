//add by jinlijun 2013/1/31
var showQueryReportWinown = function(action, needRemittance) {
	var windowObj = {};
	if (typeof caseInstReport_info_search_window_config != "undefined") {
		windowObj["config"] = caseInstReport_info_search_window_config;
	}
	if (typeof caseInstReport_info_search_window_buttons != "undefined") {
		windowObj["buttons"] = caseInstReport_info_search_window_buttons;
	}

	windowObj["items"] = [{}];
	if (typeof caseInstReport_info_search_form_config != "undefined") {
		windowObj["items"][0]["config"] = caseInstReport_info_search_form_config;
	}
	if (typeof caseInstReport_info_search_form_buttons != "undefined") {
		windowObj["items"][0]["buttons"] = caseInstReport_info_search_form_buttons;
	}
	if (typeof caseInstReport_info_search_form_items != "undefined") {
		windowObj["items"][0]["items"] = caseInstReport_info_search_form_items;
	}

	var win = Ecp.CaseReportInfoWindow.createSingleSearchWindow(windowObj, action, needRemittance);
	win.getItem(0).reset();
	win.show();
	Ecp.components.caseInstReportForm = win.getItem(0);
};
var showInstitutionTree = function(treeRecord, treeWin) {
	if (treeRecord != null) {
		var caseInstReportForm = Ecp.components.caseInstReportForm;
		var institutionId = treeRecord.attributes.id;
		caseInstReportForm.setValue("institutionId", institutionId);
		caseInstReportForm.setValue("statistic", treeRecord.attributes.name);
		treeWin.window.hide();
	} else {
		Ecp.MessageBox.alert(TXT.template_institution_need);
		return false;
	}
};
var clickStatisticBtn = function(btn, action) {
	var win = btn["ecpOwner"];
	var form = win.getItem(0);
	if (form.isValid()) {
		Ecp.showWaitingWin();
		var values = form.getValues();
		if (values.dateTo < values.dateFrom) {
			Ecp.MessageBox.alert(TXT.report_can_not_select_date);
			return;
		}
		values["cmd"] = "statisticsReport";
		values["action"] = action;
		values["requestFrom"] = "form";
		Ecp.components.reportParam = values;
		Ecp.components.caseTraceReportGrid.treeGrid.load(values);
		win.window.hide();
	}
};
var clickExcelBtn = function(action) {
	var values = Ecp.components.reportParam;
	if (values.dateTo < values.dateFrom) {
		Ecp.MessageBox.alert(TXT.report_can_not_select_date);
		return;
	}
	self.location = DISPATCH_SERVLET_URL + "?cmd=exportReport&action=" + action + "&type=d&institutionId=" 
					+ values.institutionId + "&remittance=" + values.remittance 
					+ "&dateFrom=" + values.dateFrom + "&dateTo=" + values.dateTo;
};

