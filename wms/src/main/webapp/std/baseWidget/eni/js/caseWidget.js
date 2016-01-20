/*Ecp.CaseGrid.prototype.listAllCaseAsssignment=function()
{
	this.dataStore.store.baseParams={
			cmd:'case',
			action:'listAllCaseAsssignment'};
	this.show();
}*/

Ecp.CaseSelectTriggerForm=function(){
	this.form=new Ecp.FormPanel();
	this.config={
		labelAlign :'left',
		region :'north',
		labelWidth :100,
		height :38,
		margins :'0 0 2 0',
		cmargins :'0 0 2 0',
		frame :true
	};
	this.items=[{
			xtype :'trigger',
			fieldLabel :TXT.case_internal_codeEnter,
			id :'receiveInternalCode',
			name :'receiveInternalCode',
			triggerClass :'x-form-search-trigger',
			width :200,
			allowBlank :true
		}
	];
	
	this.buttons=null;
	
	this.cusObj=null;
	
	this.eventConfig=null;
}

Ecp.CaseSelectTriggerForm.prototype.setTriggerAction=function(triggerHandler){
	this.items[0].onTriggerClick=triggerHandler;
}

Ecp.CaseSelectTriggerForm.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.CaseSelectTriggerForm.prototype.customization=function(obj){
	this.cusObj=obj;
}

Ecp.CaseSelectTriggerForm.prototype.render=function(){
	//alert(this.cusObj);
	this.form.init();
	this.form['config']=this.config;
	this.form['items']=this.items;
	this.form['buttons']=this.buttons;
	this.form.customization(this.cusObj);
	return this.form.render();
}


Ecp.CaseSelectWindow=function(){
	this.window=new Ecp.Window();
	this.config={
		id:'caseSelectInReceiveProcWin',
		width :800,
		height :455,
		autoScroll :true,
		layout :'border',
		border :false,
		minimizable :false,
		maximizable :true,
		resizable :false,
		modal :true,
		shadow:false,
		closeAction:'hide',
		layoutConfig : {
			animate :false
		},
		buttonAlign :'center'
	};
	
	this.items=null;
	
	this.buttons=[{
		text:TXT.common_btnOK,
		scope:this
	}/*,{
		text:TXT.case_search_case,
		scope:this
	}*/];
	
	this.baseParamsInternalCode={
		cmd :'case',
		action :'findByInternalCodeBlur',
		internalCode:''
	}
	
	this.baseParamsMt={
		cmd :'case',
		action :'getSelectedCaseByMT',
		mid:''
	}
	
	this.baseParamsMx={
		cmd :'case',
		action :'getSelectedCaseByMX',
		mid:''
	}
	
	this.cusObj=null;
	
	this.eventConfig=null;
	
	this.observers=[];
	
	this.dataBus=null;
}


Ecp.CaseSelectWindow.prototype.setItem=function(objArray){
	this.items=objArray;
}


Ecp.CaseSelectWindow.prototype.show=function(){
	this.window.window.show();	
}

Ecp.CaseSelectWindow.prototype.setButtonHandler=function(array){
	var win=this;
	this.buttons[0].handler=array[0];
	//this.buttons[1].handler=array[1];
}

Ecp.CaseSelectWindow.prototype.addObserver=function(observer){
	for(var i=0;i<observer.length;i++)
		this.observers.push(observer[i]);
}

Ecp.CaseSelectWindow.prototype.notifyAll=function(eventName){
	for(var i=0;i<this.observers.length;i++){
		this.observers[i].update(this.window.window,eventName);
	}
}

Ecp.CaseSelectWindow.prototype.update=function(src,eventName){
	switch(eventName)
	{
		case 'receiveMsgProEnd':
			this.window.window.hide();
			break;
	}
}

Ecp.CaseSelectWindow.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.CaseSelectWindow.prototype.customization=function(obj){
	this.cusObj=obj;
}

Ecp.CaseSelectWindow.prototype.render=function(){
	this.window.init();
	this.window['config']=this.config;
	this.window['items']=this.items;
	this.window['buttons']=this.buttons;
	this.window['title']=this.title;
	this.window['listeners']=this.eventConfig;
	this.window.customization(this.cusObj);
	return this.window.render();
}

Ecp.CaseSelectWindow.createSelectWin=function(obj,observer){
	if(!Ecp.components['caseSelectWin']){
		var caseGrid=new Ecp.CaseGrid();
		caseGrid.handleWidgetConfig(function(grid){
			delete grid.defaultGridConfig['pagination'];
			grid.defaultGridConfig['region']='center';
			grid.defaultGridConfig['id']='caseInReceiveProcGrid';
		});
		caseGrid.setWidgetEvent({
			grid:{
				dblclick:showCaseDetailIncomingMsgWin
			}
		});
		caseGrid.customization(obj['cusCaseGrid']);
		var caseSelectTriggerForm=new Ecp.CaseSelectTriggerForm();
		caseSelectTriggerForm.setTriggerAction(function(){
			win['baseParamsInternalCode']['internalCode']=this.getValue();
			caseGrid.search(win['baseParamsInternalCode']);
		});
		caseSelectTriggerForm.customization(obj['cusCaseTrigger']);
		var win=new Ecp.CaseSelectWindow();
		
		win.setItem([caseSelectTriggerForm.render(),caseGrid.render()]);
		win.eventConfig={
			hide:function(){
				//win.notifyAll('integrateMsgIntoCase');
				caseGrid.removeAllRecord();
				caseSelectTriggerForm.form.reset();
				win.dataBus=null;
			},
			show:function(){
				var dataBus=win['dataBus'];
				win[dataBus['selectCaseType']]['mid']=dataBus['mid'];
				caseGrid.search(win[dataBus['selectCaseType']]);
			}
		};
		win.setButtonHandler([integrateMsgIntoCase]);//,showPaymentSearchWin])
		win.customization(obj['cusCaseSelectWin']);
		win.addObserver(observer);
		win.render();
		Ecp.components['caseSelectWin']=win;
		Ecp.components['caseIncomingMsgGrid']=caseGrid;
	}
	return Ecp.components['caseSelectWin'];
}

//case toolbar
Ecp.CaseToolBarWidget=function(){
	this.toolBar=new Ecp.ToolBar();
	totalToolBarItem['createCaseWithFin']['childrens']=[totalToolBarItem['createCaseWith192'],
	                                                    totalToolBarItem['createCaseWith195'],
	                                                    totalToolBarItem['createCaseWith196'],
	                                                    totalToolBarItem['createCaseWith292'],
	                                                    totalToolBarItem['createCaseWith295'],
	                                                    totalToolBarItem['createCaseWith296']];
	totalToolBarItem['createCaseWithXml']['childrens']=[totalToolBarItem.createCaseWith007,
	                                                    totalToolBarItem.createCaseWith008,
	                                                    totalToolBarItem.createCaseWith026,
	                                                    totalToolBarItem.createCaseWith027,
	                                                    totalToolBarItem.createCaseWith028,
	                                                    totalToolBarItem.createCaseWith037];
	totalToolBarItem.createCase['childrens'] = [totalToolBarItem.createCaseWithFinTemplate,
	                                            totalToolBarItem.createCaseWithDraft,
	                                            totalToolBarItem.createCaseWithFin];
	if( Ecp.userInfomation.sendToSwift ) {
		totalToolBarItem.createCase['childrens'].push( totalToolBarItem.createCaseWithXml );
	}
	this.defaultToolBarItemConfig = [totalToolBarItem.queryCase,
	                                 totalToolBarItem.assignCase,
	                                 totalToolBarItem.createCase//,totalToolBarItem.listGeCloseTimeCases
	                               ];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'caseToolBar'
	};
}

//1
Ecp.CaseToolBarWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

//2
Ecp.CaseToolBarWidget.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.CaseToolBarWidget.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

//3
Ecp.CaseToolBarWidget.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.CaseToolBarWidget.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	//toolBar.setOwner(this);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}

Ecp.CaseHistoryGrid=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		root :'histories',
		id :'id',
		fields : [ {
			name :'id'
		}, {
			name :'content'
		}, {
			name :'createTime'
		}, {
			name :'assigner'
		}, {
			name :'assignee'
		}]
	};
	
	this.defaultCmConfig=[{
				header :TXT.case_history_assigner,
				dataIndex :'assigner',
				menuDisabled :true,
				width :200
			}, {
				header :TXT.case_history_assignee,
				dataIndex :'assignee',
				menuDisabled :true,
				width :200
			}, {
				header :TXT.case_history_create_time,
				dataIndex :'createTime',
				menuDisabled :true,
				width :200
			} ];
			
	this.defaultGridConfig={
		title :TXT.case_history,
		id:'eiCaseHistoryGrid',
		border:false
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;	
}

Ecp.CaseHistoryGrid.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.CaseHistoryGrid.prototype.customization=function(obj){
	this.customizationConfig=obj;
}

Ecp.CaseHistoryGrid.prototype.render=function(){
	this.dataStore.setConfig(this.defaultStoreConfig);
	this.dataStore.init();
	this.customizationConfig['store']==undefined?1:this.dataStore.customization(this.customizationConfig['store']);
	this.grid.setStore(this.dataStore.render());
	this.grid.setColumnMode(this.defaultCmConfig);
	this.grid.setSelectMode(this.defaultSelModel);
	this.grid.setConfig(this.defaultGridConfig);
	this.grid.init();
	this.customizationConfig['grid']==undefined?1:this.grid.customization(this.customizationConfig['grid']);
	this.grid['ecpOwner']=this;
	return this.grid.render();
}

Ecp.CaseHistoryGrid.prototype.loadLocalData=function(data){
	this.dataStore.store.loadData(data);
}

Ecp.CaseHistoryGrid.prototype.clearSelections=function(){
	this.grid.grid.selModel.clearSelections(true); 
}

Ecp.CaseCommentsGrid=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		root :'comments',
		id :'id',
		fields : [ {
			name :'id'
		}, {
			name :'content'
		}, {
			name :'createTime'
		}, {
			name :'createBy'
		}, {
			name :'creatorInst'
		} ]
	};
	
	this.defaultCmConfig=[{
				header :TXT.case_comment_content,
				dataIndex :'content',
				menuDisabled :true,
				width :500
			}, {
				header :TXT.case_comment_create_time,
				dataIndex :'createTime',
				menuDisabled :true,
				width :200
			}, {
				header :TXT.case_comment_create_inst,
				dataIndex :'creatorInst',
				menuDisabled :true,
				width :200
			}, {
				header :TXT.case_comment_create_by,
				dataIndex :'createBy',
				menuDisabled :true,
				width :200
			}];
			
	this.defaultGridConfig={
		//title :TXT.case_comment,
		id:'eiCaseCommentsGrid',
		border:false
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;	
}

