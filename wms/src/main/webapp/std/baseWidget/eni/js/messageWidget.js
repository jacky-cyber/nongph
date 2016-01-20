// message toolbar
Ecp.MessageToolBarWidget=function(){
	this.toolBar=new Ecp.ToolBar();
	totalToolBarItem['messageSearchMenu']['childrens']=[totalToolBarItem['searchMessage'],totalToolBarItem['searchNotwithMessage']];
	this.defaultToolBarItemConfig=[totalToolBarItem['messageSearchMenu'],totalToolBarItem['messageCase'],totalToolBarItem['messageRead'],
	                               totalToolBarItem['copyMessage'],totalToolBarItem['relateMessage'],totalToolBarItem['nakErrorCode'],
	                               totalToolBarItem['auditMessageRecord'],totalToolBarItem['relateOutcomingMessage']];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'messageToolBar'
	};
}

//1
Ecp.MessageToolBarWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

//2
Ecp.MessageToolBarWidget.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.MessageToolBarWidget.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

//3
Ecp.MessageToolBarWidget.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.MessageToolBarWidget.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	//toolBar.setOwner(this);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}

/**
 * MessageSearchForm
 */
Ecp.MessageSearchForm=function()
{
	var messageTypeStore = new Ecp.MessageTypeStore();
	messageTypeStore.handleWidgetConfig(function(store){
		store.defaultStoreConfig.fields.push({name :'convertedName'});
	});
	messageTypeStore.setEventConfig({
		'load':function(store,records){
			for(var i=0;i<records.length;i++){
				if(TXT[records[i].get('tag')+'_name']===undefined)
					records[i].set('convertedName',records[i].get('name'));
				else{
					var name=records[i].get('name');
					if(records[i].get('tag')==='camt.034.001.02')
						name='034 DUP';
					records[i].set('convertedName',name+' '+TXT[records[i].get('tag')+'_name']);
				}
			}
		}
	});
	var store=messageTypeStore.render();
	messageTypeStore.load({
		cmd:'messageType',
		action:'find'
	});
	this.form=null;
	this.config={
			labelAlign: 'left',
			region: 'center',
		    labelWidth:100,
//		    bodyStyle:'margin-top:10px',
		    layout:'column',
		    frame:true
	};
	this.items=[
	           {
					xtype : 'hidden',
					name : 'id',
					id : 'id'
			   },
				{
								columnWidth :.5,
								layout :'form',
								defaultType :'textfield',
								defaults : {anchor :'95%'},
								items : [
										{
											fieldLabel :TXT.task_messageId,
											id :'messageId',
											name :'messageId',
											tabIndex:1
										},
										{
											fieldLabel :TXT.message_assigner,
											id :'sendBic',
											name :'sendBic',
											tabIndex:3
										},
										{
											xtype :'datefield',
											fieldLabel :TXT.message_dateFrom,
											format :'Y-m-d',
											id :'createTimeFrom',
											name :'dateFrom',
											tabIndex:5
										},
										{
											fieldLabel :TXT.message_caseId,
											id :'caseId',
											name :'caseId',
											tabIndex:7
										},
										{
											xtype :'combo',
											fieldLabel :TXT.message_type,
											id :'messageTypeName',
											name :'messageTypeName',
											value :'',
											store :store,
											forceSelection :true,
											displayField :'convertedName',
											valueField :'tag',
											typeAhead :true,
											mode :'local',
											editable :false,
											triggerAction :'all',
											selectOnFocus :true,
											tabIndex:9
										} ]
							},
							{
								columnWidth :.5,
								layout :'form',
								defaultType :'textfield',
								defaults : {anchor :'95%'},
								items : [
										{
											xtype :'combo',
											fieldLabel :TXT.payment_direction,
											id :'ioFlag',
											name :'ioFlag',
											value :'',
											store :new Ext.data.SimpleStore(
													{
														fields : [ 'label',
																'value' ],
														data : [
																[ TXT.common_all, '' ],
																[
																		TXT.payment_direction_in,
																		'I' ],
																[
																		TXT.payment_direction_out,
																		'O' ] ]
													}),
											forceSelection :true,
											displayField :'label',
											valueField :'value',
											typeAhead :true,
											mode :'local',
											triggerAction :'all',
											selectOnFocus :true,
											tabIndex:2
										},
										{
											fieldLabel :TXT.message_assignee,
											id :'receiverBic',
											name :'receiverBic',
											tabIndex:4
										},
										{
											xtype :'datefield',
											fieldLabel :TXT.message_dateTo,
											format :'Y-m-d',
											id :'creatTimeTo',
											name :'dateTo',
											tabIndex:6
										},
										{
											xtype :'combo',
											fieldLabel :TXT.message_isRead,
											id :'isRead',
											name :'isRead',
											value :'',
											store :new Ext.data.SimpleStore(
													{
														fields : [ 'label',
																'value' ],
														data : [
																[ TXT.common_all, '' ],
																[
																		TXT.message_hasReaded,
																		'true' ],
																[
																		TXT.message_hasNotNotReaded,
																		'false' ] ]
													}),
											forceSelection :true,
											displayField :'label',
											valueField :'value',
											typeAhead :true,
											mode :'local',
											triggerAction :'all',
											selectOnFocus :true,
											tabIndex:8
										},
										{
											xtype :'combo',
											fieldLabel :TXT.message_swiftStatus,
											id :'swiftStatus',
											name :'swiftStatus',
											value :'',
											store :new Ext.data.SimpleStore(
													{
														fields : [ 'label',
																'value' ],
														data : [
																[ TXT.common_all, '' ],
																[
																		TXT.message_swiftStatusWait,
																		'W' ],
																[
																		TXT.message_swiftStatusAck,
																		'ACK' ],
																[
																		TXT.message_swiftStatusNack,
																		'NAK' ]
//																,
//																[
//																		TXT.message_swiftStatusSaaSuccess,
//																		'S' ],
//																[
//																		TXT.message_swiftStatusSaaFailure,
//																		'F' ]
																]
													}),
											forceSelection :true,
											displayField :'label',
											valueField :'value',
											typeAhead :true,
											mode :'local',
											triggerAction :'all',
											selectOnFocus :true,
											tabIndex:10
										} ]
							} 
	];
	this.buttons=[];
}

