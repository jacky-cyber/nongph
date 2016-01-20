/**
 * Institution Tree Panel
 */
Ecp.InstitutionGridWidget=function(){
	this.treeGrid=new Ecp.TreeGrid();
	this.topToolBar=null;
	this.config={
		id : 'institution',
		title : TXT.inst,
		border : true,
		enableHdMenu: false,
		columnResize: true,
		enableSort : false,
		region: 'center',
		isFirstLevelExpand: true,
		dataUrl:DISPATCH_SERVLET_URL,
		autoScroll:true
	};
	this.config.baseParams={
		cmd:'institution',
		action:'find'
	};

	this.columnConfig=[
			{header:TXT.institution_name,dataIndex: 'name',width:350},
			{header:TXT.institution_code,dataIndex:'internalCode',width:80},
			{header:TXT.institution_type,dataIndex:'type',width:80,
				tpl: new Ext.XTemplate('{type:this.formatType}', {
                	formatType: function(value) {
                    	/*if(value=='1')
							return TXT.institution_type_hq;
						else if(value=='0')
							return TXT.institution_type_sub;
						else*/
							return value;
               	 	}
            	})
			},
			{header:TXT.bic_code,dataIndex:'bicCode',width:100},
			{header:TXT.institution_sendSwiftReport,dataIndex:'sendToSwift',width:100,
				tpl: new Ext.XTemplate('{sendToSwift:this.formatSendToSwift}', {
                	formatSendToSwift: function(value) {
                    	if (value == true) {
						return TXT.common_yes_desc;
					} else {
						return TXT.common_no_desc;
					}
               	 	}
            	})
			},
			{header:TXT.institution_cases,dataIndex:'hasSubAccount',width:80,
				tpl: new Ext.XTemplate('{hasSubAccount:this.formatHasSubAccount}', {
                	formatHasSubAccount: function(value) {
                    	if (value == '1') {
							return TXT.institution_hasSubAccount_yes;
						} else {
							return TXT.institution_hasSubAccount_no;
						}
               	 	}
            	})
			},
			{header:TXT.institution_shortCode,dataIndex:'shortCode',width:40},
			{header:TXT.institution_logicTerminal,dataIndex:'logicTerminal',width:100},
			{header:TXT.institution_address,dataIndex:'address',width:200},
			{header:TXT.institution_city,dataIndex:'city',width:80},
			{header:TXT.institution_orgPath,dataIndex:'orgPath',width:100},
			{header:TXT.institution_telephone1,dataIndex:'telephone1',width:100},
			{header:TXT.institution_telephone2,dataIndex:'telephone2',width:100},
			{header:TXT.institution_fax1,dataIndex:'fax1',width:100},
			{header:TXT.institution_fax2,dataIndex:'fax2',width:100},
			{header:TXT.institution_clsInstId,dataIndex:'clsInstId',width:100},
			{header:TXT.institution_isSpeInstId.title,dataIndex:'isSpeInstId',width:100,
				tpl: new Ext.XTemplate('{isSpeInstId:this.formatIsSpeInstId}', {
                	formatIsSpeInstId: function(value) {
                		if (value != null && value!="") {
							return TXT.institution_isSpeInstId["_"+value];
						}else{
							return value;
						}
               	 	}
            	})
			}
	],
	this.cusObj=null;
	this.eventObj=null;
}

//1. Product Customization
Ecp.InstitutionGridWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.InstitutionGridWidget.prototype.setToolBar=function(tbar){
	this.topToolBar=tbar;
}

//3. Tree Grid Event function
Ecp.InstitutionGridWidget.prototype.setTreeGridEvent=function(obj){
	this.eventObj=obj;
}
//4 Project Customization function
Ecp.InstitutionGridWidget.prototype.customization=function(obj){
	this.cusObj=obj;
}

