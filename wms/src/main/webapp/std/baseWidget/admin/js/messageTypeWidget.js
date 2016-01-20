/******************   Ecp.MessageTypeStore **************************/
Ecp.MessageTypeStore=function(){
	this.dataStore=new Ecp.DataStore();
	
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root :'types',
		id :'id',
		fields:[
			{name :'id'},
			{name :'tag'}, 
			{name :'convertable'},
			{name :'name'}, 
			{name :'versionNumber'}, 
			{name :'needCheck'}, 
			{name :'needHqCheck'}
		]
	};
	
	this.defaultStoreConfig.baseParams={
		cmd:'messageType',
		action:'find'
	};
	
	this.customConfig=null;
}

Ecp.MessageTypeStore.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.MessageTypeStore.prototype.setEventConfig=function(obj){
	this.dataStore.setEventConfig(obj);
}

Ecp.MessageTypeStore.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.MessageTypeStore.prototype.load=function(baseParams){
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
	this.dataStore.store.load();
}

Ecp.MessageTypeStore.prototype.render=function(){
	this.dataStore.setConfig(this.defaultStoreConfig);
	this.dataStore.init();
	this.customConfig==null?1:this.dataStore.customization(this.customConfig);
	return this.dataStore.render();
}
/******************   Ecp.MessageTypeStore **************************/

