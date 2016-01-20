com.felix.std.module.login.LoginWindow = (function(thoj){
	var loginWindow = null;
	var config={
			id: 'loginWin',
			defaultButton: 'btnLogin',
			layout: 'border',
			width:500,
			height:250,
			title:TXT.login_title,
			closeAction:'hide',
			closable:false,
			plain: false,
			draggable: false,
			resizable: false
	};
	var buttons=[{
		id: 'btnLogin',
		text: TXT.login_btn_login,
		handler : function(){
			clickLoginBtn();
		}
	}];
		
	var clickLoginBtn = function(){
		var formPanel = com.felix.std.module.login.LoginFormPanel.getInstance();
		if( formPanel.isValid() ) {
			var params = formPanel.getValues();
			com.felix.nsf.Ajax.requestWithMessageBox("userAction", "login", function(result){
				if( result.state=='SUCCESS' ) {
					if( result.message=='firstLogin' ) {
						btn['lang']=params['locale'];
						showLoginChangePasswordWin(btn,e);
					} else
						window.location.href = com.felix.nsf.GlobalConstants.ENI_INDEX_URL+'?locale='+locale;
				} else {
					if( result.message=='alreadyOnline' ) {
						window.location.href=com.felix.nsf.GlobalConstants.ENI_INDEX_URL+'?locale='+locale;
						return;
					}
					
					var errorJson={'username':TXT.login_invalidusername,
							       'password':TXT.login_invalidpassword,
							       'notExist':TXT.login_notExist,
								   'errorPassword':TXT.login_errorPassword,
								   'userOnline':TXT.login_userOnline,
								   'lock':TXT.login_lock,
								   'disabled':TXT.login_disable,
								   'maxConnection':TXT.login_max_connection,
								   'licenseNotFound':TXT.login_license_not_found};
					com.felix.nsf.MessageBox.alert( errorJson[result.errorDesc.code] );
				}
			}, params );
		}
	}
	
	thoj.getInstance = function(){
		if( loginWindow==null ) {
			var logoPanel = new Ext.Panel( { baseCls: 'x-plain',
										     region: 'center'
											} );
			var loginFormPanel = com.felix.std.module.login.LoginFormPanel.getInstance();
			
			loginWindow = new com.felix.nsf.wrap.Window();
			loginWindow.setConfig( config );
			loginWindow.setButtons( buttons );
			loginWindow.setItems( [logoPanel, loginFormPanel ] );	
			loginWindow.getExtWindow().on( 'show', function(){
																var lfp = com.felix.std.module.login.LoginFormPanel.getInstance();
																var f = lfp.getExtFormPanel().items.itemAt(0);
																f.focus.defer(100, f); 
															  } );
			loginWindow.getExtWindow().addListener( 'render', function(){
								Ext.EventManager.addListener( this.el, 'keyup', function(evt){
																					if(evt.getKey()=='13'){
																						clickLoginBtn();
																					}
																				}, this);
						});
		}
		return loginWindow;
	}
		
	return thoj;
})({});