function backCasesWithInSelectedDate(){
	if(!this.form.isValid())
		return;
	var json=Ext.util.JSON.encode(this.form.getValues());
	var params={cmd:'backup',action:'checkStatus',json:json};
	Ecp.Ajax.request(params,function(result){
		if (result.result == 'success') {
			if(result.message == ''){
				Ecp.Ajax.request({cmd:'backup',action:'backup',json:json},function(result){
					if(result.result == 'success')
						Ecp.MessageBox.alert(TXT.case_backup_success);
				});
			} else if(result.message == 'caseOpen') {
				Ecp.MessageBox.confirm(TXT.case_backup_open,function(btn){
					if(btn=='yes') {
						Ecp.Ajax.request({cmd:'backup',action:'backup',json:json},function(result){
							if(result.result == 'success')
								Ecp.MessageBox.alert(TXT.case_backup_success);
						});
					}
				});
			} else {
				Ecp.MessageBox.alert(TXT.case_backup_no_record);
			}
		}
	});
}