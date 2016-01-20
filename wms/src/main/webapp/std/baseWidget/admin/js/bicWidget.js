/**
 * BicGridWidget
 */
Ecp.BicGridWidget=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'bics',
		idProperty:'id',
		fields:[
			{name: 'id'},
			{name: 'bic'},
		    {name: 'hqBic'},
		    {name: 'branchBic'},
		    {name: 'name'},
		    {name: 'branchName'},
		    {name: 'country'},
		    {name: 'location'},
		    {name: 'supportEni'},
		    {name: 'type'}]
	};
	
	this.defaultStoreConfig.baseParams={
		cmd:'bic',
		action:'find'
	};
	
	this.defaultCmConfig=[
			{header:TXT.bic_code,dataIndex: 'bic',menuDisabled: true,width:150},
			{header:TXT.bic_hqCode,dataIndex: 'hqBic',menuDisabled: true,width: 150},
			{header:TXT.bic_branchCode,dataIndex: 'branchBic',menuDisabled: true, width: 150},
			{header:TXT.bic_name,dataIndex:'name',menuDisabled: true,width:200},
			{header:TXT.bic_branchName,dataIndex: 'branchName',menuDisabled: true,width: 200},
			{header:TXT.bic_country,dataIndex:'country',menuDisabled: true,width:200},
			{header:TXT.bic_location,dataIndex:'location',menuDisabled: true,width:200},
			{header:TXT.bic_supportMx,dataIndex:'supportEni',menuDisabled: true,width:100,renderer:function(value){
				if (value)
					return TXT.bic_yes;
				else
					return TXT.bic_no;
			}},
			{header:TXT.bic_type,dataIndex:'type',menuDisabled: true,width:100,renderer:function(value){
				if (value == '0')
					return TXT.bic_typeBank;
				else
					return TXT.bic_typeCorp;
			}}
	];
	
	this.defaultGridConfig={
		title:TXT.admin_bic_mng,
		id:'bicGridId',
//		loadMask:true,
		pagination:true
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;
}
Ecp.BicGridWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.BicGridWidget.prototype.setToolBar=function(tbar){
	this.grid.setTopToolBar(tbar);
}

