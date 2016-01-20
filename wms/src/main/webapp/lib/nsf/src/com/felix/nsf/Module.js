com.felix.nsf.Module = function(project, name){
	var dependeies = [];
	var rootPackage = null;
	
	this.addDependy = function( pn, mn ){
		var md = new com.felix.nsf.ModuleDependency(pn, mn);
		com.felix.nsf.ModuleContainer.loadModuleIfAbsent( pn, mn );
		dependeies.push( md );
	};
	
	this.getModulePackage = function(){
		return rootPackage;
	};
		
	this.load = function(){
		this.loadI18N();
		rootPackage.loadClasses();
	};
	
	this.loadI18N = function(){
		document.write("<script type='text/javascript' src='"+rootPackage.getPath()+"/lang_"+locale+".js'></script>");
	};
	
	if( !com.felix[project] ) {
		com.felix.addSubPackage( project, contextPath );
		com.felix[project].addSubPackage("module");
	}
	com.felix[project].module.addSubPackage(name);
	rootPackage = com.felix[project].module[name];
};