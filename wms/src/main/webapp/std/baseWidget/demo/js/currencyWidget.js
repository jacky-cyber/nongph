Ecp.CurrencyGridWidget=function(){
	this.dataStore=new Ecp.DataStore();
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'currencies',
		idProperty:'id',
		fields:[
			{name: 'id'},
			{name: 'num'},
			{name: 'currencyCode'},
			{name: 'description'}]
	};
	this.defaultStoreConfig.baseParams={
		cmd:'cur',
		action:'findAll'
	};
	
	this.grid=new Ecp.Grid();
	
	this.defaultCmConfig=[
			{header:TXT.cur_num,dataIndex: 'num',menuDisabled: true,width:150},
			{header:TXT.cur_code,dataIndex:'currencyCode',menuDisabled: true,width:150},
			{header:TXT.cur_description,dataIndex:'description',menuDisabled: true,width:150}
	];
	
	this.defaultGridConfig={
		title:TXT.admin_currency_mng,
		id:'currencyGridId',
		loadMask:true,
		//region:'center',
		pagination:true
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;
}

Ecp.CurrencyGridWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.CurrencyGridWidget.prototype.setToolBar=function(tbar){
	this.grid.setTopToolBar(tbar);
}

Ecp.CurrencyGridWidget.prototype.setWidgetEvent=function(widgetEvent){
	widgetEvent['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(widgetEvent['grid']);
	widgetEvent['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(widgetEvent['cm']);
	widgetEvent['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(widgetEvent['store']);
}

Ecp.CurrencyGridWidget.prototype.customization=function(customizationConfig){
	this.customizationConfig=customizationConfig;
}

Ecp.CurrencyGridWidget.prototype.render=function(){
	this.dataStore.setConfig(this.defaultStoreConfig);
	this.dataStore.init();
	this.customizationConfig['store']==null?1:this.dataStore.customization(this.customizationConfig['store']);
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

Ecp.CurrencyGridWidget.prototype.show=function(){
	this.dataStore.store.load({start:0, limit:PAGE_SIZE});
}

Ecp.CurrencyGridWidget.prototype.search=function(params){
	if(this.grid['pagination']!=null)
	{
		params['start']=0;
		params['limit']=PAGE_SIZE;
	}
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load(params);
}

//observer callback
Ecp.CurrencyGridWidget.prototype.update=function(src,eventName)
{
	switch(eventName)
	{
		case 'reload':
			this.reloadUpdate(src);
			break;
	}
}

Ecp.CurrencyGridWidget.prototype.reloadUpdate=function(src)
{
	this.dataStore.store.reload();
}

// currency toolbar
Ecp.CurrencyToolBarWidget=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.addCurrency,totalToolBarItem.deleteCurrency,
	                               totalToolBarItem.editCurrency,totalToolBarItem.queryCurrency];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'currencyToolBar'
	};
}

//1
Ecp.CurrencyToolBarWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

//2
Ecp.CurrencyToolBarWidget.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.CurrencyToolBarWidget.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

//3
Ecp.CurrencyToolBarWidget.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.CurrencyToolBarWidget.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	//toolBar.setOwner(this);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}

/**
 * CurrencyInfoForm
 */
Ecp.CurrencyInfoForm=function()
{
	this.form=null;
	this.config={
		labelAlign: 'left',
		region: 'center',
		frame:true,
		defaultType: 'textfield'
	};
	this.items=[{
					xtype : 'hidden',
					name : 'id',
					id : 'id'
			   },
				{
					fieldLabel: TXT.cur_code,
					id: 'currencyCode',
					name: 'currencyCode',
					width: 150,
					maxLength: 3,
                  	maxLengthText: TXT.cur_code_length,
                  	minLength: 3,
                  	minLengthText: TXT.cur_code_length,
					allowBlank:false
				},
                {
                	fieldLabel: TXT.cur_num,
                	id: 'num',
                	name: 'num',
                	width: 150,
                	allowBlank:false
                },
                {
                	fieldLabel: TXT.cur_description,
                	id: 'description',
                	name: 'description',
                	width: 150,
                	maxLength: 16,
                	allowBlank:false
                }
	];
	this.buttons=[];
}

Ecp.CurrencyInfoForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.CurrencyInfoForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.CurrencyInfoForm.prototype.render=function()
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

/**
 * CurrencyInfoWindow
 */
Ecp.CurrencyInfoWindow=function()
{
	this.window=null;
	this.config={
	        width:300,
	        height:165,
	        autoScroll :false,
	        layout:'fit',
	        title:TXT.cur_info,
	        border:false,
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
					clickSaveCurrencyBtn(this);
				}
			},
			{
				text :TXT.common_btnClose,
				handler : function() {
					var win=this['ecpOwner'];
					win.window.hide();
				}
			}];
	this.observers=[];
	this.items=[];
}