Ecp.CaseCommentsGrid.prototype.setToolBar=function(toolBar){
	this.grid.setTopToolBar(toolBar);
}

Ecp.CaseCommentsGrid.prototype.setWidgetEvent=function(obj){
	obj['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(obj['grid']);
	obj['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(obj['cm']);
	obj['sm']==null?this.grid.setSmGridEvent({}):this.grid.setSmGridEvent(obj['sm']);
	obj['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(obj['store']);
}

Ecp.CaseCommentsGrid.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.CaseCommentsGrid.prototype.customization=function(obj){
	this.customizationConfig=obj;
}

Ecp.CaseCommentsGrid.prototype.render=function(){
	this.dataStore.setConfig(this.defaultStoreConfig);
	this.dataStore.init();
	this.customizationConfig['store']==undefined?1:this.dataStore.customization(this.customizationConfig['store']);
	this.grid.setStore(this.dataStore.render());
	this.grid.setColumnMode(this.defaultCmConfig);
	this.grid.setSelectMode(this.defaultSelModel);
	this.grid.setConfig(this.defaultGridConfig);
	this.grid.init();
	this.customizationConfig['grid']==undefined?1:this.grid.customization(this.customizationConfig['grid']);
	this.grid['ecpOwner']=this;
	return this.grid.render();
}

Ecp.CaseCommentsGrid.prototype.update=function(src,eventName){
	if(eventName=='commentsAdded')
		reloadPartCaseDetail('comments',this);
}

Ecp.CaseCommentsGrid.prototype.setCommentsCount=function(){
	this.grid.grid.setTitle(TXT.case_comment+'('+this.grid.getDataCount()+')');
}

Ecp.CaseCommentsGrid.prototype.loadLocalData=function(data){
	this.dataStore.store.loadData(data);
}

Ecp.CaseCommentsGrid.prototype.clearSelections=function(){
	this.grid.grid.selModel.clearSelections(true); 
}


Ecp.CaseCommentsForm=function(){
	this.form=new Ecp.FormPanel();
	this.config={
		labelAlign :'left',
		labelWidth :100,
		height :40,
		margins :'0 0 2 0',
		cmargins :'0 0 2 0',
		frame :true
	};
	
	this.items=[{
			x :0,
			y :60,
			xtype :'textarea',
			name :'content',
			id :'content',
			anchor :'95% 99%',
			allowBlank :false,
			maxLength :200
		}];
	
	this.buttons=[];
	
	this.cusObj=null;
	
	this.eventConfig=null;
}

Ecp.CaseCommentsForm.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.CaseCommentsForm.prototype.customization=function(obj){
	this.cusObj=obj;
}

Ecp.CaseCommentsForm.prototype.render=function(){
	this.form.init();
	this.form['config']=this.config;
	this.form['items']=this.items;
	this.form['buttons']=this.buttons;
	this.form.customization(this.cusObj);
	return this.form.render();
}

Ecp.CaseCommentsWindow=function(){
	this.window=new Ecp.Window();
	
	this.defaultConfig={
		title :TXT.case_comments,
		width :600,
		height :400,
		autoScroll :false,
		layout :'fit',
		border :false,
		closeAction:'hide',
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
	
	this.defaultItemsConfig=[];
	
	this.defaultButtonsConfig=[{
		text:TXT.common_save,
		handler:saveComment,
		scope:this
	},{
		text:TXT.common_btnClose,
		handler:function(){
			this.window.window.hide();
		},
		scope:this
	}];
	
	this.eventConfig=null;
	
	this.customConfig=null;
	
	this.observers=[];
	
	this.dataBus=null;
}

Ecp.CaseCommentsWindow.prototype.setItems=function(items){
	for(var i=0;i<items.length;i++)
		this.defaultItemsConfig.push(items[i]);
}	

Ecp.CaseCommentsWindow.prototype.transeferStatus=function(usage){
	var win=this.window.window;
	if(usage=='add'){
		this.defaultItemsConfig[0].items.itemAt(0).setReadOnly(false);
		win.buttons[0].setVisible(true);
	}
	else{
		this.defaultItemsConfig[0].items.itemAt(0).setReadOnly(true);
		win.buttons[0].setVisible(false);
	}
	win.doLayout();
}

Ecp.CaseCommentsWindow.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.CaseCommentsWindow.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.CaseCommentsWindow.prototype.addObservers=function(observers){
	for(var i=0;i<observers.length;i++)
		this.observers.push(observers[i]);
}

Ecp.CaseCommentsWindow.prototype.notifyAll=function(src,eventName){
	for(var i=0;i<this.observers.length;i++)
		this.observers[i].update(src,eventName);
}

Ecp.CaseCommentsWindow.prototype.render=function(){
	this.window.init();
	this.window['config']=this.defaultConfig;
	this.window['items']=this.defaultItemsConfig;
	this.window['buttons']=this.defaultButtonsConfig;
	this.window['listeners']=this.eventConfig;
	this.window.customization(this.customConfig);
	return this.window.render();
}

Ecp.CaseCommentsWindow.createWindow=function(obj,observers){
	if(!Ecp.components['commentsWin']){
		var commentsForm=new Ecp.CaseCommentsForm();
		commentsForm.handleWidgetConfig(function(form){
			form['items'][0]['fieldLabel']=TXT.case_comments;
		});
		commentsForm.customization(obj['cusCommentForm']);
		var win=new Ecp.CaseCommentsWindow();
		win.setItems([commentsForm.render()]);
		win.eventConfig={
			beforeShow:function(){
				commentsForm.form.reset();
			}
		};
		win.addObservers(observers);
		win.customization(obj['cusCommentWin']);
		win.render();
		Ecp.components['commentsWin']=win;
	}
	Ecp.components['commentsWin'].transeferStatus(obj['usage']);
	return Ecp.components['commentsWin'];
}

Ecp.CaseDetailForm=function(){
	//add
	this.window = null;
	this.form=new Ecp.FormPanel();
	this.config={
		frame :true,
		region :'north',
		height :245,
		margins :'0 0 2 0',
		cmargins :'0 0 2 0',
		buttonAlign:'right'
	};
	var caseTypeStore = new Ext.data.Store({
						  proxy:new Ext.data.HttpProxy({
						  url:DISPATCH_SERVLET_URL+"?cmd=case&action=getCaseTypeList",
						   method:'post'
						  }),
						  reader:new Ext.data.JsonReader({
						   root:'caseType'
						  },['name','code'])
	});
	caseTypeStore.load();
	this.items=[ {
					layout :'column',
					items : [
							{
								columnWidth :.33,
								layout :'form',
								defaultType :'textfield',
								defaults : {
									anchor :'95%'
								},
								items : [
										{
											fieldLabel :TXT.case_internal_code,
											readOnly :true,
											id :'internalCode',
											name :'internalCode'
										},
										{
											fieldLabel :TXT.case_num,
											id :'caseId',
											name :'caseId'
										},
										{
											fieldLabel :TXT.case_creator,
											readOnly :true,
											id :'creatorBic',
											name :'creatorBic'
										},
										{
											fieldLabel :TXT.case_receive,
											readOnly :true,
											id :'receiveBic',
											name :'receiveBic'
										},
										{
											fieldLabel :TXT.case_create_by,
											readOnly :true,
											id :'createBy',
											name :'createBy'
										},
										/*{
											xtype :'combo',
											fieldLabel :TXT.case_inwardOutward,
											id :'remittance',
											name :'remittance',
											value :'',
											store :new Ext.data.SimpleStore(
													{
														fields : [ 'label',
																'value' ],
														data : [
																[
																		TXT.case_inward,
																		'I' ],
																[
																		TXT.case_outward,
																		'O' ],
																[
																		TXT.case_unDefine,
																		'N' ] ]
													}),
											forceSelection :true,
											displayField :'label',
											valueField :'value',
											typeAhead :true,
											mode :'local',
											triggerAction :'all',
											editable :false,
											selectOnFocus :true,
											style:'background:#b3d7f5;'
										},
										{
											xtype :'combo',
											fieldLabel :TXT.case_subAccount,
											id :'isSubAccount',
											name :'isSubAccount',
											value :'N',
											store :new Ext.data.SimpleStore(
													{
														fields : [ 'label',
																'value' ],
														data : [
																[
																		TXT.case_isSubAccount,
																		'Y' ],
																[
																		TXT.case_isNotSubAccount,
																		'N' ] ]
													}),
											forceSelection :true,
											displayField :'label',
											valueField :'value',
											typeAhead :true,
											mode :'local',
											triggerAction :'all',
											selectOnFocus :true,
											editable :false,
											style:'background:#b3d7f5;'
										},
										{
											xtype:'compositefield',
											fieldLabel :TXT.case_enter_ibp_seq_num,
											id :'IBPSeqNumComposite',
											name :'IBPSeqNumComposite',
											//msgTarget:'under',
											items:[
												{
													xtype:'textfield',
													id :'IBPSeqNum',
													name:'IBPSeqNum',
													regex:/^[0-9a-zA-Z/\-\?:\(\)\.,'\+ ]{0,16}$/,
													regexText:TXT.case_IBP_format_error,
													width:215
												},this.ibpButton
											]
											
										}, */{
											fieldLabel :TXT.case_mergeNumber,
											id :'mergeNumber',
											name :'mergeNumber',
											style:'background:#b3d7f5;'
										} ,{xtype :'combo',
											fieldLabel :TXT.common_careFlag,
											id :'careFlag',
											name :'careFlag',
											value :'',
											store :new Ext.data.SimpleStore( {
												fields : [ 'label', 'value' ],
												data : [
														[ TXT.common_yes_desc,
																true ],
														[ TXT.common_no_desc,
																false]]
											}),
											forceSelection :true,
											displayField :'label',
											valueField :'value',
											typeAhead :true,
											mode :'local',
											triggerAction :'all',
											editable :false,
											selectOnFocus :true,
											style:'background:#b3d7f5;'
										} ]
							},
							{
								columnWidth :.33,
								layout :'form',
								defaultType :'textfield',
								defaults : {
									anchor :'95%'
								},
								items : [
										{
											fieldLabel :TXT.case_reference_num20,
											readOnly :true,
											id :'referenceNum',
											name :'referenceNum'
										},
										{
											fieldLabel :TXT.case_related_reference_num21,
											readOnly :true,
											id :'relatedReferenceNum',
											name :'relatedReferenceNum'
										},
										{
											xtype :'combo',
											fieldLabel :TXT.case_type,
											id :'caseType',
											name :'caseType',
											value :'',
											style:'background:#b3d7f5;',
											triggerAction: 'all',
											store: caseTypeStore,
											forceSelection :true,
											displayField :'name',
											valueField :'code',
											typeAhead :true,
											mode :'remote',
											triggerAction :'all',
											editable :false,
											selectOnFocus :true
										},{
											fieldLabel :TXT.case_currency,
											readOnly :true,
											id :'currency',
											name :'currency'
										}, {
											fieldLabel :TXT.case_amount,
											readOnly :true,
											id :'amount',
											name :'amount'
										}, {
											fieldLabel :TXT.case_value_date,
											readOnly :true,
											id :'valueDate',
											name :'valueDate'
										}, {
											fieldLabel :TXT.common_last_careBy,
											readOnly :true,
											id :'careBy',
											name :'careBy'
										}

								]
							}, {
								columnWidth :.33,
								layout :'form',
								defaultType :'textfield',
								defaults : {
									anchor :'95%'
								},
								items : [ {
									fieldLabel :TXT.case_owner,
									readOnly :true,
									id :'owner',
									name :'owner'
								}, {
									fieldLabel :TXT.case_assignee,
									readOnly :true,
									id :'assignee',
									name :'assignee'
								}, {
									fieldLabel :TXT.case_status,
									readOnly :true,
									id :'status',
									name :'status'
								},

								{
									fieldLabel :TXT.case_create_time,
									readOnly :true,
									id :'createTime',
									name :'createTime'
								}, {
									fieldLabel :TXT.case_close_time,
									readOnly :true,
									id :'closeTime',
									name :'closeTime'
								}, {
									fieldLabel :TXT.case_spend_time,
									readOnly :true,
									id :'spendTime',
									name :'spendTime'
								},  {
									fieldLabel :TXT.common_last_careTime,
									readOnly :true,
									id :'careTime',
									name :'careTime'
								},/*{
									fieldLabel :TXT.case_account,
									id :'account',
									name :'account',
									style:'background:#b3d7f5;'
								},*/{
									xtype:'hidden',
									id:'sendBic',
									name:'sendBic',
									labelSeparator :''
								} ,{
									xtype:'hidden',
									id:'paymentId',
									name:'paymentId'
								} ,{
									xtype:'hidden',
									id:'paymentNum',
									name:'paymentNum'
								} ]
							} ]
				} ];
	
	this.buttons=[/*{
		text:TXT.case_enter_ibp_seq_num_btn,
		handler:editIBPSeqNum,
		scope:this
	},
		'->',*/
	{
		text:TXT.common_save,
		handler:saveExtrenalCaseInfo,
		scope:this
	},{
		text:TXT.common_btnClose,
		handler:function(){
			this.window.window.hide();
		},
		scope:this
	}];
	
	this.cusObj=null;
	
	this.eventConfig=null;
}

Ecp.CaseDetailForm.prototype.setButtonHandler=function(handler){
	this.buttons[0].handler=handler;
}

Ecp.CaseDetailForm.prototype.setCaseFieldStatus=function(caseStatus){
	var form=this.form.formPanel;
	var columnForm=form.items.itemAt(0);
	var firstColumn=columnForm.items.itemAt(0);
	var secondColumn=columnForm.items.itemAt(1);
	//var thirdColumn=columnForm.items.itemAt(2);
	if(caseStatus=='C'){
		firstColumn.items.itemAt(1).setReadOnly(true);
		firstColumn.items.itemAt(5).setReadOnly(true);
		//firstColumn.items.itemAt(6).setReadOnly(true);
		//firstColumn.items.itemAt(7).items.itemAt(0).setReadOnly(true);
		//this.ibpButton.setDisabled(true);
		secondColumn.items.itemAt(2).setReadOnly(true);
		firstColumn.items.itemAt(5).setReadOnly(true);
		//thirdColumn.items.itemAt(6).setReadOnly(true);
		form.buttons[0].setVisible(false);
		//form.buttons[2].setVisible(false);
	}else{
		firstColumn.items.itemAt(1).setReadOnly(true);
		//firstColumn.items.itemAt(5).setReadOnly(false);
		//firstColumn.items.itemAt(6).setReadOnly(false);
		//firstColumn.items.itemAt(7).items.itemAt(0).setReadOnly(true);
		//firstColumn.items.itemAt(5).items.itemAt(0).setReadOnly(true);
		//this.ibpButton.setDisabled(true);
		secondColumn.items.itemAt(2).setReadOnly(false);
		firstColumn.items.itemAt(5).setReadOnly(false);
		//thirdColumn.items.itemAt(6).setReadOnly(false);
		form.buttons[0].setVisible(true);
		//form.buttons[2].setVisible(true);
	}
	form.doLayout();
	//get formField form various form and set status based caseStatus
}

Ecp.CaseDetailForm.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.CaseDetailForm.prototype.customization=function(obj){
	this.cusObj=obj;
}

Ecp.CaseDetailForm.prototype.render=function(){
	//alert(this.cusObj);
	this.form.init();
	this.form['config']=this.config;
	this.form['items']=this.items;
	this.form['buttons']=this.buttons;
	this.form.customization(this.cusObj);
	return this.form.render();
}

Ecp.CaseAccompanyInfoTab=function(){
	this.tabPanel=new Ecp.TabPanel();
	
	this.defaultConfig={
		activeTab :0,
		border :false,
		frame :false,
		region :'center',
		defaults : {
			autoScroll :false
		}
	};
	
	this.defaultItemsConfig=[];
	
	this.reaItems=[];
	
	this.serviceComponents=null;
	
	this.customConfig=null;
}

Ecp.CaseAccompanyInfoTab.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.CaseAccompanyInfoTab.prototype.setItems=function(items){
	for(var i=0;i<items.length;i++)
		this.defaultItemsConfig.push(items[i]);
}

//just for testing start
Ecp.CaseAccompanyInfoTab.prototype.setFirstItem=function(items){
	this.defaultItemsConfig.push(items[0]);
	this.reaItems.push(items[0]);
	for(var i=1;i<items.length;i++){
		this.defaultItemsConfig.push({
			title:items[i]['title']
		});
		this.reaItems.push(items[i]);
	}
	
}

//just for testing end

Ecp.CaseAccompanyInfoTab.prototype.activeTab=function(index){
	this.tabPanel.tabPanel.setActiveTab(index);
}

Ecp.CaseAccompanyInfoTab.prototype.getTab=function(index){
	return this.tabPanel.tabPanel.get(index);
}

Ecp.CaseAccompanyInfoTab.prototype.setServiceComponentTbarStatus=function(status){
	var msgToolBar=this.getTab(0).getTopToolbar();
	var paymentToolBar=this.getTab(1).getTopToolbar();
	var commentToolBar=this.getTab(3).getTopToolbar();
	var messageToolBarItems=msgToolBar.items;
	var length=messageToolBarItems.length
	if(status=='C'){
		commentToolBar.items.itemAt(0).setDisabled(true);
		paymentToolBar.items.itemAt(0).setDisabled(true);
		if(Ecp.userInfomation.menu.C200002){
			length=messageToolBarItems.length-1;
			messageToolBarItems.itemAt(length).setText(TXT.case_reopen_case);
		}
		for(var i=0;i<length;i++){
			messageToolBarItems.itemAt(i).setDisabled(true)
		}
	}else{
		commentToolBar.items.itemAt(0).setDisabled(false);
		paymentToolBar.items.itemAt(0).setDisabled(false);
		if(Ecp.userInfomation.menu.C200002){
			length=messageToolBarItems.length-1;
			messageToolBarItems.itemAt(length).setText(TXT.case_close_case);
		}
		for(var i=0;i<length;i++){
			messageToolBarItems.itemAt(i).setDisabled(false)
		}
	}
}

Ecp.CaseAccompanyInfoTab.prototype.setServiceComponent=function(serviceComponents){
	this.serviceComponents=serviceComponents;
}

Ecp.CaseAccompanyInfoTab.prototype.getServiceComponent=function(nameKey){
	return this.serviceComponents[nameKey];
}

Ecp.CaseAccompanyInfoTab.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.CaseAccompanyInfoTab.prototype.render=function()
{
	this.tabPanel.init();
	this.tabPanel.setConfig(this.defaultConfig);
	this.tabPanel.setItems(this.defaultItemsConfig);
	//alert(tabPanelObj['items']);
	this.tabPanel.customization(this.customConfig);
	this.tabPanel['ecpOwner']=this;
	return this.tabPanel.render();
}

Ecp.MessageGridTopToolBar=function(){
	this.toolBar=new Ecp.ToolBar();
	totalToolBarItem.replyMsg['childrens']=[totalToolBarItem.replyByTemplate,totalToolBarItem.replyByDraft,totalToolBarItem.replyMtInCaseDetail];
	totalToolBarItem.createCaseInDetail['childrens']=[totalToolBarItem.createCaseWithFinTemplateInDetail,totalToolBarItem.createCaseWithDraftInDetail];
	
	totalToolBarItem.createCaseWithFinInDetail['childrens']=[totalToolBarItem.createCaseWith192InDetail,totalToolBarItem.createCaseWith195InDetail,totalToolBarItem.createCaseWith196InDetail,totalToolBarItem.createCaseWith292InDetail,totalToolBarItem.createCaseWith295InDetail,totalToolBarItem.createCaseWith296InDetail];
	totalToolBarItem.createCaseInDetail['childrens'].push(totalToolBarItem.createCaseWithFinInDetail);
	totalToolBarItem.replyMtInCaseDetail['childrens']=[totalToolBarItem.replyMT192InCaseDetail,totalToolBarItem.replyMT195InCaseDetail,totalToolBarItem.replyMT196InCaseDetail,totalToolBarItem.replyMT292InCaseDetail,totalToolBarItem.replyMT295InCaseDetail,totalToolBarItem.replyMT296InCaseDetail];
	if(Ecp.userInfomation.sendToSwift){
		totalToolBarItem.replyMsg['childrens'].push(totalToolBarItem.replyByXml);
		totalToolBarItem.createCaseWithXmlInDetail['childrens']=[totalToolBarItem.createCaseWith007InDetail,totalToolBarItem.createCaseWith008InDetail,totalToolBarItem.createCaseWith026InDetail,totalToolBarItem.createCaseWith027InDetail,totalToolBarItem.createCaseWith028InDetail,totalToolBarItem.createCaseWith032InDetail,totalToolBarItem.createCaseWith033InDetail,totalToolBarItem.createCaseWith035InDetail,totalToolBarItem.createCaseWith037InDetail,totalToolBarItem.createCaseWith038InDetail];
		totalToolBarItem.createCaseInDetail['childrens'].push(totalToolBarItem.createCaseWithXmlInDetail);
	}
	this.defaultToolBarItemConfig=[totalToolBarItem.replyMsg,totalToolBarItem.createCaseInDetail,
	                               totalToolBarItem.setReaded,totalToolBarItem.delMsgFromCase,totalToolBarItem.sendMail,totalToolBarItem.assignInDetail,totalToolBarItem.assignXml,totalToolBarItem.copyMessageInCaseDetail,totalToolBarItem.auditMessageRecordInCaseDetail,totalToolBarItem.nakErrorCodeInCaseDetail,totalToolBarItem.refereshMessagesInCase,'->',totalToolBarItem.urgeDealInland,totalToolBarItem.urgeDealOutland,totalToolBarItem.closeOrReopenCase];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'caseAccompanyMsgToolBar'
	};
}

Ecp.MessageGridTopToolBar.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

Ecp.MessageGridTopToolBar.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.MessageGridTopToolBar.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

Ecp.MessageGridTopToolBar.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.MessageGridTopToolBar.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}

Ecp.PaymentGridToolBar=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.cancelRelationship];//,totalToolBarItem.applyRefundIncome];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'caseAccompanyPaymentToolBar'
	};
}

