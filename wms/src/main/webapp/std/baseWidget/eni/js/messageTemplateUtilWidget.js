Ecp.MessageTemplateListWin=function(){
	this.grid=null;
	this.window=new Ecp.Window();
	this.config={
		title :TXT.privateTemplate_public,
		width :700,
		height :400,
		autoScroll :true,
		layout :'fit',
		border :false,
//		closeAction:'hide',
		minimizable :false,
		maximizable :false,
		resizable :false,
		modal :true,
		shadow:false,
		layoutConfig : {
			animate :false
		},
		buttonAlign :'center'
	};
	
	this.items=[];
	
	this.buttons=[{
		text:TXT.common_btnOK,
		scope:this
	},{
		text:TXT.common_btnClose,
		handler:function(){
	//		this.window.window.hide();
			this.window.close();
		},
		scope:this
	}];
	
	this.cusObj=null;
	
	this.eventConfig=null;
	
	this.observers=[];
	
	this.dataBus=null;
	
}

Ecp.MessageTemplateListWin.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.MessageTemplateListWin.prototype.customization=function(obj){
	this.cusObj=obj;
}

Ecp.MessageTemplateListWin.prototype.setGrid=function(grid){
	this.grid=grid;
}

Ecp.MessageTemplateListWin.prototype.setItems=function(items){
	for(var i=0;i<items.length;i++)
		this.items.push(items[i]);
}

Ecp.MessageTemplateListWin.prototype.setButtonHandler=function(handler){
	this.buttons[0].handler=handler;
}

Ecp.MessageTemplateListWin.prototype.render=function(){
	this.window.init();
	this.window['config']=this.config;
	this.window['items']=this.items;
	this.window['buttons']=this.buttons;
	this.window['listeners']=this.eventConfig;
	this.window.customization(this.cusObj);
	return this.window.render();
}

Ecp.MessageTemplateListWin.createWindow=function(obj,observers){
	if(!Ecp.components['messageTemplateWin']){
		var messageTemplateGrid=new Ecp.MessageTemplateGridWidget();
		messageTemplateGrid.handleWidgetConfig(function(tmpGrid){
			if(obj['caseId']){
				tmpGrid['defaultStoreConfig']['baseParams']={
					cmd :'messageTemplate',
					action :'findByInst',
					type:obj['type'],
					caseId:obj['caseId']
				};
			}else{
				tmpGrid['defaultStoreConfig']['baseParams']={
					cmd :'messageTemplate',
					action :'findByInst',
					type:obj['type']
				};
			}
			tmpGrid['defaultCmConfig'].splice(2,3);
			delete tmpGrid['defaultGridConfig']['title'];
		});
		messageTemplateGrid.setWidgetEvent({
			/*store:{
				load:function(){
					messageTemplateGrid.grid.selectFirstRow();
				}
			}*/
			grid:{
				dblclick:showTemplateLayoutByDBClickGrid
			}
		});
		messageTemplateGrid.customization(obj['cusMsgTmpGrid']);
		var win=new Ecp.MessageTemplateListWin();
		win.setItems([messageTemplateGrid.render()]);
		win.setGrid(messageTemplateGrid);
		win.setButtonHandler(showTemplateLayout);
		win.eventConfig={
			hide:function(){
				messageTemplateGrid.grid.grid.selModel.clearSelections();
			}
		};
		win.customization(obj['cusMsgTmpWin']);
		win.render();
		Ecp.components['messageTemplateWin']=win;
	}else{
		Ecp.components['messageTemplateWin'].grid.dataStore.store.baseParams.type=obj['type'];
	}
	return Ecp.components['messageTemplateWin'];
}

