Ecp.components.globalView = new Ecp.GlobalView('system');
function securityExchangeImportLayout() {

	var form=new Ecp.SecurityExchangeImportForm();
	Ecp.components.securityExchangeImportForm = form;
	var securityExchangeImportPanel=new Ecp.SecurityExchangeImportPanel(form.render());
	Ecp.components.globalView.addModuleComp(form.render());
	//Ecp.components.globalView.addModuleComp(securityExchangeImportPanel);
	Ecp.components.globalView.render(TXT.LogFunc_BocSecurityExchangeAction);
	
};

Ecp.onReady(securityExchangeImportLayout);