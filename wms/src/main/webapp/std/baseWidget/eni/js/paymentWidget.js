Ecp.PaymentMessageGrid=function(){
	this.dataStore=new Ecp.DataStore();
	this.grid=new Ecp.Grid();
	
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root :'payments',
		totalProperty :'totalCount',
		id :'id',
		fields : [ {
			name :'id'
		}, {
			name :'referenceNum'
		}, {
			name :'type'
		}, {
			name :'ioFlag'
		}, {
			name :'isSimulated'
		}, {
			name :'busiType'
		},{
			name :'sendBic'
		}, {
			name :'receiverBic'
		}, {
			name :'relatedReferenceNum'
		}, {
			name :'currency'
		}, {
			name :'amount'
		}, {
			name :'valueDate'
		}, {
			name :'creationTime'
		}, {
			name :'account'
		}, {
			name :'statementNum'
		}, {
			name :'sequenceNum'
		}, {
			name :'related'
		}, {
			name :'cid'
		},{
			name:'virtualBic'	
		},{
			name:'cspSeqId'
		},{
			name:'msgFlag'
		},{
			name:'clrFlag'
		},{
			name:'dccSendFlag'
		},{
			name:'sysFlag'
		},{
			name:'txStatus'
		},{
			name:'reconcileType'
		},{
			name:'dealStatus'
		},{
			name:'years'
		},{
			name:'stmtAffixSeq'
		},{
			name:'stmtAffixPage'
		},{
			name:'entryDate'
		},{
			name:'stmtCode'
		},{
			name:'institutionName'
		},{
			name:'transRef'
		},{
			name:'clsId'
		},{
			name:'dbCr'
		},{
			name:'approvalStatus'
		}]
	};
	var commonRender=function(value, cellmeta, record, rowIndex,
				columnIndex, store) {
				if (record.data["approvalStatus"] == 'C') {
					return "<span style='color:#c0c0c0;'>" + value + "</span>";
				} else
					return value;
	};
	this.defaultCmConfig=[
					{
						header :'',
						dataIndex :'ioFlag',
						width :30,
						menuDisabled: true, 
						renderer : function(value) {
							if (value == 'I')
								return "<img src='../images/in.png' align='absmiddle'/>";
							else
								return "<img src='../images/out.png' align='absmiddle'/>";
						}
					},  {
						header :TXT.payment_type,
						dataIndex :'type',
						width :60,
						menuDisabled: true,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							if (record.data["approvalStatus"] == 'C') {
								value="<span style='color:#c0c0c0;'>" + value + "</span>"
							}
							if (record.data["isSimulated"]!=null&&record.data["isSimulated"]) {
								return "<img src='../images/iconInformation.gif' align='absmiddle'/>"
										+ value;
							} else{
								return value;
							}
						}
					},{
						header :TXT.payment_busi_type.title,
						dataIndex :'busiType',
						width :85,
						menuDisabled: true,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							var valueTXT = TXT.payment_busi_type[value];
							if (record.data["approvalStatus"] == 'C') {
								valueTXT = "<span style='color:#c0c0c0;'>" +valueTXT + "</span>";
							}
							return valueTXT
						}
					}, {
						header :TXT.payment_sender,
						dataIndex :'sendBic',
						width :95,
						menuDisabled: true,
						renderer:commonRender
					}, {
						header :TXT.payment_receiver,
						dataIndex :'receiverBic',
						menuDisabled: true,
						renderer:commonRender,
						width :95
					}, {
						header :TXT.related_ref,
						dataIndex :'referenceNum',
						width :130,
						renderer:commonRender,
						menuDisabled: true
					},{
						header :TXT.payment_relatedRef,
						dataIndex :'relatedReferenceNum',
						menuDisabled: true,
						renderer:commonRender,
						width :130
					}, {
						header :TXT.payment_currency,
						dataIndex :'currency',
						menuDisabled: true,
						renderer:commonRender,
						width :40
					}, {
						header :TXT.payment_amount,
						dataIndex :'amount',
						menuDisabled: true,
						align :'right',
						renderer:commonRender,
						width :100
					}, {
						header :TXT.payment_valueDate,
						dataIndex :'valueDate',
						menuDisabled: true,
						renderer:commonRender,
						width :80
					},{
						header :TXT.payment_related,
						dataIndex :'related',
						width :150,
						menuDisabled: true,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							var valueTXT=value;
							if (value == null || value == "") {
								valueTXT=TXT.case_payment_no;
							} else {
								valueTXT=value;
							}
							if (record.data["approvalStatus"] == 'C') {
								valueTXT= "<span style='color:#c0c0c0;'>" +valueTXT+ "</span>";
							}
							return valueTXT;
						}
					},
					{header:TXT.payment_msgFlag.title,dataIndex:'msgFlag',width:100,menuDisabled: true,
						renderer :function(value) {
							var msgFlagTxt= "";
							if(value!=null&&value.trim()!=""){
	 							msgFlagTxt = TXT.payment_msgFlag[value];
							}
							return  msgFlagTxt;
						}
					},
					{header:TXT.payment_clrFlag.title,dataIndex:'clrFlag',width:100,menuDisabled: true,
						renderer :function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							var valueTXT= "";
							if(value!=null&&value.trim()!=""){
								valueTXT = TXT.payment_clrFlag[value];
								if (record.data["approvalStatus"] == 'C') {
									"<span style='color:#c0c0c0;'>" +valueTXT + "</span>";
								}
							}
							return  valueTXT;
						}
					},
					{header:TXT.payment_dccSendFlag,dataIndex:'dccSendFlag',width:60,menuDisabled: true,
						renderer :function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							var valueTxt = '';
							if (value=='Y'||value=='y') {
								valueTxt = TXT.common_yes_desc;
							} else if (value=='N'||value=='n') {
								valueTxt = TXT.common_no_desc;
							}
							if(record.data["approvalStatus"] == 'C'){
								valueTxt =  "<span style='color:#c0c0c0;'>" +valueTxt + "</span>";
							}
							return valueTxt;
						}
					},
					{header:TXT.payment_sysFlag,dataIndex:'sysFlag',width:80,menuDisabled: true,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							var valueTxt = '';
							if (record.data["ioFlag"]=='O') {
								valueTxt = TXT.payment_sysFlag_O[value];
							} else if (record.data["ioFlag"]=='I') {
								valueTxt = TXT.payment_sysFlag_I[value];
							}
							if(record.data["approvalStatus"] == 'C'){
								valueTxt =  "<span style='color:#c0c0c0;'>" +valueTxt + "</span>";
							}
							return valueTxt;
						}
					},
					{header:TXT.payment_txStatus,dataIndex:'txStatus',width:100,menuDisabled: true,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							var value = '';
							var txStatus = '_'+record.data["txStatus"];
							if (record.data["ioFlag"]=='O') {
								value = TXT.payment_txStatus_O[txStatus];
							} else if (record.data["ioFlag"]=='I') {
								value = TXT.payment_txStatus_I[txStatus];
							}
							if(record.data["approvalStatus"] == 'C'){
								value =  "<span style='color:#c0c0c0;'>" +value + "</span>";
							}
							return value;
						}},
					{header:TXT.payment_reconcileType.title,dataIndex:'reconcileType',width:60,menuDisabled: true,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							var valueTXT= "";
							if(value!=null&&value.trim()!=""){
								valueTXT=TXT.payment_reconcileType[value];
								if(record.data["approvalStatus"] == 'C'){
									valueTXT =  "<span style='color:#c0c0c0;'>" +valueTXT + "</span>";
								}
							}
							return valueTXT;
						}
					},
					{header:TXT.payment_dealStatus.title,dataIndex:'dealStatus',width:60,menuDisabled: true,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							var valueTXT= "";
							if(value!=null&&value.trim()!=""){
								valueTXT=TXT.payment_dealStatus[value];
								if(record.data["approvalStatus"] == 'C'){
									valueTXT =  "<span style='color:#c0c0c0;'>" +valueTXT + "</span>";
								}
							}
							return valueTXT;
						}
					},
					{header:TXT.payment_years,dataIndex:'years',menuDisabled: true,renderer:commonRender,width:80},
					{header:TXT.payment_sequenceNum,dataIndex:'sequenceNum',menuDisabled: true,renderer:commonRender,width:80},
					//{header:TXT.payment_stmtAffixSeq,dataIndex:'stmtAffixSeq',menuDisabled: true,width:100},
					{header:TXT.payment_statementNum,dataIndex:'statementNum',menuDisabled: true,renderer:commonRender,width:80},
					//{header:TXT.payment_stmtAffixPage,dataIndex:'stmtAffixPage',menuDisabled: true,width:100},
					{header:TXT.payment_dbCr.title,dataIndex:'dbCr',menuDisabled: true,width:120,
						renderer :  function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							var valueTXT= "";
							if(value!=null&&value.trim()!=""){
								valueTXT=TXT.payment_dbCr[value];
								if(record.data["approvalStatus"] == 'C'){
									valueTXT =  "<span style='color:#c0c0c0;'>" +valueTXT + "</span>";
								}
							}
							return valueTXT;
						}
					},
					{header:TXT.payment_account,dataIndex:'account',menuDisabled: true,renderer:commonRender,width:95},
					{header:TXT.payment_entryDate,dataIndex:'entryDate',menuDisabled: true,renderer:commonRender,width:130},
					{header:TXT.payment_stmtCode,dataIndex:'stmtCode',menuDisabled: true,renderer:commonRender,width:90},
					{header:TXT.payment_institutionName,dataIndex:'institutionName',menuDisabled: true,renderer:commonRender,width:130},
					{header:TXT.payment_transRef,dataIndex:'transRef',menuDisabled: true,renderer:commonRender,width:130},
					{
						header :TXT.payment_creationTime,
						dataIndex :'creationTime',
						menuDisabled: true,
						renderer:commonRender,
						width :130
					},
					{header:TXT.payment_clsId,dataIndex:'clsId',menuDisabled: true,renderer:commonRender,width:120}
					
					/*, {
						header :TXT.payment_synchroTime,
						dataIndex :'synchroTime',
						menuDisabled: true,
						width :150
					}, {
						header :TXT.payment_clrSysId,
						dataIndex :'clrSysId',
						menuDisabled: true,
						width :150
					}, {
						header :TXT.payment_msgSource,
						dataIndex :'msgSource',
						menuDisabled: true,
						width :150
					}, {
						header :TXT.payment_ownBranchBic,
						dataIndex :'ownBranchBic',
						menuDisabled: true,
						width :150
					} {
						header :TXT.payment_virtualBic,
						dataIndex :'virtualBic',
						menuDisabled: true,
						width :120
					},{
						header :TXT.payment_cspSeqId,
						dataIndex :'cspSeqId',
						menuDisabled: true,
						width :120
					}*/];
	
	this.defaultGridConfig={
		title:TXT.payment,
		id:'paymentMessageGrid',
		pagination:true
	};
	
	this.defaultSelModel=0;
	
	this.customizationConfig=null;
	
	this.bottomPagination=null;

}

