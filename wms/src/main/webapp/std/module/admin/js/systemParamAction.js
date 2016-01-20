
/* change by sunyue 20130122*/
var loadSystemParam = function () {
	Ecp.components.systemtParamtsGrid.grid.selectFirstRow();
};
var clickShowQuerySystemParamWin = function () {
	Ecp.SystemtParamtsSearchWin.createWindow({"searchFormFun":function (obj) {
		Ecp.components.systemtParamtsSearchForm = obj;
	}, "searchWinFun":function (obj) {
		Ecp.components.systemtParamtsSearchWin = obj;
	}}).show();
};
var clickQueryParamsBtn = function () {
	var form = Ecp.components.systemtParamtsSearchForm.form;
	if (form.isValid()) {
		var values = form.getValues();
		values["cmd"] = "sysparam";
		values["action"] = "find";
		Ecp.components.systemtParamtsGrid.search(values);
		Ecp.components.systemtParamtsSearchWin.window.close();
	}
};
var clickAddSystemParamWin = function () {
	Ecp.SystemParamInfoWindow.createWindow({"formFun":function (obj) {
		Ecp.components.systemtParamtInfoForm = obj;
	}, "winFun":function (obj) {
		Ecp.components.systemParamInfoWindow = obj;
	}}).show();
};
var clickDeleteSystemParam = function () {
	var id = Ecp.components.systemtParamtsGrid.grid.getSelectedId();
	if (!id) {
		return;
	}
	Ecp.MessageBox.confirm(TXT.common_is_deleted, function () {
		var params = {cmd:"sysparam", action:"delete", id:id};
		Ecp.Ajax.request(params, function (result) {
			if (result.result == "success") {
				Ecp.components.systemtParamtsGrid.show();
			}
		});
	});
};
var dblclickEditSystemParamWin = function () {
	var data = Ecp.components.systemtParamtsGrid.grid.getSelectedRecord();
	if (data) {
		var params = {cmd:"sysparam", action:"get", id:data["id"]};
		Ecp.Ajax.request(params, function (result) {
			Ecp.SystemParamInfoWindow.createWindow({"formFun":function (obj) {
				Ecp.components.systemtParamtInfoForm = obj;
			}, "winFun":function (obj) {
				Ecp.components.systemParamInfoWindow = obj;
				obj.buttons[0].handler = clickEditSystemParamBtn;
			}}).show();
			Ecp.components.systemtParamtInfoForm.form.setValues(result);
		});
	}
};
var clickSaveSystemParamBtn = function () {
	saveOrUpdateSystemParam({action:"save"});
};
var clickEditSystemParamBtn = function () {
	saveOrUpdateSystemParam({action:"update", id:Ecp.components.systemtParamtsGrid.grid.getSelectedRecord()["id"]});
};
var saveOrUpdateSystemParam = function (obj) {
	var form = Ecp.components.systemtParamtInfoForm.form;
	if (form.isValid()) {
		var values = form.getValues();
		values["cmd"] = "sysparam";
		values["action"] = obj["action"];
		values["id"] = obj["id"];
		Ecp.Ajax.request(values, function (result) {
			if (result.result == "success") {
				Ecp.components.systemParamInfoWindow.close();
				Ecp.components.systemtParamtsGrid.show();
			} else {
				var errorContent = TXT.system_parameter_error;
				if (result.message == "system_parameterName_exist") {
					errorContent = result[result.message];
				}
				Ecp.MessageBox.alert(errorContent);
			}
		});
	}
};
var messageSearchFormReset = function (btn, e) {
	var win = btn["ecpOwner"];
	var form = win.getItem(0);
	form.reset();
};

