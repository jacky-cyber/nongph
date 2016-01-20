com.felix.nsf.ModuleContainer = (function(thoj){
	thoj.addModule = function( pn, mn ){
		if( !thoj[pn] )
			thoj[pn] = {};
		thoj[pn][mn] = new com.felix.nsf.Module(pn, mn);
		return thoj[pn][mn];
	}
	
	thoj.loadModuleIfAbsent = function( pn, mn ) {
		if( !thoj[pn] )
			thoj[pn] = {};
		if( !thoj[pn][mn] ) {
			var modulePath = contextPath+"/"+pn + "/module/" + mn + "/";
			document.write("<script type='text/javascript' src='"+modulePath+"loadModule.js'></script>");
		}
	};
	
	return thoj;
})({});