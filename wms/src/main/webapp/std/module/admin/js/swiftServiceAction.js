var loadSwiftServiceData=function()
{
	Ecp.components.swiftServiceGrid.grid.selectFirstRow();
}

var showAddSwiftServiceWin=function(btn,e) {
	var windowObj={};
	if(typeof swift_service_info_add_window_config!='undefined')
		windowObj['config']=swift_service_info_add_window_config;
	if(typeof swift_service_info_add_window_buttons!='undefined')
		windowObj['buttons']=swift_service_info_add_window_buttons;
	
	// item's config
	windowObj['items']=[{}];
	if (typeof swift_service_add_form_config != 'undefined')
		windowObj['items'][0]['config'] = swift_service_add_form_config;
	if (typeof swift_service_add_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = swift_service_add_form_buttons;
	if (typeof swift_service_add_form_items != 'undefined')
		windowObj['items'][0]['items'] = swift_service_add_form_items;
	
	var win=Ecp.SwiftServiceWindow.createSingleAddWindow(windowObj,[Ecp.components.swiftServiceGrid]);
	win.getItem(0).reset();
	win.show();
}

var showDeleteSwiftServiceWin=function(btn,e) {
	// if user didn't select any recored then alert a messageBox
	var id=Ecp.components.swiftServiceGrid.grid.getSelectedId();
	if(!id)
		return;
		
	Ecp.MessageBox.confirm(TXT.swift_is_deleted,function(){
		var id=Ecp.components.swiftServiceGrid.grid.getSelectedId();
		var params={
				cmd:'swiftService',
				action:'delete',
				id:id
		};
		Ecp.Ajax.request(params,function(result){
			if(result.result=='success')
				Ecp.components.swiftServiceGrid.show();
		});
	});
}

var showEditSwiftServiceWin=function(grid,btn,e) {
	// if user didn't select any recored then alert a messageBox
	var data=Ecp.components.swiftServiceGrid.grid.getSelectedRecord();
	if(data) {
		var windowObj={};
		if(typeof swift_service_edit_window_config!='undefined')
			windowObj['config']=swift_service_edit_window_config;
		if(typeof swift_service_edit_window_buttons!='undefined')
			windowObj['buttons']=swift_service_edit_window_buttons;

		// item's config
		if(typeof swift_service_edit_form_config!='undefined')
			windowObj['formConfig']=swift_service_edit_form_config;
		if(typeof swift_service_edit_form_buttons!='undefined')
			windowObj['formButtons']=swift_service_edit_form_buttons;
		if(typeof swift_service_edit_form_items!='undefined')
			windowObj['formItems']=swift_service_edit_form_items;
		
		var record=Ecp.components.swiftServiceGrid.grid.getSelectedRecord();
		
		var win = Ecp.SwiftServiceWindow.createSingleEditWindow(windowObj,[Ecp.components.swiftServiceGrid]);
		win.show();
		win.getItem(0).setValues(record);
	}
}

var showQuerySwiftServiceWin=function(btn,e) {
	var windowObj={};
	if(typeof swift_service_search_window_config!='undefined')
		windowObj['config']=swift_service_search_window_config;
	if(typeof swift_service_search_window_buttons!='undefined')
		windowObj['buttons']=swift_service_search_window_buttons;
	
	// item's config
	if(typeof swift_service_search_form_config!='undefined')
		windowObj['formConfig']=swift_service_search_form_config;
	if(typeof swift_service_search_form_buttons!='undefined')
		windowObj['formButtons']=swift_service_search_form_buttons;
	if(typeof swift_service_search_form_items!='undefined')
		windowObj['formItems']=swift_service_search_form_items;
	
	// window
	var swiftServiceSearchWin = Ecp.SwiftServiceSearchWindow.createSingleSearchWindow(windowObj);
	swiftServiceSearchWin.getItem(0).reset();
	swiftServiceSearchWin.show();
}

var showImportSwiftServiceWin=function(btn,e) {
	var dialog = new Ext.ux.UploadDialog.Dialog({
		url: DISPATCH_SERVLET_URL+'?cmd=swiftService&action=importSwiftService&type=y',
		reset_on_hide: false,
		allow_close_on_upload: true,
		upload_autostart: false,
		permitted_extensions: ['csv']
	});
	dialog.on('uploadsuccess',function(){
		Ecp.components.swiftServiceGrid.show();
	});
	dialog.show('show-button');
}

//window's button 
var clickSaveSwiftServiceBtn=function(btn,e)
{
	var win=btn['ecpOwner'];
	var form=win.getItem(0);
	var values=form.getValues();
		
	if (form.isValid()) {
		values['cmd']='swiftService';
		values['action']='save';
		Ecp.Ajax.request(values,function(r){
			win.window.hide();
			form.reset();
			win['ecpOwner'].notifyAll('reload');
		});
	}
}

var clickQuerySwiftServiceBtn=function(btn,e) {
	var win=btn['ecpOwner'];
	var form=win.getItem(0);
	if (form.isValid()) {
		var values=form.getValues();
		values['cmd']='swiftService';
		values['action']='dimSearchByBic';
		Ecp.components.swiftServiceGrid.search(values);
		win.window.hide();
		form.reset();
	}
}