Ecp.InstitutionGridWidget.prototype.render=function(){
	this.treeGrid.setIsFirstLevelExpand(true);
	this.treeGrid.setTreeConfig(this.config);
	this.treeGrid.setTreeColumnConfig(this.columnConfig);
	if(this.topToolBar!=null)
		this.treeGrid.setTopToolBar(this.topToolBar);
	if(this.eventObj!=null)
		this.treeGrid.setTreeEventConfig(this.eventObj);
	if(this.cusObj!=null)
		this.treeGrid.customization(this.cusObj);
	return this.treeGrid.render();
}

// institution toolbar
Ecp.InstitutionToolBarWidget=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[
									//totalToolBarItem.addInstitution,
									//totalToolBarItem.editInstitution,
								   //totalToolBarItem.deleteInstitution,
								   totalToolBarItem.showInstitution,
								   totalToolBarItem.queryInstitution];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'institutionToolBar'
	};
}

//1
Ecp.InstitutionToolBarWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

//2
Ecp.InstitutionToolBarWidget.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.InstitutionToolBarWidget.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

//3
Ecp.InstitutionToolBarWidget.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.InstitutionToolBarWidget.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	//toolBar.setOwner(this);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}

/**
 * Institution Information Form
 */
Ecp.InstitutionInfoForm=function()
{
	this.form=null;
	this.config={
		labelAlign: 'center',
		region: 'center',
		labelWidth: 95,
		layout:'column',
		frame:true
//		defaultType: 'textfield'	
    };
	
	this.items=[
				{
					columnWidth :.5,
					layout :'form',
					defaultType :'textfield',
					defaults : {anchor :'95%'},
					items : [{
								xtype : 'hidden',
								name : 'sid',
								id : 'sid'
							},{
								xtype: 'textfield',
								fieldLabel: TXT.institution_code,
								id: 'internalCode',
								name: 'internalCode',
								width: 180,
								minLength: 0,
								maxLength: 11,
								readOnly:true
							}, {
								xtype: 'textfield',
								fieldLabel : TXT.institution_type,
								id : 'type',
								name : 'type',
								width: 180,
								maxLength : 4,
								readOnly:true
							}, {
								xtype: 'textfield',
								fieldLabel: TXT.bic_code,
								id :'bicCode',
								name :'bicCode',
								width: 180,
								regex:/^([a-zA-Z\d]{8}$|^[a-zA-Z\d]{11}$)$/,
								readOnly:true,
								regexText: TXT.institution_bidError
							}, {
								xtype :'combo',
								fieldLabel: TXT.institution_cases,
								id :'hasSubAccount',
								name :'hasSubAccount',
								width: 180,
								value :"1",
								store :new Ext.data.SimpleStore(
								{
									fields : [ 'label', 'value'],
									data : [
										[TXT.institution_hasSubAccount_yes,"1" ],
										[TXT.institution_hasSubAccount_no,"0" ] 
									]
								}),
								forceSelection :true,
								displayField :'label',
								valueField :'value',
								typeAhead :true,
								mode :'local',
								editable:false,
								triggerAction :'all',
								readOnly:true,
								selectOnFocus :true                
							},{
								xtype: 'textfield',
								fieldLabel: TXT.institution_shortCode,
								id: 'shortCode',
								readOnly:true,
								name: 'shortCode',
								width: 180
							},{
								xtype: 'textfield',
								fieldLabel: TXT.institution_city,
								id: 'city',
								name: 'city',
								readOnly:true,
								width: 180
							},{
								xtype: 'textfield',
								fieldLabel: TXT.institution_telephone1,
								id: 'telephone1',
								name: 'telephone1',
								readOnly:true,
								width: 180
							},{
								xtype: 'textfield',
								fieldLabel: TXT.institution_fax1,
								id: 'fax1',
								name: 'fax1',
								readOnly:true,
								width: 180
							},{
								xtype: 'textfield',
								fieldLabel: TXT.institution_clsInstId,
								id: 'clsInstId',
								name: 'clsInstId',
								readOnly:true,
								width: 180
							},{
								xtype: 'textfield',
								fieldLabel: TXT.institution_address,
								id: 'address',
								name: 'address',
								readOnly:true,
								width: 180
							}
					
					]},{
					columnWidth :.5,
					layout :'form',
					defaultType :'textfield',
					defaults : {anchor :'95%'},
					items : [{
								xtype : 'hidden',
								name : 'contactMan',
								id : 'contactMan'
							},{
								xtype: 'textfield',
								fieldLabel: TXT.institution_name,
								id: 'name',
								name: 'name',
								readOnly:true,
								width: 180
							},{
								xtype: 'textfield',
								fieldLabel : TXT.institution_parent,
								id : 'parent',
								name : 'parent',
								width: 180,
								readOnly : true
							},{
								xtype :'combo',
								fieldLabel: TXT.institution_sendSwiftReport,
								id :'sendToSwift',
								name :'sendToSwift',
								width: 180,
								readOnly:true,
								value: false,
								store: new Ext.data.SimpleStore(
								{
									fields : [ 'label', 'value' ],
									data : [
										[TXT.common_yes_desc, true],
										[TXT.common_no_desc, false] 
									]
								}),
								forceSelection :true,
								displayField :'label',
								valueField :'value',
								typeAhead :true,
								mode :'local',
								editable:false,
								triggerAction :'all',
								selectOnFocus :true,
								allowBlank: false,
								listeners : {
									collapse : function(combo) {			
										if (combo.getValue() == true) {
											combo.ownerCt.ecpOwner.setAllowBlank('bicCode', false);
										} else {
											combo.ownerCt.ecpOwner.setAllowBlank('bicCode', true);
										}
									}
								}
							}, {
								xtype :'combo',
								fieldLabel: TXT.institution_notSkip,
								id :'notSkip',
								name :'notSkip',
								width: 180,
								value :true,
								readOnly:true,
								store :new Ext.data.SimpleStore({
									fields : ['label', 'value'],
									data : [
										[TXT.common_yes_desc, true ],
										[TXT.common_no_desc, false ] 
									]
								}),
								forceSelection :true,
								displayField :'label',
								valueField :'value',
								typeAhead :true,
								editable:false,
								mode :'local',
								triggerAction :'all',
								selectOnFocus :true
						   },{
								xtype: 'textfield',
								fieldLabel: TXT.institution_logicTerminal,
								id: 'logicTerminal',
								name: 'logicTerminal',
								readOnly:true,
								width: 180
							},{
								xtype: 'textfield',
								fieldLabel: TXT.institution_orgPath,
								id: 'orgPath',
								name: 'orgPath',
								readOnly:true,
								width: 180
							},{
								xtype: 'textfield',
								fieldLabel: TXT.institution_telephone2,
								id: 'telephone2',
								name: 'telephone2',
								readOnly:true,
								width: 180
							},{
								xtype: 'textfield',
								fieldLabel: TXT.institution_fax2,
								id: 'fax2',
								name: 'fax2',
								readOnly:true,
								width: 180
							},{
								xtype :'combo',
								fieldLabel: TXT.institution_isSpeInstId.title,
								id: 'isSpeInstId',
								name: 'isSpeInstId',
								readOnly:true,
								width: 180,
								store :new Ext.data.SimpleStore({
									fields : ['label', 'value'],
									data : [
										[TXT.institution_isSpeInstId._0, "0" ],
										[TXT.institution_isSpeInstId._1, "1" ],
										[TXT.institution_isSpeInstId._2, "2" ],
										[TXT.institution_isSpeInstId._3, "3" ] 
									]
								}),
								forceSelection :true,
								displayField :'label',
								valueField :'value',
								typeAhead :true,
								mode :'local',
								triggerAction :'all',
								selectOnFocus :true,
								editable:false
							}
					]}
	];
	
	this.buttons=[];
}

