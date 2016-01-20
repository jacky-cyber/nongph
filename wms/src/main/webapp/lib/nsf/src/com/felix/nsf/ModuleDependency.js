com.felix.nsf.ModuleDependency = function( pn, mn ) {
	var projectName = pn;
	var moduleName  = mn;
	
	this.getProjectName = function(){
		return projectName;
	};
	
	this.getModuleName = function(){
		return moduleName;
	};
};