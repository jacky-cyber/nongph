function userLayout(){
	var userGridPanel = new com.felix.std.module.user.UserGridPanel();
	com.felix.nsf.Context.currentComponents.UserGridPanel = userGridPanel;
	com.felix.nsf.Context.globalView = new com.felix.nsf.GlobalView('system', 1);
	com.felix.nsf.Context.globalView.mainPanel.layout = 'border';
	com.felix.nsf.Context.globalView.addModuleComp( userGridPanel.getExtGridPanel() );
	com.felix.nsf.Context.globalView.render( TXT.user_title );
	userGridPanel.load();
}
com.felix.nsf.Util.onReady(userLayout);