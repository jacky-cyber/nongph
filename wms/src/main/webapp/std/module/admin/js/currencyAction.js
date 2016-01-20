var loadCurrencyData=function()
{
	Ecp.components.currencyGrid.grid.selectFirstRow();
}

var showAddCurrencyWin = function(btn, e) {
	var windowObj = {};
	if (typeof currency_info_add_window_config != 'undefined')
		windowObj['config'] = currency_info_add_window_config;
	if (typeof currency_info_add_window_buttons != 'undefined')
		windowObj['buttons'] = currency_info_add_window_buttons;

	// item's config
	windowObj['items']=[{}];
	if (typeof currency_info_add_form_config != 'undefined')
		windowObj['items'][0]['config'] = currency_info_add_form_config;
	if (typeof currency_info_add_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = currency_info_add_form_buttons;
	if (typeof currency_info_add_form_items != 'undefined')
		windowObj['items'][0]['items'] = currency_info_add_form_items;
	
	// create singleton add currency information window
	var win=Ecp.CurrencyInfoWindow.createSingleAddWindow(windowObj,[Ecp.components.currencyGrid]);
	win.getItem(0).reset();
	win.show();
}

var clickDeleteCurrencyBtn = function(btn, e) {
	//new style for select a record to get data
	var id=Ecp.components.currencyGrid.grid.getSelectedId();
	if(!id)
		return;
	Ecp.MessageBox.confirm(TXT.cur_is_deleted, function() {
			var params = {
				cmd :'cur',
				action :'remove',
				id :id
			};
			//new style for using method of request
			Ecp.Ajax.request(params, function(
					result) {
				if (result.result == 'success')
					Ecp.components.currencyGrid.show();
			});
		});
}

var showEditCurrencyWin = function(btn, e) {
	//new style for select a record to get data
	var data=Ecp.components.currencyGrid.grid.getSelectedRecord();
	if(data)
	{
		var windowObj = {};
		if (typeof currency_info_edit_window_config != 'undefined')
			windowObj['config'] = currency_info_edit_window_config;
		if (typeof currency_info_edit_window_buttons != 'undefined')
			windowObj['buttons'] = currency_info_edit_window_buttons;

		// item's config
		windowObj['items']=[{}];
		if (typeof currency_info_edit_form_config != 'undefined')
			windowObj['items'][0]['config'] = currency_info_edit_form_config;
		if (typeof currency_info_edit_form_buttons != 'undefined')
			windowObj['items'][0]['buttons'] = currency_info_edit_form_buttons;
		if (typeof currency_info_edit_form_items != 'undefined')
			windowObj['items'][0]['items'] = currency_info_edit_form_items;

		var params = {
			cmd :'cur',
			action :'findByCurrencyCode',
			query :data['currencyCode']
		};
		Ecp.Ajax.request(params, function(result) {
			// create singleton edit currency information window
			var win = Ecp.CurrencyInfoWindow.createSingleEditWindow(windowObj,[Ecp.components.currencyGrid]);
			win.getItem(0).setReadOnly('currencyCode',true);
			win.show();
			win.getItem(0).setValues(result['currencies'][0]);
		});
	}
}

var showQueryCurrencyWin = function(btn, e) {
	var windowObj = {};
	if (typeof currency_search_window_config != 'undefined')
		windowObj['config'] = currency_search_window_config;
	if (typeof currency_search_window_buttons != 'undefined')
		windowObj['buttons'] = currency_search_window_buttons;

	// item's config
	windowObj['items']=[{}];
	if (typeof currency_search_form_config != 'undefined')
		windowObj['items'][0]['config'] = currency_search_form_config;
	if (typeof currency_search_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = currency_search_form_buttons;
	if (typeof currency_search_form_items != 'undefined')
		windowObj['items'][0]['items'] = currency_search_form_items;

	// window
	var currencySearchWin = Ecp.CurrencyInfoWindow.createSingleSearchWindow(windowObj);
	currencySearchWin.getItem(0).reset();
	currencySearchWin.show();
}

// window's button
var clickSaveCurrencyBtn = function(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);
	var values = form.getValues();

	if (form.isValid()) {
		values['cmd'] = 'cur';
		values['action'] = 'save';
		Ecp.Ajax.request(values, function(r) {
			win.window.hide();
			win['ecpOwner'].notifyAll('reload');
		});
	}
}

var clickQueryCurrencyBtn = function(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);
	if (form.isValid()) {
		var values = form.getValues();
		values['cmd']='cur';
		values['action']='findByCurrencyCode';
		Ecp.components.currencyGrid.search(values);
		win.window.hide();
	}
}