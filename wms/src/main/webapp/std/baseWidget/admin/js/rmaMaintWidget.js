//define search form
Ecp.RmaMaintWidgetToolBar=function(){
	Ecp.ServiceToolbar.call(this);
	this.defaultToolBarItemConfig=[
	                               totalToolBarItem.queryRmaMaint,totalToolBarItem.importRmaMaint];
	this.defaultToolBarConfig={
		id:'rmaMaintWidgetToolBar'
	};
};
Ecp.extend(Ecp.RmaMaintWidgetToolBar.prototype,new Ecp.ServiceToolbar());



//define grid
Ecp.RmaSearchGrid = function() { 
	Ecp.ServiceGrid.call(this); 
	this.defaultStoreConfig={
		 url:DISPATCH_SERVLET_URL,
		 root :'bocSecurityExchanges',
		 totalProperty :'totalCount',
		 id :'id',
		 fields:[
			 {name: 'id'},
			 {name: 'fromSwiftAddress'},
			 {name: 'toSwiftAddress'},
			 {name: 'cityName'},
			 {name: 'country'},
			 {name: 'institutionName'},
			 {name: 'securityStatus'},
			 {name: 'authToRcv'},
			 {name: 'authToSnd'},
			 {name: 'rmaStatus'},
			 {name: 'importStatus'}
		 ]
	 };
	this.defaultStoreConfig.baseParams={
		cmd:'rma',
		action:'query'
	};
	this.defaultCmConfig = [{
				header : TXT.RMA_ourBranch_bic,
				dataIndex : 'fromSwiftAddress',
				menuDisabled : true,
				width : 140
			}, {
				header : TXT.RMA_otherBranch_bic,
				dataIndex : 'toSwiftAddress',
				menuDisabled : true,
				width : 140
			}, {
				header : TXT.RMA_other_institutionName,
				dataIndex : 'institutionName',
				menuDisabled : true,
				width : 300
			}
			, {
				header : TXT.RMA_bic_countryName,
				dataIndex : 'country',
				menuDisabled : true,
				width : 140
			}
			, {
				header : TXT.RMA_bic_cityName,
				dataIndex : 'cityName',
				menuDisabled : true,
				width : 140
			}, {
				header : TXT.RMA_rma_status,
				dataIndex : 'securityStatus',
				menuDisabled : true,
				width : 140,
				renderer:function(val){
					if (val=='Valid')
						{
						return TXT.RMA_RMA_ENABLED;
						}
					else if(val=='Invalid'){
						return	'<font color="red">'+TXT.RMA_RMA_UNABLED+'</font>';
						}
					else if(val=='Deleted'){
						return	'<font color="red">'+TXT.RMA_glb_btnDelete+'</font>';
					}	
				}
			},
				{
				header : TXT.RMA_auth_to_rec,
				dataIndex : 'authToRcv',
				menuDisabled : true,
				renderer:function(val){
					if (val=='Enabled')
						{
						return TXT.RMA_has_auth;
						}
						else{
						return '<font color="red">'+TXT.RMA_no_auth+'</font>';
						}},
				width : 120
			},
				{
				header : TXT.RMA_auth_to_send,
				dataIndex : 'authToSnd',
				menuDisabled : true,
				renderer:function(val){
					if (val=='Enabled')
						{
						return TXT.RMA_has_auth;
						}
						else{
						return '<font color="red">'+TXT.RMA_no_auth+'</font>';
						}},
				width : 140
			},{
				header : TXT.RMA_RMC_IS_NEW,
				dataIndex : 'importStatus',
				menuDisabled : true,
				width : 100,
				renderer:function(val){
					var a = val.split("_");
					if (a[1]=="Add"&&a[1]!=null){
							return a[0]+"_"+TXT.RMA_glb_btnAdd;
					}else if(a[1]=="Modifyed"&&a[1]!=null){
						return '<font color="red">'+a[0]+"_"+TXT.RMA_RMA_EDIT+'</font>';
					}else{
						return "";
					}
					
				}
			}];

	this.defaultGridConfig = { 
		id : 'rmaSearchGrid', 
		title:TXT.RMA_Grid_title,
		pagination : true,
		margins : '5 0 0 0',
		stripeRows : true, 
		columnLines:true,
		region : 'center'
	};
 
}
Ecp.RmaSearchGrid.prototype.show=function(){
	this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}
