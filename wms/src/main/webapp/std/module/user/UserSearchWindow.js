com.felix.std.module.user.UserSearchWindow = (function(thoj){
	var userWindow = null;
	var config={
		    title:TXT.common_search,
	        width:400,
	        height:350,
	        autoScroll :false,
	        layout:'border',
	        closeAction:'hide',
	        border:false,
	        minimizable: false,
	        maximizable: false,
	        resizable: false,
	        modal:true,
			layoutConfig: {animate:false},
			buttonAlign: 'center'
	};
	var buttons=[{
				text :TXT.common_btnOK,
				handler : function() {
					clickSearchUserBtn(this);
				}
			},{
				text :TXT.common_btnClose,
				handler : function(bt) {
					bt.ownerCt.ownerCt.hide();
				}
			}];
	
	var clickSearchUserBtn = function(bt){
		var win =com.felix.nsf.Context.currentComponents.UserSearchWindow;
		var values=win.getItem(0).form.getValues();
		//TODO
		alert("clickSearchUserBtn");
	}
	
	thoj.getInstance = function(){
		if( userWindow==null ) {
			var userSearchFormPanel = com.felix.std.module.user.UserSearchFormPanel.getInstance();
			userWindow = new com.felix.nsf.wrap.Window();
			userWindow.setItems( [userSearchFormPanel.getExtFormPanel()]);	
			userWindow.setConfig( config );
			userWindow.setButtons( buttons );
		}
		return userWindow;
	}
	return thoj;
})({});