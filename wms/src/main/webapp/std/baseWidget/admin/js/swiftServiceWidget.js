/******************   Ecp.SwiftServiceGrid    ************************/
Ecp.SwiftServiceGrid=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'swiftServices',
		idProperty:'id',
		fields:[
			{name: 'id'},
			{name: 'productionEvm'},
			{name: 'bic'},
			{name: 'serviceName'},
			{name: 'dn'},
			{name: 'instName'},
			{name: 'cugCategory'},
			{name: 'operational'},
			{name: 'isDefault'}]
	};
	
	this.defaultStoreConfig.baseParams={
		cmd:'swiftService',
		action:'find',
		start:0,
		limit:25
	};
	
	this.defaultCmConfig=[
		{header:TXT.swift_production_evm,menuDisabled: true,dataIndex: 'productionEvm',width:150},
		{header:TXT.swift_service_name,menuDisabled: true,dataIndex: 'serviceName',width: 150},
		{header:TXT.swift_bic_code,menuDisabled: true,dataIndex: 'bic', width: 150},
		{header:TXT.swift_inst_name,menuDisabled: true,dataIndex:'instName',width:385},
		{header:TXT.swift_dn,menuDisabled: true,dataIndex: 'dn',width: 200},
		{header:TXT.swift_cug_category,menuDisabled: true,dataIndex:'cugCategory',width:200},
		{header:TXT.swift_operational,menuDisabled: true,dataIndex:'operational',width:100,renderer:function(value){
			if (value)
				return "Yes";
			else
				return "No";
		}},
		{header:TXT.swift_default,dataIndex:'isDefault',width:100,renderer:function(value){
			if(value)
				return "Yes";
			else
				return "No";
		}}
	];
	
	this.defaultGridConfig={
		title:TXT.swift_title,
		id:'swiftServiceGridId',
		pagination:true
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;
}
Ecp.SwiftServiceGrid.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.SwiftServiceGrid.prototype.setToolBar=function(tbar){
	this.grid.setTopToolBar(tbar);
}
Ecp.SwiftServiceGrid.prototype.setWidgetEvent=function(widgetEvent){
	widgetEvent['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(widgetEvent['grid']);
	widgetEvent['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(widgetEvent['cm']);
	widgetEvent['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(widgetEvent['store']);
}
Ecp.SwiftServiceGrid.prototype.customization=function(customizationConfig){
	this.customizationConfig=customizationConfig;
}

Ecp.SwiftServiceGrid.prototype.render=function(){
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

Ecp.SwiftServiceGrid.prototype.show=function(){
	this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}

Ecp.SwiftServiceGrid.prototype.search=function(params){
	if(this.grid['pagination']!=null)
	{
		params['start']=0;
		params['limit']=PAGE_SIZE;
	}
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load(params);
}

//observer callback
Ecp.SwiftServiceGrid.prototype.update=function(src,eventName)
{
	switch(eventName)
	{
		case 'reload':
			this.reloadUpdate(src);
			break;
	}
}

Ecp.SwiftServiceGrid.prototype.reloadUpdate=function(src)
{
	this.dataStore.store.reload();
}

// currency toolbar
Ecp.SwiftServiceToolBar=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.importSwiftService,
	                               totalToolBarItem.addSwiftService,
	                               totalToolBarItem.editSwiftService,
	                               totalToolBarItem.deleteSwiftService,
	                               totalToolBarItem.querySwiftService];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'swiftServiceToolBar'
	};
}

//1
Ecp.SwiftServiceToolBar.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

//2
Ecp.SwiftServiceToolBar.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.SwiftServiceToolBar.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

//3
Ecp.SwiftServiceToolBar.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.SwiftServiceToolBar.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	//toolBar.setOwner(this);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}
/******************   Ecp.SwiftServiceGrid    ************************/

/******************   Ecp.SwiftServiceWindow    ************************/
Ecp.SwiftServiceWindow=function()
{
	this.window=null;
	this.config={
	        width:450,
	        height:300,
	        autoScroll :false,
	        layout:'fit',
	        border:false,
	        title:TXT.swift_title,
	        minimizable: false,
	        maximizable: false,
	        resizable: false,
	        modal:true,
			layoutConfig: {animate:false},
			buttonAlign: 'center'
	};
	this.buttons=[{
			text :TXT.common_btnOK,
				handler : function() {
					clickSaveSwiftServiceBtn(this);
				}
			},
			{
				text :TXT.swift_reset,
				handler : function() {
					Ecp.components.swiftServiceForm.reset();
				}
			}];
	this.observers=[];
	this.items=[];
}

Ecp.SwiftServiceWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.SwiftServiceWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.SwiftServiceWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.SwiftServiceWindow.prototype.render=function()
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

Ecp.SwiftServiceWindow.prototype.addObserver=function(observer)
{
	this.observers.push(observer);
}

/**
 * execute every observer's update method
 */
Ecp.SwiftServiceWindow.prototype.notifyAll=function(eventName)
{
	for(var i=0;i<this.observers.length;i++)
		this.observers[i].update(this.window.window,eventName);
}

Ecp.SwiftServiceWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.SwiftServiceWindow.prototype.setCloseAction=function(isClose){
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
 * single window static array
 */
Ecp.SwiftServiceWindow.singleInfoWindows=[];

/**
 * create ecp window
 */
Ecp.SwiftServiceWindow.createSwiftServiceWindow=function(windowObj,observers,fun){
	var swiftServiceForm=new Ecp.SwiftServiceForm();

	if(windowObj['items'] && windowObj['items'][0])
		swiftServiceForm.customization(windowObj['items'][0]);
	
	Ecp.components.swiftServiceForm=swiftServiceForm;
	
	// window
	var swiftServiceWin = new Ecp.SwiftServiceWindow();
	swiftServiceWin.handleWidgetConfig(function(obj){
		if(fun && typeof fun=='function')
			swiftServiceWin.handleWidgetConfig(fun);
		obj.items=[swiftServiceForm.render()];
	});
	swiftServiceWin.customization(windowObj);
	
	// add observer
	if(observers && observers.length>0)
		swiftServiceWin.observers=observers;
	swiftServiceWin.render();
	return swiftServiceWin.window;
}

/**
 * create single add window static function
 */
Ecp.SwiftServiceWindow.createSingleAddWindow=function(windowObj,observers){
	if(!Ecp.SwiftServiceWindow.singleInfoWindows[0])
		Ecp.SwiftServiceWindow.singleInfoWindows[0]=Ecp.SwiftServiceWindow.createSwiftServiceWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	return Ecp.SwiftServiceWindow.singleInfoWindows[0];
}

/**
 * create single edit window static function
 */
Ecp.SwiftServiceWindow.createSingleEditWindow=function(windowObj,observers){
	if(!Ecp.SwiftServiceWindow.singleInfoWindows[1])
	{
		Ecp.SwiftServiceWindow.singleInfoWindows[1]=Ecp.SwiftServiceWindow.createSwiftServiceWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	}
	return Ecp.SwiftServiceWindow.singleInfoWindows[1];
}
/******************   Ecp.SwiftServiceWindow    ************************/

/******************   Ecp.SwiftServiceForm    ************************/
Ecp.SwiftServiceForm=function()
{
	this.form=null;
	this.config={
		labelAlign:'left',
		region:'center',
		labelWidth:115,
		frame:true
	};
	this.items=[
				{
				  	xtype:'hidden',
				  	id: 'id',
				  	name: 'id'
				},
	            {
                  	xtype:'textfield',
                  	fieldLabel: TXT.swift_production_evm,
                  	id: 'productionEvm',
                  	name: 'productionEvm',
              		width: 100,
                  	maxLength: 128,
                  	maxLengthText: TXT.swift_service_production_evm_max_length,
                  	minLength: 1,
                  	minLengthText: TXT.swift_service_production_evm_min_length,
                  	allowBlank:false
                },
                {
                	xtype:'textfield',
                	fieldLabel: TXT.swift_service_name,
                	id: 'serviceName',
                	name: 'serviceName',
                	width: 100,
                	maxLength: 128,
                	maxLengthText: TXT.swift_service_service_name_max_length,
                	minLength:1,
                	maxLengthText: TXT.swift_service_service_name_min_length,
                	allowBlank:false
                },
                {
                	xtype: 'textfield',
                	fieldLabel:TXT.swift_bic_code,
                	id: 'bic',
                	name: 'bic',
                	width: 100,
                	maxLength: 11,
                  	maxLengthText: TXT.bic_name_max_length,
                  	minLength: 8,
                  	minLengthText: TXT.bic_name_min_length,
                  	allowBlank:false
                },
                {
                  	xtype:'textfield',
                  	fieldLabel: TXT.swift_inst_name,
                  	id: 'instName',
                  	name: 'instName',
                  	width: 200,
                  	maxLength: 255,
                  	maxLengthText: TXT.swift_service_inst_name_max_length,
                  	minLength: 1,
                  	minLengthText: TXT.swift_service_inst_name_min_length,
                  	allowBlank:false
                },
                 {
                  	xtype:'textfield',
                  	fieldLabel: TXT.swift_dn,
                  	id: 'dn',
                  	name: 'dn',
                  	width: 200,
                  	maxLength: 255,
                  	maxLengthText: TXT.swift_service_dn_max_length,
                  	minLength: 1,
                  	minLengthText: TXT.swift_service_dn_min_length,
                  	allowBlank:false
                },
                {
                  	xtype:'textfield',
                  	fieldLabel: TXT.swift_cug_category,
                  	id: 'cugCategory',
                  	name: 'cugCategory',
                  	width: 200,
                  	maxLength: 128,
                  	maxLengthText: TXT.swift_cug_cagegory_max_length,
                  	minLength: 1,
                  	minLengthText: TXT.swift_cug_cagegory_min_length,
                  	allowBlank:false
                },
                {
                  	xtype:'checkbox',
                  	fieldLabel: TXT.swift_operational,
                  	boxLabel: 'Yes',
                  	id: 'operational',
                  	name: 'operational'
                },
                {
                	xtype:'checkbox',
                  	fieldLabel: TXT.swift_default,
                  	boxLabel: 'Yes',
                  	id: 'isDefault',
                  	name: 'isDefault'
                }
	];
	this.buttons=[];
}

Ecp.SwiftServiceForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.SwiftServiceForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.SwiftServiceForm.prototype.render=function()
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

Ecp.SwiftServiceForm.prototype.reset=function()
{
	this.form.reset();
}
/******************   Ecp.SwiftServiceForm    ************************/

/******************   Ecp.SwiftServiceSearchWindow    ************************/
Ecp.SwiftServiceSearchWindow=function()
{
	this.window=null;
	this.config={
			width:300,
	        height:120,
	        autoScroll :false,
	        layout:'fit',
	        border:false,
	        title:TXT.common_btnQuery,
	        modal:true,
	        minimizable: false,
	        maximizable: false,
			layoutConfig: {animate:false},
			resizable: false,
			buttonAlign: 'center'
	};
	this.buttons=[{
		text: TXT.common_btnOK,
		handler: function(){
			clickQuerySwiftServiceBtn(this);
		}},{text: TXT.swift_reset,handler: function(){Ecp.components.swiftServiceSearchForm.reset();}}];
	this.observers=[];
	this.items=[];
}

Ecp.SwiftServiceSearchWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.SwiftServiceSearchWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.SwiftServiceSearchWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.SwiftServiceSearchWindow.prototype.render=function()
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

Ecp.SwiftServiceSearchWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.SwiftServiceSearchWindow.prototype.setCloseAction=function(isClose){
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
Ecp.SwiftServiceSearchWindow.createSwiftServiceSearchWindow=function(windowObj,observers,fun){
	var swiftServiceSearchForm=new Ecp.SwiftServiceSearchForm();

	if(windowObj['items'] && windowObj['items'][0])
		swiftServiceSearchForm.customization(windowObj['items'][0]);
	
	// window
	var swiftServiceSearchWindow = new Ecp.SwiftServiceSearchWindow();
	swiftServiceSearchWindow.handleWidgetConfig(function(obj){
		if(fun && typeof fun=='function')
			swiftServiceSearchWindow.handleWidgetConfig(fun);
		obj.items=[swiftServiceSearchForm.render()];
	});
	swiftServiceSearchWindow.customization(windowObj);
	
	// add observer
	if(observers && observers.length>0)
		swiftServiceSearchWindow.observers=observers;
	swiftServiceSearchWindow.render();
	return swiftServiceSearchWindow.window;
}

/**
 * single search window static array
 */
Ecp.SwiftServiceSearchWindow.singleSearchWindows=null;

/**
 * create single search window static function
 */
Ecp.SwiftServiceSearchWindow.createSingleSearchWindow=function(windowObj,observers){
	if(!Ecp.SwiftServiceSearchWindow.singleSearchWindows)
		Ecp.SwiftServiceSearchWindow.singleSearchWindows=Ecp.SwiftServiceSearchWindow.createSwiftServiceSearchWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	return Ecp.SwiftServiceSearchWindow.singleSearchWindows;
}
/******************   Ecp.SwiftServiceSearchWindow    ************************/

/******************   Ecp.SwiftServiceSearchForm    ************************/
Ecp.SwiftServiceSearchForm=function()
{
	this.form=null;
	this.config={
			labelAlign: 'left',
			region: 'center',
		    labelWidth:78,
		    bodyStyle:'margin-top:10px',
		    frame:true
	};
	this.items=[
				{
					xtype: 'textfield',
	            	fieldLabel:TXT.swift_bic_code,
	            	id: 'bicSearch',
	            	name: 'bicSearch',
	            	width: 165,
	            	maxLength: 11,
                  	maxLengthText: TXT.bic_name_max_length,
                  	minLength: 1,
                  	minLengthText: TXT.bic_name_min_length
                }
	];
	this.buttons=[];
}

Ecp.SwiftServiceSearchForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.SwiftServiceSearchForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.SwiftServiceSearchForm.prototype.render=function()
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

Ecp.SwiftServiceSearchForm.prototype.reset=function()
{
	this.form.reset();
}
/******************   Ecp.SwiftServiceSearchForm    ************************/