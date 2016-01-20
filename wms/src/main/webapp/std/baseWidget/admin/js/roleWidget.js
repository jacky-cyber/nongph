/**
 * Role Grid Widget
 */
Ecp.RoleGridWidget=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'roles',
		idProperty:'id',
		fields:[
			{name: 'id'},
			{name: 'rolename'},
			{name: 'description'},
			{name: 'uumsRole'}]
	};
	
	this.defaultStoreConfig.baseParams={
		cmd:'role',
		action:'find'
	};
	
	this.defaultCmConfig=[
			{header:TXT.role_names,dataIndex: 'rolename',menuDisabled: true,width:150},
			{header:TXT.role_uumsRole,dataIndex:'uumsRole',menuDisabled: true,width:200},
			{header:TXT.role_description,dataIndex:'description',menuDisabled: true,width:300}
	];
	
	this.defaultGridConfig={
		title:TXT.role_manager,
		id:'roleGridId',
		stripeRows :true
//		pagination:true
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;
}
//1. product customization
Ecp.RoleGridWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.RoleGridWidget.prototype.setToolBar=function(tbar){
	this.grid.setTopToolBar(tbar);
}

//3 widget event function
Ecp.RoleGridWidget.prototype.setWidgetEvent=function(widgetEvent){
	widgetEvent['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(widgetEvent['grid']);
	widgetEvent['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(widgetEvent['cm']);
	widgetEvent['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(widgetEvent['store']);
}
//4 project customization function
Ecp.RoleGridWidget.prototype.customization=function(customizationConfig){
	this.customizationConfig=customizationConfig;
}

Ecp.RoleGridWidget.prototype.render=function(){
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

Ecp.RoleGridWidget.prototype.show=function(){
	this.dataStore.store.load();
}

//observer callback
Ecp.RoleGridWidget.prototype.update=function(src,eventName)
{
	switch(eventName)
	{
		case 'reload':
			this.reloadUpdate(src);
			break;
	}
}

Ecp.RoleGridWidget.prototype.reloadUpdate=function(src)
{
	this.dataStore.store.reload();
}

// role toolbar
Ecp.RoleToolBarWidget=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.showAddRoleWin,totalToolBarItem.showEditRoleWin,totalToolBarItem.clickDeleteRoleBtn];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'roleToolBar'
	};
}

//1
Ecp.RoleToolBarWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

//2
Ecp.RoleToolBarWidget.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.RoleToolBarWidget.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

//3
Ecp.RoleToolBarWidget.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.RoleToolBarWidget.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	//toolBar.setOwner(this);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}

/**
 *  Role Info Form
 * */
Ecp.RoleInfoForm=function()
{
	this.form=null;
	this.config={
		labelAlign: 'left',
		region: 'north',
		labelWidth: 120,
		height :190,
		frame:true,
		layout :'form'	
	};
	
	this.items=[
		{
			xtype : 'hidden',
			name : 'uid',
			id : 'uid'
		},{			
			xtype :'textfield',
			fieldLabel: TXT.role_names,
			id: 'rolename',
			name: 'rolename',
			width:250,
			allowBlank :false
		},{			
			xtype :'textfield',
			fieldLabel: TXT.role_uumsRole,
			id: 'uumsRole',
			name: 'uumsRole',
			width:250,
			allowBlank :false
		},{
			xtype :'textarea',
			fieldLabel: TXT.role_description,
			width:250,
			height :100,
			id: 'description',
			name: 'description'
		}
	];
	
	this.buttons=[];
}

// product customization
Ecp.RoleInfoForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// project customization
Ecp.RoleInfoForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.RoleInfoForm.prototype.render=function()
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
 *  Role Permission Grid
 */
Ecp.RolePermGridWidget=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'operations',
		idProperty:'id',
		fields:[
			{name: 'id'},
			{name: 'code'}]
	};
	
	this.defaultStoreConfig.baseParams={
		cmd:'role',
		action:'findAllOperations'
	};
	
	this.defaultCmConfig=[
			{header:TXT.role_operate_code,dataIndex: 'code',menuDisabled: true,width:300,
				renderer : function(value) {
					  return TXT.operation_code[value];
				}
			}	
	];
	
	this.defaultGridConfig={
//		border :true,
		region :'center',
		title:TXT.role_premises,
		id:'operationGridId',
		stripeRows :true
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=2;
}
//1 product customization
Ecp.RolePermGridWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