Ecp.BicGridWidget.prototype.setWidgetEvent=function(widgetEvent){
	widgetEvent['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(widgetEvent['grid']);
	widgetEvent['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(widgetEvent['cm']);
	widgetEvent['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(widgetEvent['store']);
}

Ecp.BicGridWidget.prototype.customization=function(customizationConfig){
	this.customizationConfig=customizationConfig;
}

Ecp.BicGridWidget.prototype.render=function(){
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

Ecp.BicGridWidget.prototype.show=function(){
	this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}

Ecp.BicGridWidget.prototype.search=function(params){		
		if(this.grid['pagination']!=null)
	{
		params['start']=0;
		params['limit']=PAGE_SIZE;
	}
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load(params);
}

//observer callback
Ecp.BicGridWidget.prototype.update=function(src,eventName)
{
	switch(eventName)
	{
		case 'reload':
			this.reloadUpdate(src);
			break;
	}
}

Ecp.BicGridWidget.prototype.reloadUpdate=function(src)
{
	this.dataStore.store.reload();
}

// bic toolbar
Ecp.BicToolBarWidget=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.importBic,totalToolBarItem.addBic,
	                               totalToolBarItem.editBic,totalToolBarItem.queryBic];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'bicToolBar'
	};
}

//1
Ecp.BicToolBarWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

//2
Ecp.BicToolBarWidget.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.BicToolBarWidget.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

//3
Ecp.BicToolBarWidget.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.BicToolBarWidget.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	//toolBar.setOwner(this);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}

/**
 * BicInfoForm
 */
Ecp.BicInfoForm=function()
{
	this.form=null;
	this.config={
		labelAlign: 'left',
		region: 'center',
		labelWidth:115,
		frame:true
//		defaultType: 'textfield'
	};
	this.items=[{
					xtype : 'hidden',
					name : 'id',
					id : 'id'
			   },
			   {
   				xtype:'textfield', 
   				fieldLabel: TXT.bic_code,
   				id: 'bic',
   				name: 'bic',
   				width: 200,
   				triggerClass:'x-form-search-trigger',
   				maxLength: 11,
                maxLengthText: TXT.bic_code_max_length,
                minLength: 8,
                minLengthText: TXT.bic_code_min_length,
   				allowBlank:false
   			},
               {
               	xtype:'textfield',
               	fieldLabel: TXT.bic_hqCode,
               	id: 'hqBic',
               	name: 'hqBic',
               	width: 200,
               	maxLength: 8,
               	maxLengthText: TXT.hq_bic_code_max_length,
               	minLength: 8,
               	minLengthText: TXT.hq_bic_code_min_length,
               	allowBlank:false
               },
               {
               	xtype: 'textfield',
               	fieldLabel:TXT.bic_branchCode,
               	id: 'branchBic',
               	name: 'branchBic',
               	width: 200,
               	minLength: 3,
               	minLengthText: TXT.branch_bic_code_invalid,
               	maxLength: 3,
               	maxLengthText: TXT.branch_bic_code_invalid
               },
               {
                 	xtype:'textfield',
                 	fieldLabel: TXT.bic_name,
                 	id: 'name',
                 	name: 'name',
                 	width: 200,
                 	maxLength: 100,
                 	maxLengthText: TXT.bic_name_maxLength,
                 	minLength: 1,
                 	minLengthText: TXT.bic_name_minLength,
                 	allowBlank:false
               },
                {
                 	xtype:'textfield',
                 	fieldLabel: TXT.bic_branchName,
                 	id: 'branchName',
                 	name: 'branchName',
                 	width: 200,
                 	maxLength: 255,
                 	maxLengthText: TXT.branch_name2_max_length,
                 	minLength: 1,
                 	minLengthText: TXT.branch_name2_min_length,
                 	allowBlank:false
               },
               {
                 	xtype:'textfield',
                 	fieldLabel: TXT.bic_country,
                 	id: 'country',
                 	name: 'country',
                 	width: 200,
                 	maxLength: 100,
                 	maxLengthText: TXT.bic_country_max_length,
                 	minLength: 1,
                 	minLengthText: TXT.bic_country_min_length,
                 	allowBlank:false
               },
               {
                 	xtype:'textfield',
                 	fieldLabel: TXT.bic_location,
                 	id: 'location',
                 	name: 'location',
                 	width: 200,
                 	maxLength: 100,
                 	maxLengthText: TXT.bic_location_max_length,
                 	minLength: 1,
                 	minLengthText: TXT.bic_location_min_length,
                 	allowBlank:false
               },
               {
                 	xtype:'combo',
					fieldLabel: TXT.case_status,
					id: 'type',
					name: 'type',
					value: '0',
					width: 200,
					store: new Ext.data.SimpleStore({
					fields: ['label','value'],
					data: [
							[TXT.bic_typeBank,'0'],
							[TXT.bic_typeCorp,'1']
						]
					}),
					forceSelection:true,
					displayField:'label',
					valueField: 'value',					                        			
					typeAhead: true,
					mode: 'local',
					triggerAction: 'all',                        			
					selectOnFocus:true,
					editable:false
               },
               {
                 	xtype:'checkbox',
                 	fieldLabel: TXT.bic_supportMx,
                 	boxLabel: 'Yes',
                 	id: 'supportEni',
                 	name: 'supportEni'
               }
	];
	this.buttons=[];
}

Ecp.BicInfoForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.BicInfoForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.BicInfoForm.prototype.render=function()
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
 * BicInfoWindow
 */
Ecp.BicInfoWindow=function()
{
	this.window=null;
	this.config={
		    title:TXT.admin_bic_mng,
	        width:365,
	        height:330,
	        autoScroll :false,
	        layout:'fit',
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
		            clickSaveBicBtn(this);
				}
			},
			{
				text :TXT.common_reset,
				handler : function() {
				var win=this['ecpOwner'];
				win.getItem(0).reset();
				}
			}];
	this.observers=[];
	this.items=[];
}

Ecp.BicInfoWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.BicInfoWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.BicInfoWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.BicInfoWindow.prototype.render=function()
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

Ecp.BicInfoWindow.prototype.addObserver=function(observer)
{
	this.observers.push(observer);
}

/**
 * execute every observer's update method
 */
