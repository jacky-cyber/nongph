var loadTelegraphCodeData=function(){
	Ecp.components.telegraphCodeGrid.grid.selectFirstRow();
}
var showAddTelegraphCodeWin=function(){
	var win = Ecp.TelegraphCodeInfoWindow.createTelegraphCodeAddWindow();
	win.show(Ext.getBody());
}
var showSearchTelegraphCodeWindow=function(){
	var win = Ecp.TelegraphCodeSearchWindow.createTelegraphCodeSearchWindow();
	win.show(Ext.getBody());
}
var showEditTelegraphCodeWin=function(){ 
	var data=Ecp.components.telegraphCodeGrid.grid.getSelectedRecord(); 
	if(data)
    { 
		data.commMethod = '';
		var win = Ecp.TelegraphCodeInfoWindow.createTelegraphCodeEditWindow(); 
		win.getItem(0).findFieldById("code").setDisabled(true);
		win.getItem(0).setValues(data);
		win.show(Ext.getBody());
	} 
}
var test = function(a,b,c)
{
	if(a!=""&&b!=""&&c!=""){
			return false;
		}else if(a==""&&b==""&&c=="")
		{
			return false;
		}else if(a!=""&&b!=""){
			return false;
		}else if(a!=""&&c!=""){
			return false;
		}else if(b!=""&&c!=""){
			return false;
		}else
		{
		return true;
		}
}
var isValidCode = function(code){
	return code.match('/(\d{4}/s)*/g');
	

}
var TelegraphCodeSearchBtn=function()
{
	var form = Ecp.components.telegraphCodeSearchForm.form;
	if (form.isValid()) {
		var params = form.getValues();  
		params['cmd']='telegraphCode';
		params['action']='find';
		Ecp.components.telegraphCodeGrid.search(params);
		Ecp.components.telegraphCodeSearchWindow.window.close();
	}
}
var showConfirmDel=function(){
	var params=Ecp.components.telegraphCodeGrid.grid.getSelectedRecord(); 
	if(params ){
		 Ecp.MessageBox.confirm(TXT.queue_delComfirm ,function(){
			 Ecp.Ajax.requestWithCusUrl(DISPATCH_SERVLET_URL+'/TelegraphCodeAction/deleteVirtual','POST',params,function(result){
					if( result.status=='DELSUCCESS' ) {
						Ecp.MessageBox.alert(TXT.queue_delSuccess); 
						Ecp.components.telegraphCodeGrid.show();
					}
				},null,'no');
		 });
	} 
}

var showTelegraphCodeDetailInfo=function(){
	var data=Ecp.components.telegraphCodeGrid.grid.getSelectedRecord(); 
	if(data)
    { 	
		var win = Ecp.TelegraphCodeInfoWindow.createTelegraphCodeInfoWindow(); 
		win.show(Ext.getBody());
		win.getItem(0).setValues(data);
	} 
	
}


function closeTelegraphCodeInfoWindow(){ 
	Ecp.components.telegraphCodeInfoWindow.window.close();
}

function closeTelegraphCodeSearchWindow(){
	Ecp.components.telegraphCodeSearchWindow.window.close();
}

var addTelegraphCodeInfo=function(){
	var params = Ecp.components.telegraphCodeAddForm.form.getValues(); 
	var simple=Ecp.components.telegraphCodeAddForm.form.findById('simplifiedChinese');
	var tradition=Ecp.components.telegraphCodeAddForm.form.findById('traditionalChinese');
	var form = Ecp.components.telegraphCodeAddForm.form.isValid();
//	alert(form);
	if(form){
	if(simple.length>1||tradition.length>1)
		{
			Ecp.MessageBox.alert(TXT.telegraphCOde_error);
		}
	else{
		params.action='add';
		Ecp.Ajax.requestWithCusUrl(DISPATCH_SERVLET_URL+'/TelegraphCodeAction/saveOrUpdate','POST',params,function(result){
			if( result.status=='SAVESUCCESS' ) {
				Ecp.MessageBox.alert(TXT.queue_saveSuccess);
				closeTelegraphCodeInfoWindow();
				Ecp.components.telegraphCodeGrid.show();
			 } else if (result.status=='EXIST') {
				Ecp.MessageBox.alert(TXT.telecode_exist);
//				closeTelegraphCodeInfoWindow();
				//Ecp.components.telegraphCodeGrid.show();
			 } else {
				Ecp.MessageBox.alert(TXT.telecode_exist);
			 }
		},null,'no');
	}
	}else
		{
		Ecp.MessageBox.alert(TXT.telegraphCOde_error);
		}
}

var editTelegraphCodeInfo=function(){
	var params = Ecp.components.telegraphCodeEditForm.form.getValues();  
	var simple=Ecp.components.telegraphCodeEditForm.form.findById('simplifiedChinese');
	var tradition=Ecp.components.telegraphCodeEditForm.form.findById('traditionalChinese');
	var form = Ecp.components.telegraphCodeEditForm.form.isValid();
	if(form){
	if(simple.length>1||tradition.length>1)
	{
		Ecp.MessageBox.alert(TXT.telegraphCOde_error);
	}
	else{
		params.action='edit';
		Ecp.Ajax.requestWithCusUrl(DISPATCH_SERVLET_URL+'/TelegraphCodeAction/saveOrUpdate','POST',params,function(result){
			if( result.status=='SAVESUCCESS' ) {
				Ecp.MessageBox.alert(TXT.queue_saveSuccess);
				closeTelegraphCodeInfoWindow();
				Ecp.components.telegraphCodeGrid.show();
			 } else if (result.status=='UPDATESUCCESS') {
				Ecp.MessageBox.alert(TXT.queue_updateSuccess);
				closeTelegraphCodeInfoWindow();
				Ecp.components.telegraphCodeGrid.show();
			 } else {
				Ecp.MessageBox.alert(TXT.queue_exist);
			 }
		},null,'no');
		
	}
	}else{
		Ecp.MessageBox.alert(TXT.telegraphCOde_error);
	}
	
}