var loadCorrespondentData=function(){
	Ecp.components.correspondentGrid.grid.selectFirstRow();
	changeAddOrEdit();
};

var showAddCorrespondentWin=function(btn, e) {
	Ecp.CorrespondentWindow.createCorrespondentWindow({"correspondentForm":function (obj) {
		Ecp.components.correspondentForm=obj;
	}, "correspondentWin":function (obj) {
		Ecp.components.correspondentWin=obj;
	}}).show();
};

var showEditCorrespondentWin=function(btn, e) {
	// if user didn't select any recored then alert a messageBox
	var data=Ecp.components.correspondentGrid.grid.getSelectedRecord();
	if(data) {
		var win = Ecp.CorrespondentWindow.createCorrespondentWindow({"correspondentForm":function (obj) {
			Ecp.components.correspondentForm=obj;
		}, "correspondentWin":function (obj) {
			Ecp.components.correspondentWin=obj;
		}}).show();
		Ecp.components.correspondentForm.form.setValues(data);
	}
};

var showDeleteCorrespondentWin=function(btn, e) {
	// if user didn't select any recored then alert a messageBox
	var data=Ecp.components.correspondentGrid.grid.getSelectedRecord();
	if(!data)
		return;
	var txt = TXT.correspondent_is_deleted;
	if(data['isDeleted']){
		txt =TXT.correspondent_is_reuse;
	}
	Ecp.MessageBox.confirm(txt,function(){
		var id=Ecp.components.correspondentGrid.grid.getSelectedId();
		var params={
				cmd:'cor',
				action:'remove',
				id:id
		};
		Ecp.Ajax.request(params,function(result){
			if(result.result=='success')
				Ecp.components.correspondentGrid.show();
		});
	});
};

//window's button 
var clickSaveCorrespondentBtn=function(btn,e)
{
	var win=btn['ecpOwner'];
	var form=win.getItem(0);
	var values=form.getValues();
		
	if (form.isValid()) {
		values['cmd']='cor';
		values['action']='save';
		Ecp.Ajax.request(values,function(r){
			Ext.MessageBox.hide();
			var s = r.message;
			if (s == 'notExist')
				Ext.MessageBox.alert(TXT.common_hint,TXT.bic_not_exist);
			else if(s == 'notValid')
				Ext.MessageBox.alert(TXT.common_hint,TXT.cor_not_valid);
			else {
				win.window.hide();
				form.reset();
				Ecp.components.correspondentGrid.show();
			}
		});
	}
};
var showQueryCorrespondentWin=function(){
	Ecp.CorrespondentSearchWin.createWindow({"searchFormFun":function (obj) {
		Ecp.components.correspondentSearchForm = obj;
	}, "searchWinFun":function (obj) {
		Ecp.components.correspondentSearchWin = obj;
	}}).show();
};
var clickQueryCorrespondentBtn = function(){
	var form = Ecp.components.correspondentSearchForm.form;
	if (form.isValid()) {
		var values = form.getValues();
		values["cmd"] = "cor";
		values["action"] = "find";
		Ecp.components.correspondentGrid.search(values);
		Ecp.components.correspondentSearchWin.window.close();
	}

};
var searchFormReset = function (btn, e) {
	var win = btn["ecpOwner"];
	var form = win.getItem(0);
	form.reset();
};
var showUploadFileWin=function(){
	Ecp.CorrespondentImportWindow.createWindow({"importFormFun":function (obj) {
		Ecp.components.correspondentImportForm = obj;
	}, "importWinFun":function (obj) {
		Ecp.components.correspondentImportWindow = obj;
	}}).show();
};
var downloadFile=function(){
	var recordId=Ecp.components.correspondentGrid.grid.getSelectedId();
	if(!recordId)
		return;
	window.location.href=DISPATCH_SERVLET_URL+"?cmd=cor&action=downloadFile&type=d&corId="+recordId;
};
var importFile=function(){
	var recordId=Ecp.components.correspondentGrid.grid.getSelectedId();
	if(!recordId)
		return;
	var file = Ecp.components.correspondentImportForm.form.findById('cor-file');
	/*var but = file.substring(file.lastIndexOf(".")+1,file.length);
	if(file.lastIndexOf(".")==-1||but!='pdf'){
		Ecp.MessageBox.alert(TXT.cor_import_file_error);
		return ;
	}*/
	if(file!=''){
        if(Ecp.components.correspondentImportForm.form.isValid()){
        	var extPanel =Ecp.components.correspondentImportForm.form.formPanel;
        	var win = Ecp.components.correspondentImportWindow;
        	extPanel.getForm().submit({
	                url: DISPATCH_SERVLET_URL+"?cmd=cor&action=importFile&type=pq&corId="+recordId,
	                waitTitle:TXT.common_hint,
	                waitMsg: TXT.common_wait_access,
	               	success: function(extPanel, action){
        				Ecp.components.correspondentGrid.show();
	               		Ecp.MessageBox.alert(TXT.common_import_success);
	               		win.close();
	               	},
	           		failure: function(extPanel, action){
	           			Ecp.MessageBox.alert(TXT.common_import_failure);
	     			}
        	});
        }
	}else{
		Ecp.MessageBox.alert(TXT.please_select);
	}
};
var changeAddOrEdit = function(){
	var grid = Ecp.components.correspondentGrid.grid;
	if (grid.getDataCount() == 0) {
		return ;
	}
	var record=Ecp.components.correspondentGrid.grid.getSelectedRecord();
	if(!record)
		return;
	var toolBar = Ecp.components.correspondentToolBar;
	if(record['fileImageFalg']){
		toolBar.toolBar.toolBar.findById("editCorrespondentPDF").setText(TXT.cor_btn_edit_PDF);
		toolBar.toolBar.toolBar.findById("queryCorrespondentPDF").show();
	}else{
		toolBar.toolBar.toolBar.findById("editCorrespondentPDF").setText(TXT.cor_btn_add_PDF);
		toolBar.toolBar.toolBar.findById("queryCorrespondentPDF").hide();
	}
	if(record['isDeleted']){
		var t = toolBar.toolBar.toolBar.findById("showDeleteCorrespondentWin");
		t.iconCls="icoDetail";
		toolBar.toolBar.toolBar.findById("showDeleteCorrespondentWin").setText(TXT.correspondent_reuse);
	}else{
		toolBar.toolBar.toolBar.findById("showDeleteCorrespondentWin").setText(TXT.common_btnDelete);
	}
	toolBar.toolBar.toolBar.findById("showDeleteCorrespondentWin").show();
};