Ecp.PaymentGridToolBar.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

Ecp.PaymentGridToolBar.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.PaymentGridToolBar.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

Ecp.PaymentGridToolBar.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.PaymentGridToolBar.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}

Ecp.CommentsGridToolBar=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.createCaseComment];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'caseAccompanyCommentToolBar'
	};
}

Ecp.CommentsGridToolBar.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

Ecp.CommentsGridToolBar.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.CommentsGridToolBar.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

Ecp.CommentsGridToolBar.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.CommentsGridToolBar.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}

Ecp.CaseDetailWindow=function(){
	this.window=new Ecp.Window();
	
	this.caseDetailForm=null;
	
	this.caseAccompanyInfoTab=null;
	
	this.defaultConfig={
		id:'caseDetailWin',
		width :1000,
		height :550,
		layout :'border',
		border :false,
		minimizable :false,
		maximizable :true,
		resizable :false,
		modal :true,
		title:TXT.case_detail,
		closeAction:'hide',
		shadow: false, 
		layoutConfig : {
			animate :false
		}
	};
		
	this.defaultItemsConfig=[];
	
	this.defaultButtonsConfig=[];
	
	this.eventConfig=null;
	
	this.customConfig=null;
	
	this.observers=[];
	
	this.dataBus=null;
}

