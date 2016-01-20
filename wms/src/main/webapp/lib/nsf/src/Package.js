var Package = function(nm, dp){
	var path = dp +"/"+ nm ;
	var name = nm;
	var subPackages = [];
	var classNames = [];
	
	this.addSubPackage = function(name, ndp){
		if( !subPackages[name] ){
			var dp = null;
			if( ndp )
				dp = ndp;
			else
				dp = path;
			var sp = new Package(name, dp);
			subPackages.push( sp );
			this[name] = sp;
		}
	};
	
	this.addClass = function(name){
		if( !classNames[name] )
			classNames.push( name );
	}
	
	this.loadClasses = function(){
		for( var i=0; i<classNames.length; i++ ) 
			loadClass( classNames[i] );
	
		for( var i=0; i<subPackages.length; i++ )
			var subResult = subPackages[i].loadClasses();
	};
	
	this.getPath = function(){
		return path;
	};
	
	var loadClass = function(cn){
		document.write("<script type='text/javascript' src='"+path+"/"+cn+".js'></script>");
	}
};
	
var com = new Package("com", contextPath+"/lib/nsf/src"); 
com.addSubPackage("felix");
com.felix.addSubPackage("nsf");
com.felix.nsf.addSubPackage("base");
com.felix.nsf.addSubPackage("wrap");

com.felix.nsf.addClass("GlobalConstants");
com.felix.nsf.addClass("Ajax");
com.felix.nsf.addClass("Context");
com.felix.nsf.addClass("GlobalView");
com.felix.nsf.addClass("MessageBox");
com.felix.nsf.addClass("Util");
com.felix.nsf.addClass("Module");
com.felix.nsf.addClass("ModuleContainer");
com.felix.nsf.addClass("ModuleDependency");
com.felix.nsf.addClass("ShortcutMap");
com.felix.nsf.addClass("SyncUnit");

com.felix.nsf.wrap.addClass("BaseWrap");
com.felix.nsf.wrap.addClass("DataStore");
com.felix.nsf.wrap.addClass("FormPanel");
com.felix.nsf.wrap.addClass("GridPanel");
com.felix.nsf.wrap.addClass("TabPanel");
com.felix.nsf.wrap.addClass("ToolBar");
com.felix.nsf.wrap.addClass("TreeGrid");
com.felix.nsf.wrap.addClass("Window");

com.felix.nsf.base.addClass("ServiceDataStore");
com.felix.nsf.base.addClass("ServiceForm");
com.felix.nsf.base.addClass("ServiceGrid");
com.felix.nsf.base.addClass("ServiceTabPanel");
com.felix.nsf.base.addClass("ServiceToolbar");
com.felix.nsf.base.addClass("ServiceWindow");

com.felix.nsf.loadClasses();