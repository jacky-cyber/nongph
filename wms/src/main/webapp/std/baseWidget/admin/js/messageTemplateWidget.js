/**
 * Message Template Grid Widget
 */
Ecp.MessageTemplateGridWidget=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'templates',
		idProperty:'id',
		fields:[
			{name: 'id'},
			{name: 'name'},
			{name: 'description'},
			{name: 'needCheck'},
			{name: 'type'},
			{name:'fileName'},
			{name:'needHqCheck'}]
	};
	
	this.defaultStoreConfig.baseParams={
		cmd:'messageTemplate',
		action:'findAll'
	};
	
	this.defaultCmConfig=[
			{header:TXT.common_name,dataIndex: 'name',menuDisabled: true,width:150},
			{header:TXT.message_description,dataIndex:'description',menuDisabled: true,width:300},
			{header:TXT.template_check,dataIndex: 'needCheck',menuDisabled: true,width:100,renderer:function(value){
		  		if (value==true) {
					return TXT.common_yes_desc;
				}
				else {
					return TXT.common_no_desc;
				}
		  	}},
			{header:TXT.template_type,dataIndex:'type',menuDisabled: true,width:100},
			{header:TXT.template_hq_check,dataIndex: 'needHqCheck',menuDisabled: true,width:100,renderer:function(value){
		  		if (value==true) {
					return TXT.common_yes_desc;
				}
				else {
					return TXT.common_no_desc;
				}
		  	}}
	];
	
	this.defaultGridConfig={
		title:TXT.template_set,
		id:'messageTempGridId',
		stripeRows :true
//		pagination:true
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;
	
	this.ids = new Array();
}
//1 Product Customization
Ecp.MessageTemplateGridWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.MessageTemplateGridWidget.prototype.setToolBar=function(tbar){
	this.grid.setTopToolBar(tbar);
}