Ecp.CaseDetailWindow.prototype.setValues=function(result){
	if (result.status == 'O')
		result.status=TXT.case_open;
	else if (result.status == 'C')
		result.status=TXT.case_close;
	else if (result.status == 'R')
		result.status=TXT.case_reopen;
	
	// spend time
	if (result.spendTime < 60 && result.spendTime > 0) 
		result.spendTime = result.spendTime + TXT.case_spend_time_min;
	else if (result.spendTime >= 60 && result.spendTime < 24 * 60) 
		result.spendTime = (result.spendTime / 60).toFixed(0)+ TXT.case_spend_time_hou;
	else if (result.spendTime >= 24 * 60) 
		result.spendTime = (result.spendTime / (60 * 24)).toFixed(0)+ TXT.case_spend_time_day;
	var payments={payments:result.paymentMessages};
	var messages={messages:result.messages};
	var histories={histories:result.histories};
	var comments={comments:result.comments};
	var hisIBPsns={hisIBPsns:result.hisIBPsns};
	this.caseAccompanyInfoTab.getServiceComponent('paymentMessages').loadLocalData(payments);
	this.caseAccompanyInfoTab.getServiceComponent('messages').loadLocalData(messages);
	this.caseAccompanyInfoTab.getServiceComponent('histories').loadLocalData(histories);
	this.caseAccompanyInfoTab.getServiceComponent('comments').loadLocalData(comments);
	//this.caseAccompanyInfoTab.getServiceComponent('hisIBPsns').loadLocalData(hisIBPsns);
	//this.caseAccompanyInfoTab.getServiceComponent('comments').setCommentsCount();
	//this.caseAccompanyInfoTab.getServiceComponent('messages').grid.selectFirstRow();
	delete result.paymentMessages;
	delete result.messages;
	delete result.histories;
	delete result.comments;
	this.caseAccompanyInfoTab.activeTab(0);
	this.caseDetailForm.form.setValues(result);
}

Ecp.CaseDetailWindow.prototype.changeLayoutByCaseStatus=function(status){
	this.caseDetailForm.setCaseFieldStatus(status);
	this.caseAccompanyInfoTab.setServiceComponentTbarStatus(status);
}

Ecp.CaseDetailWindow.prototype.setItems=function(items){
	for(var i=0;i<items.length;i++)
		this.defaultItemsConfig.push(items[i]);
}

Ecp.CaseDetailWindow.prototype.setCaseDetailFormServiceComp=function(caseDetailForm){
	this.caseDetailForm=caseDetailForm;
	//add
	caseDetailForm.window = this.window;
}

Ecp.CaseDetailWindow.prototype.setCaseAccompanyInfoTabServiceComp=function(caseAccompanyInfoTab){
	this.caseAccompanyInfoTab=caseAccompanyInfoTab;
}

Ecp.CaseDetailWindow.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.CaseDetailWindow.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.CaseDetailWindow.prototype.addObservers=function(observers){
	for(var i=0;i<observers.length;i++)
		this.observers.push(observers[i]);
}

Ecp.CaseDetailWindow.prototype.resetObservers=function(newObservers){
	this.observers=newObservers;
}

Ecp.CaseDetailWindow.prototype.notifyAll=function(eventName){
	for(var i=0;i<this.observers.length;i++)
		this.observers[i].update(this,eventName);
}

Ecp.CaseDetailWindow.prototype.render=function(){
	this.window.init();
	this.window['config']=this.defaultConfig;
	this.window['items']=this.defaultItemsConfig;
	this.window['buttons']=this.defaultButtonsConfig;
	this.window['listeners']=this.eventConfig;
	this.window.customization(this.customConfig);
	return this.window.render();
}

