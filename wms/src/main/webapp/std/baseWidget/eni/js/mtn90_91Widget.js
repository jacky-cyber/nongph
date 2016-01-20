/* create by sunyue 20121214 */
Ecp.MTn90_91Grid=function(){
	Ecp.ServiceGrid.call(this);
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'messages',
		idProperty:'id',
		fields:[
			 {name :'id'}, 
			 {name :'type'},
			 {name :'messageId'}, 
			 {name :'caseId'}, 
			 {name :'cId'}, 
			 {name :'referenceNum'}, 
			 {name :'relatedReferenceNum'},
			 {name :'messageType'}, 
			 {name :'messageTypeTag'}, 
			 {name :'senderBic'},
			 {name :'sendBic'}, 
			 {name :'receiverBic'}, 
			 {name :'createBy'}, 
			 {name :'createTime'}, 
			 {name :'currency'}, 
			 {name :'amount'}, 
			 {name :'valueDate'}, 
			 {name :'ioFlag'}, 
			 {name :'io'}, 
			 {name :'isRead'}, 
			 {name :'swiftStatus'}, 
			 {name :'institution'}, 
			 {name :'assignee'}, 
			 {name :'internalCode'}, 
			 {name :'deleted'} ,
			 {name :'status' }
			    ]
	};
	
	this.defaultStoreConfig.baseParams={
		cmd:'message',
		action:'findMTn9091'
	};
	this.defaultCmConfig=[
					{						
						header :TXT.message_type,
						dataIndex :'messageType',
						menuDisabled :true,
						width :100,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							if (!record.data["isRead"]) {
								return "<img src='../images/wait.gif' align='absmiddle'/> <span style='font-weight:bold;color:red;'>"
										+ value + "</span>";
							} else
								return value;
						
						}
					}, {
						header :TXT.message_assigner,
						dataIndex :'senderBic',
						menuDisabled :true,
						width :120
					}, {
						header :TXT.message_assignee,
						dataIndex :'receiverBic',
						menuDisabled :true,
						width :120
					}, 
					{
						header :TXT.task_messageId,
						dataIndex :'messageId',
						width :150,
						menuDisabled :true
					}, {
						header :TXT.case_comment_create_time,
						dataIndex :'createTime',
						menuDisabled :true,
						width :150
					}, {
						header :TXT.message_assign,
						dataIndex :'assignee',
						menuDisabled :true,
						width :140
					}
	];
	
	this.defaultGridConfig={
		title:TXT.mtn90_91_title,
		id:'mtn90_91_Grid',
		region :'center',
		stripeRows :true,
		pagination:true
	};
}

Ecp.MTn90_91Grid.prototype.show=function(){
	this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}
Ecp.MTn90_91Grid.prototype.search=function(params){

	if(this.defaultGridConfig['pagination']!=null){
		params['start']=0;
		params['limit']=PAGE_SIZE;
	}
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load(params);
}
Ecp.extend(Ecp.MTn90_91Grid.prototype,new Ecp.ServiceGrid());

Ecp.MTn90_91GridToolbar=function()
{
	Ecp.ServiceToolbar.call(this);
	this.defaultToolBarConfig={
		id:'mtn90_91_Toolbar'
	};
	this.defaultToolBarItemConfig=[totalToolBarItem.mtn90_91Find,totalToolBarItem.mtn90_91Read,totalToolBarItem.mtn90_91AssignCase];
}
Ecp.extend(Ecp.MTn90_91GridToolbar.prototype,new Ecp.ServiceToolbar());

Ecp.MTn90_91MessageSearchForm=function(){
	Ecp.ServiceForm.call(this);
	this.config={
			labelAlign: 'left',
			region: 'center',
		    labelWidth:100,
		    layout:'column',
		    frame:true
	}
	this.items=[
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
								id :'messageTypeName',
								name :'messageTypeName',
								store :new Ext.data.SimpleStore({
									fields:['convertedName','name'],
									data:[[TXT.common_all,''],['MT190','190'],['MT191','191'],['MT290','290'],['MT291','291']]
								}),
								forceSelection :true,
								displayField :'convertedName',
								valueField :'name',
								value :'',
								typeAhead :true,
								mode :'local',
								editable :false,
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
								name :'creatTimeTo',
								editable:false,
								tabIndex:6
							} ]
					} 
	];
}
Ecp.extend(Ecp.MTn90_91MessageSearchForm.prototype,new Ecp.ServiceForm());

Ecp.MTn90_91MessageSearchWin=function(){
	Ecp.ServiceWindow.call(this);
	this.config={
		 	title:TXT.common_search,
			width:650,
	        height:170,
	        autoScroll :false,
	        layout:'fit',
	        border:false,
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
			clickQueryMTn9091MessageBtn(this);
		}
	},{
		text:TXT.common_reset,
		handler:function(){
			messageSearchFormReset(this);
		}
	}];
}
Ecp.MTn90_91MessageSearchWin.createWindow=function(fun){
	var mtn90_91MessageSearchForm = new Ecp.MTn90_91MessageSearchForm();
	if(fun && fun['searchFormFun'] && typeof fun['searchFormFun']=='function'){
		mtn90_91MessageSearchForm.handleWidgetConfig(fun['searchFormFun']);
	}
	var mtn90_91MessageSearchWin = new Ecp.MTn90_91MessageSearchWin();
	if(fun && fun['winFun'] && typeof fun['winFun']=='function'){
		mtn90_91MessageSearchWin.handleWidgetConfig(fun['winFun']);
	}
	mtn90_91MessageSearchWin.items=[mtn90_91MessageSearchForm.render()];
	mtn90_91MessageSearchWin.render();
	return mtn90_91MessageSearchWin.window;
}
Ecp.extend(Ecp.MTn90_91MessageSearchWin.prototype,new Ecp.ServiceWindow());
