/******************   Ecp.LogGrid    ************************/
Ecp.LogGrid=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'traces',
		idProperty:'id',
		fields:[
			{
				name :'id'
			}, {
				name :'actionType'
			}, {
				name :'action'
			}, {
				name :'user'
			}, {
				name :'actionTime'
			} ]
	};
	
	var json = {
		'user':'',
		'dateFrom' :'',
		'dateTo' :''
	};
	
	this.defaultStoreConfig.baseParams={
		cmd:'log',
		action:'find',
		json:Ext.util.JSON.encode(json)
	};
	
	this.defaultCmConfig=[
		{
			header :TXT.log_action_type,
			dataIndex :'actionType',
			width :120,
			renderer : function(value) {
				if (TXT.log_action_type_mapping[value] != undefined)
					return TXT.log_action_type_mapping[value];
				return value;
			}
		}, {
			header :TXT.log_action,
			dataIndex :'action',
			width :300
		}, {
			header :TXT.log_user,
			dataIndex :'user',
			width :120
		}, {
			header :TXT.log_time,
			dataIndex :'actionTime',
			width :150
		}
	];
	
	this.defaultGridConfig={
		title:TXT.log_title,
		id:'logGridId',
		pagination:true
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;
}
Ecp.LogGrid.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.LogGrid.prototype.setToolBar=function(tbar){
	this.grid.setTopToolBar(tbar);
}

Ecp.LogGrid.prototype.setWidgetEvent=function(widgetEvent){
	widgetEvent['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(widgetEvent['grid']);
	widgetEvent['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(widgetEvent['cm']);
	widgetEvent['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(widgetEvent['store']);
}
Ecp.LogGrid.prototype.customization=function(customizationConfig){
	this.customizationConfig=customizationConfig;
}

Ecp.LogGrid.prototype.render=function(){
	this.dataStore.setConfig(this.defaultStoreConfig);
	this.dataStore.init();
	//this.customizationConfig['store']==null?1:this.dataStore.customization(this.customizationConfig['store']);
	this.grid.setStore(this.dataStore.render());
	//alert(typeof this.dataStore.store=='Object');
	this.grid.setColumnMode(this.defaultCmConfig);
	this.grid.setSelectMode(this.defaultSelModel);
	this.grid.setConfig(this.defaultGridConfig);
	this.grid.init();
	this.customizationConfig['grid']==null?1:this.grid.customization(this.customizationConfig['grid']);
	this.grid['ecpOwner']=this;
	return this.grid.render();
}

Ecp.LogGrid.prototype.show=function(){
	this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}

Ecp.LogGrid.prototype.search=function(params){
	if(this.grid['pagination']!=null)
	{
		params['start']=0;
		params['limit']=PAGE_SIZE;
	}
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load(params);
}

//observer callback
Ecp.LogGrid.prototype.update=function(src,eventName)
{
	switch(eventName)
	{
		case 'reload':
			this.reloadUpdate(src);
			break;
	}
}

Ecp.LogGrid.prototype.reloadUpdate=function(src)
{
	this.dataStore.store.reload();
}

// currency toolbar
Ecp.LogToolBar=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.queryLog];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'logToolBar'
	};
}

//1
Ecp.LogToolBar.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

//2
Ecp.LogToolBar.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.LogToolBar.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

//3
Ecp.LogToolBar.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.LogToolBar.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	//toolBar.setOwner(this);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}
/******************   Ecp.LogGrid    ************************/

/******************   Ecp.LogSearchForm    ************************/
Ecp.LogSearchForm=function()
{
	this.form=null;
	this.config={
		labelAlign :'left',
		region :'center',
		labelWidth :90,
		frame :true
	};
	this.items=[
		{
			xtype :'textfield',
			fieldLabel :TXT.log_user,
			id :'user',
			name :'user',
			anchor :'95%'
		},{
			xtype :'datefield',
			fieldLabel :TXT.case_dateFrom,
			format :'Y-m-d',
			id :'dateFrom',
			name :'dateFrom',
			anchor :'95%'
		}, {
			xtype :'datefield',
			fieldLabel :TXT.case_dateTo,
			format :'Y-m-d',
			id :'dateTo',
			name :'dateTo',
			anchor :'95%'
		} 
	];
	this.buttons=[];
}

Ecp.LogSearchForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.LogSearchForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.LogSearchForm.prototype.render=function()
{
	this.form=new Ecp.FormPanel();
	this.form.init();
	var obj={};
	obj['config']=this.config;
	obj['buttons']=this.buttons;
	obj['items']=this.items;
	this.form.customization(obj);
	return this.form.render();
}

Ecp.LogSearchForm.prototype.reset=function()
{
	this.form.reset();
}
/******************   Ecp.LogSearchForm    ************************/

/******************   Ecp.LogSearchWindow    ************************/
Ecp.LogSearchWindow=function()
{
	this.window=null;
	this.config={
			title :TXT.log_search,
			width :300,
			height :160,
			autoScroll :false,
			layout :'fit',
			border :false,
//			closeAction :'hide',
			minimizable :false,
			maximizable :false,
			layoutConfig : {
				animate :false
			},
			resizable :false,
			buttonAlign :'center'
	};
	this.buttons=[{
		text :TXT.common_btnOK,
		handler : function() {
			clickQueryLogBtn(this);
		}}, {text :TXT.common_reset,handler : function() {Ecp.components.logSearchForm.reset();}}];
	this.observers=[];
	this.items=[];
}

Ecp.LogSearchWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.LogSearchWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.LogSearchWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.LogSearchWindow.prototype.render=function()
{
	this.window=new Ecp.Window();
	this.window.init();
	var winObj={};
	winObj['config']=this.config;
	winObj['buttons']=this.buttons;
	winObj['items']=this.items;
	this.window.customization(winObj);
	this.window['ecpOwner']=this;
	return this.window.render();
}

Ecp.LogSearchWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.LogSearchWindow.prototype.setCloseAction=function(isClose){
	if(!isClose)
	{
		this.config.closeAction='hide';
		this.config.onHide=function(){
			this.hide();
		}
	}else{
		this.config.closeAction='close';
		this.config.onHide=function(){
			this.close();
		}
	}
}

/**
 * create ecp window
 */
Ecp.LogSearchWindow.createLogSearchWindow=function(windowObj,observers,fun){
	var logSearchForm=new Ecp.LogSearchForm();

	if(windowObj['items'] && windowObj['items'][0])
		logSearchForm.customization(windowObj['items'][0]);
	
	Ecp.components.logSearchForm=logSearchForm;
	
	// window
	var logSearchWindow = new Ecp.LogSearchWindow();
	logSearchWindow.handleWidgetConfig(function(obj){
		if(fun && typeof fun=='function')
			logSearchWindow.handleWidgetConfig(fun);
		obj.items=[logSearchForm.render()];
	});
	logSearchWindow.customization(windowObj);
	
	// add observer
	if(observers && observers.length>0)
		logSearchWindow.observers=observers;
	logSearchWindow.render();
	return logSearchWindow.window;
}

/**
 * single search window static array
 */
Ecp.LogSearchWindow.singleSearchWindows=null;

/**
 * create single search window static function
 */
Ecp.LogSearchWindow.createSingleSearchWindow=function(windowObj,observers, fun){
	if(!Ecp.LogSearchWindow.singleSearchWindows)
		Ecp.LogSearchWindow.singleSearchWindows=Ecp.LogSearchWindow.createLogSearchWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	return Ecp.LogSearchWindow.singleSearchWindows;
}
/******************   Ecp.LogSearchWindow    ************************/