//Ecp.RolePermGridWidget.prototype.setToolBar=function(tbar){
//	this.grid.setTopToolBar(tbar);
//}

//3 widget event function
Ecp.RolePermGridWidget.prototype.setWidgetEvent=function(widgetEvent){
	widgetEvent['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(widgetEvent['grid']);
	widgetEvent['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(widgetEvent['cm']);
	widgetEvent['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(widgetEvent['store']);
}
//4 project customization function
Ecp.RolePermGridWidget.prototype.customization=function(customizationConfig){
	this.customizationConfig=customizationConfig;
}

Ecp.RolePermGridWidget.prototype.render=function(){
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

Ecp.RolePermGridWidget.prototype.show=function(){
	this.dataStore.store.load();
}

/**
 * Role Info Window
 */
Ecp.RoleInfoWindow=function()
{
	this.window=null;
	this.config={
		    title:TXT.role,
	        width:600,
	        height:500,
	        autoScroll :false,
//	        layout:'fit',
	        layout :'border',
	        border:false,
	        minimizable: false,
	        maximizable: false,
	        resizable: false,
	        modal:true,
			layoutConfig: {animate:false},
			buttonAlign: 'center'
	};
	this.buttons=[{
			text :TXT.commom_btnSave,
				handler : function() {
					clickSaveRoleBtn(this);
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

// product customization interface
Ecp.RoleInfoWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// project customization interface
Ecp.RoleInfoWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.RoleInfoWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.RoleInfoWindow.prototype.render=function()
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

Ecp.RoleInfoWindow.prototype.addObserver=function(observer)
{
	this.observers.push(observer);
}

/**
 * execute every observer's update method
 */
Ecp.RoleInfoWindow.prototype.notifyAll=function(eventName)
{
	for(var i=0;i<this.observers.length;i++)
		this.observers[i].update(this.window.window,eventName);
}

Ecp.RoleInfoWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.RoleInfoWindow.prototype.setCloseAction=function(isClose){
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
Ecp.RoleInfoWindow.singleInfoWindows=[];

/**
 * create ecp window
 */
Ecp.RoleInfoWindow.createRoleInfoWindow=function(windowObj,observers,fun){
	// form
	var roleForm=new Ecp.RoleInfoForm();

	if(windowObj['items'] && windowObj['items'][0])
		roleForm.customization(windowObj['items'][0]);
	
	// grid
	var rolePermGrid=new Ecp.RolePermGridWidget();
	
	if(windowObj['items'] && windowObj['items'][1])
		rolePermGrid.customization(windowObj['items'][1]);

	// window
	var roleInfoWin = new Ecp.RoleInfoWindow();
	roleInfoWin.handleWidgetConfig(function(obj){
		if(fun && typeof fun=='function')
			roleInfoWin.handleWidgetConfig(fun);
		obj.items=[roleForm.render(), rolePermGrid.render()];
		rolePermGrid.show();
	});
	roleInfoWin.customization(windowObj);

	
	// add observer
	if(observers && observers.length>0)
		roleInfoWin.observers=observers;
	roleInfoWin.render();
	return roleInfoWin.window;
}

/**
 * create single add window static function
 */
Ecp.RoleInfoWindow.createSingleAddWindow=function(windowObj,observers){
	if(!Ecp.RoleInfoWindow.singleInfoWindows[0])
		Ecp.RoleInfoWindow.singleInfoWindows[0]=Ecp.RoleInfoWindow.createRoleInfoWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	return Ecp.RoleInfoWindow.singleInfoWindows[0];
}

/**
 * create single edit window static function
 */
Ecp.RoleInfoWindow.createSingleEditWindow=function(windowObj,observers){
	if(!Ecp.RoleInfoWindow.singleInfoWindows[1])
	{
		Ecp.RoleInfoWindow.singleInfoWindows[1]=Ecp.RoleInfoWindow.createRoleInfoWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	}
	return Ecp.RoleInfoWindow.singleInfoWindows[1];
}
