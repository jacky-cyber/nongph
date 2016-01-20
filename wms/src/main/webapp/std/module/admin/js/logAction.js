var loadLogData=function()
{
	Ecp.components.logGrid.grid.selectFirstRow();
}

var showQueryLogWin=function(btn, e) {
	var windowObj={};
	if(typeof log_search_window_config!='undefined')
		windowObj['config']=log_search_window_config;
	if(typeof log_search_window_buttons!='undefined')
		windowObj['buttons']=log_search_window_buttons;
	
	// item's config
	windowObj['items']=[{}];
	if (typeof log_search_form_config != 'undefined')
		windowObj['items'][0]['config'] = log_search_form_config;
	if (typeof log_search_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = log_search_form_buttons;
	if (typeof log_search_form_items != 'undefined')
		windowObj['items'][0]['items'] = log_search_form_items;
	
	// window
	var logSearchWin = Ecp.LogSearchWindow.createSingleSearchWindow(windowObj);
	logSearchWin.getItem(0).reset();
	logSearchWin.show();
}

var clickQueryLogBtn=function(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);
	if (form.isValid()) {
		var values = form.getValues();
		values["cmd"]="log";
		values["action"]="find";
		values["json"]=Ext.util.JSON.encode(values);
		
		Ecp.components.logGrid.search(values);
		win.window.hide();
	}
}