Ecp.RmaSearchGrid.prototype.search=function(params){
	if(this.defaultGridConfig['pagination']!=null)
	{
		params['start']=0;
		params['limit']=PAGE_SIZE;
	}
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load(params);
}
Ecp.extend(Ecp.RmaSearchGrid.prototype, new Ecp.ServiceGrid());
Ecp.RmaSearchForm = function(){
	Ecp.ServiceForm.call(this); 
	this.config={ 
			labelWidth : 65,  
			region: 'center',
			layout:'column',
			frame:true
	};
	var storeRMAStatus=new Ext.data.SimpleStore({
		fields: ['name','value'],
		data: [ 
		       [TXT.all,''],
		       [TXT.message_process_add,'Add'],
		       [TXT.RMA_ENABLED,'Valid'],						 
		       [TXT.RMA_UNABLED,'Invalid'],
		       [TXT.glb_btnDelete,'Deleted'],
		       [TXT.RMA_EDIT,'Modifyed']						
		]
	});
	this.items= [{
			columnWidth : .5,
			layout : 'form',
			defaultType :'textfield',
			defaults : {anchor :'95%'},
			items : [{	
						xtype :'datefield',
						width : 140,
						fieldLabel : TXT.payment_creationDateFrom,
						id : 'fromDate',
						name : 'fromDate',
						format:'Y-m-d',
						editable:false,
						tabIndex:1
					},{width : 140,
						fieldLabel : TXT.RMA_ourBranch_bic,
						id : 'fromSwiftAddress',
						name : 'fromSwiftAddress',
						tabIndex:3
					},{	width : 140,
						fieldLabel : TXT.RMA_bic_institutionName,
						id : 'institutionName',
						name : 'institutionName',
						tabIndex:5
					},{ width : 140,
						fieldLabel : TXT.RMA_bic_cityName,
						id : 'cityName',
						name : 'cityName',
						tabIndex:7
					}]
		},{
			columnWidth : .5,
			layout : 'form',
			defaultType :'textfield',
			defaults : {anchor :'95%'},
			items : [{
						xtype :'datefield',
						width : 140,
						fieldLabel : TXT.RMA_to_date,
						id : 'toDate',
						name : 'toDate',
						format:'Y-m-d',
						editable:false,
						tabIndex:2
					},{ width : 140,
						fieldLabel : TXT.RMA_otherBranch_bic,
						id : 'toSwiftAddress',
						name : 'toSwiftAddress',
						tabIndex:4
					},{ width : 140,
						fieldLabel : TXT.RMA_bic_countryName,
						id : 'country',
						name : 'country',
						tabIndex:6
					},{ 
						width : 140,
						xtype:'combo', 
						store: storeRMAStatus, 
						forceSelection:true,
						displayField:'name',
						valueField: 'value',
						fieldLabel : TXT.RMA_rma_status,
						id : 'rmaStatus',
						name : 'rmaStatus',
						typeAhead: true,
						mode: 'local',  
						triggerAction: 'all',
						selectOnFocus: true,
						editable:false,
						tabIndex:8
					}]
			}];
	 
}

Ecp.extend(Ecp.RmaSearchForm.prototype, new Ecp.ServiceForm());
Ecp.RmaSearchSearchWin=function(){
	Ecp.ServiceWindow.call(this);
	this.config={
		 	title:TXT.common_search,
			width:530,
	        height:200,
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
		text: TXT.common_btnQuery,
		handler: function(){
			clickRmaSearchSearchBtn(this);
		}
	},{
		text:TXT.common_reset,
		handler:function(){
			resetForm(this);
		}
	}];
}
Ecp.RmaSearchSearchWin.createWindow=function(fun){
	var rmaSearchForm = new Ecp.RmaSearchForm();
	if(fun && fun['searchFormFun'] && typeof fun['searchFormFun']=='function'){
		rmaSearchForm.handleWidgetConfig(fun['searchFormFun']);
	}
	var rmaSearchSearchWin = new Ecp.RmaSearchSearchWin();
	if(fun && fun['searchWinFun'] && typeof fun['searchWinFun']=='function'){
		rmaSearchSearchWin.handleWidgetConfig(fun['searchWinFun']);
	}
	rmaSearchSearchWin.items=[rmaSearchForm.render()];
	rmaSearchSearchWin.render();
	return rmaSearchSearchWin.window;
}
Ecp.extend(Ecp.RmaSearchSearchWin.prototype,new Ecp.ServiceWindow());