Ecp.CurrencyInfoWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.CurrencyInfoWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.CurrencyInfoWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.CurrencyInfoWindow.prototype.render=function()
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

Ecp.CurrencyInfoWindow.prototype.addObserver=function(observer)
{
	this.observers.push(observer);
}

/**
 * execute every observer's update method
 */
Ecp.CurrencyInfoWindow.prototype.notifyAll=function(eventName)
{
	for(var i=0;i<this.observers.length;i++)
		this.observers[i].update(this.window.window,eventName);
}

Ecp.CurrencyInfoWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.CurrencyInfoWindow.prototype.show=function(){
	this.window.window.show();
}

Ecp.CurrencyInfoWindow.prototype.setCloseAction=function(isClose){
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
Ecp.CurrencyInfoWindow.singleInfoWindows=[];

/**
 * create ecp window
 */
Ecp.CurrencyInfoWindow.createCurrencyInfoWindow=function(windowObj,observers,fun){
	var currencyInfoForm=new Ecp.CurrencyInfoForm();

	if(windowObj['items'] && windowObj['items'][0])
		currencyInfoForm.customization(windowObj['items'][0]);
	
	// window
	var currencyInfoWin = new Ecp.CurrencyInfoWindow();
	currencyInfoWin.handleWidgetConfig(function(obj){
		if(fun && typeof fun=='function')
			currencyInfoWin.handleWidgetConfig(fun);
		obj.items=[currencyInfoForm.render()];
	});
	currencyInfoWin.customization(windowObj);
	
	// add observer
	if(observers && observers.length>0)
		currencyInfoWin.observers=observers;
	currencyInfoWin.render();
	return currencyInfoWin.window;
}

/**
 * create single add window static function
 */
Ecp.CurrencyInfoWindow.createSingleAddWindow=function(windowObj,observers){
	if(!Ecp.CurrencyInfoWindow.singleInfoWindows[0])
		Ecp.CurrencyInfoWindow.singleInfoWindows[0]=Ecp.CurrencyInfoWindow.createCurrencyInfoWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	return Ecp.CurrencyInfoWindow.singleInfoWindows[0];
}

/**
 * create single edit window static function
 */
Ecp.CurrencyInfoWindow.createSingleEditWindow=function(windowObj,observers){
	if(!Ecp.CurrencyInfoWindow.singleInfoWindows[1])
	{
		Ecp.CurrencyInfoWindow.singleInfoWindows[1]=Ecp.CurrencyInfoWindow.createCurrencyInfoWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	}
	return Ecp.CurrencyInfoWindow.singleInfoWindows[1];
}

/**
 * CurrencySearchForm
 */
Ecp.CurrencySearchForm=function()
{
	this.form=null;
	this.config={
			labelAlign: 'left',
			region: 'center',
		    //labelWidth:78,
		    bodyStyle:'margin-top:10px',
		    frame:true
	};
	this.items=[
				{
					xtype: 'textfield',
	            	fieldLabel:TXT.cur_code,
	            	id: 'query',
	            	name: 'query',
	            	width: 165,
	            	maxLength: 3,
	              	maxLengthText: TXT.cur_code_length,
	              	minLength: 3,
	              	minLengthText: TXT.cur_code_length,
	              	allowBlank: false
                }
	];
	this.buttons=[];
}

Ecp.CurrencySearchForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.CurrencySearchForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.CurrencySearchForm.prototype.render=function()
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

/**
 * CurrencySearchWindow
 */
Ecp.CurrencySearchWindow=function()
{
	this.window=null;
	this.config={
			width:300,
	        height:110,
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
			clickQueryCurrencyBtn(this);
		}
	}];
	this.observers=[];
	this.items=[];
}

Ecp.CurrencySearchWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.CurrencySearchWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.CurrencySearchWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.CurrencySearchWindow.prototype.render=function()
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

Ecp.CurrencySearchWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.CurrencySearchWindow.prototype.setCloseAction=function(isClose){
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
 * single search window static array
 */
Ecp.CurrencyInfoWindow.singleSearchWindows=null;

/**
 * create single search window static function
 */
Ecp.CurrencyInfoWindow.createSingleSearchWindow=function(windowObj){
	if(!Ecp.CurrencyInfoWindow.singleSearchWindows)
	{
		var currencySearchForm=new Ecp.CurrencySearchForm();
	
		if(windowObj['items'] && windowObj['items'][0])
			currencySearchForm.customization(windowObj['items'][0]);
		
		// window
		var currencySearchWin = new Ecp.CurrencySearchWindow();
		currencySearchWin.handleWidgetConfig(function(obj){
			obj.items=[currencySearchForm.render()];
			obj.setCloseAction(false);
		});
		currencySearchWin.customization(windowObj);
		currencySearchWin.render();
		Ecp.CurrencyInfoWindow.singleSearchWindows=currencySearchWin.window;
	}
	return Ecp.CurrencyInfoWindow.singleSearchWindows;
}