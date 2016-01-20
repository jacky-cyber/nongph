com.felix.nsf.MessageBox = (function(thoj){
	thoj.alert = function( msg, fun, scope ){
		Ext.MessageBox.alert( TXT.common_hint, msg, fun, scope);	
	};

	thoj.confirm = function( msg, fun, scope ) {
		Ext.MessageBox.confirm( TXT.common_hint, msg, function(btn){
			if (btn == 'yes') {
				fun.call(this, btn);
			}
		},scope);
	};
	
	thoj.showWaitingWin = function(){
		Ext.MessageBox.show( {
		      msg:  TXT.common_wait_access,
			  width:300,
			  wait: true,
			  waitConfig: {
					//text: title,
					interval:200
					}
		   } );
	};
	return thoj;
})({});