Ecp.BicInfoWindow.prototype.notifyAll=function(eventName)
{
	for(var i=0;i<this.observers.length;i++)
		this.observers[i].update(this.window.window,eventName);
}

Ecp.BicInfoWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.BicInfoWindow.prototype.show=function(){
	this.window.window.show();
}

Ecp.BicInfoWindow.prototype.setCloseAction=function(isClose){
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
Ecp.BicInfoWindow.singleInfoWindows=[];

/**
 * create ecp window
 */
Ecp.BicInfoWindow.createBicInfoWindow=function(windowObj,observers,fun){
	// form
	var bicInfoForm=new Ecp.BicInfoForm();
	if(windowObj['items'] && windowObj['items'][0])
		bicInfoForm.customization(windowObj['items'][0]);
		
	// window
	var bicInfoWin = new Ecp.BicInfoWindow();
	bicInfoWin.handleWidgetConfig(function(obj){
		if(fun && typeof fun=='function')
			bicInfoWin.handleWidgetConfig(fun);
		obj.items=[bicInfoForm.render()];
	});
	bicInfoWin.customization(windowObj);
	
	// add observer
	if(observers && observers.length>0)
		bicInfoWin.observers=observers;
	bicInfoWin.render();
	return bicInfoWin.window;
}

/**
 * create single add window static function
 */
Ecp.BicInfoWindow.createSingleAddWindow=function(windowObj,observers){
	if(!Ecp.BicInfoWindow.singleInfoWindows[0])
		Ecp.BicInfoWindow.singleInfoWindows[0]=Ecp.BicInfoWindow.createBicInfoWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	return Ecp.BicInfoWindow.singleInfoWindows[0];
}

/**
 * create single edit window static function
 */
Ecp.BicInfoWindow.createSingleEditWindow=function(windowObj,observers){
	if(!Ecp.BicInfoWindow.singleInfoWindows[1])
	{
		Ecp.BicInfoWindow.singleInfoWindows[1]=Ecp.BicInfoWindow.createBicInfoWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	}
	return Ecp.BicInfoWindow.singleInfoWindows[1];
}

/**
 * BicSearchForm
 */
Ecp.BicSearchForm=function()
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
	            	fieldLabel:TXT.bic_hqCode,
	            	id: 'bic',
	            	name: 'bic',
	            	width: 165,
	            	maxLength: 11,
	              	maxLengthText:TXT.bic_name_max_length
                }
	];
	this.buttons=[];
}

Ecp.BicSearchForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.BicSearchForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.BicSearchForm.prototype.render=function()
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
 * BicSearchWindow
 */
Ecp.BicSearchWindow=function()
{
	this.window=null;
	this.config={
		    title:TXT.common_bicQuery,
			width:300,
	        height:120,
	        autoScroll :false,
	        layout:'fit',
	        border:false,
	        modal:true,
	        minimizable: false,
	        maximizable: false,
			layoutConfig: {animate:false},
			resizable: false,
			buttonAlign: 'center'
	};
	this.buttons=[{
		text: TXT.common_btnQuery,
		handler: function(){
		    clickQueryBicBtn(this);
		}
	}];
	this.observers=[];
	this.items=[];
}

Ecp.BicSearchWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.BicSearchWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.BicSearchWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.BicSearchWindow.prototype.render=function()
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

Ecp.BicSearchWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.BicSearchWindow.prototype.setCloseAction=function(isClose){
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
Ecp.BicInfoWindow.singleSearchWindows=null;

/**
 * create single search window static function
 */
Ecp.BicInfoWindow.createSingleSearchWindow=function(windowObj){
	if(!Ecp.BicInfoWindow.singleSearchWindows)
	{
		var bicSearchForm=new Ecp.BicSearchForm();
		if(windowObj['items'] && windowObj['items'][0])
			bicSearchForm.customization(windowObj['items'][0]);
		// window
		var bicSearchWin = new Ecp.BicSearchWindow();
		bicSearchWin.handleWidgetConfig(function(obj){
			obj.setCloseAction(false);
			obj.items=[bicSearchForm.render()];
		});
		bicSearchWin.customization(windowObj);		
		bicSearchWin.render();
		Ecp.BicInfoWindow.singleSearchWindows=bicSearchWin.window;
	}
	return Ecp.BicInfoWindow.singleSearchWindows;
}