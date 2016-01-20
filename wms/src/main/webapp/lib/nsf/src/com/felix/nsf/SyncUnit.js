com.felix.nsf.SyncUnit = function(){
	var handler;
	var eventList = [];
	var eventCount;
	
	this.setHandler = function(h){
		handler = h;
	}
	
	this.setEventCount = function(count){
		eventCount = count
	}
	
	this.addEvent = function(){
		var e = new com.felix.nsf.Event();
		e.setSyncUnit(this);
		eventList.push(e);
		return e.happen;
	}
	
	this.callHandlerIfAllEventHappened = function(){
		if( eventList.length<eventCount )
			return;
			
		for( var i=0;i<eventList.length; i++){
			if( !eventList[i].isHappened() ) 
				return;
		}
		handler();
	}
};

com.felix.nsf.Event = function(){
	var thoj = this;
	var su;
	var happened = false;
	
	this.setSyncUnit = function( syncUnit ) {
		su = syncUnit;
	}
	
	this.isHappened = function(){
		return happened;
	}
	
	this.happen = function(){
		happened = true;
		su.callHandlerIfAllEventHappened();
	}
};