Ecp.MessageSearchForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.MessageSearchForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.MessageSearchForm.prototype.render=function()
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
 * MessageSearchWindow
 */
Ecp.MessageSearchWindow=function()
{
	this.window=null;
	this.config={
		    title:TXT.message_search,
			width:650,
	        height:230,
	        autoScroll :false,
	        layout:'fit',
	        border:false,
	        //closeAction:'hide',
	        modal:true,
	        minimizable: false,
	        maximizable: false,
			layoutConfig: {animate:false},
			resizable: false,
			shadow:false,
			buttonAlign: 'center'
	};
	this.buttons=[{
		text: TXT.common_btnQuery,
		handler: function(){
			clickQueryMessageBtn(this);
		}
	}
	//liaozhiling add 2011-05-03
	,{
		text:TXT.common_reset,
		handler:function(){
			messageSearchFormReset(this);
		}
	}];
	this.observers=[];
	this.items=[];
}

Ecp.MessageSearchWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.MessageSearchWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.MessageSearchWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.MessageSearchWindow.prototype.render=function()
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

Ecp.MessageSearchWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.MessageSearchWindow.prototype.setCloseAction=function(isClose){
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
Ecp.MessageSearchWindow.singleSearchWindows=null;

/**
 * create single search window static function
 */
Ecp.MessageSearchWindow.createSingleSearchWindow=function(windowObj){
	if(!Ecp.MessageSearchWindow.singleSearchWindows)
	{   
		var messageSearchForm=new Ecp.MessageSearchForm();
		Ecp.components['messageSearchForm'] = messageSearchForm;
		if(windowObj['items'] && windowObj['items'][0])
			messageSearchForm.customization(windowObj['items'][0]);
		// window
		var messageSearchWin = new Ecp.MessageSearchWindow();
		messageSearchWin.handleWidgetConfig(function(obj){
			obj.setCloseAction(false);
			obj.items=[messageSearchForm.render()];
		});
		messageSearchWin.form=messageSearchForm;
		messageSearchWin.customization(windowObj);		
		messageSearchWin.render();
		Ecp.MessageSearchWindow.singleSearchWindows=messageSearchWin.window;
	}
	return Ecp.MessageSearchWindow.singleSearchWindows;
	
}

Ecp.MessageSearchWindow.createSingleWindowForHandle=function(params){
	if(!Ecp.MessageSearchWindow.singleSearchWindowForHandle)
	{   
		var messageSearchForm=new Ecp.MessageSearchForm();
		Ecp.components['messageSearchForm'] = messageSearchForm;
		messageSearchForm.handleWidgetConfig(function(form){
			form.items[2].items[0]=form.items[1].items[1];
			form.items[1].items[1]=form.items[1].items[2];
			form.items[2].items[1]=form.items[2].items[2];
			form.items[1].items[2]=form.items[1].items[4];
			form.items[1].items.splice(3,2);
			form.items[2].items.splice(2,3);
		});
		messageSearchForm.customization({});
		var messageSearchWin = new Ecp.MessageSearchWindow();
		messageSearchWin.handleWidgetConfig(function(obj){
			obj.config.height=170;
			obj.setCloseAction(false);
			obj.buttons[0].handler=params['queryMethod'];
			obj.items=[messageSearchForm.render()];
		});
		messageSearchWin.customization({});		
		messageSearchWin.render();
		Ecp.MessageSearchWindow.singleSearchWindowForHandle=messageSearchWin;
	}
	return Ecp.MessageSearchWindow.singleSearchWindowForHandle;
}

/**
 * DeleteMessageSearchForm
 */
Ecp.DeleteMessageSearchForm=function()
{
	var messageTypeStore = new Ecp.MessageTypeStore();
	messageTypeStore.handleWidgetConfig(function(store){
		store.defaultStoreConfig.fields.push({name :'convertedName'});
	});
	messageTypeStore.setEventConfig({
		'load':function(store,records){
			for(var i=0;i<records.length;i++){
				if(TXT[records[i].get('tag')+'_name']===undefined)
					records[i].set('convertedName',records[i].get('name'));
				else{
					var name=records[i].get('name');
					if(records[i].get('tag')==='camt.034.001.02')
						name='034 DUP';
					records[i].set('convertedName',name+' '+TXT[records[i].get('tag')+'_name']);
				}
			}
		}
	});
    var store=messageTypeStore.render();
	messageTypeStore.load({
		cmd:'messageType',
		action:'find'
	});

	
	this.form=null;
	this.config={
			labelAlign: 'left',
			region: 'center',
		    labelWidth:100,
//		    bodyStyle:'margin-top:10px',
		    layout:'column',
		    frame:true
	};
	this.items=[
	           {
					xtype : 'hidden',
					name : 'id',
					id : 'id'
			   },
				{
								columnWidth :.5,
								layout :'form',
								defaultType :'textfield',
								defaults : {anchor :'95%'},
								items : [
										{
											fieldLabel :TXT.task_messageId,
											id :'messageId',
											name :'messageId',
											tabIndex:1
										},
										{
											fieldLabel :TXT.message_assigner,
											id :'sendBic',
											name :'sendBic',
											tabIndex:3
										},
										{
											xtype :'datefield',
											fieldLabel :TXT.message_dateFrom,
											format :'Y-m-d',
											id :'createTimeFrom',
											name :'createTimeFrom',
											editable:false,
											tabIndex:5
										} ]
							},
							{
								columnWidth :.5,
								layout :'form',
								defaultType :'textfield',
								defaults : {
									anchor :'95%'
								},
								items : [
										{
											xtype :'combo',
											fieldLabel :TXT.message_type,
											id :'messageType',
											name :'messageType',
											store :store,
											forceSelection :true,
											displayField :'convertedName',
											valueField :'name',
											typeAhead :true,
											mode :'local',
											editable :false,
											triggerAction :'all',
											selectOnFocus :true,
											tabIndex:2
										},
										{
											fieldLabel :TXT.message_assignee,
											id :'receiveBic',
											name :'receiveBic',
											tabIndex:4
										},
										{
											xtype :'datefield',
											fieldLabel :TXT.message_dateTo,
											format :'Y-m-d',
											id :'creatTimeTo',
											name :'creatTimeTo',
											editable:false,
											tabIndex:6
										} ]
							} 
	];
	this.buttons=[];
}

Ecp.DeleteMessageSearchForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.DeleteMessageSearchForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.DeleteMessageSearchForm.prototype.render=function()
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
 * DeleteMessageSearchWindow
 */
Ecp.DeleteMessageSearchWindow=function()
{
	this.searchMessageFormForIBP=null;
	this.window=null;
	this.config={
		    title:TXT.message_deleted_search,
			width:650,
	        height:170,
	        autoScroll :false,
	        layout:'fit',
	        border:false,
	        //closeAction:'hide',
	        modal:true,
	        minimizable: false,
	        maximizable: false,
			layoutConfig: {animate:false},
			resizable: false,
			shadow:false,
			buttonAlign: 'center'
	};
	this.buttons=[{
		text: TXT.common_btnQuery,
		handler: function(){
			clickQueryDeleteMessageBtn(this);
		}
	}
	//liaozhiling add 2011-05-03
	,{
		text:TXT.common_reset,
		handler:function(){
			messageSearchFormReset(this);
		}
	}];
	this.observers=[];
	this.items=[];
}

Ecp.DeleteMessageSearchWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.DeleteMessageSearchWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.DeleteMessageSearchWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.DeleteMessageSearchWindow.prototype.render=function()
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

Ecp.DeleteMessageSearchWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.DeleteMessageSearchWindow.prototype.setCloseAction=function(isClose){
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
Ecp.DeleteMessageSearchWindow.singleSearchWindows=null;

/**
 * create single search window static function
 */
Ecp.DeleteMessageSearchWindow.createSingleSearchWindow=function(windowObj){
	if(!Ecp.DeleteMessageSearchWindow.singleSearchWindows)
	{   
		var deleteMessageSearchForm=new Ecp.DeleteMessageSearchForm();
		if(windowObj['items'] && windowObj['items'][0])
			deleteMessageSearchForm.customization(windowObj['items'][0]);
		// window
		var deleteMessageSearchWin = new Ecp.DeleteMessageSearchWindow();
		deleteMessageSearchWin.handleWidgetConfig(function(obj){
			obj.setCloseAction(false);
			obj.items=[deleteMessageSearchForm.render()];
		});
		deleteMessageSearchWin.customization(windowObj);	
		deleteMessageSearchWin.render();
		Ecp.DeleteMessageSearchWindow.singleSearchWindows=deleteMessageSearchWin.window;
	}
	return Ecp.DeleteMessageSearchWindow.singleSearchWindows;
}

Ecp.DeleteMessageSearchWindow.createSingleSearchWindowFoIBP=function(){
	if(!Ecp.DeleteMessageSearchWindow.singleSearchWindowsFoIBP)
	{   
		var deleteMessageSearchForm=new Ecp.DeleteMessageSearchForm();
		deleteMessageSearchForm.handleWidgetConfig(function(obj){
			obj.config.id='searchFormForIBP';
		});
		// window
		var deleteMessageSearchWin = new Ecp.DeleteMessageSearchWindow();
		deleteMessageSearchWin.handleWidgetConfig(function(obj){
			obj.setCloseAction(false);
			obj.config.id='messageSearchWindowForIBP';
			obj.config.title=TXT.message_IBP_search;
			obj.items=[deleteMessageSearchForm.render()];
			obj.buttons[0].handler=queryMessageForIBP;
			obj.buttons[0].scope=deleteMessageSearchWin;
		});
		deleteMessageSearchWin.searchMessageFormForIBP=deleteMessageSearchForm;
		deleteMessageSearchWin.customization({});	
		deleteMessageSearchWin.render();
		Ecp.DeleteMessageSearchWindow.singleSearchWindowsFoIBP=deleteMessageSearchWin;
	}
	return Ecp.DeleteMessageSearchWindow.singleSearchWindowsFoIBP;
}

//temporary position!!!!!!!!!!!
Ext.apply(Ext.form.VTypes, {
	vailAreaStringFin : function(value, field) {
		var index = value.search('ENIidentify');
		if (index == -1
				|| (value.indexOf('ENIidentify') != value
						.lastIndexOf('ENIidentify')))
			return false;
		else
			return true;
	},
	vailAreaStringFinText :TXT.message_vType_areaInFin_error
});

/**
 * FinNakMessageForm
 */
Ecp.FinNakMessageForm=function()
{	
	this.form=null;
	this.config={
			labelAlign: 'right',
		    labelWidth:100,
		    height :40,
            margins :'0 0 2 0',
		    cmargins :'0 0 2 0',
		    frame:true
	};
	this.items=[
	           {
					xtype : 'hidden',
					name : 'id',
					id : 'id'
			   },
			   {
			        x :0,
			        y :60,
			        fieldLabel:TXT.message_nak_errorCode,
			        xtype :'textarea',
			        name :'errorContent',
			        id :'errorContent',
			        anchor :'95% 99%', // anchor width and height
			        allowBlank :false
		        }
	];
	this.buttons=[];
}

Ecp.FinNakMessageForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.FinNakMessageForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.FinNakMessageForm.prototype.render=function()
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
 * FinNakMessageWindow
 */
Ecp.FinNakMessageWindow=function()
{
	this.window=null;
	this.config={
			width:500,
	        height:400,
	        autoScroll :false,
	        layout:'fit',
	        border:false,
	        modal:true,
	        title:TXT.message_nak_title,
	        minimizable: false,
	        maximizable: false,
			layoutConfig: {animate:false},
			resizable: false,
			buttonAlign: 'center'
	};
	this.buttons=[{
						text :TXT.common_btnClose,
						handler : function() {
							var win=this['ecpOwner'];
				            win.window.hide();
					}
					}];
	this.observers=[];
	this.items=[];
}

Ecp.FinNakMessageWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.FinNakMessageWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.FinNakMessageWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.FinNakMessageWindow.prototype.render=function()
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

Ecp.FinNakMessageWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.FinNakMessageWindow.singleFinNakMessageWindows=null;

Ecp.FinNakMessageWindow.createSingleWindow=function(windowObj){
	if(!Ecp.FinNakMessageWindow.singleFinNakMessageWindows)
	{
		var finNakMessageForm=new Ecp.FinNakMessageForm();
		if(windowObj['items'] && windowObj['items'][0])
			finNakMessageForm.customization(windowObj['items'][0]);
		// window
		var finNakMessageWin = new Ecp.FinNakMessageWindow();
		finNakMessageWin.handleWidgetConfig(function(obj){
			obj.items=[finNakMessageForm.render()];
			obj.config.closeAction='hide';
			obj.config.onHide=function(){
				obj.window.window.hide();
			}
		});
		finNakMessageWin.customization(windowObj);		
		finNakMessageWin.render();
		Ecp.FinNakMessageWindow.singleFinNakMessageWindows=finNakMessageWin.window;
	}
	return Ecp.FinNakMessageWindow.singleFinNakMessageWindows;
}

/**
 * ErrorCodeGridWidget
 */
Ecp.ErrorCodeGridWidget=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'errorCodes',
		idProperty:'id',
		fields:[
			{name: 'errorCode'},
			{name: 'severity'}]
	};
	
//	this.defaultStoreConfig.baseParams={
//		cmd:'message',
//		action:'getNakErrorCode'
//	};
	
	this.defaultCmConfig=[
			{header :TXT.message_nak_errorCode,dataIndex :'errorCode',menuDisabled: true,width :250},
			{header :TXT.message_nak_severity,dataIndex :'severity',width :100,menuDisabled: true}
	];
	
	this.defaultGridConfig={
		//title:TXT.message_nak_title,
		id:'errorCodeGridId',
//		loadMask:true,
		pagination:true
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;
}
Ecp.ErrorCodeGridWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.ErrorCodeGridWidget.prototype.setWidgetEvent=function(widgetEvent){
	widgetEvent['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(widgetEvent['grid']);
	widgetEvent['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(widgetEvent['cm']);
	widgetEvent['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(widgetEvent['store']);
}

Ecp.ErrorCodeGridWidget.prototype.customization=function(customizationConfig){
	this.customizationConfig=customizationConfig;
}

Ecp.ErrorCodeGridWidget.prototype.render=function(){
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

Ecp.ErrorCodeGridWidget.prototype.show=function(){
	this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}

Ecp.ErrorCodeGridWidget.prototype.search=function(params){
	if(this.grid['pagination']!=null)
	{
		params['start']=0;
		params['limit']=PAGE_SIZE;
	}
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load(params);
}

//observer callback
Ecp.ErrorCodeGridWidget.prototype.update=function(src,eventName)
{
	switch(eventName)
	{
		case 'reload':
			this.reloadUpdate(src);
			break;
	}
}

Ecp.ErrorCodeGridWidget.prototype.reloadUpdate=function(src)
{
	this.dataStore.store.reload();
}

/**
 * XmlNakMessageWindow
 */
Ecp.XmlNakMessageWindow=function()
{
	this.window=null;
	this.config={
			width:500,
	        height:400,
	        autoScroll :false,
	        layout:'fit',
	        border:false,
	        modal:true,
	        title:TXT.message_nak_title,
	        minimizable: false,
	        maximizable: false,
			layoutConfig: {animate:false},
			resizable: false,
			buttonAlign: 'center'
	};
	this.buttons=[{
						text :TXT.common_btnClose,
						handler : function() {
							var win=this['ecpOwner'];
				            win.window.hide();
						}
				}];
	this.observers=[];
	this.items=[];
}

Ecp.XmlNakMessageWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.XmlNakMessageWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.XmlNakMessageWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.XmlNakMessageWindow.prototype.render=function()
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

Ecp.XmlNakMessageWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.XmlNakMessageWindow.singleXmlNakMessageWindows=null;

Ecp.XmlNakMessageWindow.createSingleWindow=function(windowObj){
	if(!Ecp.XmlNakMessageWindow.singleXmlNakMessageWindows)
	{
		// grid
	    var errorCodeGrid=new Ecp.ErrorCodeGridWidget();
	
	    if(windowObj['items'] && windowObj['items'][0])
		errorCodeGrid.customization(windowObj['items'][0]);
									
		// window
		var xmlNakMessageWin = new Ecp.XmlNakMessageWindow();
		xmlNakMessageWin.handleWidgetConfig(function(obj){
		    obj.items=[errorCodeGrid.render()];
		    //errorCodeGrid.show();
		    obj.config.closeAction='hide';
			obj.config.onHide=function(){
				obj.window.window.hide();
			}
		});
		xmlNakMessageWin.customization(windowObj);		
		xmlNakMessageWin.render();
		Ecp.XmlNakMessageWindow.singleXmlNakMessageWindows=xmlNakMessageWin.window;
	}
	return Ecp.XmlNakMessageWindow.singleXmlNakMessageWindows;
}

Ecp.IBPSeqNumPromptorForm=function(){
	this.form=new Ecp.FormPanel();
	
	this.defaultFormConfig={
		region:'south',
		labelAlign :'left',
		labelWidth :75,
		height:50,
		frame :true,
		autoScroll :false,
		bodyStyle :'padding:10px 10px 10px 10px',
		defaultType:'textfield'
	};
	
	/*this.defaultButtonsConfig=[{
		text:TXT.common_search,
		scope:this
	}];*/
	
	this.customConfig=null;
}

Ecp.IBPSeqNumPromptorForm.prototype.handlerWidgetConfig=function(handler){
	handler(this);
}

Ecp.IBPSeqNumPromptorForm.prototype.init=function(){}

Ecp.IBPSeqNumPromptorForm.prototype.customization=function(cusObj){
	this.customConfig=cusObj;
}

Ecp.IBPSeqNumPromptorForm.prototype.reset=function(){
	this.form.reset();
}

Ecp.IBPSeqNumPromptorForm.prototype.getIBPSeqNumValue=function(){
	return this.form.getValues()['IBPSeqNum'];
}

/*Ecp.IBPSeqNumPromptorForm.prototype.setButtonHandler=function(handler){
	this.defaultButtonsConfig[0].handler=handler;
}*/

Ecp.IBPSeqNumPromptorForm.prototype.render=function(){
	this.form.init();
	this.form['config']=this.defaultFormConfig;
	this.form['items']=this.defaultItemsConfig;
	//this.form['buttons']=this.defaultButtonsConfig;
	if(this.customConfig===undefined)
		this.customConfig={};
	this.form.customization(this.customConfig);
	this.form['ecpOwner']=this;
	return this.form.render();
}

Ecp.IBPSeqNumPromptorWin=function(){
	this.targetSearchPanel=null;
	
	this.window=new Ecp.Window();
	
	this.defaultWinConfig={
		width :435,
		height :120,
		autoScroll :false,
		closeAction :'hide',
		layout :'fit',
		border :false,
		minimizable :false,
		maximizable :false,
		title:TXT.case_enter_ibp_seq_num,
		resizable :false,
		modal :true,
		shadow:false,
		layoutConfig : {
			animate :false
		},
		buttonAlign :'center'
	};
	
	this.defaultItemsConfig=null;
	
	this.defaultButtonsConfig=[{
		text:TXT.common_btnOK,
		scope:this
	}];
	
	this.eventConfig=null;
	
	this.customConfig=null;
	
	this.IBPSeqNumForm=null;
	
	this.dataBus=null;
}

Ecp.IBPSeqNumPromptorWin.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.IBPSeqNumPromptorWin.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.IBPSeqNumPromptorWin.prototype.setIBPsnForm=function(form){
	this.IBPSeqNumForm=form;
}

Ecp.IBPSeqNumPromptorWin.prototype.setItems=function(items){
	this.defaultItemsConfig=items;
}

Ecp.IBPSeqNumPromptorWin.prototype.setButtonHandler=function(array){
	this.defaultButtonsConfig[0].handler=array[0];
}

Ecp.IBPSeqNumPromptorWin.prototype.show=function(){
	this.window.show();
}

Ecp.IBPSeqNumPromptorWin.prototype.render=function(){
	this.window.init();
	this.window['config']=this.defaultWinConfig;
	this.window['items']=this.defaultItemsConfig;
	this.window['buttons']=this.defaultButtonsConfig;
	this.window['listeners']=this.eventConfig;
	this.window.customization(this.customConfig);
	return this.window.render();
}

Ecp.IBPSeqNumPromptorWin.createWindow=function(obj,observers){
	if(!Ecp.components['IBPSeqNumWin']){
		var form=new Ecp.IBPSeqNumPromptorForm();
		form.customization(obj['cusIBPSeqNumPromptorForm']);
		var win=new Ecp.IBPSeqNumPromptorWin();
		win.setItems([form.render()]);
		win.setButtonHandler([getIBPSeqNum]);
		win.setIBPsnForm(form);
		//win.setTargetSearchPanel(obj['targetSearchPanel']);
		win.eventConfig={
			beforeshow:function(){
				form.form.reset();
			},
			show:function(){
				form.form.findFieldById('IBPSeqNum').focus(false,true);
			}
		};
		win.customization(obj['cusIBPSeqNumWin']);
		win.render();
		Ecp.components['IBPSeqNumWin']=win;
	}
	Ecp.components['IBPSeqNumWin'].dataBus=obj['dataBus'];
	return Ecp.components['IBPSeqNumWin'];
}
Ecp.ForwaredToClsWin=function(messaageId){
	Ecp.ServiceWindow.call(this);
	this.config={
		 	title:TXT.payment_globalSearch,
		width :960,
		height :600,
		autoScroll :false,
		layout :'border',
		border :false,
		minimizable :false,
		maximizable :false,
		modal:true,
		shadow:false,
		layoutConfig : {animate :false},
		resizable :false,
		buttonAlign :'center'
	};
	this.buttons=[{
		text:TXT.common_btnOK,
		handler: function(){
			selectPaymentToCls(messaageId);
		}
	},{
		text:TXT.common_btnClose,
		scope:this,
		handler:function(){
			this.window.window.close();
		}
	}];
}
Ecp.ForwaredToClsWin.createWindow=function(fun,caseId,messaageId){
	var paymentForForwaredWinGrid = new Ecp.paymentForForwaredWinGrid();
	paymentForForwaredWinGrid.defaultStoreConfig.baseParams={caseId:caseId,cmd:'payment',action:'findPaymentInCase'};
	var forwaredToClsWin = new Ecp.ForwaredToClsWin(messaageId);
	if(fun && fun['createRefundBySelectPaymentFun'] && typeof fun['createRefundBySelectPaymentFun']=='function'){
		forwaredToClsWin.handleWidgetConfig(fun['createRefundBySelectPaymentFun']);
	}
	if(fun && fun['createRefundBySelectPaymentGridFun'] && typeof fun['createRefundBySelectPaymentGridFun']=='function'){
		paymentForForwaredWinGrid.handleWidgetConfig(fun['createRefundBySelectPaymentGridFun']);
	}
	forwaredToClsWin.items=[paymentForForwaredWinGrid.render()];
	paymentForForwaredWinGrid.show();
	forwaredToClsWin.render();
	Ecp.components.ForwaredToClsPaymentGrid=paymentForForwaredWinGrid;
	Ecp.components.ForwaredToClsWin=forwaredToClsWin;
	return forwaredToClsWin.window;
};
Ecp.extend(Ecp.ForwaredToClsWin.prototype,new Ecp.ServiceWindow());