Ecp.PaymentMessageGrid.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.PaymentMessageGrid.prototype.setToolBar=function(toolBar){
	this.grid.setTopToolBar(toolBar);
}

Ecp.PaymentMessageGrid.prototype.setWidgetEvent=function(obj){
	obj['grid']==null?this.grid.setGridEvent({}):this.grid.setGridEvent(obj['grid']);
	obj['cm']==null?this.grid.setCmGridEvent({}):this.grid.setCmGridEvent(obj['cm']);
	obj['store']==null?this.dataStore.setEventConfig({}):this.dataStore.setEventConfig(obj['store']);
}

Ecp.PaymentMessageGrid.prototype.customization=function(obj){
	this.customizationConfig=obj;
}

Ecp.PaymentMessageGrid.prototype.render=function(bottomButtons){
	this.dataStore.setConfig(this.defaultStoreConfig);
	this.dataStore.init();
	this.customizationConfig['store']==undefined?1:this.dataStore.customization(this.customizationConfig['store']);
	this.grid.setStore(this.dataStore.render());
	this.grid.setColumnMode(this.defaultCmConfig);
	this.grid.setSelectMode(this.defaultSelModel);
	if(bottomButtons!=undefined){
		if(this.defaultGridConfig['pagination']!=undefined){
			delete this.defaultGridConfig['pagination'];
			this.defaultGridConfig['bbar']=new Ext.PagingToolbar({
				 pageSize: PAGE_SIZE,
           		 store: this.dataStore.store,
				 displayInfo:true,
				 items:[bottomButtons]
			});
			this.bottomPagination=true;
		}else{
			this.defaultGridConfig['bbar']=[bottomButtons];
		}
	}
	this.grid.setConfig(this.defaultGridConfig);
	this.grid.init();
	this.customizationConfig['grid']==undefined?1:this.grid.customization(this.customizationConfig['grid']);
	this.grid['ecpOwner']=this;
	return this.grid.render();
}

Ecp.PaymentMessageGrid.prototype.search=function(baseParams){
	this.dataStore.store['baseParams']=baseParams;
	if(this.grid['pagination']!=undefined||this['bottomPagination']!=null)
		this.dataStore.store.load({
			params:{
				start:0,
				limit:PAGE_SIZE
			}
		});
	else{
		this.dataStore.store.removeAll();
		this.dataStore.store.load();
	}
}

Ecp.PaymentMessageGrid.prototype.loadLocalData=function(data){
	this.dataStore.store.loadData(data);
}

Ecp.PaymentMessageGrid.prototype.show=function(){
	this.search({
		cmd :'payment',
		action :'find',
		json :Ext.util.JSON.encode({
			referenceNum :'',
			dateFrom :'',
			dateTo :'',
			amountMin :'0',
			amountMax :'0',
			ioFlag :'',
			currency :'',
			sender:'',
			receiver:'',
			msgType:'',
			busiType:''
		})
	});
}

Ecp.PaymentMessageGrid.prototype.insertRecord=function(index,record){
	this.dataStore.store.insert(index,record);
}

Ecp.PaymentMessageGrid.prototype.remove=function(record){
	this.dataStore.store.remove(record);
}

Ecp.PaymentMessageGrid.prototype.removeAll=function(){
	this.dataStore.store.removeAll();
}

Ecp.PaymentMessageGrid.prototype.clearSelections=function(){
	this.grid.grid.selModel.clearSelections(true); 
}

Ecp.PaymentMessageGrid.prototype.update=function(src,eventName){
	if(eventName=='afterRelated')
		this.dataStore.store.reload();
	if(eventName=='reload')
		this.dataStore.store.reload();
}

Ecp.PaymentMessageGrid.prototype.reload=function(){
	this.dataStore.store.reload();
}
// add by sunyue
Ecp.paymentForForwaredWinGrid=function(){
	Ecp.ServiceGrid.call(this);
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root :'payments',
		totalProperty :'totalCount',
		id :'id',
		fields : [ {
			name :'id'
		}, {
			name :'referenceNum'
		}, {
			name :'type'
		}, {
			name :'ioFlag'
		}, {
			name :'isSimulated'
		}, {
			name :'busiType'
		},{
			name :'sendBic'
		}, {
			name :'receiverBic'
		}, {
			name :'relatedReferenceNum'
		}, {
			name :'currency'
		}, {
			name :'amount'
		}, {
			name :'valueDate'
		}, {
			name :'creationTime'
		}, {
			name :'account'
		}, {
			name :'statementNum'
		}, {
			name :'sequenceNum'
		}, {
			name :'related'
		}, {
			name :'cid'
		},{
			name:'virtualBic'	
		},{
			name:'cspSeqId'
		},{
			name:'clrFlag'
		},{
			name:'dccSendFlag'
		},{
			name:'sysFlag'
		},{
			name:'txStatus'
		},{
			name:'reconcileType'
		},{
			name:'dealStatus'
		},{
			name:'years'
		},{
			name:'stmtAffixSeq'
		},{
			name:'stmtAffixPage'
		},{
			name:'entryDate'
		},{
			name:'stmtCode'
		},{
			name:'institutionName'
		},{
			name:'transRef'
		},{
			name:'clsId'
		}]
	};
	this.defaultCmConfig=[
					{
						header :'',
						dataIndex :'ioFlag',
						width :30,
						menuDisabled: true, 
						renderer : function(value) {
							if (value == 'I')
								return "<img src='../images/in.png' align='absmiddle'/>";
							else
								return "<img src='../images/out.png' align='absmiddle'/>";
						}
					},  {
						header :TXT.payment_type,
						dataIndex :'type',
						width :60,
						menuDisabled: true,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							if (record.data["isSimulated"]!=null&&record.data["isSimulated"]) {
								return "<img src='../images/iconInformation.gif' align='absmiddle'/>"
										+ value;
							} else
								return value;
						
						}
					},{
						header :TXT.payment_busi_type.title,
						dataIndex :'busiType',
						width :85,
						menuDisabled: true,
						renderer : function(value) {
							return TXT.payment_busi_type[value];
						}
					}, {
						header :TXT.payment_sender,
						dataIndex :'sendBic',
						width :95,
						menuDisabled: true
					}, {
						header :TXT.payment_receiver,
						dataIndex :'receiverBic',
						menuDisabled: true,
						width :95
					}, {
						header :TXT.related_ref,
						dataIndex :'referenceNum',
						width :130,
						menuDisabled: true
					},{
						header :TXT.payment_relatedRef,
						dataIndex :'relatedReferenceNum',
						menuDisabled: true,
						width :130
					}, {
						header :TXT.payment_currency,
						dataIndex :'currency',
						menuDisabled: true,
						width :40
					}, {
						header :TXT.payment_amount,
						dataIndex :'amount',
						menuDisabled: true,
						align :'right',
						width :100
					}, {
						header :TXT.payment_valueDate,
						dataIndex :'valueDate',
						menuDisabled: true,
						width :80
					}, 
					{header:TXT.payment_clrFlag.title,dataIndex:'clrFlag',width:100,
						renderer :function(value){
							return TXT.payment_clrFlag[value];
						}
					},
					{header:TXT.payment_dccSendFlag,dataIndex:'dccSendFlag',width:60},
					{header:TXT.payment_sysFlag,dataIndex:'sysFlag',width:80,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							var valueTxt = '';
							if (record.data["ioFlag"]=='O') {
								valueTxt = TXT.payment_sysFlag_O[value];
							} else if (record.data["ioFlag"]=='I') {
								valueTxt = TXT.payment_sysFlag_I[value];
							}
							return valueTxt;
						}
					},
					{header:TXT.payment_txStatus,dataIndex:'txStatus',width:100,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							var value = '';
							var txStatus = '_'+record.data["txStatus"];
							if (record.data["ioFlag"]=='O') {
								value = TXT.payment_txStatus_O[txStatus];
							} else if (record.data["ioFlag"]=='I') {
								value = TXT.payment_txStatus_I[txStatus];
							}
							return value;
						}},
					{header:TXT.payment_reconcileType.title,dataIndex:'reconcileType',width:60,
						renderer : function(value) {
							return TXT.payment_reconcileType[value];
						}
					},
					{header:TXT.payment_dealStatus.title,dataIndex:'dealStatus',width:60,
						renderer : function(value) {
							return TXT.payment_dealStatus[value];
						}
					},
					{header:TXT.payment_years,dataIndex:'years',width:80},
					{header:TXT.payment_sequenceNum,dataIndex:'sequenceNum',width:80},
					{header:TXT.payment_stmtAffixSeq,dataIndex:'stmtAffixSeq',width:100},
					{header:TXT.payment_statementNum,dataIndex:'statementNum',width:80},
					{header:TXT.payment_stmtAffixPage,dataIndex:'stmtAffixPage',width:100},
					{header:TXT.payment_account,dataIndex:'account',width:95},
					{header:TXT.payment_entryDate,dataIndex:'entryDate',width:130},
					{header:TXT.payment_stmtCode,dataIndex:'stmtCode',width:90},
					{header:TXT.payment_institutionName,dataIndex:'institutionName',width:110},
					{header:TXT.payment_transRef,dataIndex:'transRef',width:120},
					{
						header :TXT.payment_creationTime,
						dataIndex :'creationTime',
						menuDisabled: true,
						width :130
					},
					
					{
						header :TXT.payment_related,
						dataIndex :'related',
						width :150,
						menuDisabled: true,
						renderer : function(value) {
							if (value == null || value == "") {
								return TXT.case_payment_no;
							} else {
								return value;
							}
						}
					}];
	this.defaultStoreConfig.baseParams={
		cmd:'payment',
		action:'find'
	};
	this.defaultGridConfig={
		id:'paymentForForwaredWinGrid',
		region :'center',
		stripeRows :true,
		pagination:true
	};
}
Ecp.paymentForForwaredWinGrid.prototype.show=function(){
	this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}
