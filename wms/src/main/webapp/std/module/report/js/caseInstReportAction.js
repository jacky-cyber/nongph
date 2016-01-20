function showQueryCaseInstReportWin(btn, e) {
	var windowObj = {};
	if (typeof caseInstReport_info_search_window_config != 'undefined')
		windowObj['config'] = caseInstReport_info_search_window_config;
	if (typeof caseInstReport_info_search_window_buttons != 'undefined')
		windowObj['buttons'] = caseInstReport_info_search_window_buttons;

	// item's config
	windowObj['items']=[{}];
	if (typeof caseInstReport_info_search_form_config != 'undefined')
		windowObj['items'][0]['config'] = caseInstReport_info_search_form_config;
	if (typeof caseInstReport_info_search_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = caseInstReport_info_search_form_buttons;
	if (typeof caseInstReport_info_search_form_items != 'undefined')
		windowObj['items'][0]['items'] = caseInstReport_info_search_form_items;

	// create singleton add case institution report information window
	var win = Ecp.CaseReportInfoWindow.createSingleSearchWindow(windowObj);
	win.getItem(0).reset();
	win.show();
	
	// register the window in the global container
	Ecp.components.caseInstReportForm = win.getItem(0);
}

function callBackCaseInstReport(treeRecord,treeWin){
	if(treeRecord!=null)
	{
		var caseInstReportForm = Ecp.components.caseInstReportForm;
		var institutionId = treeRecord.attributes.id;
		
		caseInstReportForm.setValue('institutionId', institutionId);
		caseInstReportForm.setValue('statistic', treeRecord.attributes.name);
		treeWin.window.hide();
	} else {
		Ecp.MessageBox.alert(TXT.template_institution_need);
		return false;
	}
}

function clickStatisticCaseBtn(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);

	if (form.isValid()) {
		Ecp.showWaitingWin();
		var values = form.getValues();

		if(values.dateTo < values.dateFrom){
			Ecp.MessageBox.alert(TXT.report_can_not_select_date);
			return;
		}
		
		values['cmd'] = 'report';
		values['action'] = 'genInstReport';
				
		Ecp.components.caseInstReportGrid.treeGrid.load(values);
		win.window.hide();
	}
}

function clickExcelCaseBtn(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);

	if (form.isValid()) {
		var values = form.getValues();
		
		if(values.dateTo < values.dateFrom){
			Ecp.MessageBox.alert(TXT.report_can_not_select_date);
			return;
		}

		var size = 0;
		if (values.status != false)
			size++;
		if (values.caseType != false)
			size++;
		if (values.remittance != false)
			size++;

		if (size == 0) {
			Ecp.MessageBox.alert(TXT.report_select);
			return;
		} else if (size > 2) {
			Ecp.MessageBox.alert(TXT.report_select_no_more);
			return;
		}
				
		self.location = DISPATCH_SERVLET_URL + '?cmd=excel&action=createExcelByInst&type=d&institutionId=' +
						values.institutionId + '&status=' +
						values.status + '&caseType=' + values.caseType +
						'&remittance=' + values.remittance +
						'&dateFrom=' + values.dateFrom +
						'&dateTo=' + values.dateTo + '&title=titInst';
	}
}