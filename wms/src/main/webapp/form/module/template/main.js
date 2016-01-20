function main(){
	var gridPanel = new com.felix.form.module.template.FormTemplateGridPanel();
	
	com.felix.nsf.Context.globalView = new com.felix.nsf.GlobalView('form_template', 1);
	com.felix.nsf.Context.globalView.mainPanel.layout = 'border';
	com.felix.nsf.Context.globalView.addModuleComp( gridPanel.getExtGridPanel() );
	com.felix.nsf.Context.globalView.render( TXT.task );
	
	gridPanel.load();
}
com.felix.nsf.Util.onReady(main);