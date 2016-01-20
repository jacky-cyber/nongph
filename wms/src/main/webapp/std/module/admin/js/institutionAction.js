var showAddInstitWin = function(btn, e) {
	var data = Ecp.components.institutionGrid.treeGrid.getSelectedRecord();
	if (data != null) {
		var windowObj = {};
		if (typeof institution_info_add_window_config != 'undefined')
			windowObj['config'] = institution_info_add_window_config;
		if (typeof institution_info_add_window_buttons != 'undefined')
			windowObj['buttons'] = institution_info_add_window_buttons;

		// item's config
			
		windowObj['items']=[{}];
		if (typeof institution_info_add_form_config != 'undefined')
			windowObj['items'][0]['config'] = institution_info_add_form_config;
		if (typeof institution_info_add_form_buttons != 'undefined')
			windowObj['items'][0]['buttons'] = institution_info_add_form_buttons;
		if (typeof institution_info_add_form_items != 'undefined')
			windowObj['items'][0]['items'] = institution_info_add_form_items;

		// create singleton add institution information window
		var win = Ecp.InstitutionInfoWindow.createSingleAddWindow(windowObj, [Ecp.components.institutionGrid]);
		win.getItem(0).reset();
		
		win.getItem(0).findFieldById('bicCode').clearInvalid();
		var combo = win.getItem(0).findFieldById('sendToSwift');
		if (combo.getValue() == true) {
			combo.ownerCt.ownerCt.ecpOwner.setAllowBlank('bicCode', false);
		} else {
			combo.ownerCt.ownerCt.ecpOwner.setAllowBlank('bicCode', true);
		}

		win.getItem(0).setValues({'parent': data.name,
			                      'sendToSwift': false,
			                      'hasSubAccount': '1',
			                       'notSkip': true});
		win.show();
	}
}

var clickDeleteInstitBtn = function(btn, e) {
	//new style for select a record to get data
	var data=Ecp.components.institutionGrid.treeGrid.getSelectedRecord();
	if(data != null) {
		Ecp.MessageBox.confirm(TXT.common_is_deleted, function() {
			var params = {
				cmd :'institution',
				action :'delete',
				sid :data.id
			};
			//new style for using method of request
			Ecp.Ajax.request(params, function(result) {
				if (result.result == 'success')
					Ecp.components.institutionGrid.treeGrid.reload();
				else {
					var errorJson={'children':TXT.institution_not_delete,
							   'existCase':TXT.institution_existCase,
						       'existMessage':TXT.institution_existMessage,
						       'person' : TXT.institution_person,
						       'template' : TXT.institution_template};
					Ecp.MessageBox.alert(errorJson[result.message]);
						       
				}
			});
		});	
	}
}

var showEditInstitWin = function(btn, e) {
	//new style for select a record to get data
	var data = Ecp.components.institutionGrid.treeGrid.getSelectedRecord();
	if (data != null) {
		var windowObj = {};
		if (typeof institution_info_edit_window_config != 'undefined')
			windowObj['config'] = institution_info_edit_window_config;
		if (typeof institution_info_edit_window_buttons != 'undefined')
			windowObj['buttons'] = institution_info_edit_window_buttons;

		// item's config
		windowObj['items']=[{}];
		if (typeof institution_info_edit_form_config != 'undefined')
			windowObj['items'][0]['config'] = institution_info_edit_form_config;
		if (typeof institution_info_edit_form_buttons != 'undefined')
			windowObj['items'][0]['buttons'] = institution_info_edit_form_buttons;
		if (typeof institution_info_edit_form_items != 'undefined')
			windowObj['items'][0]['items'] = institution_info_edit_form_items;

		var params = {
			cmd :'institution',
			action :'findParentName',
			id:data.id
		};
		Ecp.Ajax.request(params, function(result) {
			// create singleton edit institution information window
			var win = Ecp.InstitutionInfoWindow.createSingleEditWindow(windowObj, [Ecp.components.institutionGrid]);
			
			win.getItem(0).reset();
			win.getItem(0).findFieldById('bicCode').clearInvalid();
			var combo = win.getItem(0).findFieldById('sendToSwift');
			if (combo.getValue() == true) {
				combo.ownerCt.ownerCt.ecpOwner.setAllowBlank('bicCode', false);
			} else {
				combo.ownerCt.ownerCt.ecpOwner.setAllowBlank('bicCode', true);
			}
			
			win.show();
			win.getItem(0).setValues(data);
			win.getItem(0).setValues({'parent' : result.parentName,
			                          'sid': data.id,
			                          'contactMan': data.contactMan});
			win.getItem(0).setReadOnly('internalCode', true);
		});
	}
}

