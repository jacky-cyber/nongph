/**
 * UserGridWidget
 */
Ecp.UserGridWidget=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'users',
		idProperty:'id',
		fields:[
			{name: 'id'},
			    {name: 'username'},
			    {name: 'fullname'},
			    {name: 'tel'},
			    {name: 'email'},
			    {name: 'internalCode'},
			    {name:'institution'},
			    {name: 'isEniUser'},
			    {name: 'credentialsType'},
			    {name: 'credentialsNumber'}
			    ]
	};
	
	this.defaultStoreConfig.baseParams={
		cmd:'user',
		action:'find'
	};
	
	this.defaultCmConfig=[
			{header:TXT.user_userName,dataIndex: 'username',menuDisabled: true,width:150},
			{header:TXT.user_name,dataIndex:'fullname',menuDisabled: true,width:150},
			{header:TXT.user_tel,dataIndex:'tel',menuDisabled: true,width:150},
			{header:TXT.user_e_mail,dataIndex:'email',menuDisabled: true,width:200},
			{header:TXT.user_branched,dataIndex:'institution',menuDisabled: true,width:200},
			{header:TXT.user_credentialsType.title,dataIndex:'credentialsType',menuDisabled: true,width:200
				,renderer:function(value){
						var txt = '_'+value;
						return TXT.user_credentialsType[txt];
				}
					
			},{header:TXT.user_credentialsNumber,dataIndex:'credentialsNumber',menuDisabled: true,width:200}/*,
			{header:TXT.is_eni_user,dataIndex:'isEniUser',menuDisabled: true,width:100,renderer:function(value){
		  		if (value==true) {
					return TXT.common_y;
				}
				else {
					return TXT.common_n;
				}
		  	}}*/

	];
	
	this.defaultGridConfig={
		title:TXT.user_manager,
		id:'userGridId',
		stripeRows :true,
		pagination:true
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;
}

Ecp.UserGridWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.UserGridWidget.prototype.setToolBar=function(tbar){
	this.grid.setTopToolBar(tbar);
}


