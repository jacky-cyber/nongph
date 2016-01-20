var loadRoleData=function()
{
	Ecp.components.roleGrid.grid.selectFirstRow();
}


var showAddRoleWin=function(btn,e)
{
	var windowObj={};
	if(typeof role_info_add_window_config!='undefined')
		windowObj['config']=role_info_add_window_config;
	if(typeof role_info_add_window_buttons!='undefined')
		windowObj['buttons']=role_info_add_window_buttons;
		
	// item's config
	// form
	windowObj['items']=[{},{}];
	if(typeof role_info_add_form_config!='undefined')
		windowObj['items'][0]['config']=role_info_add_form_config;
	if(typeof role_info_add_form_buttons!='undefined')
		windowObj['items'][0]['buttons']=role_info_add_form_buttons;
	if(typeof role_info_add_form_items!='undefined')
		windowObj['items'][0]['items']=role_info_add_form_items;
	
	// store object in grid
	var cmtStoreObj = {};
	if (typeof role_info_add_form_dsConfig != 'undefined')
		cmtStoreObj['cmt_dsConfig'] = role_info_add_form_dsConfig;
	if (typeof role_info_add_form_dsEventConfig != 'undefined')
		cmtStoreObj['cmt_ds_eventConfig'] = role_info_add_form_dsEventConfig;
	windowObj['items'][1]['store'] = cmtStoreObj;
	
	// grid object in grid
	var cmtGridObj = {};
	if (typeof role_info_add_form_gridConfig != 'undefined')
		cmtGridObj['cmt_gridConfig'] = role_info_add_form_gridConfig;
	if (typeof role_info_add_form_gridCmConfig != 'undefined')
		cmtGridObj['cmt_cmConfig'] = role_info_add_form_gridCmConfig;
	if (typeof role_info_add_form_gridEventConfig != 'undefined')
		cmtGridObj['cmt_grid_eventConfig'] = role_info_add_form_gridEventConfig;
	windowObj['items'][1]['grid'] = cmtGridObj;
	
	// create singleton add role information window
	var win=Ecp.RoleInfoWindow.createSingleAddWindow(windowObj,[Ecp.components.roleGrid]);
	win.getItem(0).reset();
	win.show();
	win.getItem(1).grid.getSelectionModel().reset();
}

var clickDeleteRoleBtn=function(btn,e)
{
	// if user didn't select any recored then alert a messageBox
	var id=Ecp.components.roleGrid.grid.getSelectedId();
	if(!id)
		return;

	Ecp.MessageBox.confirm(TXT.common_is_deleted,function(){
		var params={
				cmd:'role',
				action:'delete',
				id:id
		};
		Ecp.Ajax.request(params,function(result){
			if(result.result=='success') {
				Ecp.components.roleGrid.show();
			} else if (result.result == 'failure') {
				var errorJson={'deleteError':TXT.role_deleteError};
				Ecp.MessageBox.alert(errorJson[result.message]);
			}
		});
	});
}

var showEditRoleWin=function(btn,e)
{
	// if user didn't select any recored then alert a messageBox	
	var data=Ecp.components.roleGrid.grid.getSelectedRecord();
	if(!data)
		return;

	var windowObj={};
		if (typeof role_info_edit_window_config != 'undefined')
		windowObj['config'] = role_info_edit_window_config;
	if (typeof role_info_edit_window_buttons != 'undefined')
		windowObj['buttons'] = role_info_edit_window_buttons;
	
	// item's config
	// form
	windowObj['items']=[{},{}];
	if (typeof role_info_edit_form_config != 'undefined')
		windowObj['items'][0]['config'] = role_info_edit_form_config;
	if (typeof role_info_edit_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = role_info_edit_form_buttons;
	if (typeof role_info_edit_form_items != 'undefined')
		windowObj['items'][0]['items'] = role_info_edit_form_items;

	// store object in grid
	var cmtStoreObj = {};
	if (typeof role_info_edit_form_dsConfig != 'undefined')
		cmtStoreObj['cmt_dsConfig'] = role_info_edit_form_dsConfig;
	if (typeof role_info_edit_form_dsEventConfig != 'undefined')
		cmtStoreObj['cmt_ds_eventConfig'] = role_info_edit_form_dsEventConfig;
	windowObj['items'][1]['store'] = cmtStoreObj;
		
	// grid object in grid
	var cmtGridObj = {};
	if (typeof role_info_edit_form_gridConfig != 'undefined')
		cmtGridObj['cmt_gridConfig'] = role_info_edit_form_gridConfig;
	if (typeof role_info_edit_form_gridCmConfig != 'undefined')
		cmtGridObj['cmt_cmConfig'] = role_info_edit_form_gridCmConfig;
	if (typeof role_info_edit_form_gridEventConfig != 'undefined')
		cmtGridObj['cmt_grid_eventConfig'] = role_info_edit_form_gridEventConfig;
	windowObj['items'][1]['grid'] = cmtGridObj;
		
	var win = Ecp.RoleInfoWindow.createSingleEditWindow(windowObj,[Ecp.components.roleGrid]);
	win.getItem(0).reset();
	win.show();
	win.getItem(1).grid.getSelectionModel().reset();
	
	var id = Ecp.components.roleGrid.grid.getSelectedId();
	win.getItem(0).setValue('uid', id);

	var params={
			cmd:'role',
			action:'findAllOperations'
	};	
	Ecp.Ajax.request(params,function(operResult){
		var params={
				cmd:'role',
				action:'get',
				id:id
		};
		Ecp.Ajax.request(params,function(result){
			win.getItem(0).setValues(result['roles'][0]);						
			
			for ( var i = 0; i < operResult.operations.length; i++) {
				var record = operResult.operations[i];
				for ( var j = 0; j < result['roles'][0].permisions.length; j++) {
					var role = result['roles'][0].permisions[j];	
					if (record.id == role.id) {
						win.getItem(1).grid.getSelectionModel().selectRow(i,true);
					}	
				}	
			}
		});
	});
}

// window's button 
var clickSaveRoleBtn=function(btn,e)
{
	var win=btn['ecpOwner'];
	var form=win.getItem(0);
	
	if (form.isValid()) {
		var values=form.getValues();
		if(values['uid'] == '') {
			delete values['uid'];
		}
		
		values['permisions'] = [];
		var operationRecord = win.getItem(1).getSelectedRecordDatas();
		if(!operationRecord)
		return;
		for (i = 0; i < operationRecord.length; i++) {	
			values['permisions'][i] = operationRecord[i].id;
		}

		values['cmd'] = 'role';
		values['action'] = 'save';
		Ecp.Ajax.request(values,function(result){
			if (result.result == 'success') {
				win.window.hide();
				win['ecpOwner'].notifyAll('reload');
			} else {				
				var errorJson={'roleName':TXT.role_need_rolename,
							   'permissionsEmpty':TXT.role_need_permission,
						       'roleNameExist':TXT.role_exist};
				Ecp.MessageBox.alert(errorJson[result.message]);
			}	
		});
	}
}