Ecp.CaseDetailWindow.createWindow=function(obj){
		var win=new Ecp.CaseDetailWindow();
		var caseDetailForm=new Ecp.CaseDetailForm();
		caseDetailForm.customization(obj['cusCaseDetailForm']);
		var messageGridToolBar=new Ecp.MessageGridTopToolBar();
		messageGridToolBar.setWidgetEvent({
			urgeDealInland:urgeDealInland,
			urgeDealOutland:urgeDealOutland,
			closeOrReopenCase:closeOrReopenCase,
			setReaded:setMessageReadedFlag,
			delMsgFromCase:cancelMsgRelationShipFromCase,
			sendMail:sendNotificationMail,
			assignInDetail:assignCaseToInstInDetail,
			auditMessageRecordInCaseDetail:showAuditMessageRecordWinInCaseDetail,
			assignXml:showAssignXmlMsgSelector,
			createCaseWith008InDetail:createCaseWith008InDetail,
			createCaseWith007InDetail:createCaseWith007InDetail,
			createCaseWith026InDetail:createCaseWith026InDetail,
			createCaseWith027InDetail:createCaseWith027InDetail,
			createCaseWith028InDetail:createCaseWith028InDetail,
			createCaseWith037InDetail:createCaseWith037InDetail,
			createCaseWith032InDetail:createCaseWith032InDetail,
			createCaseWith038InDetail:createCaseWith038InDetail,
			createCaseWith033InDetail:createCaseWith033InDetail,
			createCaseWith035InDetail:createCaseWith035InDetail,
			replyByXml:showReplyMsgListWin,
			nakErrorCodeInCaseDetail:getErrorCode,
			replyByTemplate:showReplyMsgTmpWinWithCaseDetail,
			replyByDraft:showReplyPrivateMsgTmpWinWithCaseDetail,
			createCaseWithFinTemplateInDetail:showMsgTmpWinWithCaseDetail,
			createCaseWithDraftInDetail:showPrivateMsgTmpWinWithCaseDetail,
			createCaseWith192InDetail:createCaseWith192InDetail,
			createCaseWith195InDetail:createCaseWith195InDetail,
			createCaseWith196InDetail:createCaseWith196InDetail,
			createCaseWith292InDetail:createCaseWith292InDetail,
			createCaseWith295InDetail:createCaseWith295InDetail,
			createCaseWith296InDetail:createCaseWith296InDetail,
			replyMT192InCaseDetail:replyMT192InCaseDetail,
			replyMT195InCaseDetail:replyMT195InCaseDetail,
			replyMT196InCaseDetail:replyMT196InCaseDetail,
			replyMT292InCaseDetail:replyMT292InCaseDetail,
			replyMT295InCaseDetail:replyMT295InCaseDetail,
			replyMT296InCaseDetail:replyMT296InCaseDetail,
			copyMessageInCaseDetail:showCopyMessageWinInMessageListInCD,
			refereshMessagesInCase:refereshMessagesInCase
		});
		messageGridToolBar.customization(obj['cusMessageGridTbar']);
		messageGridToolBar.setPremission(Ecp.userInfomation.menu);
		var messageGrid=new Ecp.MessageGridWidget();
		messageGrid.handleWidgetConfig(function(obj){
			delete obj['defaultGridConfig']['pagination'];
			obj['defaultGridConfig']['id']='msgGridInCaseDetail';
			obj['defaultGridConfig']['title']=TXT.case_message;
			obj['defaultGridConfig']['border']=false;
			obj['defaultStoreConfig']['fields'].push({name:'templateName'});
			delete obj['defaultStoreConfig']['url'];
			
			
			
			var commonRender=function(value, cellmeta, record, rowIndex,
				columnIndex, store) {
				if (record.data["status"] == 'C') {
					return "<span style='color:#c0c0c0;'>" + value + "</span>";
				} else
					return value;
			};
		
			obj['defaultCmConfig']=[{
						header :'',
						dataIndex :'ioFlag',
						width :25,
						menuDisabled :true,
						renderer : function(value) {
							if (value == 'I')
								return "<img src='../images/in.png' align='absmiddle'/>";
							else
								return "<img src='../images/out.png' align='absmiddle'/>";
						}
					},
					{
						header :'',
						dataIndex :'swiftStatus',
						width :50,
						menuDisabled :true,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							if (record.data["status"] != 'C')
								if (value == 'NAK')
									return "<span style='color:red;'>" + value
											+ "</span>";
								else
									return "<span style='color:green;'>"
											+ value + "</span>";
							else
								return "<span style='color:#c0c0c0;'>" + value
										+ "</span>";
						}
					},{
						header :TXT.task_messageType,
						dataIndex :'type',
						width :100,
						menuDisabled :true,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
								
							function getValue(value, record) {
								if (value == 'TMP')
									value = TXT.message_type_tmp;
								else if (value == 'MT')
									value = TXT.message_type_mt;
								else if (value == 'MX')
									value = TXT.message_type_mx;
								else if (value = 'RPL')
									value = TXT.message_type_rpl;
								if (record.data["status"] == 'C') {
									return "<span style='color:#c0c0c0;'>" + value
											+ "</span>";
								} else
									return value;
							}


							if (record.data["status"] != 'C') {
								if (record.data["careFlag"]) {
									return "<img src='../images/wait.gif' align='absmiddle'/> <span style='font-weight:bold;color:green;'>"
											+  getValue(value, record) + "</span>";
								}else if (!record.data["isRead"]) {
									return "<img src='../images/wait.gif' align='absmiddle'/> <span style='font-weight:bold;color:red;'>"
											+ getValue(value, record)+ "</span>";
								} else
									return getValue(value, record);
							} else {
								if (record.data["careFlag"]) {
									return "<img src='../images/wait.gif' align='absmiddle'/> <span style='font-weight:bold;color:green;'>"
											+  getValue(value, record) + "</span>";
								}else if (!record.data["isRead"]) {
									return "<img src='../images/wait.gif' align='absmiddle'/> <span style='font-weight:bold;color:#c0c0c0;'>"
											+ getValue(value, record) + "</span>";
								} else
									return "<span style='color:#c0c0c0;'>"
											+ getValue(value, record)+ "</span>";
							}
						}
					},{
						header :TXT.message_name,
						dataIndex :'messageType',
						menuDisabled :true,
						width :120,
						renderer : commonRender
					},{
						header :TXT.message_assigner,
						dataIndex :'senderBic',
						menuDisabled :true,
						width :140,
						renderer : commonRender
					}, {
						header :TXT.message_assignee,
						dataIndex :'receiverBic',
						menuDisabled :true,
						width :140,
						renderer : commonRender
					},
					{
						header :TXT.task_messageId,
						dataIndex :'messageId',
						width :200,
						menuDisabled :true,
						renderer : commonRender
					}, {
						header :TXT.case_num,
						dataIndex :'caseId',
						menuDisabled :true,
						width :210,
						renderer : commonRender
					},  {
						header :TXT.case_create_by,
						dataIndex :'createBy',
						menuDisabled :true,
						width :100,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							if (record.data["status"] != 'C') {
								if (value == '')
									return TXT.case_system;
								else
									return value;
							} else {
								if (value == '')
									return "<span style='color:#c0c0c0;'>"
											+ TXT.case_system + "</span>";
								else
									return "<span style='color:#c0c0c0;'>"
											+ value + "</span>";
							}
						}		
					}, {
						header :TXT.message_createTime,
						dataIndex :'createTime',
						menuDisabled :true,
						width :150,
						renderer : commonRender
					}, {
						header :TXT.message_assign,
						dataIndex :'assignee',
						menuDisabled :true,
						width :140,
						renderer : commonRender
					}, {
						header :TXT.message_institution,
						dataIndex :'institution',
						menuDisabled :true,
						width :150,
						renderer : commonRender
					},
{
						header :TXT.case_status,
						dataIndex :'status',
						menuDisabled :true,
						width :100,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							if (record.data["status"] != 'C') {
								if (value == 'A')
									return TXT.message_status_approval;
								else if (value == 'W')
									return TXT.message_status_wait;
								else if (value == 'R')
									return TXT.message_status_reject;
								else if (value == 'C')
									return TXT.message_status_cancel;
								else if (value == 'S')
									return TXT.task_detail_account_success;
								else if (value == 'F')
									return TXT.task_detail_account_fail;
							   else if (value == 'P')
									return TXT.task_detail_account_P_run;
								else
									return value;
							} else {
								if (value == 'A')
									return "<span style='color:#c0c0c0;'>"
											+ TXT.message_status_approval
											+ "</span>";
								else if (value == 'W')
									return "<span style='color:#c0c0c0;'>"
											+ TXT.message_status_wait
											+ "</span>";
								else if (value == 'R')
									return "<span style='color:#c0c0c0;'>"
											+ TXT.message_status_reject
											+ "</span>";
								else if (value == 'C')
									return "<span style='color:#c0c0c0;'>"
											+ TXT.message_status_cancel
											+ "</span>";
								else if (value == 'S')
									return "<span style='color:#c0c0c0;'>"
											+ TXT.task_detail_account_success
											+ "</span>";
								else if (value == 'F')
									return "<span style='color:#c0c0c0;'>"
											+ TXT.task_detail_account_fail
											+ "</span>";
							   else if (value == 'P')
									return "<span style='color:#c0c0c0;'>"
											+ TXT.task_detail_account_P_run
											+ "</span>";
								else
									return "<span style='color:#c0c0c0;'>"
											+ value + "</span>";
							}
						}
					},{
						header :TXT.message_noticeFlag.title,
						dataIndex :'noticeFlag',
						menuDisabled :true,
						width :80,
						renderer : function(value) {
							var valueText = '_'+value;
							return TXT.message_noticeFlag[valueText];
						}
					}, {
						header :TXT.message_noticeTimes,
						dataIndex :'noticeTimes',
						menuDisabled :true,
						width :80
					}];
			
			/*obj['defaultCmConfig'].splice(4,1);
			var cmAssignee=obj['defaultCmConfig'][10];
			obj['defaultCmConfig'].splice(10,1);
			obj['defaultCmConfig'].splice(3,0,cmAssignee);
			obj['defaultCmConfig'].splice(12,0,{
						header :TXT.case_status,
						dataIndex :'status',
						menuDisabled :true,
						width :100,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							if (record.data["status"] != 'C') {
								if (value == 'A')
									return TXT.message_status_approval;
								else if (value == 'W')
									return TXT.message_status_wait;
								else if (value == 'R')
									return TXT.message_status_reject;
								else if (value == 'C')
									return TXT.message_status_cancel;
								else
									return value;
							} else {
								if (value == 'A')
									return "<span style='color:#c0c0c0;'>"
											+ TXT.message_status_approval
											+ "</span>";
								else if (value == 'W')
									return "<span style='color:#c0c0c0;'>"
											+ TXT.message_status_wait
											+ "</span>";
								else if (value == 'R')
									return "<span style='color:#c0c0c0;'>"
											+ TXT.message_status_reject
											+ "</span>";
								else if (value == 'C')
									return "<span style='color:#c0c0c0;'>"
											+ TXT.message_status_cancel
											+ "</span>";
								else
									return "<span style='color:#c0c0c0;'>"
											+ value + "</span>";
							}
						}
					});	
			obj['defaultCmConfig'][1].renderer=function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							if (record.data["status"] != 'C')
								if (value == 'NAK')
									return "<span style='color:red;'>" + value
											+ "</span>";
								else
									return "<span style='color:green;'>"
											+ value + "</span>";
							else
								return "<span style='color:#c0c0c0;'>" + value
										+ "</span>";
						}
			obj['defaultCmConfig'][2].renderer=function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							if (record.data["status"] != 'C') {
								if (!record.data["isRead"]) {
									return "<img src='../images/wait.gif' align='absmiddle'/> <span style='font-weight:bold;color:red;'>"
											+ value + "</span>";
								} else
									return value;
							} else {
								if (!record.data["isRead"]) {
									return "<img src='../images/wait.gif' align='absmiddle'/> <span style='font-weight:bold;color:#c0c0c0;'>"
											+ value + "</span>";
								} else
									return "<span style='color:#c0c0c0;'>"
											+ value + "</span>";
								;
							}
						}
			obj['defaultCmConfig'][3].renderer=function(value, cellmeta, record, rowIndex,
				columnIndex, store) {
				if (record.data["status"] == 'C') {
					return "<span style='color:#c0c0c0;'>" + value + "</span>";
				} else
					return value;
			};
			obj['defaultCmConfig'][5].renderer=obj['defaultCmConfig'][3].renderer
			obj['defaultCmConfig'][6].renderer=obj['defaultCmConfig'][3].renderer;
			obj['defaultCmConfig'][7].renderer=obj['defaultCmConfig'][3].renderer;
			obj['defaultCmConfig'][8].renderer=obj['defaultCmConfig'][3].renderer;
			obj['defaultCmConfig'][10].renderer=obj['defaultCmConfig'][3].renderer;
			obj['defaultCmConfig'][11].renderer=obj['defaultCmConfig'][3].renderer;
			obj['defaultCmConfig'][4].renderer=function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							if (value == 'TMP')
								value = TXT.message_type_tmp;
							else if (value == 'MT')
								value = TXT.message_type_mt;
							else if (value == 'MX')
								value = TXT.message_type_mx;
							else if (value = 'RPL')
								value = TXT.message_type_rpl;
							if (record.data["status"] == 'C') {
								return "<span style='color:#c0c0c0;'>" + value
										+ "</span>";
							} else
								return value;
						}
			obj['defaultCmConfig'][9].renderer=function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							if (record.data["status"] != 'C') {
								if (value == '')
									return TXT.case_system;
								else
									return value;
							} else {
								if (value == '')
									return "<span style='color:#c0c0c0;'>"
											+ TXT.case_system + "</span>";
								else
									return "<span style='color:#c0c0c0;'>"
											+ value + "</span>";
							}
						}*/
		});
		messageGrid.setToolBar(messageGridToolBar.render());
		messageGrid.setWidgetEvent({
			sm:{
				rowselect:setTbarStatusInMsgGrid
			},
			grid:{
				dblclick:getMessageInCaseDetail
			}
		});
		messageGrid.customization(obj['cusCaseMessageGrid']);
		var paymentGridToolBar=new Ecp.PaymentGridToolBar();
		paymentGridToolBar.setWidgetEvent({
			cancelRelationship:cancelPaymentRelationShipWithCase,
			applyRefundIncome:applyRefundIncomeWithCase
		});
		paymentGridToolBar.customization(obj['cusPaymentGridTbar']);
		var paymentGrid=new Ecp.PaymentMessageGrid();
		paymentGrid.handleWidgetConfig(function(obj){
			delete obj['defaultStoreConfig']['url'];
			delete obj['defaultGridConfig']['pagination'];
			obj['defaultGridConfig']['title']=TXT.case_payment;
			obj['defaultGridConfig']['border']=false;
			//obj['defaultCmConfig'].splice(10,1);
			obj['defaultCmConfig'].splice(10,1);
		});
		paymentGrid.setToolBar(paymentGridToolBar.render());
		paymentGrid.setWidgetEvent({
			grid:{
			//TODO
				dblclick:getPaymentMessage
			}
		});
		paymentGrid.customization(obj['cusCasePaymentGrid']);
		var historyGrid=new Ecp.CaseHistoryGrid();
		historyGrid.customization(obj['cusCaseHistoryGrid']);
		var commentGridToolBar=new Ecp.CommentsGridToolBar();
		commentGridToolBar.setWidgetEvent({
			createCaseComment:showAddCommentsWin
		});
		commentGridToolBar.customization(obj['cusCommentGridTbar']);
		var commentGrid=new Ecp.CaseCommentsGrid();
		commentGrid.setToolBar(commentGridToolBar.render());
		commentGrid.setWidgetEvent({
			grid:{
				dblclick:showCommentsWin
			},
			store:{
				load:function(){
					commentGrid.setCommentsCount();
				}
			}
		});
		commentGrid.customization(obj['cusCaseCommentsGrid']);
		//var hisIBPsnsGrid=new Ecp.CaseHistoricalIBPsnGrid();
		//hisIBPsnsGrid.customization(obj['cusCaseHisIBPsnGrid']);
		var caseAccompanyInfoTab=new Ecp.CaseAccompanyInfoTab();
		caseAccompanyInfoTab.setServiceComponent({paymentMessages:paymentGrid,messages:messageGrid,histories:historyGrid,comments:commentGrid});
		caseAccompanyInfoTab.setItems([messageGrid.render(),paymentGrid.render(),historyGrid.render(),commentGrid.render()]);
		//caseAccompanyInfoTab.setFirstItem([messageGrid.render(),paymentGrid.render(),historyGrid.render(),commentGrid.render()]);
		caseAccompanyInfoTab.customization(obj['cusCaseAccompanyInfoTab']);
		win.setCaseDetailFormServiceComp(caseDetailForm);
		win.setCaseAccompanyInfoTabServiceComp(caseAccompanyInfoTab);
		win.setItems([caseDetailForm.render(),caseAccompanyInfoTab.render()]);
		win.eventConfig={
			activate:function(window){
				window.toBack();
			},
			hide:function(){
				messageGrid.clearSelections();
				paymentGrid.clearSelections();
				historyGrid.clearSelections();
				commentGrid.clearSelections();
				//hisIBPsnsGrid.clearSelections();
				caseDetailForm.form.reset();
				win.notifyAll('reload');
			}
		};
		win.customization(obj['cusCaseDetailWindow']);
		win.render();
		Ecp.components['caseDeatilWin']=win;
}

