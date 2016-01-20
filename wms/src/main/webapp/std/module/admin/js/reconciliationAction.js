var loadReconciliationLayoutData=function()
{
	Ecp.components.reconciliationGrid.grid.selectFirstRow();
}

var showImportReconciliationFileWin=function(btn, e) {
	var dialog = new Ext.ux.UploadDialog.Dialog({
		url: DISPATCH_SERVLET_URL+'?cmd=reconciliation&action=importReconciliation&type=y',
		reset_on_hide: false,
		allow_close_on_upload: true,
		upload_autostart: false
	});
	dialog.on('uploadsuccess',function(obj,fileName,response){
		Ecp.components.reconciliationGrid.dataStore.store.loadData({results:response['data']});
	});
	dialog.show('show-button');
}

var clickQueryReconciliationBtn=function(btn, e) {
	var win = btn['ecpOwner'];
	var form = win.getItem(0);
	if (form.isValid()) {
		var values = form.getValues();
		values["cmd"]="reconciliation";
		values["action"]="find";
		values["json"]=Ext.util.JSON.encode(values);
		
		Ecp.components.reconciliationGrid.search(values);
		win.window.hide();
	}
}