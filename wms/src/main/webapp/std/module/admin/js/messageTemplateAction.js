var loadMessageTemplateData=function()
{
	Ecp.components.messageTempGrid.grid.selectFirstRow();
}

var showEditMessageTempWin=function(btn,e)
{
	// if user didn't select any recored then alert a messageBox	
	var data=Ecp.components.messageTempGrid.grid.getSelectedRecord();
	if(!data)
		return;

	var windowObj={};
		if (typeof messageTemplate_info_edit_window_config != 'undefined')
		windowObj['config'] = messageTemplate_info_edit_window_config;
	if (typeof messageTemplate_info_edit_window_buttons != 'undefined')
		windowObj['buttons'] = messageTemplate_info_edit_window_buttons;

	// item's config
	// form
	windowObj['items']=[{}];
	if (typeof messageTemplate_info_edit_form_config != 'undefined')
		windowObj['items'][0]['config'] = messageTemplate_info_edit_form_config;
	if (typeof messageTemplate_info_edit_form_buttons != 'undefined')
		windowObj['items'][0]['buttons'] = messageTemplate_info_edit_form_buttons;
	if (typeof messageTemplate_info_edit_form_items != 'undefined')
		windowObj['items'][0]['items'] = messageTemplate_info_edit_form_items;
	
	var win=Ecp.MessageTemplateInfoWindow.createSingleEditWindow(windowObj,[Ecp.components.messageTempGrid]);
//	win.getItem(0).reset();
//	win.getItem(1).root.reload();
	win.show();

	var listValues = {};
	
	var id = Ecp.components.messageTempGrid.grid.getSelectedId();
	listValues['id'] = id;

	listValues['type'] = 'list';
	listValues['cmd'] = 'messageTemplate';
	listValues['action'] = 'findInst';

	Ecp.Ajax.request(listValues, function(result) {
		for (var i = 0; i < result.results.length; i++) {
			Ecp.components.messageTempGrid.ids[i] = result.results[i].id;
		}
	});

	var params={
		cmd:'messageTemplate',
		action:'get',
		id:id
	};
	
	Ecp.Ajax.request(params,function(result){
		win.getItem(0).setValues(result['templates'][0]);	
	});
}

// window's button 
var clickSaveAllMessageTempBtn=function(btn,e)
{
	var win=btn['ecpOwner'];
	var form=win.getItem(0);
	
	if (form.isValid()) {
		var values=form.getValues();
		
		var id=Ecp.components.messageTempGrid.grid.getSelectedId();
		values['uid'] = id;
		
		values['all'] = 'yes';
		values['cmd'] = 'messageTemplate';
		values['action'] = 'edit';
		
		Ecp.Ajax.request(values, function(result) {
			win.window.hide();
			win['ecpOwner'].notifyAll('reload');

		});
	}
}

// window's button 
var clickSaveMessageTempBtn=function(btn,e)
{
	var win=btn['ecpOwner'];
	var form=win.getItem(0);
	
	if (form.isValid()) {
		var values=form.getValues();
		
		var tree = win.getItem(1);
		var  ids = Ecp.components.messageTempGrid.ids;
		
		values['institutions'] = [];
		var j=0;
		for (var i = 0; i < ids.length; i++) {
			if (tree.getNodeById(ids[i]) != null || tree.getNodeById(ids[i]) != undefined) {
				if (tree.getNodeById(ids[i]).attributes.checked) {
					values['institutions'][j++] = ids[i];
				} 
			}
		}
		if(values['institutions'] == '') {
			delete values['institutions'];
			Ecp.MessageBox.alert(TXT.template_institution_need);
			return;
		}

		var id = Ecp.components.messageTempGrid.grid.getSelectedId();
		values['uid'] = id;
		values['cmd'] = 'messageTemplate';
		values['action'] = 'edit';
		
		Ecp.Ajax.request(values,function(result){
			if (result.result == 'success') {
				win.window.hide();
				win['ecpOwner'].notifyAll('reload');
			} else {
				var errorJson={'institutionNeed':TXT.template_institution_need};
				Ecp.MessageBox.alert(errorJson[result.message]);
			}	
		});
	}
}