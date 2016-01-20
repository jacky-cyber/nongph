com.felix.eni.module.message.MessageGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	
	this.getDataStore().setConfig( { url:com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL,
						   root: 'messages',
						   totalProperty: 'totalCount',
						   idProperty: 'id',
						   fields: [ { name :'id' }, 
						   			 { name :'type' }, 
						   			 { name :'messageId' }, 
						   			 { name :'caseId' }, 
						   			 { name :'cId' }, 
						   			 { name :'referenceNum' }, 
						   			 { name :'relatedReferenceNum' }, 
						   			 { name :'messageType' }, 
						   			 { name :'messageTypeTag' }, 
						   			 { name :'senderBic' },
						   			 { name :'sendBic' }, 
						   			 { name :'receiverBic' }, 
						   			 { name :'createBy' }, 
						   			 { name :'createTime' }, 
						   			 { name :'currency' }, 
						   			 { name :'amount' }, 
						   			 { name :'valueDate' }, 
						   			 { name :'ioFlag' }, 
						   			 { name :'io' }, 
						   			 { name :'isRead' }, 
						   			 { name :'swiftStatus' }, 
						   			 { name :'institution' }, 
						   			 { name :'assignee' }, 
						   			 { name :'internalCode' }, 
						   			 { name :'deleted' } ,
						   			 { name :'status' },
						   			 { name :'careFlag' },
						   			 { name :'careBy' },
						   			 { name :'careTime' },
						   			 { name :'noticeFlag' },
						   			 { name :'noticeTimes' },
						   			 { name :'caseReferenceNum' },
						   			 { name :'caseStatus' },
						   			 { name :'stmtmiscNum' }
						   			 ],
						   	baseParams: { cmd:'message', action:'find'}
						   	} );

	this.search = function( params ) {
		if( this.getGridConfig()['pagination']!=null ) {
			params['start']=0;
			params['limit']=PAGE_SIZE;
		}
		dataStore.getExtStore()['baseParams'] = params;
		dataStore.getExtStore().load(params);
	}

	this.searchDeletedMsg = function(params){
		if(this.grid['pagination']!=null)
		{
			params['start']=0;
			params['limit']=PAGE_SIZE;
		}
		this.getDataStore().getExtStore()['baseParams']=params;
		this.getDataStore().getExtStore().load(params);
	}

	this.searchOutcomingMsg = function(params){
		if(this.grid['pagination']!=null){
			params['start']=0;
			params['limit']=PAGE_SIZE;
		}
		this.getDataStore().getExtStore()['baseParams'] = {cmd:'message', action:'findNewOutcomingMsgFromIBP' };
		this.getDataStore().getExtStore().load( { params:params} );
	}
	
	this.loadLocalData = function(data){
		this.getDataStore().getExtStore().loadData(data);
	}
	
	this.clearSelections = function(){
		this.getExtGridPanel().selModel.clearSelections(true); 
	}
	
	this.findNewIncomingMessage = function() {
		this.getDataStore().getExtStore().baseParams.action='findNewIncomingMessage';
		this.load();
	}
	
	this.setColumnModelConfig( [ { header:'', dataIndex :'ioFlag',     width :25, menuDisabled :true, renderer : function(value) {
																							if (value == 'I')
																								return "<img src='../images/in.png' align='absmiddle'/>";
																							else
																								return "<img src='../images/out.png' align='absmiddle'/>";
																						  } },
							 { header:'', dataIndex :'swiftStatus',width :50, menuDisabled :true, renderer : function(value) {
																							if (value == 'NAK')
																								return "<span style='color:red;'>" + value + "</span>";
																							else if(value=='S')
																								return "<span style='color:green;'>" + value + "</span>";
																							else if(value=='F')
																								return "<span style='color:red;'>" + value + "</span>";
																							else
																								return "<span style='color:green;'>" + value + "</span>";
																						   } },
							 { header:TXT.task_messageType, dataIndex :'type', width :80, menuDisabled :true, renderer : function(value, cellmeta, record, rowIndex, columnIndex, store) {
																							var valueMap = {};
																							valueMap['TMP'] = TXT.message_type_tmp;
																							valueMap['MT']  = TXT.message_type_mt;
																							valueMap['MX']  = TXT.message_type_mx;
																							valueMap['RPL'] = TXT.message_type_rpl;
																							if( record.data["careFlag"] ) {
																								return "<img src='../images/wait.gif' align='absmiddle'/> <span style='font-weight:bold;color:green;'>"
																										+ valueMap[value] + "</span>";
																							} else if (!record.data["isRead"]) {
																								return "<img src='../images/wait.gif' align='absmiddle'/> <span style='font-weight:bold;color:red;'>"
																										+ valueMap[value] + "</span>";
																							} else
																								return valueMap[value];
																						} },
							{ header :TXT.message_name,	        dataIndex :'messageType', menuDisabled :true, width :100 },
							{ header :TXT.message_assigner,     dataIndex :'senderBic',   menuDisabled :true, width :120 }, 
							{ header :TXT.message_assignee,	    dataIndex :'receiverBic', menuDisabled :true, width :120 },
							{ header :TXT.task_messageId,       dataIndex :'messageId',   menuDisabled :true, width :150 }, 
							{ header :TXT.message_internalCode,	dataIndex :'internalCode',menuDisabled :true, width :150 }, 
							{ header :TXT.case_num,				dataIndex :'caseId',      menuDisabled :true, width :150 },
							{ header :TXT.case_create_by,		dataIndex :'createBy',    menuDisabled :true, width :80, renderer : function(value) {
																															if (value == '')
																																return TXT.case_system;
																															else
																																return value;
																														} }, 
							{ header :TXT.message_createTime,	dataIndex :'createTime',  menuDisabled :true, width :150 }, 
							{ header :TXT.message_assign,		dataIndex :'assignee',    menuDisabled :true, width :140 }, 
							{ header :TXT.message_institution,	dataIndex :'institution', menuDisabled :true, width :150 },
							{ header :TXT.message_noticeFlag.title,	dataIndex :'noticeFlag', menuDisabled :true, width :100, renderer : function(value) {
																													var valueText = '_'+value;
																													return TXT.message_noticeFlag[valueText];
																												} }, 
							{ header :TXT.message_noticeTimes,	dataIndex :'noticeTimes', menuDisabled :true, width :80	}
							] );
	
	this.setGridConfig( { title:TXT.message,
						  id:'msgGridId',
						  loadMask:true,
						  pagination:true } );
	
	this.setSelectModel( 0 );
}