Ecp.extend(Ecp.paymentForForwaredWinGrid.prototype,new Ecp.ServiceGrid());
//------------------------
Ecp.PaymentToolBar=function(){
	this.toolBar=new Ecp.ToolBar();
	totalToolBarItem['createCaseWithXml']['childrens']=[totalToolBarItem['createCaseWith007'],totalToolBarItem['createCaseWith008'],totalToolBarItem['createCaseWith026'],totalToolBarItem['createCaseWith027'],totalToolBarItem['createCaseWith028'],totalToolBarItem['createCaseWith037']];
	totalToolBarItem['createCaseWithFin']['childrens']=[totalToolBarItem['createCaseWith192'],totalToolBarItem['createCaseWith195'],totalToolBarItem['createCaseWith196'],totalToolBarItem['createCaseWith292'],totalToolBarItem['createCaseWith295'],totalToolBarItem['createCaseWith296']];
	totalToolBarItem['createCase']['childrens']=[totalToolBarItem['createCaseWithFinTemplate'],totalToolBarItem['createCaseWithDraft'],totalToolBarItem['createCaseWithFin']];
	//totalToolBarItem['paymentSearchMenu']['childrens']=[totalToolBarItem['explicitSearchPayment'],totalToolBarItem['searchPayment'],totalToolBarItem['keyWordsSearchPayment']];
	if(Ecp.userInfomation.sendToSwift){
		totalToolBarItem.createCase['childrens'].push(totalToolBarItem.createCaseWithXml);
	}
	//totalToolBarItem['paymentSearchMenu']['childrens']=[totalToolBarItem['searchPayment']];
	this.defaultToolBarItemConfig=[totalToolBarItem['searchPayment'],totalToolBarItem['createCase'],
	                               totalToolBarItem['relatedPaymentWithCase'],//totalToolBarItem['importPayment']
	                               totalToolBarItem['paymentRelateCase']
	                               ];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'paymentToolBar'
	};
}


Ecp.PaymentToolBar.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}


Ecp.PaymentToolBar.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.PaymentToolBar.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}


Ecp.PaymentToolBar.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.PaymentToolBar.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}

Ecp.PaymentMessageExplicitlySearchForm=function(){
	this.form=new Ecp.FormPanel();
	
	this.defaultFormConfig={
		labelAlign :'left',
		labelWidth :60,
		frame :true,
		autoScroll :false,
		bodyStyle :'padding:10px 10px 10px 10px',
		defaultType:'textfield'
	};
	
	this.defaultItemsConfig=[{
		fieldLabel :TXT.payment_refNum,
		width :280,
		id :'referenceNum',
		name :'referenceNum',
		maxLength :16,
		allowBlank :false
	},{
		xtype:'hidden',
		id :'cmd',
		name :'cmd',
		value:'payment'
	},{
		xtype:'hidden',
		id :'action',
		name :'action',
		value:'searchByRefNum'
	}];
	
	this.defaultButtonsConfig=[{
		text:TXT.common_search,
		scope:this
	}];
	
	this.customConfig=null;
}

Ecp.PaymentMessageExplicitlySearchForm.prototype.handlerWidgetConfig=function(handler){
	handler(this);
}

Ecp.PaymentMessageExplicitlySearchForm.prototype.init=function(){}

Ecp.PaymentMessageExplicitlySearchForm.prototype.customization=function(cusObj){
	this.customConfig=cusObj;
}

Ecp.PaymentMessageExplicitlySearchForm.prototype.setButtonHandler=function(handler){
	this.defaultButtonsConfig[0].handler=handler;
}

Ecp.PaymentMessageExplicitlySearchForm.prototype.render=function(){
	this.form.init();
	this.form['config']=this.defaultFormConfig;
	this.form['items']=this.defaultItemsConfig;
	this.form['buttons']=this.defaultButtonsConfig;
	this.form.customization(this.customConfig);
	this.form['ecpOwner']=this;
	return this.form.render();
}

Ecp.PaymentMessageExplicitlySearchWin=function(){
	this.targetSearchPanel=null;
	
	this.window=new Ecp.Window();
	
	this.defaultWinConfig={
		width :410,
		height :120,
		autoScroll :false,
		closeAction :'hide',
		layout :'fit',
		border :false,
		minimizable :false,
		maximizable :false,
		title:TXT.payment_searchByRefNum,
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
		text:TXT.common_btnQuery,
		scope:this
	}];
	
	this.eventConfig=null;
	
	this.customConfig=null;
}

Ecp.PaymentMessageExplicitlySearchWin.prototype.setItems=function(array){
	this.defaultItemsConfig=array;
}

Ecp.PaymentMessageExplicitlySearchWin.prototype.setButtonHandler=function(array){
	this.defaultButtonsConfig[0].handler=array[0];
}

Ecp.PaymentMessageExplicitlySearchWin.prototype.setTargetSearchPanel=function(obj){
	this.targetSearchPanel=obj;
}

Ecp.PaymentMessageExplicitlySearchWin.prototype.show=function(){
	this.window.window.show();
}

Ecp.PaymentMessageExplicitlySearchWin.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.PaymentMessageExplicitlySearchWin.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.PaymentMessageExplicitlySearchWin.prototype.render=function(){
	this.window.init();
	this.window['config']=this.defaultWinConfig;
	this.window['items']=this.defaultItemsConfig;
	this.window['buttons']=this.defaultButtonsConfig;
	this.window['listeners']=this.eventConfig;
	this.window.customization(this.customConfig);
	return this.window.render();
}

Ecp.PaymentMessageExplicitlySearchWin.createWindow=function(obj,observer){
	if(!Ecp.components['PaymentMessageExplicitlySearchWin']){
		var form=new Ecp.PaymentMessageExplicitlySearchForm();
		form.handlerWidgetConfig(function(form){
			form['defaultButtonsConfig']=null;
		});
		form.customization(obj['cusExplicitSearchForm']);
		var win=new Ecp.PaymentMessageExplicitlySearchWin();
		win.setItems([form.render()]);
		win.setButtonHandler([explicitSearchPayment]);
		win.setTargetSearchPanel(obj['targetSearchPanel']);
		win.eventConfig={
			beforeshow:function(){
				form.form.reset();
			}
		};
		win.customization(obj['cusExplicitSearchWin']);
		win.render();
		Ecp.components['PaymentMessageExplicitlySearchWin']=win;
	}
	return Ecp.components['PaymentMessageExplicitlySearchWin'];
}


Ecp.PaymentMessageKeyWordsSearchForm=function(){
	
	this.form=new Ecp.FormPanel();
	
	this.defaultFormConfig={
		labelAlign :'left',
		region :'center',
		labelWidth :90,
		frame :true
	};
	this.defaultItemsConfig=[{
			layout :'column',
			items : [ {
				columnWidth :.5,
				layout :'form',
				defaultType :'textfield',
				defaults : {
					anchor :'90%'
				},
				items : [ {
					fieldLabel :TXT.payment_sender,
					id :'sendBic',
					name :'sendBic',
					tabIndex:1
				},{
					xtype:'datefield',
					fieldLabel :TXT.payment_creationDateFrom,
					id :'creationDateFrom',
					name :'creationDateFrom',
					format :"Y-m-d",
					editable:false,
					validateOnBlur:false,
					validationEvent:false,
					tabIndex:3
				}, {
					fieldLabel :TXT.payment_keywords_lable,
					id :'keywords',
					name :'keywords',
					tabIndex:5
				} ]
			}, {
				columnWidth :.5,
				layout :'form',
				defaultType :'textfield',
				defaults : {
					anchor :'90%'
				},
				items : [ {
					fieldLabel :TXT.payment_receiver,
					id :'receiverBic',
					name :'receiverBic',
					tabIndex:2
				}, {
					xtype:'datefield',
					fieldLabel :TXT.payment_creationDateTo,
					id :'creationDateTo',
					name :'creationDateTo',
					format :"Y-m-d",
					editable:false,
					validateOnBlur:false,
					validationEvent:false,
					tabIndex:4
				}]
			} ]
		} ];
	
	this.defaultButtonsConfig=[{
		text:TXT.common_search,
		scope:this
	}];
	
	this.customConfig=null;
}

Ecp.PaymentMessageKeyWordsSearchForm.prototype.handlerWidgetConfig=function(handler){
	handler(this);
}

Ecp.PaymentMessageKeyWordsSearchForm.prototype.init=function(){}

Ecp.PaymentMessageKeyWordsSearchForm.prototype.customization=function(cusObj){
	this.customConfig=cusObj;
}

Ecp.PaymentMessageKeyWordsSearchForm.prototype.dateFromValidator=function(value){
	var dateToValue=this['ecpOwner'].form.findById('creationDateTo');
	this.clearInvalid();
	if(value==''&&dateToValue!=''){
		return TXT.common_from_to_error;
	}
	if(value!=''&&dateToValue!=''&&this.getValue().getTime()>this['ecpOwner'].form.findFieldById('creationDateTo').getValue().getTime()){
		return TXT.common_date_error;
	}
	return true;
}

Ecp.PaymentMessageKeyWordsSearchForm.prototype.dateToValidator=function(value){
	var dateFromValue=this['ecpOwner'].form.findById('creationDateFrom');
	this.clearInvalid();
	if(value==''&&dateFromValue!=''){
		return TXT.common_from_to_error;
	}
	return true;
}

Ecp.PaymentMessageKeyWordsSearchForm.prototype.setValidator=function(){
	this.defaultItemsConfig[0]['items'][0]['items'][1]['validator']=this['dateFromValidator'];
	this.defaultItemsConfig[0]['items'][0]['items'][1]['ecpOwner']=this;
	this.defaultItemsConfig[0]['items'][1]['items'][1]['validator']=this['dateToValidator'];
	this.defaultItemsConfig[0]['items'][1]['items'][1]['ecpOwner']=this;
}

Ecp.PaymentMessageKeyWordsSearchForm.prototype.setButtonHandler=function(handler){
	this.defaultButtonsConfig[0].handler=handler;
}

Ecp.PaymentMessageKeyWordsSearchForm.prototype.render=function(){
	this.form.init();
	this.form['config']=this.defaultFormConfig;
	this.form['items']=this.defaultItemsConfig;
	this.form['buttons']=this.defaultButtonsConfig;
	//alert(this.form);
	//alert(this.defaultItemsConfig['']);
	this.setValidator();
	/*this.form['ecpOwner']=this;
	alert(this.form.constructor);*/
	this.form.customization(this.customConfig);
	return this.form.render();
}

Ecp.PaymentMessageKeyWordsSearchWin=function(){
	this.targetSearchPanel=null;
	
	this.window=new Ecp.Window();
	
	this.defaultWinConfig={
		width :600,
		height :165,
		autoScroll :false,
		layout :'fit',
		border :false,
		closeAction :'hide',
		minimizable :false,
		maximizable :false,
		title:TXT.payment_keywords,
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
		text:TXT.common_btnQuery,
		scope:this
	}
	//liaozhiling add 2011-05-03
	,{
		text:TXT.common_reset,
		handler:function(){
			this.defaultItemsConfig[0]['ecpOwner'].reset();
		}
	}];
	
	this.eventConfig=null;
	
	this.customConfig=null;
}

Ecp.PaymentMessageKeyWordsSearchWin.prototype.setItems=function(array){
	this.defaultItemsConfig=array;
}

Ecp.PaymentMessageKeyWordsSearchWin.prototype.setButtonHandler=function(array){
	this.defaultButtonsConfig[0].handler=array[0];
}

Ecp.PaymentMessageKeyWordsSearchWin.prototype.setTargetSearchPanel=function(obj){
	this.targetSearchPanel=obj;
}

Ecp.PaymentMessageKeyWordsSearchWin.prototype.show=function(){
	this.window.window.show();
}

