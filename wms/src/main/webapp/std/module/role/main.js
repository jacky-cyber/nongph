function roleLayout(){
	var roleGridPanel = new com.felix.std.module.role.RoleGridPanel();
	
	com.felix.nsf.Context.currentComponents.roleGridPanel = roleGridPanel;
	com.felix.nsf.Context.globalView = new com.felix.nsf.GlobalView('system', 1);
	com.felix.nsf.Context.globalView.mainPanel.layout = 'border';
	com.felix.nsf.Context.globalView.addModuleComp( roleGridPanel.getExtGridPanel() );
	com.felix.nsf.Context.globalView.render( TXT.role_title );
	roleGridPanel.load();
}
com.felix.nsf.Util.onReady(roleLayout);