Ecp.CaseDetailWindow.createNoSubDetailWin=function(obj,observers){
	if(!Ecp.components['caseDeatilWin']){
		Ecp.CaseDetailWindow.createWindow(obj);
		Ecp.components['caseDeatilWin'].addObservers(observers);
	}
	return Ecp.components['caseDeatilWin'];
}

Ecp.CaseDetailWindow.subHideActionfunction=null;

Ecp.CaseDetailWindow.showHideActionfunction=null;

Ecp.CaseDetailWindow.createComplexDetailWin=function(obj,extraObservers,hideBelowWinFun){
	var win=null;
	if(!Ecp.components['caseDeatilWin']){
		Ecp.CaseDetailWindow.createWindow(obj);
		win=Ecp.components['caseDeatilWin'];
	}else{
		win=Ecp.components['caseDeatilWin'];
		if(win.window.window.hasListener('beforehide')){
			win.window.window.removeListener('beforehide',Ecp.CaseDetailWindow.subHideActionfunction);
			win.window.window.removeListener('beforeshow',Ecp.CaseDetailWindow.showHideActionfunction);
		}
	}
	win.resetObservers(extraObservers);
	if(hideBelowWinFun!=undefined){
		Ecp.CaseDetailWindow.subHideActionfunction=function(){caseDetailBeforeHide(hideBelowWinFun)};
		Ecp.CaseDetailWindow.showHideActionfunction=function(){caseDetailBeforeShow(hideBelowWinFun)};
		win.window.window.addListener('beforehide',Ecp.CaseDetailWindow.subHideActionfunction);
		win.window.window.addListener('beforeshow',Ecp.CaseDetailWindow.showHideActionfunction);
	}
	return win;
}

Ecp.CaseDetailWindow.hiddenRelatedWindow=[];

Ecp.ReplyMsgTab=function(){
	this.commendTypeGrid=null;
	
	this.allTypeGrid=null;
	
	this.tabPanel=new Ecp.TabPanel();
	
	this.defaultConfig={
		activeTab :0,
		border :false,
		frame :false,
		defaults : {
			autoScroll :false
		}
	};
	
	this.defaultItemsConfig=[];
	
	this.serviceComponents=null;
	
	this.customConfig=null;
}

Ecp.ReplyMsgTab.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.ReplyMsgTab.prototype.setItems=function(items){
	for(var i=0;i<items.length;i++)
		this.defaultItemsConfig.push(items[i]);
	//alert(this.defaultItemsConfig.length);	
}

Ecp.ReplyMsgTab.prototype.getTab=function(index){
	this.tabPanle.tabPanel.getTab(index);
}

Ecp.ReplyMsgTab.prototype.getActiveTab=function(){
	return this.tabPanel.tabPanel.getActiveTab();
}

Ecp.ReplyMsgTab.prototype.setServiceComponent=function(obj){
	this.serviceComponents=obj;
}

Ecp.ReplyMsgTab.prototype.getServiceComponent=function(key){
	return this.serviceComponents[key];
}

Ecp.ReplyMsgTab.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.ReplyMsgTab.prototype.render=function(){
	this.tabPanel.init();
	this.tabPanel.setConfig(this.defaultConfig);
	this.tabPanel.setItems(this.defaultItemsConfig);
	this.tabPanel.customization(this.customConfig);
	this.tabPanel['ecpOwner']=this;
	return this.tabPanel.render();
}

Ecp.ReplyMsgListWindow=function(){
	this.window=new Ecp.Window();
	
	this.replyMsgTab=null;
	
	this.defaultConfig={
		id:'replyMsgWin',
		title :TXT.case_replyMX,
		width :700,
		height :500,
		autoScroll :false,
		layout :'fit',
		border :false,
		closeAction:'hide',
		minimizable :false,
		maximizable :false,
		resizable :false,
		modal :true,
		layoutConfig : {
			animate :false
		},
		buttonAlign :'center'
	};
		
	this.defaultItemsConfig=[];
	
	this.defaultButtonsConfig=[{
					text :TXT.common_btnOK,
					//handler :
					scope:this
				}, {
					text :TXT.common_btnClose,
					handler : function() {
						this.window.window.hide();
					},
					scope:this
				}];
	
	this.eventConfig=null;
	
	this.customConfig=null;
	
	this.observers=[];
	
	this.dataBus=null;
}

Ecp.ReplyMsgListWindow.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.ReplyMsgListWindow.prototype.setButtonHandler=function(handler){
	this.defaultButtonsConfig[0].handler=handler;
}

Ecp.ReplyMsgListWindow.prototype.setItem=function(item){
	this.defaultItemsConfig.push(item);
}

Ecp.ReplyMsgListWindow.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.ReplyMsgListWindow.prototype.render=function(){
	this.window.init();
	this.window['config']=this.defaultConfig;
	this.window['items']=this.defaultItemsConfig;
	this.window['buttons']=this.defaultButtonsConfig;
	this.window['listeners']=this.eventConfig;
	this.window.customization(this.customConfig);
	return this.window.render();
}

Ecp.ReplyMsgListWindow.createSingleWindow=function(obj,observers){
	if(!Ecp.components['replyMsgTabWin']){
		var recommendMsgTypeGrid=new Ecp.MessageTypeGrid();
		recommendMsgTypeGrid.handleWidgetConfig(function(obj){
			obj.defaultGridConfig.title=TXT.case_recommendTypeGrid_title;
			obj.defaultGridConfig['id']='recommendTypeGrid';
			delete obj.defaultGridConfig['pagination'];
			obj.defaultCmConfig.splice(2,3);
		});
		recommendMsgTypeGrid.customization(obj['cusRecommendGrid']);
		var allMsgTypeGrid=new Ecp.MessageTypeGrid();
		allMsgTypeGrid.handleWidgetConfig(function(obj){
			obj.defaultGridConfig.title=TXT.case_allTypeGrid_title;
			obj.defaultGridConfig['id']='allMsgTypeGrid';
			delete obj.defaultGridConfig['pagination'];
			obj.defaultCmConfig.splice(2,3);
		});
		allMsgTypeGrid.customization(obj['cusAllTypeGrid']);
		var replyMsgTabPanel=new Ecp.ReplyMsgTab();
		replyMsgTabPanel.setItems([recommendMsgTypeGrid.render(),allMsgTypeGrid.render()]);
		replyMsgTabPanel.setServiceComponent({recommend:recommendMsgTypeGrid,all:allMsgTypeGrid});
		replyMsgTabPanel.customization(obj['cusReplyTab']);
		var win=new Ecp.ReplyMsgListWindow();
		win.setItem(replyMsgTabPanel.render());
		win.setButtonHandler(showReplyXml);
		win.eventConfig={
			hide:function(){
				replyMsgTabPanel.tabPanel.tabPanel.setActiveTab(0);
				recommendMsgTypeGrid.grid.grid.selModel.clearSelections();
				allMsgTypeGrid.grid.grid.selModel.clearSelections();
				this['dataBus']=null;
			}
		};
		win.replyMsgTab=replyMsgTabPanel;
		win.customization(obj['cusRplyMsgListWin']);
		win.render();
		Ecp.components['replyMsgTabWin']=win
		recommendMsgTypeGrid.dataStore.load({cmd:'message',action:'getReplyMsgType',id:obj['messageId']});
		allMsgTypeGrid.dataStore.load({cmd:'message',action:'getAllCurrentVersionMsgType'});
	}else{
		var win=Ecp.components['replyMsgTabWin'];
		win.replyMsgTab.getServiceComponent('recommend').dataStore.load({cmd:'message',action:'getReplyMsgType',id:obj['messageId']});
		win.replyMsgTab.getServiceComponent('all').dataStore.load({cmd:'message',action:'getAllCurrentVersionMsgType'});
	}
	return Ecp.components['replyMsgTabWin'];
}

