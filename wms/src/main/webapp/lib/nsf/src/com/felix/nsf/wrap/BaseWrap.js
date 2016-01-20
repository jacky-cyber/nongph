com.felix.nsf.wrap.BaseWrap = function(){
	this.WRAP_FLAG = true;
	
	var parent = null;
	
	this.getParent = function(){
		return parent;
	};
	
	this.setParent = function( p ) {
		parent = p;
	}
	
	this.getExtComponent = function( wrapComponent ) {
		if( Ext.isArray(wrapComponent) ) {
			var result = [];
			for( var i=0; i<wrapComponent.length; i++ ) {
				result[i] = this.getExtComponent(wrapComponent[i]);
			}
			return result;
		} else {
			if( wrapComponent.WRAP_FLAG ) {
				for ( property in wrapComponent ) {
					if( (typeof wrapComponent[property]) == "function" ){
						if( property!="getExtComponent" && property.indexOf("getExt")==0 ) {
							return wrapComponent[property]();
						}
					}
			 	}
		 	}
		 	return  wrapComponent;
		}
	}
}