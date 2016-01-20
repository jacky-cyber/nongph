/* change by sunyue 20121212 */
Ecp.MT499_799Grid=function(){
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
		action:'findMT499799'
	};
	
	this.defaultCmConfig=[
					{
						header :TXT.message_type,
						dataIndex :'messageType',
						menuDisabled :true,
						width :120,
						renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
							if (!record.data["deleted"]) {
								return "<img src='../images/exsit.png' align='absmiddle'/>"
										+ value + "</span>";
							} else
							return "<img src='../images/delete.png' align='absmiddle'/>"
										+ value + "</span>";
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
					},{
						header :TXT.case_comment_create_time,
						dataIndex :'createTime',
						menuDisabled :true,
						width :150
					},{
						header :TXT.case_status,
						dataIndex :'deleted',
						menuDisabled :true,
						width :150,
						renderer : function(value) {
							var txt = "";
							if (value){
								txt=TXT.mt499_799_abolish;
							}else{
								txt=TXT.mt499_799_exist;
							}
							return txt;
						}
					}
	];
	
	this.defaultGridConfig={
		title:TXT.mt499_799_title,
		id:'mt499_799Grid',
		region :'center',
		stripeRows :true,
		pagination:true
	};
	this.defaultSelModel=2;
}

Ecp.MT499_799Grid.prototype.show=function(){
	this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}
Ecp.MT499_799Grid.prototype.search=function(params){

	if(this.defaultGridConfig['pagination']!=null){
		params['start']=0;
		params['limit']=PAGE_SIZE;
	}
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load(params);
	//this.doSort();
}
Ecp.extend(Ecp.MT499_799Grid.prototype,new Ecp.ServiceGrid());

Ecp.MT499_799GridToolbar=function()
{
	Ecp.ServiceToolbar.call(this);
	this.defaultToolBarConfig={
		id:'MT499_799GridToolbar'
	};
	this.defaultToolBarItemConfig=[totalToolBarItem.mt499_799Find,totalToolBarItem.mt499_799Abolish];
}
Ecp.extend(Ecp.MT499_799GridToolbar.prototype,new Ecp.ServiceToolbar());

Ecp.MT499799MessageSearchForm=function(){
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
									data:[[TXT.common_all,''],['MT499','499'],['MT799','799']]
								}),
								forceSelection :true,
								displayField :'convertedName',
								valueField :'name',
								typeAhead :true,
								mode :'local',
								value:'',
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
Ecp.extend(Ecp.MT499799MessageSearchForm.prototype,new Ecp.ServiceForm());

Ecp.MT499799MessageFinSearchWin=function(){
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
			clickQueryMT499799MessageBtn(this);
		}
	},{
		text:TXT.common_reset,
		handler:function(){
			messageSearchFormReset(this);
		}
	}];
}
Ecp.MT499799MessageFinSearchWin.createWindow=function(fun){
	var mt499799MessageSearchForm = new Ecp.MT499799MessageSearchForm();
	if(fun && fun['searchFormFun'] && typeof fun['searchFormFun']=='function'){
		mt499799MessageSearchForm.handleWidgetConfig(fun['searchFormFun']);
	}
	var mt499799SearchWin = new Ecp.MT499799MessageFinSearchWin();
	if(fun && fun['winFun'] && typeof fun['winFun']=='function'){
		mt499799SearchWin.handleWidgetConfig(fun['winFun']);
	}
	mt499799SearchWin.items=[mt499799MessageSearchForm.render()];
	mt499799SearchWin.render();
	return mt499799SearchWin.window;
}
Ecp.extend(Ecp.MT499799MessageFinSearchWin.prototype,new Ecp.ServiceWindow());