// Product Customization
Ecp.InstitutionInfoForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// Project Customization
Ecp.InstitutionInfoForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.InstitutionInfoForm.prototype.render=function()
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
 * the institution window
 *
 * attribute: some of them have their own default values
 * method: should have the methods such as handleWidgetConfig, customization, render, setValues, getValues
 *
 * note:
 * if the window uses observer pattern, the class should have addObserver, notifyAll
 */
Ecp.InstitutionInfoWindow=function()
{
	this.window=null;
	this.config={
		title: TXT.inst,
		width:750,
		height:380,
		autoScroll:false,
		layout: 'fit',
		border:false,
		minimizable: false,
		maximizable: false,
		resizable: false,
		modal:true,
		layoutConfig: {animate:false},
		buttonAlign: 'center'
	};
	this.buttons=[{
		//text :TXT.common_btnOK,
		//handler : function() {
		//	clickSaveInstitBtn(this);
		//}
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
Ecp.InstitutionInfoWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// Project Customization Interface
Ecp.InstitutionInfoWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.InstitutionInfoWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.InstitutionInfoWindow.prototype.render=function()
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

Ecp.InstitutionInfoWindow.prototype.addObserver=function(observer)
{
	this.observers.push(observer);
}

Ecp.InstitutionInfoWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.InstitutionInfoWindow.prototype.setCloseAction=function(isClose){
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
Ecp.InstitutionInfoWindow.singleInfoWindows=[];

/**
 * create ecp window
 */
Ecp.InstitutionInfoWindow.createInstitutionInfoWindow=function(windowObj,observers,fun){
	// form
	var institutionForm=new Ecp.InstitutionInfoForm();

	if(windowObj['items'] && windowObj['items'][0])
		institutionForm.customization(windowObj['items'][0]);
	
	// window
	var institInfoWin = new Ecp.InstitutionInfoWindow();
	institInfoWin.handleWidgetConfig(function(obj){
		if(fun && typeof fun=='function')
			institInfoWin.handleWidgetConfig(fun);
		obj.items=[institutionForm.render()];
	});
	institInfoWin.customization(windowObj);

	
	// add observer
	if(observers && observers.length>0)
		institInfoWin.observers=observers;
	institInfoWin.render();
	return institInfoWin.window;
}

/**
 * create single add window static function
 */
Ecp.InstitutionInfoWindow.createSingleAddWindow=function(windowObj,observers){
	if(!Ecp.InstitutionInfoWindow.singleInfoWindows[0])
		Ecp.InstitutionInfoWindow.singleInfoWindows[0]=Ecp.InstitutionInfoWindow.createInstitutionInfoWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	return Ecp.InstitutionInfoWindow.singleInfoWindows[0];
}

/**
 * create single edit window static function
 */
Ecp.InstitutionInfoWindow.createSingleEditWindow=function(windowObj,observers){
	if(!Ecp.InstitutionInfoWindow.singleInfoWindows[1])
	{
		Ecp.InstitutionInfoWindow.singleInfoWindows[1]=Ecp.InstitutionInfoWindow.createInstitutionInfoWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	}
	return Ecp.InstitutionInfoWindow.singleInfoWindows[1];
}

/**
 * Institution Search Form
 */
Ecp.InstitutionSearchForm=function()
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
	            	fieldLabel:TXT.institution_code,
	            	id: 'internalCode',
	            	name: 'internalCode',
	            	width: 165,
	              	allowBlank: false
                }
	];
	this.buttons=[];
}

// Product Customization
Ecp.InstitutionSearchForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// Project Customization
Ecp.InstitutionSearchForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.InstitutionSearchForm.prototype.render=function()
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
 * Institution Search Window
 */
Ecp.InstitutionSearchWindow=function()
{
	this.window=null;
	this.config={
			title: TXT.institution_search,
			width:300,
	        height:115,
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
			clickQueryInstitBtn(this);
		}
	}];
	this.observers=[];
	this.items=[];
}

