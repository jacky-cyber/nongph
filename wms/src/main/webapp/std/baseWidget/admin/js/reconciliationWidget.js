Ecp.ReconciliationGrid=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'results',
		idProperty:'messageType',
		fields:[
			{
				name :'messageType'
			}, {
				name :'powerxO'
			}, {
				name :'powerxI'
			}, {
				name :'saaO'
			}, {
				name :'saaI'
			} ]
	};
	
	this.defaultStoreConfig.baseParams={
		//cmd:'log',
		//action:'find'
	};
	
	this.defaultCmConfig=[
		{
			header :TXT.reconciliation_messageType,
			dataIndex :'messageType',
			width :200
		}, {
			header :TXT.reconciliation_powerO,
			dataIndex :'powerxO',
			width :150
		}, {
			header :TXT.reconciliation_powerI,
			dataIndex :'powerxI',
			width :150
		}, {
			header :TXT.reconciliation_saaO,
			dataIndex :'saaO',
			width :150,
			renderer:function(value,metadata,record)
			{
				if(record.data['powerxO']!=value)
					return '<font color="red">'+value+'</font>';
				return value;
			}
		}, {
			header :TXT.reconciliation_saaI,
			dataIndex :'saaI',
			width :150,
			renderer:function(value,metadata,record)
			{
				if(record.data['powerxI']!=value)
					return '<font color="red">'+value+'</font>';
				return value;
			}
		}
	];
	
	this.defaultGridConfig={
		title:TXT.reconciliation,
		id:'reconciliationGridId',
		pagination:true
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;
}
Ecp.ReconciliationGrid.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.ReconciliationGrid.prototype.setToolBar=function(tbar){
	this.grid.setTopToolBar(tbar);
}

Ecp.ReconciliationGrid.prototype.setWidgetEvent=function(widgetEvent){
	widgetEvent['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(widgetEvent['grid']);
	widgetEvent['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(widgetEvent['cm']);
	widgetEvent['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(widgetEvent['store']);
}
Ecp.ReconciliationGrid.prototype.customization=function(customizationConfig){
	this.customizationConfig=customizationConfig;
}

Ecp.ReconciliationGrid.prototype.render=function(){
	this.dataStore.setConfig(this.defaultStoreConfig);
	this.dataStore.init();
	this.grid.setStore(this.dataStore.render());
	this.grid.setColumnMode(this.defaultCmConfig);
	this.grid.setSelectMode(this.defaultSelModel);
	this.grid.setConfig(this.defaultGridConfig);
	this.grid.init();
	this.customizationConfig['grid']==null?1:this.grid.customization(this.customizationConfig['grid']);
	this.grid['ecpOwner']=this;
	return this.grid.render();
}

Ecp.ReconciliationGrid.prototype.show=function(){
	this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}

Ecp.ReconciliationGrid.prototype.search=function(params){
	if(this.grid['pagination']!=null)
	{
		params['start']=0;
		params['limit']=PAGE_SIZE;
	}
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load(params);
}

//observer callback
Ecp.ReconciliationGrid.prototype.update=function(src,eventName)
{
	switch(eventName)
	{
		case 'reload':
			this.reloadUpdate(src);
			break;
	}
}

Ecp.ReconciliationGrid.prototype.reloadUpdate=function(src)
{
	this.dataStore.store.reload();
}

// toolbar
Ecp.ReconciliationToolBar=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.importReconciliationFile];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'reconciliationToolBar'
	};
}

Ecp.ReconciliationToolBar.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

Ecp.ReconciliationToolBar.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.ReconciliationToolBar.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

Ecp.ReconciliationToolBar.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.ReconciliationToolBar.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	//toolBar.setOwner(this);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}