Ecp.MessageTemplateListWin.createPrivateMsgListWindow=function(obj,observers){
	//if(!Ecp.components['privateMessageTemplateWin']){
		var privateMessageTemplateGrid=new Ecp.PrivateMsgTmpListGrid();
		privateMessageTemplateGrid.setWidgetEvent({
			/*store:{
				load:function(){
					messageTemplateGrid.grid.selectFirstRow();
				}
			}*/
			grid:{
				dblclick:showTemplateLayoutByDBClickGrid
			}
		});
		if(obj['caseId']&&obj['caseId']=='0'){
			privateMessageTemplateGrid.defaultStoreConfig.baseParams = {
					'cmd' :'messageTemplate',
					'action' :'findPrivateTemplate',
					caseId:obj['caseId']
			};
		}
		privateMessageTemplateGrid.customization(obj['cusPrivaeMsgTmpGrid']);
		var win=new Ecp.MessageTemplateListWin();
		win.handleWidgetConfig(function(winConfig){
			winConfig['config']['title']=TXT.private_message_template;
			winConfig['buttons'].splice(1,0,{
				text:TXT.common_btnDelete,
				handler:deleteDraft,
				scope:win
			});
		});
		win.setItems([privateMessageTemplateGrid.render()]);
		win.setGrid(privateMessageTemplateGrid);
		win.eventConfig={
			hide:function(){
				privateMessageTemplateGrid.grid.grid.selModel.clearSelections();
			}
		};
		win.setButtonHandler(showTemplateLayout);
		win.customization(obj['cusPrivaeMsgTmpWin']);
		win.render();
		Ecp.components['privateMessageTemplateWin']=win;
//	}
	return Ecp.components['privateMessageTemplateWin'];
}

Ecp.PrivateMsgTmpListGrid=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'privateMessageTemplates',
		idProperty:'id',
		fields:[
			{name: 'id'},
			{name: 'name'},
			{name: 'type'},
			{name: 'owner'},
			{name: 'createTime'},
			{name:'ownerId'},
			{name:'templateId'}
			]
	};
	this.defaultStoreConfig.baseParams = {
		'cmd' :'messageTemplate',
		'action' :'findPrivateTemplate'
	};
	this.defaultCmConfig=[ {
				header :TXT.private_message_template_name,
				dataIndex :'name',
				menuDisabled: true,
				width :200
			}, {
				header :TXT.private_message_template_type,
				dataIndex :'type',
				menuDisabled: true,
				width :100
			}, {
				header :TXT.private_message_template_owner,
				dataIndex :'owner',
				menuDisabled: true,
				width :100
			}, {
				header :TXT.private_message_template_createTime,
				dataIndex :'createTime',
				menuDisabled: true,
				width :200
			}];
	
	this.defaultGridConfig={
		id:'privateMsgTmpGrid',
		pagination:true
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;
}

Ecp.PrivateMsgTmpListGrid.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.PrivateMsgTmpListGrid.prototype.setToolBar=function(tbar){
	this.grid.setTopToolBar(tbar);
}

