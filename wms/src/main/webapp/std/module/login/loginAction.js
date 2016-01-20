var showLoginChangePasswordWin=function(btn,e)
{
	if(!Ecp.components.loginChangePasswordWindow)
	{
		var windowObj={};
		if(typeof login_change_password_window_config!='undefined')
			windowObj['config']=login_change_password_window_config;
		if(typeof login_change_password_window_buttons!='undefined')
			windowObj['buttons']=login_change_password_window_buttons;
		
		// item's config
		windowObj['items']=[{}];
		if(typeof login_change_password_form_config!='undefined')
			windowObj['items'][0]['config']=login_change_password_form_config;
		if(typeof login_change_password_form_buttons!='undefined')
			windowObj['items'][0]['buttons']=login_change_password_form_buttons;
		if(typeof login_change_password_form_items!='undefined')
			windowObj['items'][0]['items']=login_change_password_form_items;
	
		// window
		var changePasswordWin=Ecp.LoginChangePasswordWindow.createLoginChangePasswordWindow(windowObj,btn['lang']);
		Ecp.components.loginChangePasswordWindow=changePasswordWin;
	}
	var params={
		cmd:'user',
		action:'get'
	};
	Ecp.Ajax.requestWithCusUrl(DISPATCH_SERVLET_URL,'post',params,function(result){
		result['password']=undefined;
		var win=Ecp.components.loginChangePasswordWindow;
		win.getItem(0).setValues(result);
		win.window.show();
	});
}

var clickLoginChangePasswordWBtn=function(btn,e)
{
	var win=btn['ecpOwner'];
	var changeFormPanel=win.getItem(0);
	if(changeFormPanel.isValid())
	{	
		var values=changeFormPanel.getValues();
		values['cmd']='user';
		values['action']='editProfile';
		values['isFirstLogin']='false';
		Ecp.Ajax.requestWithCusUrl(DISPATCH_SERVLET_URL,'post',values,function(result){			
			if (result.result == 'success') {
				window.location.href=ENI_INDEX_URL+'?locale='+values['locale'];
			}else{
				var errorJson={'rechangePwd':TXT.change_password_fail,
						'emailExist':TXT.userEmail_exist,
						'duplicate':TXT.user_pwd_duplicate};
				Ecp.MessageBox.alert(errorJson[result.message]);
			}
		});
	}
}

var showChangePasswordFormWin=function(btn,e){
		var windowObj={};
		if(typeof profile_edit_window_config!='undefined')
			windowObj['config']=profile_edit_window_config;
		if(typeof profile_edit_window_buttons!='undefined')
			windowObj['buttons']=profile_edit_window_buttons;
		
		// item's config
		windowObj['items']=[{}];
		if(typeof profile_edit_tabpanel_config!='undefined')
			windowObj['items'][0]['config']=profile_edit_tabpanel_config;
		if(typeof profile_edit_tabpanel_items!='undefined')
			windowObj['items'][0]['items']=profile_edit_tabpanel_items;
			
		windowObj['items'][0]['items']=[{},{}]
		if(typeof profile_edit_tabpanel_config!='undefined')
			windowObj['items'][0]['items'][0]['config']=profile_edit_form1_config;
		if(typeof profile_edit_tabpanel_items!='undefined')
			windowObj['items'][0]['items'][0]['buttons']=profile_edit_form1_items;
		if(typeof profile_edit_tabpanel_items!='undefined')
			windowObj['items'][0]['items'][0]['items']=profile_edit_form1_items;
		if(typeof profile_edit_tabpanel_config!='undefined')
			windowObj['items'][0]['items'][1]['config']=profile_edit_form2_config;
		if(typeof profile_edit_tabpanel_items!='undefined')
			windowObj['items'][0]['items'][1]['buttons']=profile_edit_form2_items;
		if(typeof profile_edit_tabpanel_items!='undefined')
			windowObj['items'][0]['items'][1]['items']=profile_edit_form2_items;
		
	var params={
			cmd:'user',
			action:'get'
	};
	com.felix.nsf.Ajax.request(params,function(result){
		var win = Ecp.ProfileInfoWindow.createSingleEditWinow(windowObj);
		win.getItem(0).getItem(0).setValues(result);
		win.getItem(0).getItem(1).reset();
		win.getItem(0).reset();
		win.show();
	});
    
}

var clickChangeInfoBtn=function(btn,e)
{
	var form=btn['ecpOwner'];
	var values=form.getValues();
	if (form.isValid()) {
		values['cmd']='user';
		values['action']='editProfile';
		values['isFirstLogin']=false;
		com.felix.nsf.Ajax.request(values,function(r){
			if (r.result == 'success'){
				Ecp.ProfileInfoWindow.singleInfoWinows.window.hide();
			}else{
				var errorJson={'emailExist':TXT.userEmail_exist,
						'duplicate':TXT.user_pwd_duplicate};
				Ecp.MessageBox.alert(errorJson[r.message]);
			}
		});
	}
}