var showQueryInstitWin = function(btn, e) {
	var windowObj = {};
	if (typeof institution_search_window_config != 'undefined')
		windowObj['config'] = institution_search_window_config;
	if (typeof institution_search_window_buttons != 'undefined')
		windowObj['buttons'] = institution_search_window_buttons;

	// item's config
	windowObj['items']=[{}];
	if (typeof institution_search_form_config != 'undefined')
		windowObj['items'][0]['config'] = institution_search_form_config;
	if (typeof institution_search_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = institution_search_form_buttons;
	if (typeof institution_search_form_items != 'undefined')
		windowObj['items'][0]['items'] = institution_search_form_items;

	// window
	var institutionSearchWin = Ecp.InstitutionInfoWindow.createSingleSearchWindow(windowObj);
	institutionSearchWin.getItem(0).reset();
	institutionSearchWin.show();
}

// window's button
var clickSaveInstitBtn = function(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);
	var values = form.getValues();

	if (form.isValid()) {
		var sid = values['sid'];
		var data = Ecp.components.institutionGrid.treeGrid.getSelectedRecord();
		values['parentId'] = data.id;
		values['cmd'] = 'institution';
		values['action'] = 'save';
		Ecp.Ajax.request(values, function(result) {
			if (result.result == 'success') {
				if(sid == '') {
					var baseParams={
						cmd:'institution',
						action:'find'
					};
					Ecp.components.institutionGrid.treeGrid.load(baseParams);	
				} else {
					Ecp.components.institutionGrid.treeGrid.reload();	
				}
			win.window.hide();
			} else {				
				var errorJson={'name':TXT.institution_name_exsit,
							   'internalCode':TXT.institution_code_exsit};
				Ecp.MessageBox.alert(errorJson[result.message]);
			}
		});
	}
}

var clickQueryInstitBtn = function(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);
	if (form.isValid()) {
		var values = form.getValues();
		var baseParams={
			cmd:'institution',
			action:'findByInternalCode',
			internalCode: values['internalCode']
		};
		Ecp.components.institutionGrid.treeGrid.load(baseParams);
		win.window.hide();
	}
}

/**
 * Show Common Institution Tree Window 
 */
function showInstitutionTreeWin(callBackFun,observers) {
	var windowObj = {};
	if (typeof institution_info_search_window_config != 'undefined')
		windowObj['config'] = institution_info_search_window_config;
	if (typeof institution_info_search_window_buttons != 'undefined')
		windowObj['buttons'] = institution_info_search_window_buttons;

	// item's config
	windowObj['items']=[{},{}];
	if (typeof institution_info_search_form_config != 'undefined')
		windowObj['items'][0]['config'] = institution_info_search_form_config;
	if (typeof institution_info_search_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = institution_info_search_form_buttons;
	if (typeof institution_info_search_form_items != 'undefined')
		windowObj['items'][0]['items'] = institution_info_search_form_items;
		
	// tree grid object
	if (typeof institution_info_search_form_treeColumn != 'undefined')
		windowObj['items'][1]['cmtTreeColumn'] = institution_info_search_form_treeColumn;
	if (typeof institution_info_search_form_treeConfig != 'undefined')
		windowObj['items'][1]['cmtConfig'] = institution_info_search_form_treeConfig;
	if (typeof institution_info_search_form_treeEvent != 'undefined')
		windowObj['items'][1]['cmtTreeEvent'] = institution_info_search_form_treeEvent;


	// create singleton add institution information window
	var win = Ecp.InternalCodeSearchWindow.createSingleSearchWindow(windowObj,callBackFun,observers);
	win.getItem(0).reset();
	win.show();
	
	// register the window in the global container
	Ecp.components.institutionSelectWin=win;
}

function showInstitutionTreeWithSubAccountWin(callBackFun,observers) {
	var windowObj = {};
	if (typeof institution_info_search_window_config != 'undefined')
		windowObj['config'] = institution_info_search_window_config;
	if (typeof institution_info_search_window_buttons != 'undefined')
		windowObj['buttons'] = institution_info_search_window_buttons;

	// item's config
	windowObj['items']=[{},{}];
	if (typeof institution_info_search_form_config != 'undefined')
		windowObj['items'][0]['config'] = institution_info_search_form_config;
	if (typeof institution_info_search_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = institution_info_search_form_buttons;
	if (typeof institution_info_search_form_items != 'undefined')
		windowObj['items'][0]['items'] = institution_info_search_form_items;
		
	// tree grid object
	if (typeof institution_info_search_form_treeColumn != 'undefined')
		windowObj['items'][1]['cmtTreeColumn'] = institution_info_search_form_treeColumn;
	if (typeof institution_info_search_form_treeConfig != 'undefined')
		windowObj['items'][1]['cmtConfig'] = institution_info_search_form_treeConfig;
	if (typeof institution_info_search_form_treeEvent != 'undefined')
		windowObj['items'][1]['cmtTreeEvent'] = institution_info_search_form_treeEvent;


	// create singleton add institution information window
	var win = Ecp.InternalCodeSearchWindow.createSingleSearchWithSubAccountWindow(windowObj,callBackFun,observers);
	win.getItem(0).reset();
	win.show();
	
	// register the window in the global container
	Ecp.components.institutionSelectWithSubAccountWin=win;
}

/**
 * Search Tree by Internal Code for common institution
 * @param {} btn
 */
var clickSearchInstitBtn = function(window,btn) {
	var win=window.window;
	var form = win.getItem(0);

	if (form.isValid()) {
		var baseParams;
		var internalCode=btn.getValue();
		if(internalCode == '') {
			if(window['gridBaseParams']==null)
				baseParams = {
					cmd: 'institution',
					action: 'find'
				};
			else
				baseParams=window['gridBaseParams'];
		} else {
			baseParams = {
				cmd: 'institution',
				action: 'findByInternalCode',
				internalCode: internalCode	
			}
		}
		win.getItem(1).load(baseParams);
	}
}