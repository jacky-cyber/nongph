Ecp.components.globalView=new Ecp.GlobalView('caseBackUpMenu');
function caseLayout(){
	var cmtFormObj={};
	
	var backupForm=new Ecp.BackUpForm();
	backupForm.setBackupHandler(backCasesWithInSelectedDate);
	backupForm.customization(cmtFormObj);
	
	Ecp.components.globalView.addModuleComp(backupForm.render());
	Ecp.components.globalView.render(TXT.caseBackUp);
	
	Ecp.components.backupForm=backupForm;
};

Ecp.onReady(caseLayout);