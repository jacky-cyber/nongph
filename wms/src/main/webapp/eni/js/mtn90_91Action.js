
/* change by sunyue 20121212*/
var loadMTn90_91Data = function () {
	Ecp.components.mtn90_91Grid.grid.selectFirstRow();
};
var showMTn90_91ContentWin = function () {
	var grid = Ecp.components.mtn90_91Grid.grid;
	if (grid.getDataCount() == 0) {
		return ;
	}
	var record = Ecp.components.mtn90_91Grid.grid.getSelectedRecord();
	if(!record){
		return ;
	}
	var formObj = {};
	var winObj = {};
	var winId = "mesageShowInMsg" + record.id;
	if (Ext.WindowMgr.get(winId) != undefined) {
		return;
	}
	var win = new Ecp.MessageFinContentWin();
	win.initForm({msgConf:{needValidate:false}, cusObj:formObj});
	win.setButtons([{text:TXT.message_transform,handler:function(bt){
												var body = win.items[0].items.items[5].value;
												transformFinContent(body);
											},
						scope:win
					},
					{text:TXT.message_print, handler:printMsg, scope:win}, 
					{text:TXT.common_btnClose, handler:function () {
		this["ecpOwner"].window.close();
	}}]);
	win.handleWidgetConfig(function (win) {
		win["config"]["id"] = winId;
		win["config"]["title"] = TXT.message + " " + record.messageId;
	});
	win.customization(winObj);
	win.render();
	Ecp.Ajax.request({cmd:"message", action:"getFinMessage", messageId:record.id}, function (result) {
		win.window.getItem(0).setValues(result);
		win.window.window.setPosition(undefined, undefined);
		win.show();
		win["dataBus"] = {mid:record.id};
	});
};
var clickQueryMTn9091MessageBtn = function () {
	var form = Ecp.components.mtn90_91MessageSearchForm;
	var values = form.form.getValues();
	if ( (values.createTimeFrom).length > 0 && (values.creatTimeTo).length > 0) {
	   	   if( values.createTimeFrom > values.creatTimeTo){
		   		Ecp.MessageBox.alert(TXT.common_time_startEndError); 
		   		return false;
	   	   }
    } 
	values["cmd"] = "message";
	values["action"] = "findMTn9091";
	Ecp.components.mtn90_91Grid.search(values);
	Ecp.components.mtn90_91MessageSearchWin.window.close();
};
var showFindMTn90_91Win = function () {
	Ecp.MTn90_91MessageSearchWin.createWindow({"searchFormFun":function (obj) {
		Ecp.components.mtn90_91MessageSearchForm = obj;
	}, "winFun":function (obj) {
		Ecp.components.mtn90_91MessageSearchWin = obj;
	}}).show();
};
var clickMessageReadWin = function () {
	var record = Ecp.components.mtn90_91Grid.grid.getSelectedRecord();
	if (!record) {
		return;
	}
	if (record["deleted"]) {
		Ecp.MessageBox.alert(TXT.message_is_deleted);
		return;
	}
	var params = {cmd:"message", action:"setNewFlag", id:record["id"]};
	Ecp.Ajax.request(params, function (result) {
		if (result.result == "success") {
			Ecp.components.mtn90_91Grid.show();
		} else {
			Ecp.MessageBox.alert(TXT.message_NoPower);
		}
	});
};
var showAssignMessageWin = function () {
	var data = Ecp.components.mtn90_91Grid.grid.getSelectedRecord();
	if (data) {
		var id = Ecp.components.mtn90_91Grid.grid.getSelectedId();
		assignMesssage(id, function (treeWin) {
		}, [Ecp.components.mtn90_91Grid]);
	}
};
function assignMesssage(id, afterAssignAction, observers, listeners) {
	Ecp.Ajax.request({cmd:'message',action:'checkMessageOwner',cid:id},function(result){
		if (result.result == 'failure') {
			Ecp.MessageBox.alert(TXT[result.message]);
			return;
		} else {
			var obj = {mtn9091Id:id};
			var win = Ecp.InternalCodeSearchWindow.createSingleSearchWithSubAccountWindow(obj, function (treeRecord, treeWin) {
				if (treeRecord == null) {
					Ecp.MessageBox.alert(TXT.case_institution_choice);
					return;
				}
				Ecp.Ajax.request({cmd:"message", action:"reassignMTn9091", id:id, institutionid:treeRecord.attributes.id}, function (result) {
						if (result.result == "failure") {
							if (result.message == "self") {
								Ecp.MessageBox.alert(TXT.mtn90_91_self);
							} 
							Ecp.MessageBox.alert(TXT.mtn90_91_error);
							return;
						}else if(result.result == "success"){
							Ecp.components.mtn90_91Grid.show();
						}
						afterAssignAction(treeWin);
					});
				}, observers, listeners);
			win.show();
		}
	});	
}
var clickRecordForRead = function () {
	var grid = Ecp.components.mtn90_91Grid.grid;
	if (grid.getDataCount() == 0) {
		return ;
	}
	var record = grid.getSelectedRecord();
	var toolBar = Ecp.components.mtn90_91GridToolbar;
	if (!record["isRead"]) {
		toolBar.toolBar.toolBar.findById("mtn90_91Read").setText(TXT.message_read);
	} else {
		toolBar.toolBar.toolBar.findById("mtn90_91Read").setText(TXT.message_notRead);
	}
	
};
var changeAssignOrBackUp = function(){
	var grid = Ecp.components.mtn90_91Grid.grid;
	if (grid.getDataCount() == 0) {
		return ;
	}
	var toolBar = Ecp.components.mtn90_91GridToolbar;
	if(!Ecp.userInfomation.isInstHQ){
		toolBar.toolBar.toolBar.findById("mtn90_91AssignCase").setText(TXT.task_reject);
		toolBar.toolBar.toolBar.findById("mtn90_91AssignCase").setHandler(clickBackUpRecord);
	}
};
var clickBackUpRecord=function(){
	var record = Ecp.components.mtn90_91Grid.grid.getSelectedRecord();
	if (!record) {
		return;
	}
	if (record["deleted"]) {
		Ecp.MessageBox.alert(TXT.message_is_deleted);
		return;
	}
	var params = {cmd:"message", action:"backUpMTn9091", id:record["id"]};
	Ecp.Ajax.request(params, function (result) {
		if (result.result == "success") {
			Ecp.components.mtn90_91Grid.show();
		} else {
			Ecp.MessageBox.alert(TXT.message_NoPower);
		}
	});
}


