function showQueryCaseStatusReportWin(btn, e) {
	var windowObj = {};
	if (typeof caseStatusReport_info_search_window_config != 'undefined')
		windowObj['config'] = caseStatusReport_info_search_window_config;
	if (typeof caseStatusReport_info_search_window_buttons != 'undefined')
		windowObj['buttons'] = caseStatusReport_info_search_window_buttons;

	// item's config
	windowObj['items']=[{}];
	if (typeof caseStatusReport_info_search_form_config != 'undefined')
		windowObj['items'][0]['config'] = caseStatusReport_info_search_form_config;
	if (typeof caseStatusReport_info_search_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = caseStatusReport_info_search_form_buttons;
	if (typeof caseStatusReport_info_search_form_items != 'undefined')
		windowObj['items'][0]['items'] = caseStatusReport_info_search_form_items;
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
        	xtype: 'checkbox',
        	fieldLabel :TXT.report_summary,
            boxLabel: TXT.report_type,
            id :'caseType',
			name :'caseType'
        },
        {
        	xtype :'checkbox',
            boxLabel: TXT.report_remittance,
            id :'remittance',
			name :'remittance',
			labelSeparator :''                
		},	
		{
			xtype :'checkbox',
			boxLabel :TXT.branch,
			id :'institution',
			name :'institution',
			labelSeparator :'',
			listeners : {
				check : function(checkbox, checked) {
					if (checked == true) {
						checkbox.ownerCt.ecpOwner.findFieldById('statistic').setVisible(true);
					} else
						checkbox.ownerCt.ecpOwner.findFieldById('statistic').setVisible(false);
				}
			}
		},
		{
			xtype: 'trigger',
			id: 'statistic',
			name: 'statistic',
			labelSeparator :'',
			width: 150,
			onTriggerClick: function(){
    			showInstitutionTreeWin(callBackCaseStatusReport);
    		},
			triggerClass: 'x-form-search-trigger',
			editable: false,
			hidden :true
		}]		
	}
	
	// create singleton add case status report information window
	var win = Ecp.CaseReportInfoWindow.createSingleSearchWindow(windowObj);
	win.getItem(0).reset();
	win.show();
	
	// register the window in the global container
	Ecp.components.caseStatusReportForm = win.getItem(0);
}

function callBackCaseStatusReport(treeRecord,treeWin){
	if(treeRecord!=null)
	{
		var caseStatusReportForm = Ecp.components.caseStatusReportForm;
		var institutionId = treeRecord.attributes.id;
		
		caseStatusReportForm.setValue('institutionId', institutionId);
		caseStatusReportForm.setValue('statistic', treeRecord.attributes.name);
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
		values['action'] = 'genStatusReport';
				
		Ecp.components.caseStatusReportGrid.treeGrid.load(values);
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
		if (values.institution != false)
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
				
		self.location = DISPATCH_SERVLET_URL + '?cmd=excel&action=createExcelByStatus&type=d&institution=' +
						values.institution + '&institutionId=' + values.institutionId +
						'&caseType=' + values.caseType +
						'&remittance=' + values.remittance +
						'&dateFrom=' + values.dateFrom +
						'&dateTo=' + values.dateTo + '&title=titStatus';
	}
}