/************************************ Add by ying ming start here *****************************/
/******************   Ecp.CaseSearchForm    ************************/
Ecp.CaseSearchForm=function()
{
	var currencyStore = new Ext.data.Store( {
		proxy :new Ext.data.HttpProxy( {
			url :DISPATCH_SERVLET_URL + "?cmd=cur&action=findAll"
		}),
		reader :new Ext.data.JsonReader( {
			root :"currencies",
			fields : [ {
				name :'currencyCode'
			} ]
		})
	});
	currencyStore.load();
	
	this.form=null;
	this.config={
		labelAlign :'left',
		region :'center',
		labelWidth :100,
		layout:'form',
		margins:'0 0 2 0',
		cmargins:'0 0 2 0',
		frame :true
	};
	
	this.items=[{
		xtype:'fieldset',
        collapsible: false,
        autoHeight:true,
        header:true, 
        title:'',
		items:[
			   {
					xtype:'hidden',
					id: 'magInstCode',
					name: 'magInstCode'
				},
				{
					xtype:'hidden',
					id: 'realCaseAssignee',
					name: 'realCaseAssignee'
				},
			{
			layout:'column',
			items:[{
				columnWidth :.5,
				layout :'form',
				defaultType :'textfield',
				defaults : {
					anchor :'90%'
				},
				items:[
					{
						fieldLabel :TXT.case_internal_code,
						id :'internalCode',
						name :'internalCode',
						tabIndex:1
					}, {
						fieldLabel :TXT.case_num,
						id :'caseId',
						name :'caseId',
						tabIndex:3
					}
				]
			},{
				columnWidth :.5,
				layout :'form',
				defaultType :'textfield',
				defaults : {
					anchor :'90%'
				},
				items:[
					/*{
						fieldLabel :TXT.case_enter_ibp_seq_num,
						id :'IBPSeqNum',
						name :'IBPSeqNum',
						tabIndex:2
					},*/{
						fieldLabel :TXT.case_creator,
						id :'creatorBic',
						name :'creatorBic',
						tabIndex:2
					}			
				]
			}]
		}]
	},{
		xtype:'fieldset',
        collapsible: false,
        autoHeight:true,
        header:true, 
        title:'',
        items:[{
        	
			layout:'column',
			items:[{
				columnWidth :.5,
				layout :'form',
				defaultType :'textfield',
				defaults : {
					anchor :'90%'
				},
				items:[
					{
						fieldLabel :TXT.case_reference_num,
						id :'referenceNum',
						name :'referenceNum',
						tabIndex:5		
					}, new Ext.form.NumberField( {
						id :'amountFrom',
						name :'amountFrom',
						fieldLabel :TXT.case_amountFrom,
						allowDecimals :true,
						decimalPrecision :2,
						listeners: {
							specialkey: function(f,e){
							if(e.getKey()==9)
							{
								f.ecpOwner.form.setValue('amountTo',f.ecpOwner.form.findById('amountFrom'));
							}
						 }
						},
						tabIndex:7
					}),{
						xtype :'datefield',
						fieldLabel :TXT.case_valueFrom,
						format :'Y-m-d',
						id :'valueDateFrom',
						name :'valueDateFrom',
						editable :false,
						tabIndex:9			
					} 
				]
			},{
				columnWidth :.5,
				layout :'form',
				defaultType :'textfield',
				defaults : {
					anchor :'90%'
				},
				items:[
					{
						fieldLabel :TXT.case_currency,
						id :'currency',
						name :'currency',
						tabIndex:6
					},new Ext.form.NumberField( {
						id :'amountTo',
						name :'amountTo',
						fieldLabel :TXT.case_amountTo,
						allowDecimals :true,
						decimalPrecision :2,
						tabIndex:8
					}),{
						xtype :'datefield',
						fieldLabel :TXT.case_valueTo,
						format :'Y-m-d',
						id :'valueDateTo',
						name :'valueDateTo',
						editable :false,
						tabIndex:10
					}		
				]
			}]
        }]
	},{
		xtype:'fieldset',
        collapsible: false,
        autoHeight:true,
        header:true, 
        title:'',
        items:[{
        	
			layout:'column',
			items:[{
				columnWidth :.5,
				layout :'form',
				defaultType :'textfield',
				defaults : {
					anchor :'90%'
				},
				items:[
					{
						xtype :'combo',
						fieldLabel :TXT.case_type,
						id :'type',
						name :'type',
						value :'',
						store :new Ext.data.SimpleStore( {
							fields : [ 'label', 'value' ],
							data : [
									[ TXT.case_type_RTCP,'RTCP' ],
									[ TXT.case_type_RTMP,'RTMP' ],
									[ TXT.case_type_UTA,'UTA' ],
									[ TXT.case_type_CNR,'CNR' ],
									[ TXT.case_type_OTHER,'OTHER' ]]
							}),
					    forceSelection :true,
						displayField :'label',
						valueField :'value',
						typeAhead :true,
						mode :'local',
						triggerAction :'all',
						editable :false,
						selectOnFocus :true,
						tabIndex:11
					}, {
						xtype :'datefield',
						fieldLabel :TXT.case_dateFrom,
						format :'Y-m-d',
						id :'createTimeFrom',
						name :'createTimeFrom',
						editable :false,
						tabIndex:13	
					}
				]
			},{
				columnWidth :.5,
				layout :'form',
				defaultType :'textfield',
				defaults : {
					anchor :'90%'
				},
				items:[
					{
						xtype:'trigger',
						fieldLabel :TXT.case_assignee,
						id :'caseAssignee',
						name :'caseAssignee',
						triggerClass:'x-form-search-trigger',
						onTriggerClick:function(){
							showInstitutionTreeWin(callBackForAssignee);
						},
						editable:false,
						tabIndex:12
					},{
						xtype :'datefield',
						fieldLabel :TXT.case_dateTo,
						format :'Y-m-d',
						id :'createTimeTo',
						name :'createTimeTo',
						editable :false,
						tabIndex:14
					}			
				]
			}]
        }]
	}];
	
	/*this.items=[
						{
						  	xtype:'hidden',
						  	id: 'magInstCode',
						  	name: 'magInstCode'
						},
						{
							xtype:'hidden',
						  	id: 'realCaseAssignee',
						  	name: 'realCaseAssignee'
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
								fieldLabel :TXT.case_internal_code,
								id :'internalCode',
								name :'internalCode',
								tabIndex:1
							}, {
								fieldLabel :TXT.case_num,
								id :'caseId',
								name :'caseId',
								tabIndex:3
							}, {
								fieldLabel :TXT.case_reference_num,
								id :'referenceNum',
								name :'referenceNum',
								tabIndex:5
							}, new Ext.form.NumberField( {
								id :'amountFrom',
								name :'amountFrom',
								fieldLabel :TXT.case_amountFrom,
								allowDecimals :true,
								decimalPrecision :2,
								tabIndex:7,
								listeners: {
												specialkey: function(f,e){
													if(e.getKey()==9)
													{
														f.ecpOwner.form.setValue('amountTo',f.ecpOwner.form.findById('amountFrom'));
													}
												}
											}
							}), 
								{
									xtype :'datefield',
									fieldLabel :TXT.case_valueFrom,
									format :'Y-m-d',
									id :'valueDateFrom',
									name :'valueDateFrom',
									editable :false,
									tabIndex:9
								},
							{
								xtype :'datefield',
								fieldLabel :TXT.case_dateFrom,
								format :'Y-m-d',
								id :'createTimeFrom',
								name :'createTimeFrom',
								editable :false,
								tabIndex:11
							},
							{
								xtype :'combo',
								fieldLabel :TXT.case_type,
								id :'type',
								name :'type',
								value :'',
								store :new Ext.data.SimpleStore( {
										fields : [ 'label', 'value' ],
										data : [
												[ TXT.case_type_RTCP,
															'RTCP' ],
												[ TXT.case_type_RTMP,
															'RTMP' ],
												[ TXT.case_type_UTA,
															'UTA' ],
												[ TXT.case_type_CNR,
															'CNR' ],
												[ TXT.case_type_OTHER,
														'OTHER' ] ]
									}),
								forceSelection :true,
								displayField :'label',
								valueField :'value',
								typeAhead :true,
								mode :'local',
								triggerAction :'all',
								editable :false,
								selectOnFocus :true,
								tabIndex:13
							},
							new Ext.form.NumberField( {
								id :'spendTime',
								name :'spendTime',
								fieldLabel :TXT.case_spend_time,
								allowDecimals :false,
								decimalPrecision:0,
								FormatComma:false,
								tabIndex:15
							}), {
								xtype:'trigger',
								fieldLabel :TXT.case_magInst,
								id :'magInstDesc',
								name :'magInstDesc',
								triggerClass:'x-form-search-trigger',
								//allowBlank:false,
								onTriggerClick:function(){
									showInstitutionTreeWin(callBack);
								},
								editable:false,
								tabIndex:17
							},{
								fieldLabel :TXT.case_enter_ibp_seq_num,
								id :'IBPSeqNum',
								name :'IBPSeqNum',
								tabIndex:19
							}]
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
									xtype :'combo',
									fieldLabel :TXT.case_status,
									id :'status',
									name :'status',
									value :'',
									store :new Ext.data.SimpleStore(
											{
												fields : [ 'label',
														'value' ],
												data : [
														[ TXT.common_all, '' ],
														[
																TXT.case_open,
																'O' ],
														[
																TXT.case_close,
																'C' ],
														[
																TXT.case_reopen,
																'R' ] ]
											}),
									forceSelection :true,
									displayField :'label',
									valueField :'value',
									typeAhead :true,
									mode :'local',
									triggerAction :'all',
									selectOnFocus :true,
									editable :false,
									tabIndex:2
								},
								{
									fieldLabel :TXT.case_creator,
									id :'creatorBic',
									name :'creatorBic',
									tabIndex:4
								},
//								{
//									xtype :'combo',
//									fieldLabel :TXT.case_currency,
//									id :'currency',
//									name :'currency',
//									value :'',
//									//store :currencyStore,
//									maxLength :3,
//									forceSelection :false,
//									displayField :'currencyCode',
//									valueField :'currencyCode',
//									typeAhead :true,
//									mode :'local',
//									editable :true,
//									listeners : {
//										blur : function(hidden) {
//											var rowValue = hidden
//													.getRawValue();
//											var value = hidden
//													.getValue();
//											if (rowValue == TXT.common_all)
//												this.setValue('');
//											else if (rowValue == value)
//												this.setValue(value);
//											else
//												this.setValue(rowValue);
//										}
//									},
//									triggerAction :'all',
//									selectOnFocus :true,
//									tabIndex:6
//								},
								{
									fieldLabel :TXT.case_currency,
									id :'currency',
									name :'currency',
									maxLength :3,
									tabIndex:6
								},new Ext.form.NumberField( {
									id :'amountTo',
									name :'amountTo',
									fieldLabel :TXT.case_amountTo,
									allowDecimals :true,
									decimalPrecision :2,
									tabIndex:8
								}),
								{
									xtype :'datefield',
									fieldLabel :TXT.case_valueTo,
									format :'Y-m-d',
									id :'valueDateTo',
									name :'valueDateTo',
									editable :false,
									tabIndex:10
								},
								{
									xtype :'datefield',
									fieldLabel :TXT.case_dateTo,
									format :'Y-m-d',
									id :'createTimeTo',
									name :'createTimeTo',
									editable :false,
									tabIndex:12
								},
								{
									fieldLabel :TXT.case_account,
									id :'account',
									name :'account',
									tabIndex:14
								},
								{
									xtype :'combo',
									fieldLabel :TXT.case_inwardOutward,
									id :'remittance',
									name :'remittance',
									value :'',
									store :new Ext.data.SimpleStore(
											{
												fields : [ 'label',
														'value' ],
												data : [
														[ TXT.common_all, '' ],
														[
																TXT.case_inward,
																'I' ],
														[
																TXT.case_outward,
																'O' ],
														[
																TXT.case_unDefine,
																'N' ] ]
											}),
									forceSelection :true,
									displayField :'label',
									valueField :'value',
									typeAhead :true,
									mode :'local',
									triggerAction :'all',
									editable :false,
									selectOnFocus :true,
									tabIndex:16
								},
								{
									xtype :'combo',
									fieldLabel :TXT.case_subAccount,
									id :'isSubAccount',
									name :'isSubAccount',
									value :'',
									store :new Ext.data.SimpleStore(
											{
												fields : [ 'label',
														'value' ],
												data : [
														[ TXT.common_all, '' ],
														[
																TXT.case_isSubAccount,
																'Y' ],
														[
																TXT.case_isNotSubAccount,
																'N' ] ]
											}),
									forceSelection :true,
									displayField :'label',
									valueField :'value',
									typeAhead :true,
									mode :'local',
									triggerAction :'all',
									selectOnFocus :true,
									editable :false,
									tabIndex:18
								},{
									xtype:'trigger',
									fieldLabel :TXT.case_assignee,
									id :'caseAssignee',
									name :'caseAssignee',
									triggerClass:'x-form-search-trigger',
									//allowBlank:false,
									onTriggerClick:function(){
										showInstitutionTreeWin(callBackForAssignee);
									},
									editable:false,
									tabIndex:20
								}
							]
					}
	            ];*/
	this.buttons=[];
}

