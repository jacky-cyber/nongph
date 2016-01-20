com.felix.nsf.Ajax = (function (thoj){
	var dispatchUrl      = com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL;
	var ACCESS_ERROR     = "[ERROR]";
	var PERMISSION_ERROR = "[PERMISSION_ERROR]";
	var TIME_OUT         = '[TIME_OUT]';
	var USER_NULL        = "[null]";
	var NO_HASP          = "[NO_HASP]";
	var HASP_TIME        = "[HASP_TIME]";
	var NO_LICENSE       = '[NO_LICENSE]';
	
	thoj.requestWithoutMessageBox = function( action, command, handler, params, scope ) {
		thoj.request( action, command, handler, params, false, scope );
	}
	
	thoj.requestWithMessageBox = function( action, command, handler, params, scope ) {
		thoj.request( action, command, handler, params, true, scope );
	}
	
	thoj.request = function( action, command, handler, params, withMB, scope ) {
		var resultConfig={
			url    : dispatchUrl + "/" + action + "/" + command,
			method : 'post',
			success: function( response, options ) {
					if( withMB )
						Ext.MessageBox.hide();
					var s = response.responseText;
					if ( s == PERMISSION_ERROR ) {
						Ext.MessageBox.alert( TXT.common_hint, TXT.common_no_permission );
						return;
					} else if ( s == ACCESS_ERROR ) {
						Ext.MessageBox.alert( TXT.common_hint, TXT.common_access_fail, function(){} );
						return;
					} else if ( s == USER_NULL ) {
						Ext.MessageBox.alert( TXT.common_hint,
								TXT.common_relogin, function() {
									if( com.felix.nsf.GlobalConstants.APP_MENU_TYPE==2 )
										parent.location.href = com.felix.nsf.GlobalConstants.URL_LOGIN;
									else
										window.location.href = com.felix.nsf.GlobalConstants.URL_LOGIN;
								});
						return;
					} else if (s == TIME_OUT){
						Ext.MessageBox.alert(TXT.common_hint,
								TXT.common_time_out, function() {
									if( com.felix.nsf.GlobalConstants.APP_MENU_TYPE==2 )
										parent.location.href = com.felix.nsf.GlobalConstants.URL_LOGIN;
									else
										window.location.href = com.felix.nsf.GlobalConstants.URL_LOGIN;
								});
						return;
					}
					
					var result = "";
					if( s.startWith('{') || s.startWith('[')  )
						result = Ext.util.JSON.decode(s);
					else if( s.startWith("<?xml") )
						result = response.responseXML;
					else
						result = s;	
					handler.call(this, result);
			},
			failure : function( response, options ) {
				if( withMB )
					Ext.MessageBox.hide();
				Ext.MessageBox.alert( TXT.common_hint, TXT.common_access_fail );
			}
		};
		
		if( params )
			resultConfig['params'] = params;
		
		if( scope )
			resultConfig['scope'] = scope;
		
		if( withMB )
			com.felix.nsf.MessageBox.showWaitingWin();
		Ext.Ajax.request(resultConfig);
	};
	
	return thoj;
})({});