//3 Widget Event Function
Ecp.PrivateMsgTmpListGrid.prototype.setWidgetEvent=function(widgetEvent){
	widgetEvent['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(widgetEvent['grid']);
	widgetEvent['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(widgetEvent['cm']);
	widgetEvent['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(widgetEvent['store']);
}
//4 Project Customization function
Ecp.PrivateMsgTmpListGrid.prototype.customization=function(customizationConfig){
	this.customizationConfig=customizationConfig;
}

Ecp.PrivateMsgTmpListGrid.prototype.render=function(){
	this.dataStore.setConfig(this.defaultStoreConfig);
	this.dataStore.init();
	this.customizationConfig['store']==null?1:this.dataStore.customization(this.customizationConfig['store']);
	this.grid.setStore(this.dataStore.render());
	this.grid.setColumnMode(this.defaultCmConfig);
	this.grid.setSelectMode(this.defaultSelModel);
	this.grid.setConfig(this.defaultGridConfig);
	this.grid.init();
	this.customizationConfig['grid']==null?1:this.grid.customization(this.customizationConfig['grid']);
	this.grid['ecpOwner']=this;
	return this.grid.render();
}

Ecp.PrivateMsgTmpListGrid.prototype.show=function(){
	this.dataStore.store.load({
		params:{
			start :0,
			limit :PAGE_SIZE
		}
	});
}

Ecp.PrivateMsgTmpListGrid.prototype.update=function(src,eventName){
	if(eventName=='reload')
		this.dataStore.store.reload();
}

Ecp.DraftNameSetForm=function(){
	this.form=new Ecp.FormPanel();
	
	this.defaultFormConfig={
		labelAlign :'left',
		labelWidth :60,
		frame :true,
		autoScroll :false,
		bodyStyle :'padding:10px 10px 10px 10px',
		layout :'form'
	};
	
	this.defaultItemsConfig=[{
		xtype :'textfield',
		fieldLabel :TXT.privateTemplate_entername,
		width :280,
		id :'tName',
		name :'tName',
		allowBlank :false
	}];
	
	this.defaultButtonsConfig=[];
	
	this.customConfig=null;
}

Ecp.DraftNameSetForm.prototype.handlerWidgetConfig=function(handler){
	handler(this);
}

Ecp.DraftNameSetForm.prototype.customization=function(cusObj){
	this.customConfig=cusObj;
}

Ecp.DraftNameSetForm.prototype.render=function(){
	this.form.init();
	this.form['config']=this.defaultFormConfig;
	this.form['items']=this.defaultItemsConfig;
	this.form['buttons']=this.defaultButtonsConfig;
	this.form.customization(this.customConfig);
	this.form['ecpOwner']=this;
	return this.form.render();
}

Ecp.DraftNameSetWindow=function(){
	
	this.form=null;
	
	this.window=new Ecp.Window();
	
	this.config={
		title :TXT.privateTemplate_name,
		width :420,
		height :130,
		closeAction:'hide',
		autoScroll :false,
		layout :'fit',
		border :false,
		minimizable :false,
		maximizable :false,
		resizable :false,
		modal :true,
		shadow:false,
		layoutConfig : {
			animate :false
		},
		buttonAlign :'center'
	};
	
	this.items=[];
	
	this.buttons=[{
		text:TXT.common_btnOK,
		scope:this
	}];
	
	this.cusObj=null;
	
	this.eventConfig=null;
	
	this.observers=[];
	
	this.dataBus=null;
}

Ecp.DraftNameSetWindow.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.DraftNameSetWindow.prototype.customization=function(obj){
	this.cusObj=obj;
}

Ecp.DraftNameSetWindow.prototype.setForm=function(form){
	this.form=form;
}

Ecp.DraftNameSetWindow.prototype.setItems=function(items){
	for(var i=0;i<items.length;i++)
		this.items.push(items[i]);
}

Ecp.DraftNameSetWindow.prototype.setButtonHandler=function(handler){
	this.buttons[0].handler=handler;
}

Ecp.DraftNameSetWindow.prototype.addObservers=function(observers){
	for(var i=0;i<observers.length;i++)
		this.observers.push(observers[i]);
}

Ecp.DraftNameSetWindow.prototype.notifyAll=function(eventName){
	for(var i=0;i<this.observers.length;i++)
		this.observers[i].update(this,eventName);
}

Ecp.DraftNameSetWindow.prototype.render=function(){
	this.window.init();
	this.window['config']=this.config;
	this.window['items']=this.items;
	this.window['buttons']=this.buttons;
	this.window['listeners']=this.eventConfig;
	this.window.customization(this.cusObj);
	return this.window.render();
}

Ecp.DraftNameSetWindow.createWindow=function(obj,observers){
	if(!Ecp.components['draftNameSetWindow']){
		var form=new Ecp.DraftNameSetForm();
		form.customization(obj['cusNameForm']);
		var win=new Ecp.DraftNameSetWindow();
		win.setItems([form.render()]);
		win.setForm(form);
		win.customization(obj['cusNameWin']);
		win.eventConfig={
			beforeshow:function(){
				win.form.form.reset();
			}
		}
		win.setButtonHandler(saveOrEditDraftTemplate);
		//win.addObservers(observers);
		win.render();
		Ecp.components['draftNameSetWindow']=win;
	}
	if(observers.length!=0){
		if(Ecp.components['draftNameSetWindow'].observers.length==0)
			Ecp.components['draftNameSetWindow'].addObservers(observers);
	}
	return Ecp.components['draftNameSetWindow'];
}