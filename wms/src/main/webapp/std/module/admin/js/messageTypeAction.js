var loadMessageTypeData=function()
{
	Ecp.components.messageTypeGrid.grid.selectFirstRow();
}

var showEditMessageTypeWin=function(btn, e) {
	// if user didn't select any recored then alert a messageBox
	var data=Ecp.components.messageTypeGrid.grid.getSelectedRecord();
	if(data) {
		var windowObj={};
		if(typeof message_type_edit_window_config!='undefined')
			windowObj['config']=message_type_edit_window_config;
		if(typeof message_type_edit_window_buttons!='undefined')
			windowObj['buttons']=message_type_edit_window_buttons;
		
		// item's config
		windowObj['items']=[{}];
		if (typeof message_type_edit_form_config != 'undefined')
			windowObj['items'][0]['config'] = message_type_edit_form_config;
		if (typeof message_type_edit_form_buttons != 'undefined')
			windowObj['items'][0]['buttons'] = message_type_edit_form_buttons;
		if (typeof message_type_edit_form_items != 'undefined')
			windowObj['items'][0]['items'] = message_type_edit_form_items;
		
		var record=Ecp.components.messageTypeGrid.grid.getSelectedRecord();

		var win = Ecp.MessageTypeWindow.createSingleEditWindow(windowObj,[Ecp.components.messageTypeGrid]);
		win.show();
		win.getItem(0).setValues(record);
	}
}

var clickSaveMessageTypeBtn=function(btn, e) {
	var win=btn['ecpOwner'];
	var form=win.getItem(0);
	var values=form.getValues();
		
	if (form.isValid()) {
		values['cmd']='messageType';
		values['action']='edit';
		Ecp.Ajax.request(values,function(r){
			win.window.hide();
			form.reset();
			win['ecpOwner'].notifyAll('reload');
		});
	}
}