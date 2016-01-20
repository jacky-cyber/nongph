com.felix.nsf.ServiceGrid = function(){
	this.dataStore = new com.felix.nsf.wrap.DataStore();
	this.grid = new com.felix.nsf.wrap.Grid();
	this.defaultSelModel = 0;

	this.handleWidgetConfig = function(handler){
		handler(this);
	}
	
	this.setWidgetEvent = function( widgetEvent ) {
		if( widgetEvent['grid']==null )
			widgetEvent['grid'] = {};
		if( widgetEvent['cm']==null )
			widgetEvent['cm'] = {};
		if( widgetEvent['sm'] == null )
			widgetEvent['sm'] = {}; 
		if( widgetEvent['store']==null )
			widgetEvent['store'] = {};
		
		this.grid.setGridEvent( widgetEvent['grid'] );
		this.grid.setCmGridEvent(widgetEvent['cm']);
		this.grid.setSmGridEvent(widgetEvent['sm']);
		this.dataStore.setEventConfig(widgetEvent['store']);
	}
	
	this.doSort = function(){
		if(this.grid.gridConfig['tbar'])
		{
			var sorting=this.grid.gridConfig['tbar']['ecpOwner'].getSortParams();
			if(sorting && sorting.length>0)
			{
				var params=this.dataStore.store['baseParams'];
				if(this.defaultGridConfig['pagination']!=null)
				{
					params['start']=0;
					params['limit']=PAGE_SIZE;
				}
				var sortingJson=[];
				for(var i=0;i<sorting.length;i++)
					sortingJson[i]={field:sorting[i].field,direction:sorting[i].direction.toUpperCase(),idx:sorting[i].idx};
				params['sorting']=Ext.util.JSON.encode(sortingJson);
				this.dataStore.store.load(params);
			}
		}
	}
	
	this.setToolBar = function(tbar){
		this.grid.setTopToolBar(tbar);
	}
	
	this.render = function(){
		this.dataStore.setConfig(this.defaultStoreConfig);
		this.dataStore.init();
		this.grid.setStore(this.dataStore.render());
		this.grid.setColumnMode(this.defaultCmConfig);
		this.grid.setSelectMode(this.defaultSelModel);
		this.grid.setConfig(this.defaultGridConfig);
		this.grid.init();
		this.grid['ecpOwner']=this;
		return this.grid.render();
	}
	
	this.showAll = function(){
		this.dataStore.store.load();
	}
	
	this.getData = function( resultKey ){
		var array=[];
		//this.grid.grid.store.commitChanges();
		var records= this.dataStore.store.getRange();
		for(var i=0;i<records.length;i++)
			array.push(records[i].data);
		var json=Ext.util.JSON.encode(array);
		if(resultKey)
			return '{"'+resultKey+'":'+json+'}';
		return json;
	}
	
	this.isEmpty = function(){
		if((this.grid.gridCompConfig['sm']==0 || this.grid.gridCompConfig['sm']==1) && this.dataStore.store.getRange().length>0 ||
				this.grid.gridCompConfig['sm']==2 && this.dataStore.store.getTotalCount()>0 && this.grid.getSelectedCount()>0)
			return false;
		else
			return true;
	}
}