Ecp.UserGridWidget.prototype.setWidgetEvent=function(widgetEvent){
	widgetEvent['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(widgetEvent['grid']);
	widgetEvent['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(widgetEvent['cm']);
	widgetEvent['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(widgetEvent['store']);
}

Ecp.UserGridWidget.prototype.customization=function(customizationConfig){
	this.customizationConfig=customizationConfig;
}

Ecp.UserGridWidget.prototype.render=function(){
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

Ecp.UserGridWidget.prototype.show=function(){
	this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}

Ecp.UserGridWidget.prototype.search=function(params){
	if(this.grid['pagination']!=null)
	{
		params['start']=0;
		params['limit']=PAGE_SIZE;
	}
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load(params);
}

//observer callback
Ecp.UserGridWidget.prototype.update=function(src,eventName)
{
	switch(eventName)
	{
		case 'reload':
			this.reloadUpdate(src);
			break;
	}
}

Ecp.UserGridWidget.prototype.reloadUpdate=function(src)
{
	this.dataStore.store.reload();
}

// User toolbar
Ecp.UserToolBarWidget=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.showQueryUserWin];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'userToolBar'
	};
}

//1.product customization
Ecp.UserToolBarWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

//2
Ecp.UserToolBarWidget.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.UserToolBarWidget.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

//3  widget event function
Ecp.UserToolBarWidget.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

//4 project customization function
Ecp.UserToolBarWidget.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	//toolBar.setOwner(this);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}

/**
 * UserInfoForm
 */
Ecp.UserInfoForm=function()
{
	this.form=null;
	this.config={
		labelAlign: 'left',
		region: 'north',
		labelWidth:80,
		height: 175,
		margins:'0 0 2 0',
	    cmargins:'0 0 2 0',
	    layout:'column',
		frame:true
	};
	this.items=[
		       {
					xtype : 'hidden',
					name : 'id',
					id : 'id'
			   },{
				  	xtype:'hidden',
				  	id: 'internalCode',
				  	name: 'internalCode'
				},
				{
							columnWidth:.5,
	              			layout: 'form',
	              			defaultType: 'textfield',
	              			defaults:{anchor:'85%'},
							items: [
								{
	                  				fieldLabel: TXT.user_userName,
	                  				id: 'username',
	                  				name: 'username',
	                  				readOnly:true
	                			},
								{
	                  				fieldLabel: TXT.user_name,
	                  				id: 'firstname',
	                  				name: 'firstname',
	                  				readOnly:true
	                			},
	                			/*{
	                  				fieldLabel: TXT.user_pass_word,
	                  				inputType: 'password',
	                  				id: 'password',
	                  				name: 'password',
	                  				readOnly:true,
									allowBlank:false
	                			},*/
	                			{
	                				xtype:'trigger', 
	                				fieldLabel: TXT.user_branched,
	                				id: 'institution',
	                				name: 'institution',
	                				readOnly:true
	                			},{	xtype:'combo',
	                  				fieldLabel: TXT.user_credentialsType.title,
	                  				id: 'credentialsType',
	                  				name: 'credentialsType',
	                  				value: false,
	                				readOnly:true,
                  					store: new Ext.data.SimpleStore({
									fields: ['label','value'],
									data: [
									[TXT.user_credentialsType._0,'0'],
									[TXT.user_credentialsType._1,'1']
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
	                			},{
	                				xtype:'combo',
	                				fieldLabel: TXT.user_isLock,
	                				id: 'isLock',
	                				name: 'isLock',
	                				value: false,
	                				readOnly:true,
                  					store: new Ext.data.SimpleStore({
									fields: ['label','value'],
									data: [
									[TXT.user_lock,true],
									[TXT.user_notLock,false]
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
	                			}
							]
						},{
							columnWidth:.5,
	              			layout: 'form',
	              			defaultType: 'textfield',
	              			defaults:{anchor:'85%'},
							items: [
								{
	                  				fieldLabel: TXT.user_e_mail,
	                  				id: 'email',
	                  				vtype: 'email', 
	                  				name: 'email',
	                  				readOnly:true
	                			},
								{
	                  				fieldLabel: TXT.user_tel,
	                  				id: 'tel',
	                  				name: 'tel',
	                  				readOnly:true
	                			},
	                			/*{
	                  				fieldLabel: TXT.user_confirm_pwd,
	                  				inputType: 'password',
	                  				id: 'confirmPwd',
	                  				name: 'password',
									allowBlank:false,
									readOnly:true,
									vtype: 'password',
                                    initialPassField: 'password' 
	                			},*/
	                			{
	                				xtype:'combo',
                  	                fieldLabel: TXT.user_receiveEmail,
                  					id: 'receiveEmail',
                  					name: 'receiveEmail',
                  					value:true,
                  					readOnly:true,
                  					store: new Ext.data.SimpleStore({
									fields: ['label','value'],
									data: [
									[TXT.user_receive,true],
									[TXT.user_notReceive,false]
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
	                			},{
	                  				fieldLabel: TXT.user_credentialsNumber,
	                  				id: 'credentialsNumber',
	                  				name: 'credentialsNumber',
	                  				readOnly:true
	                			},
	                			/*{
	                				xtype:'combo',
                  	                fieldLabel: TXT.is_eni_user,
                  					id: 'isEniUser',
                  					name: 'isEniUser',
                  					value:true,
                  					store: new Ext.data.SimpleStore({
									fields: ['label','value'],
									data: [
									[TXT.common_y,true],
									[TXT.common_n,false]
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
	                			},*/
	                			{
	                				xtype:'hidden',
	                				id: 'isFirstLogin',
	                				name: 'isFirstLogin',
	                				value: true
	                			}
							]
						}
	];
	this.buttons=[];
}

Ecp.UserInfoForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// 
Ecp.UserInfoForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.UserInfoForm.prototype.render=function()
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
Ecp.UserInfoForm.prototype.setValue=function(propertyName, value)
{
	this.form.setValue(propertyName, value);
}

Ecp.UserInfoForm.prototype.setValues=function(json)
{
	this.form.setValues(json);
}

Ecp.UserInfoForm.prototype.getValues=function(asString)
{
	return this.form.getValues(asString);
}

/**
 *  User Permission Grid
 */
Ecp.UserPermGridWidget=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'roles',
		idProperty:'id',
		fields:[
			{name: 'id'},
			{name: 'rolename'},
			{name: 'description'}]
	};
	
	this.defaultCmConfig=[
			{header:TXT.role_names,dataIndex: 'rolename',menuDisabled: true,width:150},
			{header:TXT.role_description,dataIndex:'description',menuDisabled: true,width:210}
	];
	
	this.defaultGridConfig={
		border :true,
		region :'center',
		title:TXT.role_set_up,
		id:'roleGridId',
		stripeRows :true,
		loadMask: true
	};
	
	this.customizationConfig={};
	
	//this.defaultSelModel=2;  //modify by sunyue 20130130
}
//1 product customization
Ecp.UserPermGridWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

//3 widget event function
Ecp.UserPermGridWidget.prototype.setWidgetEvent=function(widgetEvent){
	widgetEvent['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(widgetEvent['grid']);
	widgetEvent['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(widgetEvent['cm']);
	widgetEvent['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(widgetEvent['store']);
}
//4 project customization function
Ecp.UserPermGridWidget.prototype.customization=function(customizationConfig){
	this.customizationConfig=customizationConfig;
}

Ecp.UserPermGridWidget.prototype.render=function(){
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

Ecp.UserPermGridWidget.prototype.show=function(){
	//this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}}); modify by sunyue 20130131
}

/**
 * UserInfoWindow
 */
Ecp.UserInfoWindow=function()
{
	this.window=null;
	this.config={
		    title:TXT.user_info,
	        width:600,
	        height:420,
	        autoScroll :false,
	        layout:'border',
	        border:false,
	        //closeAction:'hide',
	        minimizable: false,
	        maximizable: false,
	        resizable: false,
	        modal:true,
			layoutConfig: {animate:false},
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
	
	// events
	this.listeners=null;
}

// product customization
Ecp.UserInfoWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// project customization
Ecp.UserInfoWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.UserInfoWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.UserInfoWindow.prototype.render=function()
{
	this.window=new Ecp.Window();
	this.window.init();
	var winObj={};
	winObj['config']=this.config;
	winObj['buttons']=this.buttons;
	winObj['items']=this.items;
	winObj['listeners']=this.listeners;
	this.window.customization(winObj);
	this.window['ecpOwner']=this;
	return this.window.render();
}


Ecp.UserInfoWindow.prototype.addObserver=function(observer)
{
	this.observers.push(observer);
}

/**
 * execute every observer's update method
 */
Ecp.UserInfoWindow.prototype.notifyAll=function(eventName)
{
	for(var i=0;i<this.observers.length;i++)
		this.observers[i].update(this.window.window,eventName);
}

Ecp.UserInfoWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.UserInfoWindow.prototype.show=function(){
	this.window.window.show();
}

Ecp.UserInfoWindow.prototype.setCloseAction=function(isClose){
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
Ecp.UserInfoWindow.singleInfoWindows=[];

/**
 * create ecp window
 */
Ecp.UserInfoWindow.createUserInfoWindow=function(windowObj,observers,fun){
	var userForm=new Ecp.UserInfoForm();
	if(windowObj['items'] && windowObj['items'][0])
		userForm.customization(windowObj['items'][0]);
	
	Ecp.components.userForm=userForm;
	// grid
	var userPermGrid=new Ecp.UserPermGridWidget();
	
	if(windowObj['items'] && windowObj['items'][1])
		userPermGrid.customization(windowObj['items'][1]);
	
	// window
	var userInfoWin = new Ecp.UserInfoWindow();
	userInfoWin.handleWidgetConfig(function(obj){
		if(fun && typeof fun=='function')
			userInfoWin.handleWidgetConfig(fun);
		obj.items=[userForm.render(), userPermGrid.render()];
		userPermGrid.show();
	});
	userInfoWin.customization(windowObj);
	
	// add observer
	if(observers && observers.length>0)
		userInfoWin.observers=observers;
	userInfoWin.render();
	return userInfoWin.window;
}

/**
 * create single add window static function
 */
Ecp.UserInfoWindow.createSingleAddWindow=function(windowObj,observers){
	if(!Ecp.UserInfoWindow.singleInfoWindows[0])
		Ecp.UserInfoWindow.singleInfoWindows[0]=Ecp.UserInfoWindow.createUserInfoWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	return Ecp.UserInfoWindow.singleInfoWindows[0];
}

/**
 * create single edit window static function
 */
Ecp.UserInfoWindow.createSingleEditWindow=function(windowObj,observers){
	if(!Ecp.UserInfoWindow.singleInfoWindows[1])
	{
		Ecp.UserInfoWindow.singleInfoWindows[1]=Ecp.UserInfoWindow.createUserInfoWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	}
	return Ecp.UserInfoWindow.singleInfoWindows[1];
}

/**
 * UserSearchForm
 */
Ecp.UserSearchForm=function()
{
	this.form=null;
	this.config={
			labelAlign: 'left',
			region: 'center',
		    labelWidth:58,
		    bodyStyle:'margin-top:10px',
		    frame:true
	};
	this.items=[
				{
					xtype: 'textfield',
	            	fieldLabel:TXT.user_userName,
	            	id: 'username',
	            	name: 'username',
	            	width: 175
                },
                {//modify by sunyue 20130130
					xtype: 'textfield',
	            	fieldLabel:TXT.user_name,
	            	id: 'fullname',
	            	name: 'fullname',
	            	width: 175
                }
	];
	this.buttons=[];
}

Ecp.UserSearchForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.UserSearchForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.UserSearchForm.prototype.render=function()
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
 * UserSearchWindow
 */
Ecp.UserSearchWindow=function()
{
	this.window=null;
	this.config={
		    title:TXT.user_find,
			width:300,
	        height:140,
	        autoScroll :false,
	        layout:'fit',
	        border:false,
	        //closeAction:'hide',
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
			clickQueryUserBtn(this);
		}
	}];
	this.observers=[];
	this.items=[];
}

Ecp.UserSearchWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.UserSearchWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.UserSearchWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.UserSearchWindow.prototype.render=function()
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

Ecp.UserSearchWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.UserSearchWindow.prototype.setCloseAction=function(isClose){
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
Ecp.UserInfoWindow.singleSearchWindows=null;

/**
 * create single search window static function
 */
Ecp.UserInfoWindow.createSingleSearchWindow=function(windowObj){
	if(!Ecp.UserInfoWindow.singleSearchWindows)
	{
		var userSearchForm=new Ecp.UserSearchForm();
		if(windowObj['items'] && windowObj['items'][0])
			userSearchForm.customization(windowObj['items'][0]);
			
		// window
		var userSearchWin = new Ecp.UserSearchWindow();
		userSearchWin.handleWidgetConfig(function(obj){
			obj.setCloseAction(false);
			obj.items=[userSearchForm.render()];
		});
		userSearchWin.customization(windowObj);		
		userSearchWin.render();
		Ecp.UserInfoWindow.singleSearchWindows=userSearchWin.window;
	}
	return Ecp.UserInfoWindow.singleSearchWindows;
}