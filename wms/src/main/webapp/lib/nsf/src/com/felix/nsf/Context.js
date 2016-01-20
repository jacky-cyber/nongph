com.felix.nsf.Context = (function(thoj){
	thoj.currentComponents = {};
	thoj.currentUser = null;
	thoj.currrentLocale = null;
	thoj.contextPath = contextPath;
	thoj.locale = locale;
	thoj.globalView = null;
	
	return thoj;
})({});

function changeSkin(css) {
	var date=new Date();
	date.setTime(date.getTime()+30*24*3066*1000);
	document.getElementsByTagName("link")[1].href=contextPath+"/lib/extjs/resources/css/"+css;
	document.cookie="css="+css+";path="+contextPath+"; expires="+date.toGMTString();
	
	if(APP_MENU_TYPE==2)
		for(var i=0;i<opendPages.length;i++)
		{
			var page=document.getElementById(opendPages[i]);
			if(page!=null)
				page.contentWindow.createGlobalSkin(); 
		}
}

function logout(){
	Ecp.MessageBox.confirm(TXT.app_sure_quit, function(){
		Ecp.Ajax.request({cmd:'login',action:'logOff',type:'y'}, function(result) {
				window.location.href = URL_LOGOUT;
		});
	}, null);
}