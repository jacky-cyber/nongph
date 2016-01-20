// base window
com.felix.nsf.base.ServiceWindow = function(){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.Window);
		
	this.observers=[];

	this.addObserver = function(observer){
		this.observers.push(observer);
	}
	
	this.notifyAll = function(eventName) {
		for(var i=0;i<this.observers.length;i++)
			this.observers[i].update(this.window.window,eventName);
	}
}