//导入的form
Ecp.SecurityExchangeImportForm=function(){
	Ecp.ServiceForm.call(this); 
	delete this.config.defaultType;
	this.delimitedManner = null;
	this.config={ 
			height : 400,
			labelWidth : 100,  
			region : 'north',
			padding:20,
			fileUpload:true,
			frame:true
	};
	var fileupload=new Ext.ux.form.FileUploadField({
		buttonText:TXT.common_select_file,
        width: 200
    });
	this.items=[{
				  	xtype:'hidden',
				  	id: 'internalCode',
				  	name: 'internalCode'
				},{
					xtype: 'fileuploadfield',
                    id: 'rma-file',
                    width:150,
                    emptyText: TXT.common_please_select,
                    fieldLabel: TXT.common_please_select,
                    name: 'photo-path',
                    buttonText: TXT.common_please_select
				},{
    				xtype:'trigger', 
    				fieldLabel: TXT.user_branch,
    				width:150,
    				id: 'institution',
    				name: 'institution',
    				triggerClass:'x-form-search-trigger',
    				allowBlank:false,
    				onTriggerClick:function(){
    					showInstitutionTreeWin(callBack);
    				},
    				editable:false
    			}];
 }
Ecp.extend(Ecp.SecurityExchangeImportForm.prototype, new Ecp.ServiceForm());
Ecp.SecurityExchangeImportWindow=function(){
	Ecp.ServiceWindow.call(this); 
	this.config={
	        width:400,
	        height:400,
	        autoScroll :false,
	        layout:'fit',
	        border:false,
	        minimizable: false,
	        maximizable: false,
	        resizable: false,
	        modal:true,
	        title:TXT.common_import,
			layoutConfig: {animate:false},
			buttonAlign: 'center'
	};
	this.buttons=[{
		text: TXT.common_btnOK,
		handler: function(){
			clickRmaImportBtn(this);
		}
	},{
		text:TXT.common_reset,
		handler:function(){
			resetImportForm(this);
		}
	}];
}
Ecp.SecurityExchangeImportWindow.createWindow=function(fun){
	var securityExchangeImportForm = new Ecp.SecurityExchangeImportForm();
	if(fun && fun['importFormFun'] && typeof fun['importFormFun']=='function'){
		securityExchangeImportForm.handleWidgetConfig(fun['importFormFun']);
	}
	var securityExchangeImportWindow = new Ecp.SecurityExchangeImportWindow();
	if(fun && fun['importWinFun'] && typeof fun['importWinFun']=='function'){
		securityExchangeImportWindow.handleWidgetConfig(fun['importWinFun']);
	}
	securityExchangeImportWindow.items=[securityExchangeImportForm.render()];
	securityExchangeImportWindow.render();
	return securityExchangeImportWindow.window;
}
Ecp.extend(Ecp.SecurityExchangeImportWindow.prototype,new Ecp.ServiceWindow());
function callBack(treeRecord,treeWin){
	if(treeRecord!=null){
		Ecp.components.securityExchangeImportForm.form.setValue('internalCode', treeRecord.attributes.internalCode);
		Ecp.components.securityExchangeImportForm.form.setValue('institution', treeRecord.attributes.name);
		treeWin.window.hide();
	}else{
		if(uid!='')
		{
		  treeWin.window.hide();
		  return;
		}
		Ext.MessageBox.alert(TXT.common_hint,TXT.case_institutionNeed);
		return false;
	}
}