Ecp.PaymentMessageKeyWordsSearchWin.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.PaymentMessageKeyWordsSearchWin.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.PaymentMessageKeyWordsSearchWin.prototype.render=function(){
	this.window.init();
	this.window['config']=this.defaultWinConfig;
	this.window['items']=this.defaultItemsConfig;
	this.window['buttons']=this.defaultButtonsConfig;
	this.window['listeners']=this.eventConfig;
	this.window.customization(this.customConfig);
	return this.window.render();
}

Ecp.PaymentMessageKeyWordsSearchWin.createWindow=function(obj,observer){
	if(!Ecp.components['PaymentMessageKeyWordsSearchWin']){
		var form=new Ecp.PaymentMessageKeyWordsSearchForm();
		form.handlerWidgetConfig(function(form){
			form['defaultButtonsConfig']=null;
		});
		form.customization(obj['cusKeyWordsSearchForm']);
		var win=new Ecp.PaymentMessageKeyWordsSearchWin();
		win.setItems([form.render()]);
		win.setButtonHandler([keyWordsSearchPayment]);
		win.setTargetSearchPanel(obj['targetSearchPanel']);
		//liaozhiling modify 2011-05-03
//		win.eventConfig={
//			beforeshow:function(){
//				form.form.reset();
//			}
//		};
		win.customization(obj['cusKeyWordsSearchWin']);
		win.render();
		Ecp.components['PaymentMessageKeyWordsSearchWin']=win;
	}
	return Ecp.components['PaymentMessageKeyWordsSearchWin'];
}