/******************   Ecp.CorrespondentGrid    ************************/
Ecp.MessageTypeGrid=function(){
	this.dataStore=new Ecp.MessageTypeStore();
	this.grid=new Ecp.Grid();
	
	this.defaultCmConfig=[
		{
			header :TXT.message_type_tag,
			dataIndex :'tag',
			menuDisabled: true,
			width :150
		}, {
			header :TXT.message_type_name,
			dataIndex :'name',
			menuDisabled: true,
			width :225
			,
			renderer : function(value) {
				if (value == '008 RTCP')
					return TXT.eni_008;
				else if (value == '007 RTMP')
					return TXT.eni_007;
				else if (value == '026 UTA')
					return TXT.eni_026;
				else if (value == '027 CNR')
					return TXT.eni_027;
				else if (value == '028 API')
					return TXT.eni_028;
				else if (value == '029 ROI')
					return TXT.eni_029;
				else if (value == '030 NOCA')
					return TXT.eni_030;
				else if (value == '031 RCA')
					return TXT.eni_031;
				else if (value == '032 CCA')
					return TXT.eni_032;
				else if (value == '033 RFD')
					return TXT.eni_033;
				else if (value == '034 D')
					return TXT.eni_034;
				else if (value == '035 PFI')
					return TXT.eni_035;
				else if (value == '036 DAResp')
					return TXT.eni_036;
				else if (value == '037 DAReq')
					return TXT.eni_037;
				else if (value == '038 CSRR')
					return TXT.eni_038;
				else if (value == '039 CSR')
					return TXT.eni_039;
				else
					return value;
			}
		}, {
			header :TXT.message_type_version,
			dataIndex :'versionNumber',
			menuDisabled: true,
			width :80
		}, {
			header :TXT.message_type_needCheck,
			dataIndex :'needCheck',
			menuDisabled: true,
			width :100,
			renderer : function(value) {
				if (value == true) {
					return TXT.message_type_needCheck_yes;
				} else {
					return TXT.message_type_needCheck_no;
				}
			}
		}, {
			header :TXT.message_type_hq_check,
			dataIndex :'needHqCheck',
			menuDisabled: true,
			width :100,
			renderer : function(value) {
				if (value == true) {
					return TXT.message_type_needCheck_yes;
				} else {
					return TXT.message_type_needCheck_no;
				}
			}
		}
	];
	
	this.defaultGridConfig={
		title:TXT.message_type_title,
		id:'messageTypeGridId',
		pagination:true
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;
}
Ecp.MessageTypeGrid.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.MessageTypeGrid.prototype.setToolBar=function(tbar){
	this.grid.setTopToolBar(tbar);
}

Ecp.MessageTypeGrid.prototype.setWidgetEvent=function(widgetEvent){
	widgetEvent['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(widgetEvent['grid']);
	widgetEvent['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(widgetEvent['cm']);
	widgetEvent['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(widgetEvent['store']);
}
Ecp.MessageTypeGrid.prototype.customization=function(customizationConfig){
	this.customizationConfig=customizationConfig;
}

Ecp.MessageTypeGrid.prototype.render=function(){
//	this.dataStore.setConfig(this.defaultStoreConfig);
//	this.dataStore.init();
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

Ecp.MessageTypeGrid.prototype.show=function(){
	this.dataStore.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}

//observer callback
Ecp.MessageTypeGrid.prototype.update=function(src,eventName)
{
	switch(eventName)
	{
		case 'reload':
			this.reloadUpdate(src);
			break;
	}
}

Ecp.MessageTypeGrid.prototype.reloadUpdate=function(src)
{
	this.dataStore.dataStore.store.reload();
}

// currency toolbar
Ecp.MessageTypeToolBar=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.editMessageType];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'messageTypeToolBar'
	};
}

//1
Ecp.MessageTypeToolBar.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

//2
Ecp.MessageTypeToolBar.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.MessageTypeToolBar.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

//3
Ecp.MessageTypeToolBar.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.MessageTypeToolBar.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	//toolBar.setOwner(this);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}
/******************   Ecp.MessageTypeGrid    ************************/

/******************   Ecp.MessageTypeForm    ************************/
Ecp.MessageTypeForm=function()
{	
	this.form=null;
	this.config={
		labelAlign:'left',
		region:'north',
		labelWidth:100,
		height:100,
		layout:'column',
		margins:'0 0 2 0',
		cmargins:'0 0 2 0',
		frame:true
	};
	this.items=[
	            {
				  	xtype:'hidden',
				  	id: 'id',
				  	name: 'id'
				},
				{
					columnWidth :.5,
					layout :'form',
					defaultType :'textfield',
					defaults : {
						anchor :'85%'
					},
					items : [ 
					{
						fieldLabel :TXT.message_type_tag,
						id :'tag',
						name :'tag',
						readOnly :true
					},{
						fieldLabel :TXT.message_type_version,
						id :'versionNumber',
						name :'versionNumber',
						readOnly :true
					},
					{
						xtype :'combo',
						fieldLabel :TXT.message_type_hq_check,
						id :'needHqCheck',
						name :'needHqCheck',
						value :true,
						store :new Ext.data.SimpleStore( {
							fields : [ 'label', 'value' ],
							data : [ [ TXT.message_type_yes, true ],
									[ TXT.message_type_no, false ] ]
						}),
						forceSelection :true,
						displayField :'label',
						valueField :'value',
						typeAhead :true,
						mode :'local',
						triggerAction :'all',
						selectOnFocus :true,
						editable :false
					}
				
					]
				},
				{
					columnWidth :.5,
					layout :'form',
					defaultType :'textfield',
					defaults : {
						anchor :'85%'
					},
					items : [
							{
								fieldLabel :TXT.message_type_name,
								id :'name',
								name :'name',
								readOnly :true
							},
							{
								xtype :'combo',
								fieldLabel :TXT.message_type_needCheck,
								id :'needCheck',
								name :'needCheck',
								value :true,
								store :new Ext.data.SimpleStore( {
									fields : [ 'label', 'value' ],
									data : [ [ TXT.message_type_yes, true ],
											[ TXT.message_type_no, false ] ]
								}),
								forceSelection :true,
								displayField :'label',
								valueField :'value',
								typeAhead :true,
								mode :'local',
								triggerAction :'all',
								selectOnFocus :true,
								editable :false
							}

					]
				}
				
	];
	this.buttons=[];
}

Ecp.MessageTypeForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.MessageTypeForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.MessageTypeForm.prototype.render=function()
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

Ecp.MessageTypeForm.prototype.reset=function()
{
	this.form.reset();
}
/******************   Ecp.MessageTypeForm    ************************/

/******************   Ecp.MessageTypeWindow    ************************/
Ecp.MessageTypeWindow=function()
{
	this.window=null;
	this.config={
			title :TXT.template,
			width :600,
			height :160,
			autoScroll :false,
			border :false,
			minimizable :false,
			maximizable :false,
			resizable :false,
			modal :true,
			layoutConfig : {
				animate :false
			},
			buttonAlign :'center'
	};
	this.buttons=[{
						text :TXT.message_type_save,
						handler : function() {
							clickSaveMessageTypeBtn(this);
						}
					},
					{
						text :TXT.message_type_close,
						handler : function() {
							var win=this['ecpOwner'];
							win.window.hide();
						}
					}];
	this.observers=[];
	this.items=[];
}

Ecp.MessageTypeWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.MessageTypeWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.MessageTypeWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.MessageTypeWindow.prototype.render=function()
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

Ecp.MessageTypeWindow.prototype.addObserver=function(observer)
{
	this.observers.push(observer);
}

/**
 * execute every observer's update method
 */
Ecp.MessageTypeWindow.prototype.notifyAll=function(eventName)
{
	for(var i=0;i<this.observers.length;i++)
		this.observers[i].update(this.window.window,eventName);
}

Ecp.MessageTypeWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.MessageTypeWindow.prototype.setCloseAction=function(isClose){
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
Ecp.MessageTypeWindow.singleInfoWindows=[];

/**
 * create ecp window
 */
Ecp.MessageTypeWindow.createCorrespondentWindow=function(windowObj,observers,fun){
	var messageTypeForm=new Ecp.MessageTypeForm();

	if(windowObj['items'] && windowObj['items'][0])
		messageTypeForm.customization(windowObj['items'][0]);
	
	// window
	var messageTypeWin = new Ecp.MessageTypeWindow();
	messageTypeWin.handleWidgetConfig(function(obj){
		if(fun && typeof fun=='function')
			messageTypeWin.handleWidgetConfig(fun);
		obj.items=[messageTypeForm.render()];
	});
	messageTypeWin.customization(windowObj);
	
	// add observer
	if(observers && observers.length>0)
		messageTypeWin.observers=observers;
	messageTypeWin.render();
	return messageTypeWin.window;
}

/**
 * create single add window static function
 */
Ecp.MessageTypeWindow.createSingleAddWindow=function(windowObj,observers){
	if(!Ecp.MessageTypeWindow.singleInfoWindows[0])
		Ecp.MessageTypeWindow.singleInfoWindows[0]=Ecp.MessageTypeWindow.createCorrespondentWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	return Ecp.MessageTypeWindow.singleInfoWindows[0];
}

/**
 * create single edit window static function
 */
Ecp.MessageTypeWindow.createSingleEditWindow=function(windowObj,observers){
	if(!Ecp.MessageTypeWindow.singleInfoWindows[1])
	{
		Ecp.MessageTypeWindow.singleInfoWindows[1]=Ecp.MessageTypeWindow.createCorrespondentWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	}
	return Ecp.MessageTypeWindow.singleInfoWindows[1];
}
/******************   Ecp.CorrespondentGrid    ************************/