//3 Widget Event Function
Ecp.MessageTemplateGridWidget.prototype.setWidgetEvent=function(widgetEvent){
	widgetEvent['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(widgetEvent['grid']);
	widgetEvent['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(widgetEvent['cm']);
	widgetEvent['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(widgetEvent['store']);
}
//4 Project Customization function
Ecp.MessageTemplateGridWidget.prototype.customization=function(customizationConfig){
	this.customizationConfig=customizationConfig;
}

Ecp.MessageTemplateGridWidget.prototype.render=function(){
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

Ecp.MessageTemplateGridWidget.prototype.show=function(){
	this.dataStore.store.load();
}

//observer callback
Ecp.MessageTemplateGridWidget.prototype.update=function(src,eventName)
{
	switch(eventName)
	{
		case 'reload':
			this.reloadUpdate(src);
			break;
	}
}

Ecp.MessageTemplateGridWidget.prototype.reloadUpdate=function(src)
{
	this.dataStore.store.reload();
}

// message template toolbar
Ecp.MessageTemplateToolBarWidget=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.editMessageTemp];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'messageTempToolBar'
	};
}

//1
Ecp.MessageTemplateToolBarWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

//2
Ecp.MessageTemplateToolBarWidget.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.MessageTemplateToolBarWidget.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

//3
Ecp.MessageTemplateToolBarWidget.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.MessageTemplateToolBarWidget.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	//toolBar.setOwner(this);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}

/**
 *  Message Template Information Form
 * */
Ecp.MessageTemplateInfoForm=function()
{
	this.form=null;
	this.config={
		labelAlign: 'left',
		region: 'north',
		labelWidth: 80,
		height :100,
		margins:'0 0 2 0',
		cmargins:'0 0 2 0',
		frame:true,
		layout :'column'	
	};
	
	this.items=[
		{
			
			columnWidth:.5,
			layout: 'form',
			defaultType: 'textfield',
	        defaults:{anchor:'85%'},
	        items : [
				{
					fieldLabel: TXT.common_name,
					id: 'name',
					name: 'name',
					readOnly:true
				},
				{
					fieldLabel: TXT.template_type,
					id: 'type',
					name: 'type',
					readOnly: true
				},
				{
					xtype : 'combo',
					fieldLabel : TXT.template_hq_check,
					id : 'needHqCheck',
					name : 'needHqCheck',
					value : true,
					store : new Ext.data.SimpleStore({
								fields : ['label', 'value'],
								data : [[TXT.common_yes_desc, true],
										[TXT.common_no_desc, false]]
							}),
					forceSelection : true,
					displayField : 'label',
					valueField : 'value',
					typeAhead : true,
					mode : 'local',
					triggerAction : 'all',
					selectOnFocus : true
				}	
			]
		},
		{
            columnWidth:.5,
			layout: 'form',
			defaultType: 'textfield',
	        defaults:{anchor:'85%'},
	        items : [
	        	{
					fieldLabel: TXT.message_description,
					id: 'description',
					name: 'description'
				},
				{
					xtype : 'combo',
					fieldLabel : TXT.template_check,
					id : 'needCheck',
					name : 'needCheck',
					value : true,
					store : new Ext.data.SimpleStore({
								fields : ['label', 'value'],
								data : [[TXT.common_yes_desc, true],
										[TXT.common_no_desc, false]]
							}),
					forceSelection : true,
					displayField : 'label',
					valueField : 'value',
					typeAhead : true,
					mode : 'local',
					triggerAction : 'all',
					selectOnFocus : true
				}
			]
		}
	];
	
	this.buttons=[];
}

// Product Customization
Ecp.MessageTemplateInfoForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// Project Customization
Ecp.MessageTemplateInfoForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.MessageTemplateInfoForm.prototype.render=function()
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
 *  Message Template Tree Grid
 */
Ecp.MessageTemplateTreeGridWidget=function(){
	var id=Ecp.components.messageTempGrid.grid.getSelectedId();
	
	this.treeGrid = new Ext.tree.TreePanel({
		title : TXT.template_institutionSelect,
		region: 'center',
		border : true,
		autoScroll:true,
		checked: true,
		loader: new Ext.tree.TreeLoader({
            dataUrl:DISPATCH_SERVLET_URL+'?cmd=messageTemplate&action=findInst&id=' + id,
            preloadChildren: true
        }),
        rootVisible:false
	});
	
	// set the root node
    var root = new Ext.tree.AsyncTreeNode({
    	id : 'messageTemplate',
        draggable:false,
        checked: true   
    });
    this.treeGrid.setRootNode(root);
    
     this.treeGrid.addListener('load',function(node){
    	/*if(node==root){
    		//root.select();
//    		win.show();
    	}	*/
    });
    
	this.cusObj=null;
	this.eventObj=null;
}
//1 Product Customization
Ecp.MessageTemplateTreeGridWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

//Ecp.MessageTemplateTreeGridWidget.prototype.setToolBar=function(tbar){
//	this.topToolBar=tbar;
//}

//3 Tree Grid Event function
Ecp.MessageTemplateTreeGridWidget.prototype.setTreeGridEvent=function(obj){
	this.eventObj=obj;
}
//4 Project Customization function
Ecp.MessageTemplateTreeGridWidget.prototype.customization=function(obj){
	this.cusObj=obj;
}

Ecp.MessageTemplateTreeGridWidget.prototype.render=function(){
	this.treeGrid['root'].addListener('expand',function(){
			this.treeGrid.root['firstChild'].expand();
		},this)
	this.treeGrid['ecpOwner']=this.treeGrid;
	return this.treeGrid;
}

/**
 * Message Template  Info Window
 */
Ecp.MessageTemplateInfoWindow=function()
{
	this.window=null;
	this.config={
			title: TXT.template_set,
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
			text :TXT.common_btnSaveAll,
				handler : function() {
					clickSaveAllMessageTempBtn(this);
				}
			},
			{
			text: TXT.commom_btnSave,
				handler: function() {
					clickSaveMessageTempBtn(this);
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

// Product Customization Interface
Ecp.MessageTemplateInfoWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// Project Customization Interface
Ecp.MessageTemplateInfoWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.MessageTemplateInfoWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.MessageTemplateInfoWindow.prototype.render=function()
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

Ecp.MessageTemplateInfoWindow.prototype.addObserver=function(observer)
{
	this.observers.push(observer);
}

/**
 * execute every observer's update method
 */
Ecp.MessageTemplateInfoWindow.prototype.notifyAll=function(eventName)
{
	for(var i=0;i<this.observers.length;i++)
		this.observers[i].update(this.window.window,eventName);
}

Ecp.MessageTemplateInfoWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

// set window single function
Ecp.MessageTemplateInfoWindow.prototype.setCloseAction=function(isClose){
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
 * single window array
 */
Ecp.MessageTemplateInfoWindow.singleInfoWindows=null;

/**
 * create ecp window
 */
Ecp.MessageTemplateInfoWindow.editMessageTempInfoWindow=function(windowObj,observers,fun){
	// form
	var messageTempForm=new Ecp.MessageTemplateInfoForm();

	if(windowObj['items'] && windowObj['items'][0])
		messageTempForm.customization(windowObj['items'][0]);

	// tree panel
	var messageTempTreeGrid=new Ecp.MessageTemplateTreeGridWidget();
		
	// window
	var messageTempInfoWin = new Ecp.MessageTemplateInfoWindow();
	// Product Customization
	messageTempInfoWin.handleWidgetConfig(function(obj){
		if(fun && typeof fun=='function')
			messageTempInfoWin.handleWidgetConfig(fun);
		obj.items=[messageTempForm.render(), messageTempTreeGrid.render()];
		messageTempTreeGrid.treeGrid.root.reload();
	});
	// Project Customization
	messageTempInfoWin.customization(windowObj);
	
	// add observer
	if(observers && observers.length>0)
		messageTempInfoWin.observers=observers;
		
	messageTempInfoWin.render();
	return messageTempInfoWin.window;
}

/**
 * create single edit window function
 */
Ecp.MessageTemplateInfoWindow.createSingleEditWindow=function(windowObj,observers){
	Ecp.MessageTemplateInfoWindow.singleInfoWindows=Ecp.MessageTemplateInfoWindow.editMessageTempInfoWindow(windowObj,observers,
		function(obj){
			obj.setCloseAction(false);
		}); 
	return Ecp.MessageTemplateInfoWindow.singleInfoWindows;
}