// Product Customization Interface
Ecp.InstitutionSearchWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// Project Customization Interface
Ecp.InstitutionSearchWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.InstitutionSearchWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.InstitutionSearchWindow.prototype.render=function()
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

Ecp.InstitutionSearchWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.InstitutionSearchWindow.prototype.setCloseAction=function(isClose){
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
Ecp.InstitutionInfoWindow.singleSearchWindows=null;

/**
 * create single search window static function
 */
Ecp.InstitutionInfoWindow.createSingleSearchWindow=function(windowObj){	
	if(!Ecp.InstitutionInfoWindow.singleSearchWindows)
	{
		// form
		var institutionSearchForm=new Ecp.InstitutionSearchForm();

		if(windowObj['items'] && windowObj['items'][0])
			institutionSearchForm.customization(windowObj['items'][0]);

		// window
		var institutionSearchWin = new Ecp.InstitutionSearchWindow();
		institutionSearchWin.handleWidgetConfig(function(obj){
			obj.items=[institutionSearchForm.render()];
			obj.setCloseAction(false);
		});
		// Project Customization
		institutionSearchWin.customization(windowObj);
		
		institutionSearchWin.render();
		Ecp.InstitutionInfoWindow.singleSearchWindows=institutionSearchWin.window;
	}
	return Ecp.InstitutionInfoWindow.singleSearchWindows;
}

/**
 * Internal Code Search Form for common institution
 */
Ecp.InternalCodeSearchForm=function()
{
	this.form=null;
	this.config={
			labelAlign: 'left',
			region: 'north',
		    labelWidth: 70,
		    height: 40,
		    frame:true
	};
	this.items=[
				{
					xtype:'trigger',
	            	fieldLabel:TXT.branch_internalCode,
	            	id: 'internalCode',
	            	name: 'internalCode',
	              	triggerClass:'x-form-search-trigger',
		            allowBlank:true
                }
	];
	this.buttons=[];
}

// Product Customization
Ecp.InternalCodeSearchForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// Project Customization
Ecp.InternalCodeSearchForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.InternalCodeSearchForm.prototype.render=function()
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
 * Internal Code Search Window for common institution
 */
Ecp.InternalCodeSearchWindow=function()
{
	this.window=null;
	this.config={
			title: TXT.institution_choice,
			width:718,
	        height:380,
	        autoScroll :false,
	        layout:'border',
	        border:false,
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
//			var win=this['ecpOwner'];
//			var treeRecord=win.getItem(1).tree.getSelectionModel().getSelectedNode();
//			win['ecpOwner'].callBack(treeRecord,win);
//			win.window.hide();
		}
	}];
	this.observers=[];
	this.items=[];
	this.eventConfig={};
	this.considerHide=null;
	//this.callBack=function(treeRecord,win){};
}

