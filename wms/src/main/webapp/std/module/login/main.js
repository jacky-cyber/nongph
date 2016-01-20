function main(){
	var loginWin = com.felix.std.module.login.LoginWindow.getInstance();
	loginWin.show();
	
	com.felix.nsf.Context.globalView = new com.felix.nsf.GlobalView('login',  1, false, true);
	com.felix.nsf.Context.globalView.render( TXT.login_title );
};

function createLoginChangePasswordWindow(windowObj,lang){
	if( !com.felix.nsf.Context.currrentComponents.loginChangePasswordWindow ) {
		var loginChangePasswordForm = new com.felix.std.module.login.LoginChangePasswordForm();
	
		if(windowObj['items'] && windowObj['items'][0])
			loginChangePasswordForm.customization(windowObj['items'][0]);
		
		// window
		var loginChangePasswordWindow = new com.felix.std.module.login.LoginChangePasswordWindow();
		loginChangePasswordWindow.handleWidgetConfig(function(obj){
			obj.items=[loginChangePasswordForm.render()];
			obj.setCloseAction(false);
		});
		loginChangePasswordWindow.customization(windowObj);
		loginChangePasswordWindow.render();
		com.felix.nsf.Context.currrentComponents.loginChangePasswordWindow = loginChangePasswordWindow.window;
	}
	if(typeof lang!='undefined')
		loginChangePasswordForm.form.setValue('lang',lang);
	return com.felix.nsf.Context.currrentComponents.loginChangePasswordWindow;
}
	
Ext.onReady(main);