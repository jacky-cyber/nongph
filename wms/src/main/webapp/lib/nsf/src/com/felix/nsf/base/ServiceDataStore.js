// dataStore
com.felix.nsf.base.ServiceDataStore = function(){
	this.dataStore = new com.felix.nsf.wrap.DataStore();

	this.handleWidgetConfig = function(handler){
		handler(this);
	}
	
	this.setEventConfig=function(obj){
		this.dataStore.setEventConfig(obj);
	}
	
	this.load=function(baseParams,callback){
		if(baseParams!=undefined)
			this.dataStore.store['baseParams']=baseParams;
		if(baseParams!=undefined && baseParams['cmd']!=null)
			this.dataStore.store['baseParams']['cmd']=baseParams['cmd'];
		else
			this.dataStore.store['baseParams']['cmd']=this.baseParams['cmd'];
		
		if(baseParams!=undefined && baseParams['action']!=null)
			this.dataStore.store['baseParams']['action']=baseParams['action'];
		else
			this.dataStore.store['baseParams']['action']=this.baseParams['action'];
		
		if(callback)
			this.dataStore.store.load({params:this.dataStore.store['baseParams'],callback:callback});
		else
			this.dataStore.store.load();
	}
	
	this.loadData=function(data){
		this.dataStore.store.loadData(data);
	}
	
	this.reload=function(){
		this.dataStore.store.reload();
	}
	
	this.removeAll=function(){
		this.dataStore.store.removeAll();
	}
	
	this.render=function(){
		this.dataStore.setConfig(this.defaultStoreConfig);
		this.dataStore['ecpOwner']=this;
		this.dataStore.init();
		return this.dataStore.render();
	}
}