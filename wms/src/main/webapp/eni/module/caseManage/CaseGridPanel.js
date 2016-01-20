//org caseGridPanel
com.felix.eni.module.caseManage.CaseGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.GridPanel );
	
	this.search = function(baseParam){
		var extStore = this.getDataStore().getExtStore();
		extStore['baseParams']=baseParam;
		if( this.getExtGridPanel()['pagination']!=undefined )
			extStore.load( {params:{ start:0, limit:com.felix.nsf.GlobalConstants.PAGE_SIZE } } );
		else{
			extStore.removeAll();
			extStore.load();
		}
	}
	
	this.loadLocalData=function(data){
		this.getDataStore().getExtStore().loadData(data);
	}
	
	this.removeAllRecord=function(){
		this.getDataStore().getExtStore().removeAll();
	}

	this.getDataStore().setConfig({
		url: com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL,
		root :'cases',
		totalProperty :'totalCount',
		id :'id',
		fields : [ { name :'caseId' }, 
				   { name :'creatorBic' }, 
				   { name :'sendBic' }, 
				   { name :'receiveBic' }, 
				   { name :'createTime' }, 
				   { name :'ioFlag'	}, 
				   { name :'isRead' }, 
				   { name :'owner' }, 
				   { name :'assignee' }, 
				   { name :'typeName' }, 
				   { name :'status' }, 
				   { name :'remittance' }, 
				   { name :'internalCode' },
				   { name :'currency' }, 
				   { name :'amount'	},
				   { name :'valueDate' }, 
				   { name :'closeTime' }, 
				   { name :'spendTime' }, 
				   { name :'referenceNum' }, 
				   { name :'createBy' }, 
				   { name :'relatedReferenceNum' }, 
				   { name :'statementNum' }, 
				   { name :'account' }, 
				   { name :'careFlag' }, 
				   { name :'paymentNum' }, 
				   { name :'paymentId'}]
		});
		
	this.setColumnModelConfig( [
			{ header :'', dataIndex :'ioFlag', width :30, align :'left', menuDisabled :true, renderer : function(value) {
								if (value == 'I')
									return "<img src='../images/in.png' align='absmiddle'/>";
								else
									return "<img src='../images/out.png' align='absmiddle'/>";
							} },
			{ header :TXT.case_internal_code, dataIndex :'internalCode', width :160, align :'left',	menuDisabled :true, renderer : function(value, cellmeta, record, rowIndex,
									columnIndex, store) {
								if (record.data["careFlag"]) {
									return "<img src='../images/wait.gif' align='absmiddle'/> <span style='font-weight:bold;color:green;'>"
											+ value + "</span>";
								}else if (!record.data["isRead"]) {
									return "<img src='../images/wait.gif' align='absmiddle'/> <span style='font-weight:bold;color:red;'>"
											+ value + "</span>";
								} else
									return value;
							} },
			{ header :TXT.case_assignee, dataIndex :'assignee', menuDisabled :true,	width :130 },
			{ header :TXT.case_type,     dataIndex :'typeName', menuDisabled :true,	width :80, align :'left' },
			{ header :TXT.case_status,	 dataIndex :'status',	menuDisabled :true,	width :80, renderer : function(value) {
								if (value == 'O') {
									return TXT.case_open;
								} else if (value == 'C') {
									return TXT.case_close;
								} else if (value == 'R') {
									return TXT.case_reopen;
								}
							} },
			{ header :TXT.case_num,	                   dataIndex :'caseId',             menuDisabled :true, width :150	},
			{ header :TXT.case_reference_num20,	       dataIndex :'referenceNum',       menuDisabled :true, width :120, align :'left' },
			{ header :TXT.case_related_reference_num21,dataIndex :'relatedReferenceNum',menuDisabled :true, width :120, align :'left'	},
			{ header :TXT.case_currency,               dataIndex :'currency',    	    menuDisabled :true,	width :80,  align :'center' },
			{ header :TXT.case_amount,                 dataIndex :'amount', 			menuDisabled :true,	width :100, align :'right'	},
			{ header :TXT.case_value_date,  		   dataIndex :'valueDate',			menuDisabled :true,	width :100,	align :'left'	},
			{ header :TXT.case_creatorBic,			   dataIndex :'creatorBic',			menuDisabled :true,	width :120,	align :'left'	},
			{ header :TXT.case_inwardOutward,		   dataIndex :'remittance',			menuDisabled :true,	width :80,	renderer : function(value) {
								if (value == 'I') {
									return TXT.case_inward;
								} else if (value == 'O') {
									return TXT.case_outward;
								} else if (value == 'N') {
									return TXT.case_unDefine;
								}
							} },
			{ header :TXT.case_create_time,			   dataIndex :'createTime',			menuDisabled :true,	width :150 },
			{ header :TXT.case_close_time,  		   dataIndex :'closeTime',			menuDisabled :true,	width :150 },
			{ header :TXT.case_spend_time, 			   dataIndex :'spendTime',			menuDisabled :true,	width :100, align :'center', renderer : function(value) {
																							if (value < 60 && value > 0) 
																								return value + TXT.case_spend_time_min;
																							else if (value >= 60 && value < 24 * 60) 
																								return (value / 60).toFixed(0)+ TXT.case_spend_time_hou;
																							else if (value >= 24 * 60) 
																								return (value / (60 * 24)).toFixed(0)+ TXT.case_spend_time_day;
																							else
																								return '';
																						} },
	
			{ header :TXT.case_owner,     dataIndex :'owner',   menuDisabled :true, width :120 },
			{ header :TXT.case_create_by, dataIndex :'createBy',menuDisabled :true,	width :80, renderer : function(value) {
																								if (value == '') {
																									return TXT.case_system;
																								} else {
																									return value;
																								}
																							} }
		]);
					
	this.setGridConfig({
		title:TXT.eiCase,
		id:'eiCaseGrid',
		loadMask:true,
		pagination:true
	});
	
	this.setSelectModel( 0 );				
}