Ecp.PaymentMessageSearchForm=function(){
	this.form=new Ecp.FormPanel();
	
	this.currencyStore=new Ecp.CurrencyStore();
	
	this.defaultFormConfig={
		labelAlign :'left',
		region :'center',
		labelWidth :90,
		frame :true
	};
	
	this.defaultItemsConfig=[ {
					layout :'column',
					items : [
							{
								columnWidth :.5,
								layout :'form',
								defaultType :'textfield',
								defaults : {
									anchor :'93%'
								},
								items : [
										{
											fieldLabel :TXT.payment_refNum,
											id :'referenceNum',
											name :'referenceNum',
											maxLength :16,
											tabIndex:1
										},
										{
											fieldLabel :TXT.payment_sender,
											id :'sender',
											name :'sender',
											maxLength :11,
											tabIndex:3
										},
										{
											xtype:'datefield',
											fieldLabel :TXT.payment_valueDateFrom,
											id :'dateFrom',
											name :'dateFrom',
											format :"Y-m-d",
											editable :false,
											validateOnBlur:false,
											validationEvent:false,
											tabIndex:5
										},
										{
											xtype :'numberfield',
											fieldLabel :TXT.payment_amount + '(min)',
											id :'amountMin',
											name :'amountMin',
											minValue :0,
											validateOnBlur:false,
											validationEvent:false,
											tabIndex:7,
											listeners: {
												specialkey: function(f,e){
													if(e.getKey()==9)
													{
														f.ecpOwner.form.setValue('amountMax',f.ecpOwner.form.findById('amountMin'));
													}
												}
											}
										},
										{
											fieldLabel :TXT.payment_currency,
											id :'currency',
											name :'currency',
											tabIndex:9
										},{
											xtype :'combo',
											fieldLabel :TXT.payment_busi_type.title,
											id :'busiType',
											name :'busiType',
											value :'',
											store :new Ext.data.ArrayStore({
														fields : [ 'label','value' ],
														data : [
																[TXT.payment_direction_inOut,'' ],
																[TXT.payment_busi_type[1],'1' ],
																[TXT.payment_busi_type[2],'2' ],
																[TXT.payment_busi_type[3],'3' ],
																[TXT.payment_busi_type[4],'4' ],
																[TXT.payment_busi_type[5],'5']
																]
											}),
											forceSelection :true,
											displayField :'label',
											valueField :'value',
											typeAhead :true,
											mode :'local',
											triggerAction :'all',
											selectOnFocus :true,
											editable :false,
											tabIndex:11
										}
										/*{
											xtype :'combo',
											fieldLabel :TXT.payment_currency,
											id :'currency',
											name :'currency',
											value :'',
											// tpl: '<tpl for="."><div
											// ext:qtip="提示：ID={ID}；TypeCName={TypeCName}"
											// class="x-combo-list-item">{TypeCName}</div></tpl>',
											// store:currencyStore,
											//store :currencyStore,
											maxLength :3,
											forceSelection :false,
											displayField :'currencyCode',
											valueField :'currencyCode',
											typeAhead :true,
											mode :'local',
											editable :true,
											listeners : {
												blur : function(hidden) {
													var rowValue = hidden
															.getRawValue();
													var value = hidden
															.getValue();
													if (rowValue == TXT.common_all)
														this.setValue('');
													else if (rowValue == value)
														this.setValue(value);
													else
														this.setValue(rowValue);
												}
											},
											triggerAction :'all',
											selectOnFocus :true
										}*/
								]
							},
							{
								columnWidth :.5,
								layout :'form',
								defaultType :'textfield',
								defaults : {
									anchor :'93%'
								},
								items : [
										{
											xtype :'combo',
											fieldLabel :TXT.payment_direction,
											id :'ioFlag',
											name :'ioFlag',
											value :'',
											store :new Ext.data.ArrayStore(
													{
														fields : [ 'label',
																'value' ],
														data : [
																[
																		TXT.payment_direction_inOut,
																		'' ],
																[
																		TXT.payment_direction_out,
																		'O' ],
																[
																		TXT.payment_direction_in,
																		'I' ] ]
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
											fieldLabel :TXT.payment_receiver,
											id :'receiver',
											name :'receiver',
											maxLength :11,
											tabIndex:4
										},
										{
											xtype:'datefield',
											fieldLabel :TXT.payment_valueDateTo,
											id :'dateTo',
											name :'dateTo',
											format :"Y-m-d",
											editable :false,
											validateOnBlur:false,
											validationEvent:false,
											tabIndex:6
										},
										{
											xtype :'numberfield',
											fieldLabel :TXT.payment_amount + '(max)',
											id :'amountMax',
											name :'amountMax',
											minValue :0,
											validateOnBlur:false,
											validationEvent:false,
											tabIndex:8
										},{
											xtype :'combo',
											fieldLabel :TXT.payment_type,
											id :'msgType',
											name :'msgType',
											value :'',
											store :new Ext.data.ArrayStore(
											{
												fields : [ 'label','value' ],
												data : [
														[TXT.common_all,''],
														 ['103','103'],
														 ['202','202'],
														 ['202COV','202COV'],
														 ['900','900'],
														 ['910','910'],
														 ['940','940'],
														 ['950','950']
													   ]
													}),
											maxLength :6,
											forceSelection :false,
											displayField :'label',
											valueField :'value',
											typeAhead :true,
											mode :'local',
											editable :true,
											/*listeners : {
												blur : function(hidden) {
													alert(this.getValue());
												}
											},*/
											triggerAction :'all',
											selectOnFocus :true,
											tabIndex:10
										}
								]
							} ]
				}];
	
	this.defaultButtonsConfig=[{
		text:TXT.common_search,
		scope:this
	}];
	
	this.customConfig=null;
}

Ecp.PaymentMessageSearchForm.prototype.handlerWidgetConfig=function(handler){
	handler(this);
}

Ecp.PaymentMessageSearchForm.prototype.init=function(){}

Ecp.PaymentMessageSearchForm.prototype.customization=function(cusObj){
	this.customConfig=cusObj;
}

Ecp.PaymentMessageSearchForm.prototype.initCurrencyStore=function(){
	this.currencyStore.handleWidgetConfig(function(serviceStore){
		serviceStore['defaultStoreConfig']['fields']=[
			{
				name: 'currencyCode'
			}
		];
		serviceStore['defaultStoreConfig']['baseParams']={
			cmd:'cur',
			action:'findAll'
		};
	});
	this.currencyStore.render();
	this.currencyStore.load();
	return this.currencyStore.dataStore.store;
}

Ecp.PaymentMessageSearchForm.prototype.dateFromValidator=function(value){
	var dateToValue=this['ecpOwner'].form.findById('dateTo');
	this.clearInvalid();
	if(value==''&&dateToValue!='')
		return TXT.common_from_to_error;
	if(value!=''&&dateToValue!=''&&this.getValue().getTime()>this['ecpOwner'].form.findFieldById('dateTo').getValue().getTime())
		return TXT.common_date_error;
	return true;
}

Ecp.PaymentMessageSearchForm.prototype.dateToValidator=function(value){
	var dateFromValue=this['ecpOwner'].form.findById('dateFrom');
	this.clearInvalid();
	if(value==''&&dateFromValue!='')
		return TXT.common_from_to_error;
	return true;
}	

Ecp.PaymentMessageSearchForm.prototype.amountMinValidator=function(value){
	var amountMax=this['ecpOwner'].form.findById('amountMax');
	this.clearInvalid();
	if(value==''&&amountMax!='')
		return TXT.common_from_to_error;
	if(value != "" && amountMax != ""&& this.getValue() > amountMax)
		return TXT.common_amount_error;
	return true;
}	

Ecp.PaymentMessageSearchForm.prototype.amountMaxValidator=function(value){
	var amountMin=this['ecpOwner'].form.findById('amountMin');
	this.clearInvalid();
	if(amountMin!=''&&value=='')
		return TXT.common_from_to_error;
	return true;
}

Ecp.PaymentMessageSearchForm.prototype.setValidator=function(){
	this.defaultItemsConfig[0]['items'][0]['items'][2]['validator']=this['dateFromValidator'];
	this.defaultItemsConfig[0]['items'][0]['items'][2]['ecpOwner']=this;
	this.defaultItemsConfig[0]['items'][1]['items'][2]['validator']=this['dateToValidator'];
	this.defaultItemsConfig[0]['items'][1]['items'][2]['ecpOwner']=this;
	this.defaultItemsConfig[0]['items'][0]['items'][3]['validator']=this['amountMinValidator'];
	this.defaultItemsConfig[0]['items'][0]['items'][3]['ecpOwner']=this;
	this.defaultItemsConfig[0]['items'][1]['items'][3]['validator']=this['amountMaxValidator'];
	this.defaultItemsConfig[0]['items'][1]['items'][3]['ecpOwner']=this;
}

Ecp.PaymentMessageSearchForm.prototype.setButtonHandler=function(handler){
	this.defaultButtonsConfig[0].handler=handler;
}

Ecp.PaymentMessageSearchForm.prototype.render=function(){
	this.form.init();
	this.form['config']=this.defaultFormConfig;
	//var store=this.initCurrencyStore();
	//this.defaultItemsConfig[0]['items'][0]['items'][4]['store']=store;
	this.form['items']=this.defaultItemsConfig;
	this.form['buttons']=this.defaultButtonsConfig;
	this.setValidator();
	this.form.customization(this.customConfig);
	this.form['ecpOwner']=this;
	return this.form.render();
}

Ecp.PaymentMessageSearchWin=function(){
	this.targetSearchPanel=null;
	
	this.window=new Ecp.Window();
	
	this.defaultWinConfig={
		width :600,
		height :250,
		autoScroll :false,
		layout :'fit',
		border :false,
		closeAction :'hide',
		title:TXT.payment_search,
		minimizable :false,
		maximizable :false,
		layoutConfig : {
			animate :false
		},
		resizable :false,
		modal :true,
		shadow:false,
		buttonAlign :'center'
	};
	
	this.defaultItemsConfig=null;
	
	this.defaultButtonsConfig=[{
		text:TXT.common_btnQuery,
		scope:this
	},{
		text:TXT.common_reset,
		scope:this,
		handler:function(){
			this.defaultItemsConfig[0]['ecpOwner'].reset();
		}
	}];
	
	this.eventConfig=null;
	
	this.customConfig=null;
}

Ecp.PaymentMessageSearchWin.prototype.setItems=function(array){
	this.defaultItemsConfig=array;
}

Ecp.PaymentMessageSearchWin.prototype.setButtonHandler=function(array){
	this.defaultButtonsConfig[0].handler=array[0];
}

Ecp.PaymentMessageSearchWin.prototype.setTargetSearchPanel=function(obj){
	this.targetSearchPanel=obj;
}

Ecp.PaymentMessageSearchWin.prototype.show=function(){
	this.window.window.show();
}

Ecp.PaymentMessageSearchWin.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.PaymentMessageSearchWin.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.PaymentMessageSearchWin.prototype.render=function(){
	this.window.init();
	this.window['config']=this.defaultWinConfig;
	this.window['items']=this.defaultItemsConfig;
	this.window['buttons']=this.defaultButtonsConfig;
	this.window['listeners']=this.eventConfig;
	this.window.customization(this.customConfig);
	return this.window.render();
}

Ecp.PaymentMessageSearchWin.createWindow=function(obj,observer){
	if(!Ecp.components['PaymentMessageSearchWin'])
	{
		var form=new Ecp.PaymentMessageSearchForm();
		if(!form){
			form=new Ecp.PaymentMessageSearchForm();
		}
		form.handlerWidgetConfig(function(form){
			form['defaultButtonsConfig']=null;
		});
		form.customization(obj['cusSearchForm']);
		var win=new Ecp.PaymentMessageSearchWin();
		win.setItems([form.render()]);
		win.setButtonHandler([searchPayment]);
		win.setTargetSearchPanel(obj['targetSearchPanel']);
//		win.eventConfig={
//			beforeshow:function(){
//				form.form.reset();
//			}
//		};
		win.customization(obj['cusSearchWin']);
		win.render();
		Ecp.components['PaymentMessageSearchWin']=win;
	}
	return Ecp.components['PaymentMessageSearchWin'];
}

Ecp.PaymentMutilSearchPanel=function(){
	this.tabPanel=new Ecp.TabPanel();
	
	this.defaultConfig={
		activeTab :0,
		border :false,
		frame :false,
		region :'north',
		height :215,
		layoutOnTabChange :true,
		defaults : {
			autoScroll :false
		}
	};
	
	this.defaultItemsConfig=[];
	
	this.customConfig=null;
}

Ecp.PaymentMutilSearchPanel.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.PaymentMutilSearchPanel.prototype.setItems=function(items){
	for(var i=0;i<items.length;i++)
		this.defaultItemsConfig.push(items[i]);
}

Ecp.PaymentMutilSearchPanel.prototype.activeTab=function(index){
	this.tabPanel.tabPanel.setActiveTab(index);
}

Ecp.PaymentMutilSearchPanel.prototype.getTab=function(index){
	return this.tabPanel.tabPanel.get(index);
}

Ecp.PaymentMutilSearchPanel.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.PaymentMutilSearchPanel.prototype.render=function()
{
	this.tabPanel.init();
	this.tabPanel.setConfig(this.defaultConfig);
	this.tabPanel.setItems(this.defaultItemsConfig);
	//alert(tabPanelObj['items']);
	this.tabPanel.customization(this.customConfig);
	this.tabPanel['ecpOwner']=this;
	return this.tabPanel.render();
}

Ecp.PaymentMessageIntegrateWindow=function(){
	this.window=new Ecp.Window();
	//need reset
	this.selectGridPanel=new Ecp.PaymentMessageGrid();
	//need reset
	this.targetGridPanel=new Ecp.PaymentMessageGrid();
	//need reset
	this.mutilSearchPanel=new Ecp.PaymentMutilSearchPanel();
	
	this.defaultConfig={
		title:TXT.payment_globalSearch,
		width :960,
		height :600,
		autoScroll :false,
		layout :'border',
		border :false,
		closeAction :'hide',
		minimizable :false,
		maximizable :false,
		modal:true,
		shadow:false,
		layoutConfig : {
			animate :false
		},
		resizable :false,
		buttonAlign :'center'
	};
	
	this.defaultItemsConfig=[];
	
	this.defaultButtonsConfig=[{
		text:TXT.common_btnOK,
		scope:this
	},{
		text:TXT.common_btnClose,
		scope:this,
		handler:function(){
			this.window.window.hide();
		}
	}];
	
	this.eventConfig=null;
	
	this.customConfig=null;
	//need reset
	this.targetRecordIndex=0;
	
	this.targetRecordCollections={};
	
	this.dataBus=null;
	
	this.observers=[];
	
	this.resetObservers=null;
}

Ecp.PaymentMessageIntegrateWindow.prototype.initSelectGridPanel=function(obj){
	this.selectGridPanel.handleWidgetConfig(function(grid){
		grid['defaultStoreConfig']['fields'].splice(15,0,{name:'caseType'});
		grid['defaultGridConfig']['id']='selectGridPanel';
		grid['defaultGridConfig']['region']='center';
		//grid['defaultGridConfig']['height']=160;
		grid['defaultGridConfig']['title']=TXT.payment_searched;
	});
	this.selectGridPanel.setWidgetEvent(obj['selecteGridEventConfig']);
	this.selectGridPanel.customization(obj['cusSelectGridPanel']);
	var bbarButtons=obj['bbarButtons'];
	if(bbarButtons!=undefined)
		for(var i=0;i<bbarButtons.length;i++)
			bbarButtons[i]['scope']=this;
	//alert(this.selectGridPanel['defaultGridConfig']['region']);	
	this.defaultItemsConfig.push(this.selectGridPanel.render(bbarButtons));
}

Ecp.PaymentMessageIntegrateWindow.prototype.initTargetGridPanel=function(obj){
	this.targetGridPanel.handleWidgetConfig(function(grid){
		grid['defaultStoreConfig']['fields'].splice(15,0,{name:'caseType'});
		delete grid['defaultStoreConfig']['url'];
		delete grid['defaultGridConfig']['pagination'];
		grid['defaultGridConfig']['id']='targetGridPanel';
		grid['defaultGridConfig']['region']='south';
		grid['defaultGridConfig']['height']=140;
		grid['defaultGridConfig']['title']=TXT.payment_selected;
	});
	this.targetGridPanel.setWidgetEvent(obj['targetGridEventConfig']);
	this.targetGridPanel.customization(obj['cusTargetGridPanel']);
	this.defaultItemsConfig.push(this.targetGridPanel.render());
}

Ecp.PaymentMessageIntegrateWindow.prototype.initMutilSearchPanel=function(obj){
	//obj['searchForms'][1]=obj['searchForms'][1].render();
	//alert(obj['searchForms'][1].items.itemAt(0).items.itemAt(0).items.itemAt(0).id);
	this.mutilSearchPanel.setItems(obj['searchForms']);
	this.mutilSearchPanel.customization(obj['cusMutilSearchPanel']);
	this.defaultItemsConfig.push(this.mutilSearchPanel.render());
}

Ecp.PaymentMessageIntegrateWindow.prototype.show=function(){
	this.window.window.show();
}

Ecp.PaymentMessageIntegrateWindow.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.PaymentMessageIntegrateWindow.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.PaymentMessageIntegrateWindow.prototype.setConfirmButtonHandler=function(handler){
	this.defaultButtonsConfig[0]['handler']=handler;
}

Ecp.PaymentMessageIntegrateWindow.prototype.addObservers=function(observers){
	for(var i=0;i<observers.length;i++)
		this.observers.push(observers[i]);
}

Ecp.PaymentMessageIntegrateWindow.prototype.setResetObservers=function(observers){
	this.resetObservers=observers;
}

Ecp.PaymentMessageIntegrateWindow.prototype.notifyAll=function(eventName){
	//alert(this.observers.length);
	for(var i=0;i<this.observers.length;i++)
		this.observers[i].update(this,eventName);
	for(var i=0;i<this.resetObservers.length;i++)
		this.resetObservers[i].update(this,eventName);
}

Ecp.PaymentMessageIntegrateWindow.prototype.reset=function(){
	this.mutilSearchPanel.activeTab(0);
	this.selectGridPanel.removeAll();
	this.targetGridPanel.removeAll();
	this.targetRecordIndex=0;
	this.targetRecordCollections={};
	this.dataBus=null;
}

Ecp.PaymentMessageIntegrateWindow.prototype.resetInCaseDetail=function(){
	this.mutilSearchPanel.activeTab(0);
	this.selectGridPanel.removeAll();
	this.dataBus=null;
}

Ecp.PaymentMessageIntegrateWindow.prototype.setValues=function(obj){
	this.mutilSearchPanel.getTab(0)['ecpOwner'].setValues(obj);
	//this.mutilSearchPanel.getTab(1)['ecpOwner'].setValues(obj);
	//this.mutilSearchPanel.getTab(2)['ecpOwner'].setValues(obj);
}

Ecp.PaymentMessageIntegrateWindow.prototype.render=function(){
	this.window.init();
	this.window['config']=this.defaultConfig;
	this.window['items']=this.defaultItemsConfig;
	this.window['buttons']=this.defaultButtonsConfig;
	this.window['listeners']=this.eventConfig;
	this.window.customization(this.customConfig);
	return this.window.render();
}

Ecp.PaymentMessageIntegrateWindow.createWindow=function(obj,observers,resetObservers){
	if(!Ecp.components['receiverPaymentSearchWin']){
		var win=new Ecp.PaymentMessageIntegrateWindow();
		var forms=[];
		/*var explicitForm=new Ecp.PaymentMessageExplicitlySearchForm();
		explicitForm.handlerWidgetConfig(function(form){
			form['defaultFormConfig']['title']=TXT.payment_searchByRefNum;
			form['defaultItemsConfig'][0]['allowBlank']=true;
		});
		explicitForm['targetSearchPanel']=win['selectGridPanel'];
		explicitForm.setButtonHandler(explicitSearchPayment);
		explicitForm.customization(obj['cusExplicitForm']);
		//forms.push(explicitForm.render());
		var explicitExtForm=explicitForm.render();*/
		var searchForm=new Ecp.PaymentMessageSearchForm();
		searchForm.handlerWidgetConfig(function(form){
			form['defaultFormConfig']['title']=TXT.payment_search;
		});
		searchForm['targetSearchPanel']=win['selectGridPanel'];
		searchForm.setButtonHandler(searchPayment);
		searchForm.customization(obj['searchForm']);
		//forms.push(searchForm.render());
		var searchExtForm=searchForm.render();
		/*var searchKeyWordsForm=new Ecp.PaymentMessageKeyWordsSearchForm();
		searchKeyWordsForm.handlerWidgetConfig(function(form){
			form['defaultFormConfig']['title']=TXT.payment_keywords;
		});
		searchKeyWordsForm['targetSearchPanel']=win['selectGridPanel'];
		searchKeyWordsForm.setButtonHandler(keyWordsSearchPayment);
		searchKeyWordsForm.customization(obj['cusKeyWordsForm']);
		var searchKeyWordsExtForm=searchKeyWordsForm.render();*/
	//	forms.push(searchKeyWordsForm.render());
		var forms=[searchExtForm];
		//alert(explicitExtForm.items.itemAt(0).id);
		//alert(searchExtForm.items.itemAt(0).items.itemAt(0).items.itemAt(0).id);
		var mutilParam={
			searchForms:forms,
			cusMutilSearchPanel:obj['cusMutilSearchPanel']
		};
		win.initMutilSearchPanel(mutilParam);
		totalToolBarItem.addReceivePayment.handler=addReceivePayment;
		totalToolBarItem.removeReceivePayment.handler=removeReceivePayment;
		win.initSelectGridPanel({
			cusSelectGridPanel:obj['cusSelectGridPanel'],
			bbarButtons:['-',totalToolBarItem.addReceivePayment,'-',totalToolBarItem.removeReceivePayment],
			selecteGridEventConfig:{
				grid:{
					dblclick:showPaymentInRceiveProc
				}
			}
		});
		win.initTargetGridPanel({
			cusTargetGridPanel:obj['cusTargetGridPanel'],
			targetGridEventConfig:{
				grid:{
					dblclick:showPaymentInRceiveProc
				}
			}
		});
		win.eventConfig={
			beforeshow:function(){
				//explicitForm.form.reset();
				searchForm.form.reset();
				//searchKeyWordsForm.form.reset();
				win.reset();
			}
		};
		win.setConfirmButtonHandler(checkSelectedPayment);
		win.addObservers(observers);
		win.customization(obj['cusSearchWin']);
		win.render();
		Ecp.components['receiverPaymentSearchWin']=win;
	}
	if(resetObservers!==undefined)
		Ecp.components['receiverPaymentSearchWin'].setResetObservers(resetObservers);
	return Ecp.components['receiverPaymentSearchWin'];
}

Ecp.PaymentMessageIntegrateWindow.createSelectWinForAssignXml=function(obj){
	if(!Ecp.components['selectWinForAssignXml']){
		var win=new Ecp.PaymentMessageIntegrateWindow();
		var forms=[];
		/*var explicitForm=new Ecp.PaymentMessageExplicitlySearchForm();
		explicitForm.handlerWidgetConfig(function(form){
			form['defaultFormConfig']['title']=TXT.payment_searchByRefNum;
			form['defaultItemsConfig'][0]['allowBlank']=true;
		});
		explicitForm['targetSearchPanel']=win['selectGridPanel'];
		explicitForm.setButtonHandler(explicitSearchPayment);
		explicitForm.customization(obj['cusExplicitForm']);
		//var explicitExtForm=explicitForm.render();
		forms.push(explicitForm.render());*/
		var searchForm=new Ecp.PaymentMessageSearchForm();
		searchForm.handlerWidgetConfig(function(form){
			form['defaultFormConfig']['title']=TXT.payment_search;
		});
		searchForm['targetSearchPanel']=win['selectGridPanel'];
		searchForm.setButtonHandler(searchPayment);
		searchForm.customization(obj['searchForm']);
		//var searchExtForm=searchForm.render();
		forms.push(searchForm.render());
		/*var searchKeyWordsForm=new Ecp.PaymentMessageKeyWordsSearchForm();
		searchKeyWordsForm.handlerWidgetConfig(function(form){
			form['defaultFormConfig']['title']=TXT.payment_keywords;
		});
		searchKeyWordsForm['targetSearchPanel']=win['selectGridPanel'];
		searchKeyWordsForm.setButtonHandler(keyWordsSearchPayment);
		searchKeyWordsForm.customization(obj['cusKeyWordsForm']);
		//var searchExtKeyWordsForm=searchKeyWordsForm.render();
		//var forms=[explicitExtForm,searchExtForm,searchKeyWordsForm];
		forms.push(searchKeyWordsForm.render());*/
		var mutilParam={
			searchForms:forms,
			cusMutilSearchPanel:obj['cusMutilSearchPanel']
		};
		win.handleWidgetConfig(function(obj){
			obj['defaultConfig']['height']=500;
		});
		win.initMutilSearchPanel(mutilParam);
		win.initSelectGridPanel({
			cusSelectGridPanel:obj['cusSelectGridPanel'],
			bbarButtons:undefined,
			selecteGridEventConfig:{
				grid:{
					dblclick:showPaymentInRceiveProc
				}
			}
		});
		win.eventConfig={
			beforeshow:function(){
				//explicitForm.form.reset();
				searchForm.form.reset();
				//searchKeyWordsForm.form.reset();
				win.resetInCaseDetail();
			}
		};
		win.setConfirmButtonHandler(obj['handlerFunction']);
		//win.addObservers(observers);
		win.customization(obj['cusSearchWin']);
		win.render();
		Ecp.components['selectWinForAssignXml']=win;
	}else{
		Ecp.components['selectWinForAssignXml'].window.window.buttons[0].handler=obj['handlerFunction'];
	}
	return Ecp.components['selectWinForAssignXml'];
}

Ecp.LinkCaseAndPaymenForm=function(){
	this.form=new Ecp.FormPanel();
	
	this.currencyStore=new Ecp.CurrencyStore();
	
	this.defaultConfig={
		labelAlign :'left',
		region :'north',
		height :200,
		labelWidth :110,
		frame :true
	};
	
	this.defaultItemsConfig=[ {
					layout :'column',
					items : [
							{
								columnWidth :.33,
								layout :'form',
								defaultType :'textfield',
								defaults : {
									anchor :'92%'
								},
								items : [{
											xtype:'hidden',
						  					id: 'realCaseAssignee',
						  					name: 'realCaseAssignee'
										},
										{
											fieldLabel :TXT.case_internal_code,
											id :'internalCode',
											name :'internalCode'
										},
										{
											fieldLabel :TXT.case_reference_num,
											id :'referenceNum',
											name :'referenceNum'
										},
										{
											xtype:'numberfield',
											id :'amountFrom',
											name :'amountFrom',
											fieldLabel :TXT.case_amountFrom,
											allowDecimals :true,
											decimalPrecision :2,
											validateOnBlur:false,
											validationEvent:false
										},
										{
											xtype :'datefield',
											fieldLabel :TXT.case_valueFrom,
											format :'Y-m-d',
											id :'valueDateFrom',
											name :'valueDateFrom',
											editable :false,
											validateOnBlur:false,
											validationEvent:false
										},
										{
											xtype :'datefield',
											fieldLabel :TXT.case_dateFrom,
											format :'Y-m-d',
											id :'createTimeFrom',
											name :'createTimeFrom',
											editable :false,
											validateOnBlur:false,
											validationEvent:false
										}
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
											selectOnFocus :true
										},*/ ]
							},
							{
								columnWidth :.33,
								layout :'form',
								defaultType :'textfield',
								defaults : {
									anchor :'92%'
								},
								items : [
										{
											fieldLabel :TXT.case_num,
											id :'caseId',
											name :'caseId'
										},
										{
											fieldLabel :TXT.case_currency,
											id :'currency',
											name :'currency'
										}
										/*{
											xtype :'combo',
											fieldLabel :TXT.case_currency,
											id :'currency',
											name :'currency',
											value :'',
											forceSelection :false,
											displayField :'currencyCode',
											valueField :'currencyCode',
											typeAhead :true,
											maxLength :3,
											mode :'local',
											editable :true,
											listeners : {
												blur : function(hidden) {
													var rowValue = hidden
															.getRawValue();
													var value = hidden
															.getValue();
													if (rowValue == TXT.common_all)
														this.setValue('');
													else if (rowValue == value)
														this.setValue(value);
													else
														this.setValue(rowValue);
												}
											},
											triggerAction :'all',
											selectOnFocus :true
										}*/, {
											xtype:'numberfield',
											id :'amountTo',
											name :'amountTo',
											fieldLabel :TXT.case_amountTo,
											allowDecimals :true,
											decimalPrecision :2,
											validateOnBlur:false,
											validationEvent:false
										}, {
											xtype :'datefield',
											fieldLabel :TXT.case_valueTo,
											format :'Y-m-d',
											id :'valueDateTo',
											name :'valueDateTo',
											editable :false,
											validateOnBlur:false,
											validationEvent:false
										}, {
											xtype :'datefield',
											fieldLabel :TXT.case_dateTo,
											format :'Y-m-d',
											id :'createTimeTo',
											name :'createTimeTo',
											editable :false,
											validateOnBlur:false,
											validationEvent:false
										} ]
							},
							{
								columnWidth :.33,
								layout :'form',
								defaultType :'textfield',
								defaults : {
									anchor :'92%'
								},
								items : [
										{
											fieldLabel :TXT.case_creator,
											id :'creatorBic',
											name :'creatorBic'
										},
										{
											fieldLabel :TXT.case_type,
											id :'type',
											name :'type'
										}, {
											fieldLabel :TXT.case_magInst,
											id :'magInstCode',
											name :'magInstCode',
											maxLength :4,
											minLength :4
										},{		
											xtype:'trigger',
											fieldLabel :TXT.case_assignee,
											id :'caseAssignee',
											name :'caseAssignee',
											triggerClass:'x-form-search-trigger',
											//allowBlank:false,
											onTriggerClick:function(){
												Ecp.components.caseSearchFormInPay=this.relatedForm;
												showInstitutionTreeWin(callBackForAssigneeInPay);
											},
											editable:false,
											relatedForm:this.form
										}]
							} ]
				} ];
		
	this.defaultButtonsConfig=[
	{
		text:TXT.common_search,
		scope:this
	},{
		text :TXT.common_reset,
		handler:function(){
			this.form.reset()
		},
		scope:this
	}];
	
	this.customConfig=null;
	
	this.eventConfig=null;
	
	this.targetSearchGrid=null;
}

Ecp.LinkCaseAndPaymenForm.prototype.getForm=function(){
	return this.form;
}

Ecp.LinkCaseAndPaymenForm.prototype.initCurrencyStore=function(){
	this.currencyStore.handleWidgetConfig(function(serviceStore){
		serviceStore['defaultStoreConfig']['fields']=[
			{
				name: 'currencyCode'
			}
		];
		serviceStore['defaultStoreConfig']['baseParams']={
			cmd:'cur',
			action:'findAll'
		};
	});
	this.currencyStore.render();
	this.currencyStore.load();
	return this.currencyStore.dataStore.store;
}

Ecp.LinkCaseAndPaymenForm.prototype.setTargetSearchGrid=function(grid){
	this.targetSearchGrid=grid;
}

Ecp.LinkCaseAndPaymenForm.prototype.setButtonHanlder=function(handler){
	this.defaultButtonsConfig[0]['handler']=handler;
}
//dateTo
Ecp.LinkCaseAndPaymenForm.prototype.dateFromValidator=function(value){
	var dateToValue=this['ecpOwner'].form.findById(this['associatedField']);
	this.clearInvalid();
	if(value==''&&dateToValue!='')
		return TXT.common_from_to_error;
	if(value!=''&&dateToValue!=''&&this.getValue().getTime()>this['ecpOwner'].form.findFieldById(this['associatedField']).getValue().getTime())
		return TXT.common_date_error;
	return true;
}
//date from
Ecp.LinkCaseAndPaymenForm.prototype.dateToValidator=function(value){
	var dateFromValue=this['ecpOwner'].form.findById(this['associatedField']);
	this.clearInvalid();
	if(value==''&&dateFromValue!='')
		return TXT.common_from_to_error;
	return true;
}	

Ecp.LinkCaseAndPaymenForm.prototype.amountMinValidator=function(value){
	var amountMax=this['ecpOwner'].form.findById('amountTo');
	this.clearInvalid();
	if(value==''&&amountMax!='')
		return TXT.common_from_to_error;
	//alert(this.getValue() > amountMax);
	if(value != "" && amountMax != ""&& this.getValue() > amountMax)
		return TXT.common_amount_error;
	return true;
}	

Ecp.LinkCaseAndPaymenForm.prototype.amountMaxValidator=function(value){
	var amountMin=this['ecpOwner'].form.findById('amountFrom');
	this.clearInvalid();
	if(amountMin!=''&&value==''){
		return TXT.common_from_to_error;
	}
	return true;
}


Ecp.LinkCaseAndPaymenForm.prototype.setValidator=function(){
	this.defaultItemsConfig[0]['items'][0]['items'][3]['validator']=this['dateFromValidator'];
	this.defaultItemsConfig[0]['items'][0]['items'][3]['ecpOwner']=this;
	this.defaultItemsConfig[0]['items'][0]['items'][3]['associatedField']='valueDateTo';
	this.defaultItemsConfig[0]['items'][1]['items'][3]['validator']=this['dateToValidator'];
	this.defaultItemsConfig[0]['items'][1]['items'][3]['ecpOwner']=this;
	this.defaultItemsConfig[0]['items'][1]['items'][3]['associatedField']='valueDateFrom';
	this.defaultItemsConfig[0]['items'][0]['items'][4]['validator']=this['dateFromValidator'];
	this.defaultItemsConfig[0]['items'][0]['items'][4]['ecpOwner']=this;
	this.defaultItemsConfig[0]['items'][0]['items'][4]['associatedField']='createTimeTo';
	this.defaultItemsConfig[0]['items'][1]['items'][4]['validator']=this['dateToValidator'];
	this.defaultItemsConfig[0]['items'][1]['items'][4]['ecpOwner']=this;
	this.defaultItemsConfig[0]['items'][1]['items'][4]['associatedField']='createTimeFrom';
	this.defaultItemsConfig[0]['items'][0]['items'][2]['validator']=this['amountMinValidator'];
	this.defaultItemsConfig[0]['items'][0]['items'][2]['ecpOwner']=this;
	this.defaultItemsConfig[0]['items'][1]['items'][2]['validator']=this['amountMaxValidator'];
	this.defaultItemsConfig[0]['items'][1]['items'][2]['ecpOwner']=this;
}

Ecp.LinkCaseAndPaymenForm.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.LinkCaseAndPaymenForm.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.LinkCaseAndPaymenForm.prototype.render=function(){
	this.form.init();
	this.form['config']=this.defaultConfig;
	//var store=this.initCurrencyStore();
	//this.defaultItemsConfig[0]['items'][1]['items'][1]['store']=store;
	this.form['items']=this.defaultItemsConfig;
	this.form['buttons']=this.defaultButtonsConfig;
	this.setValidator();
	this.form.customization(this.customConfig);
	return this.form.render();
}

Ecp.LinkCaseAndPaymenWin=function(){
	this.window=new Ecp.Window();
	
	this.grid=new Ecp.CaseGrid();
	
	this.form=new Ecp.LinkCaseAndPaymenForm();
	
	this.defaultConfig={
		width :850,
		height :550,
		autoScroll :false,
		layout :'border',
		border :false,
		closeAction :'close',
		minimizable :false,
		maximizable :false,
		layoutConfig : {
			animate :false
		},
		resizable :false,
		shadow:false,
		buttonAlign :'center'
	};
	
	this.defaultItemsConfig=[];
	
	this.defaultButtonsConfig=[{
		text:TXT.common_btnOK,
		scope:this
	},{
		text:TXT.common_btnClose,
		handler:function(){
			this.window.window.close();
		},
		scope:this
	}];
		
	this.customConfig=null;
	
	this.eventConfig=null;
	
	this.dataBus=null;
	
	this.observers=[];
}

Ecp.LinkCaseAndPaymenWin.prototype.initForm=function(obj){
	if(obj['handler']!=undefined)
		this.form.handleWidgetConfig(obj['handler']);
	this.form.setButtonHanlder(obj['searchCaseHandler']);
	this.form.setTargetSearchGrid(this.grid);
	this.form.customization(obj['cusFormConfig']);
	this.defaultItemsConfig.push(this.form.render());
}

Ecp.LinkCaseAndPaymenWin.prototype.initCaseGrid=function(obj){
	var id=this.defaultConfig['id'];
	this.grid.handleWidgetConfig(function(grid){
		grid['defaultGridConfig']['id']='linkCaseGridPanel'+id;
		grid['defaultGridConfig']['region']='center';
		grid['defaultGridConfig']['title']=TXT.case_selectRelated;
	});
	this.grid.setWidgetEvent(obj['gridEventConfig']);
	this.grid.customization(obj['cusGridPanel']);
	this.defaultItemsConfig.push(this.grid.render());
}

Ecp.LinkCaseAndPaymenWin.prototype.setButtonHandler=function(handler){
	this.defaultButtonsConfig[0]['handler']=handler;
}

Ecp.LinkCaseAndPaymenWin.prototype.show=function(){
	this.window.window.show();
}

Ecp.LinkCaseAndPaymenWin.prototype.addObservers=function(observers){
	for(var i=0;i<observers.length;i++)
		this.observers.push(observers[i]);
}

Ecp.LinkCaseAndPaymenWin.prototype.notifyAll=function(eventName){
	for(var i=0;i<this.observers.length;i++)
		this.observers[i].update(this,eventName);
}

Ecp.LinkCaseAndPaymenWin.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.LinkCaseAndPaymenWin.prototype.customization=function(obj){
	this.customConfig=obj;
}

Ecp.LinkCaseAndPaymenWin.prototype.render=function(obj){
	this.window.init();
	this.window['config']=this.defaultConfig;
	this.window['items']=this.defaultItemsConfig;
	this.window['buttons']=this.defaultButtonsConfig;
	this.window['listeners']=this.eventConfig;
	this.window.customization(this.customConfig);
	return this.window.render();
}

Ecp.LinkCaseAndPaymenWin.createSingleWin=function(obj){
	var formObj={};
	var winObj={};
	var cusGridObj={};
	var win=new Ecp.LinkCaseAndPaymenWin();
	win.handleWidgetConfig(function(win){
		win['defaultConfig']['id']='linkCaseInMsgWin';
		win['defaultConfig']['modal']=true;
		win['defaultConfig']['title']=TXT.message_linkCase;
		//win['defaultConfig']['closeAction']='hide';
		/*win['defaultButtonsConfig'][1]['handler']=function(){
			this.window.window.hide();
		};*/
	});
	win.initForm({
		cusFormConfig:formObj,
		searchCaseHandler:obj['searchCase']
	});
	win.initCaseGrid({
		gridEventConfig:{
			grid:{
				dblclick:obj['showCaseDetailInPaymentLink']
			}
		},
		cusGridPanel:cusGridObj
	});
	win.eventConfig={
		'beforeshow':function(){
			win.form.form.reset();
			win.grid.dataStore.store.removeAll();
		}
	};
	win.setButtonHandler(obj['linkCaseInMessage']);
	win.addObservers([Ecp.components.messageGrid]);
	win.customization(winObj);
	win.render();
	Ecp.components.linkCaseWinForMessage=win;
	Ecp.components.linkCaseWinForMessage['dataBus']=obj['dataBus'];
	return Ecp.components.linkCaseWinForMessage;
}

function callBackForAssigneeInPay(treeRecord,treeWin){
	if(treeRecord!=null)
	{
		Ecp.components.caseSearchFormInPay.setValue('realCaseAssignee', treeRecord.attributes.internalCode);
		Ecp.components.caseSearchFormInPay.setValue('caseAssignee', treeRecord.attributes.name);
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

Ecp.paymentMessageCLSInfoForm=function()
{
	Ecp.ServiceForm.call(this);
	this.config={
			id:'paymentMessageCLSInfoForm',
			labelAlign :'left',
			region :'south',
			labelWidth :100,
			height :450,
			margins :'4 0 0 0',
			cmargins :'4 0 0 0',
			autoScroll :true,
			frame :true,
			layout :'column'
	};
	this.items=[
				{
					columnWidth :.5,
					layout :'form',
					defaultType :'textfield',
					defaults : {anchor :'95%'},
					items : [
							//add by jinlijun @2013/2/19	start
							{
								fieldLabel :TXT.payment_type,
								id :'type',
								name :'type',
								readOnly :true,
								tabIndex:1
							},
							//add by jinlijun @2013/2/19	end
							{
								fieldLabel :TXT.payment_clrFlag.title,
								id :'clrFlag',
								name :TXT.payment_clrFlag['clrFlag'],
								readOnly :true,
								tabIndex:3
							},{
								fieldLabel :TXT.payment_sysFlag,
								id :'sysFlag',
								name :'sysFlag',
								readOnly :true,
								tabIndex:5
							},{
								fieldLabel :TXT.payment_reconcileType.title,
								id :'reconcileType',
								name :'reconcileType',
								readOnly :true,
								tabIndex:7
							},{
								fieldLabel :TXT.payment_cancFlag.title,
								id :'cancFlag',
								name :'cancFlag',
								readOnly :true,
								tabIndex:9
							},{
								fieldLabel :TXT.payment_settleAmt,
								id :'settleAmt',
								name :'settleAmt',
								readOnly :true,
								tabIndex:11
							},
							/*{
								fieldLabel :TXT.payment_dccType,
								id :'dccType',
								name :'dccType',
								readOnly :true,
								tabIndex:11
							},*/
							{
								fieldLabel :TXT.payment_settleBank,
								id :'settleBank',
								name :'settleBank',
								readOnly :true,
								tabIndex:13
							},{
								fieldLabel :TXT.payment_settleSwiftNo,
								id :'settleSwiftNo',
								name :'settleSwiftNo',
								readOnly :true,
								tabIndex:15
							},{
								fieldLabel :TXT.payment_settleMsgSeq,
								id :'settleMsgSeq',
								name :'settleMsgSeq',
								readOnly :true,
								tabIndex:17
							},{
								fieldLabel :TXT.payment_bankNo,
								id :'bankNo',
								name :'bankNo',
								readOnly :true,
								tabIndex:19
							},{
								fieldLabel :TXT.payment_midInstId,
								id :'midInstId',
								name :'midInstId',
								readOnly :true,
								tabIndex:21
							},{
								fieldLabel :TXT.payment_ackFlag.title,
								id :'ackStatus',
								name :TXT.payment_clrFlag['ackStatus'],
								readOnly :true,
								tabIndex:23
							},{
								fieldLabel :TXT.payment_refundTimes,
								id :'refundTimes',
								name :'refundTimes',
								readOnly :true,
								tabIndex:25
							},{
								fieldLabel :TXT.payment_institutionName,
								id :'institutionName',
								name :'institutionName',
								readOnly :true,
								tabIndex:27
							},{
								fieldLabel :TXT.payment_dealDate,
								id :'dealDate',
								name :'dealDate',
								readOnly :true,
								tabIndex:29
							}]
				},
				{
					columnWidth :.5,
					layout :'form',
					defaultType :'textfield',
					defaults : {
						anchor :'95%'
					},
					items : [
							//add by jinlijun @2013/2/19	start
							{
								fieldLabel :TXT.payment_msgFlag.title,
								id :'msgFlag',
								name :TXT.payment_msgFlag['msgFlag'],
								readOnly :true,
								tabIndex:2
							},
							//add by jinlijun @2013/2/19	end
							{
								fieldLabel :TXT.payment_dccSendFlag,
								id :'dccSendFlag',
								name :'dccSendFlag',
								readOnly :true,
								tabIndex:4
							},{
								fieldLabel :TXT.payment_txStatus,
								id :'txStatus',
								name :'txStatus',
								readOnly :true,
								tabIndex:6
							},{
								fieldLabel :TXT.payment_dealStatus.title,
								id :'dealStatus',
								name :'dealStatus',
								readOnly :true,
								tabIndex:8
							},{
								fieldLabel :TXT.payment_settleFlag.title,
								id :'settleFlag',
								name :'settleFlag',
								readOnly :true,
								tabIndex:10
							},{
								fieldLabel :TXT.payment_settleValueDate,
								id :'settleValueDate',
								name :'settleValueDate',
								readOnly :true,
								tabIndex:12
							},
							/*{
								fieldLabel :TXT.payment_dealType.title,
								id :'dealType',
								name :'dealType',
								readOnly :true,
								tabIndex:12
							},*/
							{
								fieldLabel :TXT.payment_clsAccBk,
								id :'clsAccBk',
								name :'clsAccBk',
								readOnly :true,
								tabIndex:14
							},{
								fieldLabel :TXT.payment_settleMsgDate,
								id :'settleMsgDate',
								name :'settleMsgDate',
								readOnly :true,
								tabIndex:16
							},{
								fieldLabel :TXT.payment_settleSeq,
								id :'settleSeq',
								name :'settleSeq',
								readOnly :true,
								tabIndex:18
							},{
								fieldLabel :TXT.payment_deptNo,
								id :'deptNo',
								name :'deptNo',
								readOnly :true,
								tabIndex:20
							},{
								fieldLabel :TXT.payment_nostroBic,
								id :'nostroBic',
								name :'nostroBic',
								readOnly :true,
								tabIndex:22
							},{
								fieldLabel :TXT.payment_stmtFlag,
								id :'stmtFlag',
								name :'stmtFlag',
								readOnly :true,
								tabIndex:24
							},{
								fieldLabel :TXT.payment_transRef,
								id :'transRef',
								name :'transRef',
								readOnly :true,
								tabIndex:26
							},{
								fieldLabel :TXT.payment_clsId,
								id :'clsId',
								name :'clsId',
								readOnly :true,
								tabIndex:28
							},{
								fieldLabel :TXT.payment_dealUser,
								id :'dealUser',
								name :'dealUser',
								readOnly :true,
								tabIndex:30
							}]
					} 
	];
}
Ecp.extend(Ecp.paymentMessageCLSInfoForm.prototype,new Ecp.ServiceForm());
Ecp.paymentMessageInfoForm=function()
{
	Ecp.ServiceForm.call(this);
	this.config={
			id:'paymentMessageInfoForm',
			labelAlign :'left',
			region :'south',
			labelWidth :95,
			height :450,
			margins :'4 0 0 0',
			cmargins :'4 0 0 0',
			autoScroll :true,
			frame :true,
			layout :'form'
	};
	this.items=[
			{
				layout :'form',
				defaultType :'textfield',
				defaults : {anchor :'95%'},
				items : [
						{
							fieldLabel :TXT.payment_dealStatus.title,
							id :'dealStatus',
							name :'dealStatus',
							readOnly :true
						},{
							fieldLabel :TXT.payment_years,
							id :'years',
							name :'years',
							readOnly :true
						},{
							fieldLabel :TXT.payment_dbCr.title,
							id :'dbCr',
							name :'dbCr',
							readOnly :true
						},{
							fieldLabel :TXT.payment_statementNum,
							id :'statementNum',
							name :'statementNum',
							readOnly :true
						},{
							fieldLabel :TXT.payment_stmtAffixSeq,
							id :'stmtAffixSeq',
							name :'stmtAffixSeq',
							readOnly :true
						},{
							fieldLabel :TXT.payment_sequenceNum,
							id :'sequenceNum',
							name :'sequenceNum',
							readOnly :true
						},{
							fieldLabel :TXT.payment_stmtAffixPage,
							id :'stmtAffixPage',
							name :'stmtAffixPage',
							readOnly :true
						},{
							fieldLabel :TXT.payment_account,
							id :'account',
							name :'account',
							readOnly :true
						},{
							fieldLabel :TXT.payment_entryDate,
							id :'entryDate',
							name :'entryDate',
							readOnly :true
						},{
							fieldLabel :TXT.payment_stmtCode,
							id :'stmtCode',
							name :'stmtCode',
							readOnly :true
						},{
							fieldLabel :TXT.payment_transRef,
							id :'transRef',
							name :'transRef',
							readOnly :true
						}
					]
			}
	];
}
Ecp.extend(Ecp.paymentMessageInfoForm.prototype,new Ecp.ServiceForm());

Ecp.MessageFinContentFormForCLSForm=function(){
	Ecp.ServiceForm.call(this);
	this.config={
		id:'messageFinContentFormForCLSForm',
		labelAlign :'center',
		region :'center',
		labelWidth :80,
		height:450,
		bodyStyle :'padding:10px 10px 10px 10px',
		frame :true,
		defaultType :'textfield',
		defaults : {
			anchor :'95%'
		}
	};
	
	this.items=[
		{
			fieldLabel :TXT.message_assigner,
			id :'sender',
			name :'sender',
			allowBlank :false,
			minLength :8,
			maxLength :11,
			readOnly :true
		},
		{
			fieldLabel :TXT.message_assignee,
			id :'receiver',
			name :'receiver',
			allowBlank :false,
			minLength :8,
			maxLength :11,
			readOnly:true
		},
		{
			fieldLabel :TXT.message_20,
			id :'reference',
			name :'reference',
			allowBlank :false,
			readOnly :true
		},{
			fieldLabel :TXT.message_21,
			id :'relatedReference',
			name :'relatedReference',
			maxLength :16,
			readOnly:true
		},{
			xtype:'hidden',
			id :'type',
			name :'type'
		},{
			fieldLabel :TXT.message_body,
			xtype :'textarea',
			name :'body',
			id :'body',
			height :250,
			readOnly:true
		}
	];
}
Ecp.extend(Ecp.MessageFinContentFormForCLSForm.prototype,new Ecp.ServiceForm());

Ecp.MessageFinContentTransformForm=function(){
	Ecp.ServiceForm.call(this);
	this.config={
		id:'MessageFinContentTransformForm',
		labelAlign :'center',
		region :'center',
		labelWidth :80,
		height:500,
		bodyStyle :'padding:10px 10px 10px 10px',
		frame :true,
		defaultType :'textarea',
		defaults : {
			anchor :'95%'
		}
	};
	
	this.items=[
		{
			fieldLabel :TXT.message_transform_content,
			id :'content',
			name :'content',
			height :150,
			readOnly :true
		},
		{
			fieldLabel :TXT.message_transform_simple,
			id :'simpleChinese',
			name :'simpleChinese',
			height :150,
			readOnly :true
		},
		{
			fieldLabel :TXT.message_transform_tradition,
			id :'traditionalChinese',
			name :'traditionalChinese',
			height :150,
			readOnly :true
		}
	];
}
Ecp.extend(Ecp.MessageFinContentTransformForm.prototype,new Ecp.ServiceForm());
Ecp.MessageFinContentTransWin=function(){
	Ecp.ServiceWindow.call(this);
	this.config={
		 	title:TXT.message_transform,
			width:650,
	        height:550,
	        autoScroll :false,
	        layout:'fit',
	        border:false,
	       	modal:true,
	        minimizable: false,
	        maximizable: false,
			layoutConfig: {animate:false},
			resizable: false,
			//shadow:false,
			buttonAlign: 'center'
	};
	this.buttons=[{
		text:TXT.common_btnClose,
		scope:this,
		handler:function(){
			this.window.window.close();
		}
	}];
}
Ecp.MessageFinContentTransWin.createWindow=function(fun){
	var messageFinContentTransformForm = new Ecp.MessageFinContentTransformForm();
	if(fun && fun['formFun'] && typeof fun['formFun']=='function'){
		messageFinContentTransformForm.handleWidgetConfig(fun['formFun']);
	}
	var messageFinContentTransWin = new Ecp.MessageFinContentTransWin();
	if(fun && fun['winFun'] && typeof fun['winFun']=='function'){
		messageFinContentTransWin.handleWidgetConfig(fun['winFun']);
	}
	messageFinContentTransWin.items=[messageFinContentTransformForm.render()];
	messageFinContentTransWin.render();
	return messageFinContentTransWin.window;
}
Ecp.extend(Ecp.MessageFinContentTransWin.prototype,new Ecp.ServiceWindow());