//Product Customization Interface
Ecp.InternalCodeSearchWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// Project Customization Interface
Ecp.InternalCodeSearchWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.InternalCodeSearchWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.InternalCodeSearchWindow.prototype.render=function()
{
	this.window=new Ecp.Window();
	this.window.init();
	var winObj={};
	winObj['config']=this.config;
	winObj['buttons']=this.buttons;
	winObj['items']=this.items;
	winObj['listeners']=this.eventConfig;
	this.window.customization(winObj);
	this.window['ecpOwner']=this;
	return this.window.render();
}

Ecp.InternalCodeSearchWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.InternalCodeSearchWindow.prototype.addObserver=function(observer)
{
	this.observers.push(observer);
}

Ecp.InternalCodeSearchWindow.prototype.setCloseAction=function(isClose){
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

Ecp.InternalCodeSearchWindow.prototype.setCallBack=function(callBackFun)
{
	this.window.window.buttons[0].setHandler(function(){
		//var win=Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows;
		var treeRecord=this.window.getItem(1).tree.getSelectionModel().getSelectedNode();
		callBackFun(treeRecord,this.window);
		if(this.considerHide==true){
			this.window.window.suspendEvents(false);
			this.window.window.hide();
			this.window.window.resumeEvents();
			this.considerHide=null;
		}else
			this.window.window.hide();
	},this);
}

Ecp.InternalCodeSearchWindow.prototype.notifyAll=function(eventName)
{
	for(var i=0;i<this.observers.length;i++)
		this.observers[i].update(this.window.window,eventName);
}

/**
 * single search window static array
 */
Ecp.InternalCodeSearchWindow.singleSearchWindows=null;

/**
 * create single search window static function
 */
Ecp.InternalCodeSearchWindow.createSingleSearchWindow=function(obj,callBackFun,observers){
	if(!Ecp.InternalCodeSearchWindow.singleSearchWindows)
	{
		// window
		var institutionSearchWin = new Ecp.InternalCodeSearchWindow();
		
		// init form
		var institutionSearchForm=new Ecp.InternalCodeSearchForm();
		
		institutionSearchForm.handleWidgetConfig(function(obj){
				obj['items'][0]['onTriggerClick']=function() {
					clickSearchInstitBtn(institutionSearchWin,this);
				}
			});

		if(obj['items'] && obj['items'].length<=2 && obj['items'][0]!=null)
			institutionSearchForm.customization(obj['items'][0]);
		
		// init grid
		var institutionGrid=new Ecp.InstitutionGridWidget();
		
		institutionGrid.handleWidgetConfig(function(obj){
			obj['columnConfig']=[
				{header:TXT.institution_name,dataIndex: 'name',width:330},
				{header:TXT.institution_code,dataIndex:'internalCode',width:125},
				{header:TXT.bic_code,dataIndex:'bicCode',width:125}
			];	
		});
	
		if(obj['items'] && obj['items'].length<=2 && obj['items'][1]!=null)
			institutionGrid.customization(obj['items'][1]);
		
		institutionSearchWin.handleWidgetConfig(function(obj){
			if(observers && observers.length>0)
				obj.observers=observers;
//			if(callBackFun && typeof callBackFun=='function')
//				obj.callBack=callBackFun;
			obj.items=[institutionSearchForm.render(), institutionGrid.render()];
			obj.setCloseAction(false);
		});
		institutionSearchWin.customization(obj);
		
		institutionSearchWin.render();
		//institutionSearchWin.window.window['needClose']=true;
		Ecp.InternalCodeSearchWindow.singleSearchWindows=institutionSearchWin.window;
	} else {
		var baseParams={
		cmd:'institution',
		action:'find'
		}
		Ecp.InternalCodeSearchWindow.singleSearchWindows.getItem(1).load(baseParams);
	}
	if(callBackFun && typeof callBackFun=='function')
		Ecp.InternalCodeSearchWindow.singleSearchWindows['ecpOwner'].setCallBack(callBackFun);
	return Ecp.InternalCodeSearchWindow.singleSearchWindows;
}

Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows=null;

Ecp.InternalCodeSearchWindow.createSingleSearchWithSubAccountWindow=function(obj,callBackFun,observers,listeners){
	var caseId=null;
	var mtn9091Id = null;
	if(Ecp.components.caseGrid!=null)
		caseId=Ecp.components.caseGrid.grid.getSelectedId();
	if(obj['caseId']!==undefined)
		caseId=obj['caseId'];	
	if(obj['mtn9091Id']!==undefined)
		mtn9091Id=obj['mtn9091Id'];	
	if(!Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows)
	{
		// window
		var institutionSearchWin = new Ecp.InternalCodeSearchWindow();
		
		// form
		var institutionSearchForm=new Ecp.InternalCodeSearchForm();
		
		institutionSearchForm.handleWidgetConfig(function(obj){
			obj.config['layout']='column';
			obj.items=[{
			columnWidth :.5,
			layout :'form',
			defaultType :'textfield',
			defaults : {
				anchor :'80%'
			},
			items : [ {
				xtype:'trigger',
            	fieldLabel:TXT.branch_internalCode,
            	id: 'internalCode',
            	name: 'internalCode',
              	triggerClass:'x-form-search-trigger',
	            allowBlank:true,
	            onTriggerClick : function() {
					if(caseId!=null){
						institutionSearchWin['gridBaseParams']={
								cmd:'case',
								action:'getReassignInstitution',
								id:caseId
							};
					}else if(mtn9091Id!=null){
						institutionSearchWin['gridBaseParams']={
								cmd:'message',
								action:'getReassignInstitution',
								id:mtn9091Id
							};
					}
					clickSearchInstitBtn(institutionSearchWin,this);
				}
            } ]
			},
						{
							columnWidth :.5,
							layout :'form',
							defaultType :'textfield',
							defaults : {
								anchor :'80%'
							},
							items : [ {
								xtype :'combo',
								fieldLabel :TXT.institution_hasSubAccount,
								id :'accountAssigne',
								name :'accountAssigne',
								//value :true,
								store :new Ext.data.SimpleStore( {
									fields : [ 'label', 'value' ],
									data : [ [ TXT.common_yes_desc, 'Y' ],
											[ TXT.common_no_desc, 'N' ] ]
								}),
								forceSelection:true,
								displayField:'label',
								valueField: 'value',					                        			
								typeAhead: true,
								mode: 'local',
								//hideTrigger:false,
								triggerAction: 'all',                        			
								selectOnFocus:true,
								editable:false
							} ]
						} ];
		});
		if(obj['items'] && obj['items'][0]!=null)
			institutionSearchForm.customization(obj['items'][0]);
		
		// grid
		var institutionGrid=new Ecp.InstitutionGridWidget();
		
		institutionGrid.handleWidgetConfig(function(obj){
			obj['columnConfig']=[
				{header:TXT.institution_name,dataIndex: 'name',width:330},
				{header:TXT.institution_code,dataIndex:'internalCode',width:125},
				{header:TXT.bic_code,dataIndex:'bicCode',width:125},
				{header:TXT.institution_sendSwiftReport,dataIndex:'sendToSwift',width:100,
					tpl: new Ext.XTemplate('{sendToSwift:this.formatSendToSwift}', {
	                	formatSendToSwift: function(value) {
	                    	if (value == true) {
							return TXT.common_yes_desc;
						} else {
							return TXT.common_no_desc;
						}
	               	 	}
	            	})
				}
			];
			if(caseId!=null){
				obj.config.baseParams={
						cmd:'case',
						action:'getReassignInstitution',
						id:caseId
				};
			}else if(mtn9091Id!=null){
					obj.config.baseParams={
							cmd:'message',
							action:'getReassignInstitution',
							id:mtn9091Id
						};
			}
		});
		
		if(obj['items'] && obj['items'][1]!=null)
			institutionGrid.customization(obj['items'][1]);

		// window
		institutionSearchWin.handleWidgetConfig(function(obj){
			if(observers && observers.length>0)
				obj.observers=observers;
			obj.items=[institutionSearchForm.render(), institutionGrid.render()];
			obj.config.closeAction='hide';
			obj.config.onHide=function(){
				this.hide();
			}
			//obj.setCloseAction(false);
			if(mtn9091Id!=null){
				obj.config.title=TXT.mtn90_91_institution_choice;
			}else{
				obj.config.title=TXT.case_institution_choice;
			}
		});
		institutionSearchWin.eventConfig={
			'beforeshow':function(){
				institutionSearchForm.form.reset();
			}
		};
		institutionSearchWin.customization(obj);
		
		institutionSearchWin.render();
		Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows=institutionSearchWin.window;
	} else {
		var baseParams=null;
		if(caseId!=null){
			baseParams={
					cmd:'case',
					action:'getReassignInstitution',
					id:caseId
				};
		}else if(mtn9091Id!=null){
			baseParams={
						cmd:'message',
						action:'getReassignInstitution',
						id:mtn9091Id
						};
		}else
			baseParams={
				cmd:'institution',
				action:'find'
			};
		//Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows['ecpOwner'].observers=observers;
		Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows.getItem(1).load(baseParams);
	}
	// callBack function
	if(callBackFun && typeof callBackFun=='function')
	{
		Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows['ecpOwner'].setCallBack(callBackFun);
//		Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows.window.buttons[0].setHandler(function(){
//			var win=Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows;
//			var treeRecord=win.getItem(1).tree.getSelectionModel().getSelectedNode();
//			callBackFun(treeRecord,win);
//			win.window.hide();
//		});
	}
	if(listeners && listeners!=null)
	{
		//Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows.window.purgeListeners();
//		Ext.iterate(listeners,function(key,value){
//			Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows.window.removeListener(key,value);
//		});
//		Ext.iterate(listeners,function(key,value){
//			Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows.window.addListener(key,value);
//		});
		Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows['ecpOwner'].considerHide=true;
		Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows.window.addListener('hide',listeners['hide']);
	}
	return Ecp.InternalCodeSearchWindow.singleSearchWithSubAccountWindows;
}