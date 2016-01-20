function showQueryCaseTraceReportWin(btn, e) {
	var windowObj = {};
	if (typeof caseTraceReport_info_search_window_config != 'undefined')
		windowObj['config'] = caseTraceReport_info_search_window_config;
	if (typeof caseTraceReport_info_search_window_buttons != 'undefined')
		windowObj['buttons'] = caseTraceReport_info_search_window_buttons;

	// item's config
	windowObj['items']=[{}];
	if (typeof caseTraceReport_info_search_form_config != 'undefined')
		windowObj['items'][0]['config'] = caseTraceReport_info_search_form_config;
	if (typeof caseTraceReport_info_search_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = caseTraceReport_info_search_form_buttons;
	if (typeof caseTraceReport_info_search_form_items != 'undefined')
		windowObj['items'][0]['items'] = caseTraceReport_info_search_form_items;
	else {
		windowObj['items'][0]['items'] = [
		{
			xtype: 'hidden',
			id: 'institutionId',
			name: 'institutionId'
		},
        {
        	xtype: 'datefield',
            fieldLabel: TXT.report_date_from,
            id: 'dateFrom',
            name: 'dateFrom',
            width: 150,
			format: 'Y-m-d',
			allowBlank: false,
			editable: false
        },
        {
        	xtype: 'datefield',
			fieldLabel : TXT.report_date_to,
			id : 'dateTo',
			name : 'dateTo',
			width: 150,
			format: 'Y-m-d',
			allowBlank : false,
			editable: false
		}, 
		{
			xtype: 'trigger',
			fieldLabel :TXT.branch,
			id: 'statistic',
			name: 'statistic',
			width: 150,
			onTriggerClick: function(){
    			showInstitutionTreeWin(callBackCaseTraceReport);
    		},
			triggerClass: 'x-form-search-trigger',
			editable: false
		}]		
	}
	
	// create singleton add case Type report information window
	var win = Ecp.CaseReportInfoWindow.createSingleSearchWindow(windowObj);
	win.getItem(0).reset();
	win.show();
	
	// register the window in the global container
	Ecp.components.caseTraceReportForm = win.getItem(0);
}

function callBackCaseTraceReport(treeRecord,treeWin){
	if(treeRecord!=null)
	{
		var caseTraceReportForm = Ecp.components.caseTraceReportForm;
		var institutionId = treeRecord.attributes.id;
		
		caseTraceReportForm.setValue('institutionId', institutionId);
		caseTraceReportForm.setValue('statistic', treeRecord.attributes.name);
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
		values['action'] = 'genTraceReport';
				
		Ecp.components.caseTraceReportGrid.treeGrid.load(values);
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
				
		self.location = DISPATCH_SERVLET_URL + '?cmd=excel&action=createExcelByTrace&type=d&institutionId=' + 
						values.institutionId +
						'&dateFrom=' + values.dateFrom +
						'&dateTo=' + values.dateTo + '&title=titTrace';
	}
}