var loadRmaData=function(){
	Ecp.components.rmaSearchGrid.grid.selectFirstRow();
}
var resetForm=function(){
	Ecp.components.rmaSearchForm.form.reset();
}
function clickQueryRmaMaintBtn(){ 
	Ecp.RmaSearchSearchWin.createWindow({"searchFormFun":function (obj) {
		Ecp.components.rmaSearchForm = obj;
	}, "searchWinFun":function (obj) {
		Ecp.components.rmaSearchSearchWin = obj;
	}}).show();
}
function clickRmaMaintImportBtn(){
	Ecp.SecurityExchangeImportWindow.createWindow({"importFormFun":function (obj) {
		Ecp.components.securityExchangeImportForm = obj;
	}, "importWinFun":function (obj) {
		Ecp.components.securityExchangeImportWindow = obj;
	}}).show();
}
var resetImportForm=function(){
	Ecp.components.securityExchangeImportForm.form.reset();
}
function clickRmaSearchSearchBtn(){
	var params=Ecp.components.rmaSearchForm.form.getValues(); 
  	if ( (params.fromDate).length > 0 && (params.toDate).length > 0) 
	       {
	   	   if( params.fromDate > params.toDate){
		   		Ecp.MessageBox.alert(TXT.common_time_startEndError); 
		   		return false;
	   	   }
    } 
    params["cmd"] = "rma";
	params["action"] = "query";
    Ecp.components.rmaSearchSearchWin.close();
	Ecp.components.rmaSearchGrid.search(params);
}
var clickRmaImportBtn=function(){
	var file = Ecp.components.securityExchangeImportForm.form.findById('rma-file');
	var branch = Ecp.components.securityExchangeImportForm.form.findById('internalCode');
	if(file!=''){
        if(Ecp.components.securityExchangeImportForm.form.isValid()){
        	var extPanel =Ecp.components.securityExchangeImportForm.form.formPanel;
        	Ecp.components.securityExchangeImportForm.form.formPanel.getForm().submit({
                url: DISPATCH_SERVLET_URL+"?cmd=rma&action=importRMA&type=pq",
                waitTitle:TXT.common_please_wait,
                waitMsg: TXT.common_wait_access,
               	success: function(extPanel, o){
               		if(o.result.file=='SUCCESS'){
               			return Ecp.MessageBox.alert('结果: "成功",已导入："'+o.result.total+'条",未导入："'+o.result.notIn+'条"');
              	 	}else{
                   		return Ecp.MessageBox.alert(TXT.RMA_imput_faile);
                   	}},
           		failure: function(extPanel, o){
           			Ecp.MessageBox.alert(TXT.RMA_imput_faile);
     			}
        	});
        }
	}else{
		Ecp.MessageBox.alert(TXT.please_select);
	}
}
