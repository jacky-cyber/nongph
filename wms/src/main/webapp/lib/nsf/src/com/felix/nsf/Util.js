com.felix.nsf.Util = ( function(thoj){
	thoj.queryFromUrl = function queryFromUrl(param){   
	    var str = location.href;
	    var num = str.indexOf("?");    
	    str=str.substr(num+1);
	    var arrtmp=str.split("&"); 
	    for(var i=0;i < arrtmp.length;i++){
	        num=arrtmp[i].indexOf("=");
	        if(num>0 && param==arrtmp[i].substring(0,num))
	           return arrtmp[i].substr(num+1);
	    }
	    return '';
	};
	
	thoj.bindEvent = function(obj, events){
		if( events!=null ) {
			Ext.iterate( events, function(key, value) {
				if(	value!='' )
					obj.addListener(key, value);
			}, this);
		}
	};
	
	thoj.extend = function( subClassObject, parentClass ) {
		var pcObject = new parentClass();
		for ( property in pcObject ) {
	 		subClassObject[property] = pcObject[property];
	 	}
		subClassObject.super = pcObject;
	};
	
	thoj.onReady = function( readyFunction ){
		com.felix.nsf.Ajax.requestWithoutMessageBox("userAction", "getCurrentUser",function(result){
			com.felix.nsf.Context.currentUser = result;
			Ext.onReady( readyFunction );
		});
	};
	return thoj;
})({});
