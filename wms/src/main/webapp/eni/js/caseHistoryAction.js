function showBackUpSearchWin(){
	var windowObj={};
	if(typeof case_resume_query_window_config!='undefined')
		windowObj['config']=case_resume_query_window_config;
	if(typeof case_resume_query_window_buttons!='undefined')
		windowObj['buttons']=case_resume_query_window_buttons;

	// item's config
	windowObj['items']=[{}];
	if (typeof case_resume_search_form_config != 'undefined')
		windowObj['items'][0]['config'] = case_resume_search_form_config;
	if (typeof case_resume_search_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = case_resume_search_form_buttons;
	if (typeof case_resume_search_form_items != 'undefined')
		windowObj['items'][0]['items'] = case_resume_search_form_items;
	
	// window
	var caseSearchWin = Ecp.CaseSearchWindow.createSingleResumeSearchWindow(windowObj);
	caseSearchWin.getItem(0).reset();
	caseSearchWin.show();
}

function searchBackupedCases(btn,e){
	var win = btn['ecpOwner'];
	var form = win.getItem(0);
	if (form.isValid()) {
		var values = form.getValues();
		values['amountFrom']=(values['amountFrom']==''||values['amountFrom']==null?0:values['amountFrom']);
		values['amountTo']=(values['amountTo']==''||values['amountTo']==null?0:values['amountTo']);
		values['spendTime']=(values['spendTime']==''||values['spendTime']==null?0:values['spendTime']);
		var params={};
		params['cmd']='caseBackup';
		params['action']='find';
		params['json']=Ext.util.JSON.encode(values);
		//alert(params['cmd']);
		Ecp.components.caseGrid.search(params);
		win.window.hide();
	}
}

function resumeCase(){
	var id=Ecp.components.caseGrid.grid.getSelectedId();
	if(!id)
		return;
		var a=Ecp.components.caseGrid.grid.getSelected();
	Ecp.MessageBox.confirm(TXT.case_resume_confirm1+Ecp.components.caseGrid.grid.getSelected()['data']['internalCode']+TXT.case_resume_confirm2, function() {
			Ecp.Ajax.request({cmd:'backup',action:'resume',id:id},function(result){
				if(result.result == 'success')
					//Ecp.MessageBox.alert(TXT.case_resume_success);
					Ecp.components.caseGrid.reloadUpdate();
				});
		});
	
}