Ecp.CaseSearchForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.CaseSearchForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.CaseSearchForm.prototype.render=function()
{
	this.form=new Ecp.FormPanel();
	this.form.init();
	var obj={};
	obj['config']=this.config;
	obj['buttons']=this.buttons;
	obj['items']=this.items;
	this.form.customization(obj);
	this.form['ecpOwner']=this;
	this.items[1]['items'][0]['items'][0]['items'][1]['ecpOwner']=this;
	return this.form.render();
}

Ecp.CaseSearchForm.prototype.reset=function()
{
	this.form.reset();
}

Ecp.CaseSearchForm.prototype.setValue=function(propertyName, value)
{
	this.form.setValue(propertyName, value);
}
/******************   Ecp.CaseSearchForm    ************************/

/******************   Ecp.CaseSearchWindow    ************************/
Ecp.CaseSearchWindow=function()
{
	this.window=null;
	this.config={
			title :TXT.case_search,
			width :680,
			height :370,
			autoScroll :false,
			layout :'fit',
			border :false,
			resizable :false,
			minimizable :false,
			maximizable :false,
			shadow:false,
			layoutConfig : {
				animate :false
			},
			buttonAlign :'center'
	};
	this.buttons=[{
		text :TXT.common_btnOK,
		handler : function() {
			clickQueryCaseBtn(this);
		}}, {text :TXT.common_reset,handler : function(){clickResetQueryCaseBtn(this)}}];
	this.observers=[];
	this.items=[];
}

Ecp.CaseSearchWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.CaseSearchWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.CaseSearchWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.CaseSearchWindow.prototype.render=function()
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

Ecp.CaseSearchWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.CaseSearchWindow.prototype.setCloseAction=function(isClose){
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
Ecp.CaseSearchWindow.createCaseSearchWindow=function(windowObj,observers,fun){
	 
	//liaozhiling modify 2011-05-03
	 if(!Ecp.components['CaseSearchWindow']){

		var caseSearchForm=new Ecp.CaseSearchForm();
	
		if(windowObj['items'] && windowObj['items'][0])
			caseSearchForm.customization(windowObj['items'][0]);
		
		Ecp.components.caseSearchForm=caseSearchForm;
		
		// window
		var caseSearchWindow = new Ecp.CaseSearchWindow();
		caseSearchWindow.handleWidgetConfig(function(obj){
			if(fun && typeof fun=='function')
				caseSearchWindow.handleWidgetConfig(fun);
			obj.items=[caseSearchForm.render()];
		});
		caseSearchWindow.customization(windowObj);
		
		// add observer
		if(observers && observers.length>0)
			caseSearchWindow.observers=observers;
		caseSearchWindow.render();
		
		Ecp.components['CaseSearchWindow']=caseSearchWindow;
		
	 } 
	 return Ecp.components['CaseSearchWindow'].window;
	//return caseSearchWindow.window;
	
	
	
}

/**
 * single search window static array
 */
Ecp.CaseSearchWindow.singleSearchWindows=null;

/**
 * create single search window static function
 */
Ecp.CaseSearchWindow.createSingleSearchWindow=function(windowObj,observers, fun){
	//if(!Ecp.CaseSearchWindow.singleSearchWindows)
		Ecp.CaseSearchWindow.singleSearchWindows=Ecp.CaseSearchWindow.createCaseSearchWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
				});
	return Ecp.CaseSearchWindow.singleSearchWindows;
}

Ecp.CaseSearchWindow.createSingleResumeSearchWindow=function(windowObj,observers, fun){
	//if(!Ecp.CaseSearchWindow.singleSearchWindows)
		Ecp.CaseSearchWindow.singleSearchWindows=Ecp.CaseSearchWindow.createCaseSearchWindow(windowObj,observers,
				function(obj){
					obj.setCloseAction(false);
					obj.buttons[0].handler=searchBackupedCases;
				});			
	return Ecp.CaseSearchWindow.singleSearchWindows;
}

Ecp.CaseSearchWindow.createSingleSearchWindowForHandle=function(obj){
	 if(!Ecp.components['CaseSearchWindowForHandle']){
			//alert();
		var caseSearchForm=new Ecp.CaseSearchForm();
	
		if(obj['items'] && obj['items'][0])
			caseSearchForm.customization(obj['items'][0]);
		
		caseSearchForm.handleWidgetConfig(function(form){
			var caseType=form.items[2].items[0].items[0].items[0];
			form.items[2].items[0].items[0].items[0]=form.items[2].items[0].items[0].items[1];
			form.items[2].items[0].items[0].items[0].tabIndex=11;
			form.items[2].items[0].items[0].items[1]=caseType;
			form.items[2].items[0].items[0].items[1].tabIndex=13;
			form.items[2].items[0].items[1].items[0]=form.items[2].items[0].items[1].items[1];
			form.items[2].items[0].items[1].items[0].tabIndex=12;
			form.items[2].items[0].items[1].items.splice(1,1);
		});
			
		Ecp.components.caseSearchForm=caseSearchForm;
		
		// window
		var caseSearchWindow = new Ecp.CaseSearchWindow();
		caseSearchWindow.handleWidgetConfig(function(obj){
			obj.setCloseAction(false);
			obj.buttons[0].handler=searchCasesForHandle;
			obj.config.modal=true;
			obj.items=[caseSearchForm.render()];
		});
		caseSearchWindow.customization(obj);
		
		// add observer
		if(obj['observers']!==undefined && obj['observers'].length>0)
			caseSearchWindow.observers=obj['observers'];
		caseSearchWindow.render();
		
		Ecp.components['CaseSearchWindowForHandle']=caseSearchWindow;
		
	 } 
	 return Ecp.components['CaseSearchWindowForHandle'];
}

/******************   Ecp.CaseSearchWindow    ************************/

/******************   Tree Control Callback ***************************/
function callBack(treeRecord,treeWin){
	if(treeRecord!=null)
	{
		Ecp.components.caseSearchForm.setValue('magInstCode', treeRecord.attributes.internalCode);
		Ecp.components.caseSearchForm.setValue('magInstDesc', treeRecord.attributes.name);
		treeWin.window.hide();
	}
	else
	{
		if(uid!='')
		{
		  treeWin.window.hide();
		  return;
		}
		Ext.MessageBox.alert(TXT.common_hint,TXT.case_institutionNeed);
		return false;
	}
}

function callBackForAssignee(treeRecord,treeWin){
	//alert('a');
	if(treeRecord!=null)
	{
		Ecp.components.caseSearchForm.setValue('realCaseAssignee', treeRecord.attributes.internalCode);
		Ecp.components.caseSearchForm.setValue('caseAssignee', treeRecord.attributes.name);
		treeWin.window.hide();
	}
	else
	{
		if(uid!='')
		{
		  treeWin.window.hide();
		  return;
		}
		Ext.MessageBox.alert(TXT.common_hint,TXT.case_institutionNeed);
		return false;
	}
}
/************************************ Add by ying ming end here *****************************/

Ecp.CaseHistoricalIBPsnGrid=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root :'hisIBPsns',
		id :'id',
		fields : [ {
			name :'id'
		}, {
			name :'value'
		}, {
			name :'creator'
		}, {
			name :'date'
		}]
	};
	
	this.defaultCmConfig=[{
				header :TXT.case_IBP_latest,
				dataIndex :'value',
				menuDisabled :true,
				width :150,
				renderer:function(value){
					if(value=='')
						return TXT.case_IBP_latest_null;
					return value;
				}
			}, {
				header :TXT.case_IBP_latest_user,
				dataIndex :'creator',
				menuDisabled :true,
				width :80
			}, {
				header :TXT.case_IBP_latest_date,
				dataIndex :'date',
				menuDisabled :true,
				width :150
			} ];
			
	this.defaultGridConfig={
		title :TXT.case_IBP_seq,
		id:'eiCaseHistoricalIBPsnGrid',
		border:false
	};
	
	this.customizationConfig={};
	
	this.defaultSelModel=0;	
}

Ecp.CaseHistoricalIBPsnGrid.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.CaseHistoricalIBPsnGrid.prototype.customization=function(obj){
	this.customizationConfig=obj;
}

Ecp.CaseHistoricalIBPsnGrid.prototype.setWidgetEvent=function(obj){
	obj['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(obj['grid']);
	obj['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(obj['cm']);
	obj['sm']==null?this.grid.setSmGridEvent({}):this.grid.setSmGridEvent(obj['sm']);
	obj['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(obj['store']);
}

Ecp.CaseHistoricalIBPsnGrid.prototype.render=function(){
	this.dataStore.setConfig(this.defaultStoreConfig);
	this.dataStore.init();
	this.customizationConfig['store']==undefined?1:this.dataStore.customization(this.customizationConfig['store']);
	this.grid.setStore(this.dataStore.render());
	this.grid.setColumnMode(this.defaultCmConfig);
	this.grid.setSelectMode(this.defaultSelModel);
	this.grid.setConfig(this.defaultGridConfig);
	this.grid.init();
	this.customizationConfig['grid']==undefined?1:this.grid.customization(this.customizationConfig['grid']);
	this.grid['ecpOwner']=this;
	return this.grid.render();
}

Ecp.CaseHistoricalIBPsnGrid.prototype.loadLocalData=function(data){
	this.dataStore.store.loadData(data);
}

Ecp.CaseHistoricalIBPsnGrid.prototype.clearSelections=function(){
	this.grid.grid.selModel.clearSelections(true); 
}
