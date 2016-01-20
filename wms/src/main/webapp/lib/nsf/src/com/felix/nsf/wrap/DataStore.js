/**
 * com.felix.nsf.DataStore
 * 
 * usage:load data from server or client's cache
 * 
 * @augments store storeConfig storeEventConfig eventBinder
 */
com.felix.nsf.wrap.DataStore = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.BaseWrap );
	
	//wraped Ext.data.JsonStore
	var extStore=null;
	//Ext.data.JsonStore's configuration
	var storeConfig=null;
	//Ext.data.JsonStore's event such as click dblclick and so on
	var storeEventConfig=null;

	/**
	 * set the configuration of Ext.data.JsonStore
	 * @param {} config
	 * 
	 *There two four kinds of prop hasing its omitted value,anyone of them can be covered by the 'conifg'
	 */
	this.setConfig = function(config){
		storeConfig = config;
		if( storeConfig['autoDestroy']==null ) 
			storeConfig['autoDestroy']=true;
		if( storeConfig['totalProperty']==null )
			storeConfig['totalProperty']='totalCount';
	};

	/**
	 * set the dataStore's configuration of event
	 * 
	 * @param {} eventConfig
	 */
	this.setEventConfig = function(eventConfig){
		storeEventConfig=eventConfig;
	};

	/**
	 * set the params for server
	 * 
	 * @param {} baseParams
	 */
	this.setBaseParam = function(baseParams){
		storeConfig['baseParams']=baseParams;
	};
	
	
	/**
	 * Ecp.DataStore's renderer
	 * @return {} Ext.data.JsonStore
	 */
	this.getExtStore = function(){
		if( extStore==null ) {
			if( storeConfig.groupField )
				extStore = new Ext.data.GroupingStore( storeConfig );
			else
				extStore = new Ext.data.JsonStore( storeConfig );
			extStore.remoteSort=true;
			com.felix.nsf.Util.bindEvent( extStore, storeEventConfig );
		}
		return extStore;
	};
}