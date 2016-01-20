
/* add by jinlijun 2013/1/28 */
var showQueryReportWinown = function (formicAction, needRemittance, onlyYear) {
	var reportQueryWin = Ecp.CaseReportQueryFormWindow.createWindow(formicAction, needRemittance, onlyYear);
	reportQueryWin.getItem(0).reset();
	reportQueryWin.show();
	Ecp.components.reportQueryWin = reportQueryWin;
	Ecp.components.showInstitutionForm = reportQueryWin.getItem(0);
};

var showInstitutionTree = function (treeRecord, treeWin) {
	if (treeRecord != null) {
		var showInstitutionForm = Ecp.components.showInstitutionForm;
		var institutionId = treeRecord.attributes.id;
		showInstitutionForm.setValue("institutionId", institutionId);
		showInstitutionForm.setValue("statistic", treeRecord.attributes.name);
		treeWin.window.hide();
	} else {
		Ecp.MessageBox.alert(TXT.template_institution_need);
		return false;
	}
};

var clickStatisticBtn = function (formicAction) {
	var reportQueryWin = Ecp.components.reportQueryWin;
	var form = reportQueryWin.getItem(0);
	if (form.isValid()) {
		var values = form.getValues();
		if (values.dateTo < values.dateFrom) {
			Ecp.MessageBox.alert(TXT.report_can_not_select_date);
			return;
		}
		values["cmd"] = "statisticsReport";
		values["action"] = formicAction;
		values["requestFrom"] = "form";
		Ecp.components.reportParam = values;
		Ecp.components.reportGrid.statistic(values);
		reportQueryWin.window.hide();
	}
};

var clickExcelBtn = function(action) {
	var values = Ecp.components.reportParam;
	if (!values) {
		return;
	}
	if (values.dateTo < values.dateFrom) {
		Ecp.MessageBox.alert(TXT.report_can_not_select_date);
		return;
	}
	self.location = DISPATCH_SERVLET_URL + "?cmd=exportReport&action=" + action 
					+ "&type=d&institutionId=" + values.institutionId + "&dateFrom=" + values.dateFrom 
					+ "&dateTo=" + values.dateTo + "&remittance=" + values.remittance;
};

