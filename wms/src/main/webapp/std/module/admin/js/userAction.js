var loadUserData=function()
{
	Ecp.components.userGrid.grid.selectFirstRow();
}

var showAddUserWin = function(btn, e) {
	var windowObj = {};
	if (typeof user_info_add_window_config != 'undefined')
		windowObj['config'] = user_info_add_window_config;
	if (typeof user_info_add_window_buttons != 'undefined')
		windowObj['buttons'] = user_info_add_window_buttons;

	// item's config
	// form
	windowObj['items']=[{},{}];
	if (typeof user_info_add_form_config != 'undefined')
		windowObj['items'][0]['config'] = user_info_add_form_config;
	if (typeof user_info_add_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = user_info_add_form_buttons;
	if (typeof user_info_add_form_items != 'undefined')
		windowObj['items'][0]['items'] = user_info_add_form_items;
	
	// store object in grid
	var cmtStoreObj = {};
	if (typeof user_info_add_form_dsConfig != 'undefined')
		cmtStoreObj['cmt_dsConfig'] = user_info_add_form_dsConfig;
	if (typeof user_info_add_form_dsEventConfig != 'undefined')
		cmtStoreObj['cmt_ds_eventConfig'] = user_info_add_form_dsEventConfig;
	windowObj['items'][1]['store'] = cmtStoreObj;

	// grid object in grid
    var cmtGridObj = {};
	if (typeof user_info_add_form_gridConfig != 'undefined')
		cmtGridObj['cmt_gridConfig'] = user_info_add_form_gridConfig;
	if (typeof user_info_add_form_gridCmConfig != 'undefined')
		cmtGridObj['cmt_cmConfig'] = user_info_add_form_gridCmConfig;
	if (typeof user_info_add_form_gridEventConfig != 'undefined')
		cmtGridObj['cmt_grid_eventConfig'] = user_info_add_form_gridEventConfig;
	windowObj['items'][1]['grid'] = cmtGridObj;
		
	// create singleton add user information window
	var win=Ecp.UserInfoWindow.createSingleAddWindow(windowObj,[Ecp.components.userGrid]);
	win.getItem(0).reset();
	win.getItem(0).setValues({isLock:false});
	win.getItem(0).setValues({receiveEmail:true});
	win.getItem(0).setValues({isEniUser:true});
	win.show();
	win.getItem(1).grid.getSelectionModel().reset();
}

var clickDeleteUserBtn = function(btn, e) {
	var id=Ecp.components.userGrid.grid.getSelectedId();
	if(!id)
		return;
	Ecp.MessageBox.confirm(TXT.common_is_deleted, function() {
			var params = {
				cmd :'user',
				action :'disable',
				id :id
			};
			//new style for using method of request
			Ecp.Ajax.request(params, function(result) {
				if (result.result == 'success')
					Ecp.components.userGrid.show();
			});
		});
}

var showEditUserWin = function(btn, e) {
	var data=Ecp.components.userGrid.grid.getSelectedRecord();
	if(data)
	{
		var windowObj = {};
		if (typeof user_info_edit_window_config != 'undefined')
			windowObj['config'] = user_info_edit_window_config;
		if (typeof user_info_edit_window_buttons != 'undefined')
			windowObj['buttons'] = user_info_edit_window_buttons;

		// item's config
		windowObj['items']=[{},{}];
		if (typeof user_info_edit_form_config != 'undefined')
			windowObj['items'][0]['config'] = user_info_edit_form_config;
		if (typeof user_info_edit_form_buttons != 'undefined')
			windowObj['items'][0]['buttons'] = user_info_edit_form_buttons;		
		if (typeof user_info_edit_form_items != 'undefined')
			windowObj['items'][0]['items'] = user_info_edit_form_items;
			
		// store object in grid
		var cmtStoreObj = {};
	    if (typeof user_info_edit_form_dsConfig != 'undefined')
		    cmtStoreObj['cmt_dsConfig'] = user_info_edit_form_dsConfig;
	    if (typeof user_info_edit_form_dsEventConfig != 'undefined')
		    cmtStoreObj['cmt_ds_eventConfig'] = user_info_edit_form_dsEventConfig;
		windowObj['items'][1]['store'] = cmtStoreObj;

	    // grid object in grid
		var cmtGridObj = {};
	    if (typeof user_info_edit_form_gridConfig != 'undefined')
		   cmtGridObj['cmt_gridConfig'] = user_info_edit_form_gridConfig;
	    if (typeof user_info_edit_form_gridCmConfig != 'undefined')
		   cmtGridObj['cmt_cmConfig'] = user_info_edit_form_gridCmConfig;
	    if (typeof user_info_edit_form_gridEventConfig != 'undefined')
		   cmtGridObj['cmt_grid_eventConfig'] = user_info_edit_form_gridEventConfig;
		windowObj['items'][1]['grid'] = cmtGridObj;
		
        var win = Ecp.UserInfoWindow.createSingleEditWindow(windowObj,[Ecp.components.userGrid]);
		win.getItem(1).grid.store.removeAll() ;
		win.getItem(0).reset();
	    win.show();
	   // win.getItem(1).grid.getSelectionModel().reset();
	    //modify by sunyue 20130131
	   // var params={
	//		cmd:'role',
		//	action:'find'
	 //   };	
	//    Ecp.Ajax.request(params,function(roleResult){
		var params={
				cmd:'user',
				action:'get',
				id:data['id']
		};
		Ecp.Ajax.request(params,function(result){
			win.getItem(0).setValues(result);
			win.getItem(0).setValues({confirmPwd:result.password});
			win.getItem(0).setValues({receiveEmail:result.receiveEmail});
			win.getItem(1).grid.store.loadData(result);
		});
	/*	Ecp.Ajax.request(params,function(result){
			win.getItem(0).setValues(result);
			win.getItem(0).setValues({confirmPwd:result.password});
			win.getItem(0).setValues({receiveEmail:result.receiveEmail});
			
			for ( var i = 0; i < roleResult.roles.length; i++) {
				var record = roleResult.roles[i];
				for ( var j = 0; j < result.roles.length; j++) {
					var role = result.roles[j];	
					if (record.id == role.id) {
						win.getItem(1).grid.getSelectionModel().selectRow(i,true);
					}	
				}	
			}
		});
	});*/
	}
}

var showQueryUserWin = function(btn, e) {
	var windowObj = {};
	if (typeof user_search_window_config != 'undefined')
		windowObj['config'] = user_search_window_config;
	if (typeof user_search_window_buttons != 'undefined')
		windowObj['buttons'] = user_search_window_buttons;

	// item's config
	windowObj['items']=[{}];
	if (typeof user_search_form_config != 'undefined')
		windowObj['items'][0]['config'] = user_search_form_config;
	if (typeof user_search_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = user_search_form_buttons;
	if (typeof user_search_form_items != 'undefined')
		windowObj['items'][0]['items'] = user_search_form_items;

	// window
	var userSearchWin = Ecp.UserInfoWindow.createSingleSearchWindow(windowObj);
	userSearchWin.getItem(0).reset();
	userSearchWin.show();
}

var clickSaveUserBtn = function(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);

	if (form.isValid()) {
		var values=form.getValues();
		
		if (values['id'] == ''){
			delete values['id'];
		}
		
		
		values['roleId'] = [];
		var roleRecord = win.getItem(1).getSelectedRecordDatas();
//		if(!roleRecord.length){delete values['roleId']};
		if (!roleRecord){
//		Ext.MessageBox.updateText(TXT.user_need_role);
		return;
		}
		
		for (i = 0; i < roleRecord.length; i++) {	
			values['roleId'][i] = roleRecord[i].id;
		}
		
		
		values['cmd'] = 'user';
		values['action'] = 'save';
		Ecp.Ajax.request(values, function(result) {
			if (result.result == 'success') {
				win.window.hide();
				win['ecpOwner'].notifyAll('reload');
			} else {				
				var errorJson={'username':TXT.user_need_username,
							   'firstname':TXT.user_need_firstname,
						       'lastname':TXT.user_need_lastname,
						       'password':TXT.user_need_password,
							   'internalCode':TXT.user_need_branch,
						       'roleId':TXT.user_need_role,
						       'emailExist':TXT.userEmail_exist,
							   'notExit':TXT.user_inst_not_exit,
						       'notValid':TXT.user_not_valid,
						       'exist':TXT.user_exist};
				Ecp.MessageBox.alert(errorJson[result.message]);
			}
		});
	}
}

var clickQueryUserBtn = function(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);
	if (form.isValid()) {
		var values = form.getValues();
		values['cmd']='user';
		values['action']='findByParam';
		Ecp.components.userGrid.search(values);
		win.window.hide();
	}
}


function callBack(treeRecord,treeWin){
	if(treeRecord!=null)
	{
		Ecp.components.userForm.setValue('internalCode', treeRecord.attributes.internalCode);
		Ecp.components.userForm.setValue('institution', treeRecord.attributes.name);
		treeWin.window.hide();
	}
	else
	{
		if(uid!='')
		{
		  treeWin.window.hide();
		  return;
		}
		Ext.MessageBox.alert(TXT.common_hint,TXT.case_institutionNeed);
		return false;
	}
}