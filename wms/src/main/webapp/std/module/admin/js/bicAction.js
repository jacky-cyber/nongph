var loadBicData=function()
{
	Ecp.components.bicGrid.grid.selectFirstRow();
}

var showImportBicWin=function(btn,e)
{
	var dialog = new Ext.ux.UploadDialog.Dialog({
			url: DISPATCH_SERVLET_URL+'?cmd=bic&action=importBanks&ulf=y&type=y',
			reset_on_hide: false,
			allow_close_on_upload: true,
			upload_autostart: false,
			permitted_extensions: ['txt','TXT']
	});
	dialog.on('uploadsuccess',function(){
		Ecp.components.bicGrid.show();
		});
	dialog.show('show-button');
}

var showAddBicWin=function(btn,e)
{
		var windowObj={};
		if(typeof bic_info_add_window_config!='undefined')
			windowObj['config']=bic_info_add_window_config;
		if(typeof bic_info_add_window_buttons!='undefined')
			windowObj['buttons']=bic_info_add_window_buttons;
		
		// item's config
		windowObj['items']=[{}];
		if(typeof bic_info_add_form_config!='undefined')
			windowObj['items'][0]['config']=bic_info_add_form_config;
		if(typeof bic_info_add_form_buttons!='undefined')
			windowObj['items'][0]['buttons']=bic_info_add_form_buttons;
		if(typeof bic_info_add_form_items!='undefined')
			windowObj['items'][0]['items']=bic_info_add_form_items;
	
		// create singleton add bic information window
	    var win=Ecp.BicInfoWindow.createSingleAddWindow(windowObj,[Ecp.components.bicGrid]);
	    win.getItem(0).reset();
        win.getItem(0).setValues({type:'0'});
	    win.show();	
	    
}

var showEditBicWin=function(btn,e) {
	// if user didn't select any recored then alert a messageBox
	var data=Ecp.components.bicGrid.grid.getSelectedRecord();
	if(data)
    {
		var windowObj={};
		if(typeof bic_info_edit_window_config!='undefined')
			windowObj['config']=bic_info_edit_window_config;
		if(typeof bic_info_edit_window_buttons!='undefined')
			windowObj['buttons']=bic_info_edit_window_buttons;
		
		// item's config
		windowObj['items']=[{}];
		if(typeof bic_info_edit_form_config!='undefined')
			windowObj['items'][0]['config']=bic_info_edit_form_config;
		if(typeof bic_info_edit_form_buttons!='undefined')
			windowObj['items'][0]['buttons']=bic_info_edit_form_buttons;
		if(typeof bic_info_edit_form_items!='undefined')
			windowObj['items'][0]['items']=bic_info_edit_form_items;
        else{
		}
		
	//var bicCode=Ecp.components.bicGrid.grid.getSelectedRecord()['bic'];
	var params={
			cmd:'bic',
			action:'findByCode',
			query:data['bic']
	};
	Ecp.Ajax.request(params,function(result){
		var win = Ecp.BicInfoWindow.createSingleEditWindow(windowObj,[Ecp.components.bicGrid]);
		win.getItem(0).setValues({type:'0'});
		win.show();
		win.getItem(0).setValues(result['bics'][0]);
	});
	}
}

var showQueryBicWin=function(btn,e)
{
		var windowObj={};
		if(typeof bic_search_window_config!='undefined')
			windowObj['config']=bic_search_window_config;
		if(typeof bic_search_window_buttons!='undefined')
			windowObj['buttons']=bic_search_window_buttons;
		
		// item's config
		windowObj['items']=[{}];
		if(typeof bic_search_form_config!='undefined')
			windowObj['items'][0]['config']=bic_search_form_config;
		if(typeof bic_search_form_buttons!='undefined')
			windowObj['items'][0]['buttons']=bic_search_form_buttons;
		if(typeof bic_search_form_items!='undefined')
			windowObj['items'][0]['items']=bic_search_form_items;
		
		// window
	    var bicSearchWin = Ecp.BicInfoWindow.createSingleSearchWindow(windowObj);
	    bicSearchWin.getItem(0).reset();
	    bicSearchWin.show();
}

var clickSaveBicBtn=function(btn,e)
{
	var win=btn['ecpOwner'];
	var form=win.getItem(0);
	var values=form.getValues();
		
	if (form.isValid()) {
		values['cmd']='bic';
		values['action']='save';
		Ecp.Ajax.request(values,function(r){
			if (r.result == 'success'){
			win.window.hide();
			win['ecpOwner'].notifyAll('reload');
			}else{
				var errorJson={'biclen':TXT.bic_code_invalid,
						'hqBiclen':TXT.hq_bic_code_invalid,
						'branchBiclen':TXT.branch_bic_code_invalid,
						'namelen':TXT.bic_name_invalid,
						'branchNamelen':TXT.bic_branch_name_invalid,
						'countrylen':TXT.bic_country_invalid,
						'locationlen':TXT.bic_location_invalid,
						'exist':TXT.bic_exist};
				Ecp.MessageBox.alert(errorJson[r.message]);
			}
		});
	}
}

var clickQueryBicBtn=function(btn,e)
{
	var win=btn['ecpOwner'];
	var form=win.getItem(0);
	if (form.isValid()) {
		var values=form.getValues();
		values['cmd']='bic';
		values['action']='search';
		Ecp.components.bicGrid.search(values);
		win.window.hide();
	}
}