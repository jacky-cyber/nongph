/******************   Ecp.CorrespondentGrid    ************************/
//modify by sunyue 20130123
Ecp.CorrespondentGrid=function(){
	Ecp.ServiceGrid.call(this);
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root: 'correspondents',
		totalProperty: 'totalCount',
		id: 'id',
		fields:[
			{name: 'id'},
		    {name: 'bankName'},
		    {name: 'bic'},
		    {name: 'tel'},
		    {name: 'bicCurrency'},
		    {name: 'institution'},
		    {name: 'internalCode'},
		    {name: 'description'},
		    {name: 'address'},
		    {name: 'fileImageFalg'},
		    {name: 'contactMan'},
		    {name: 'isDeleted'}
		]
	};
	
	this.defaultStoreConfig.baseParams={
		cmd:'cor',
		action:'findAll'
	};
	
	this.defaultCmConfig=[
		{header:TXT.bic_code,dataIndex: 'bic',menuDisabled: true,width:150},
		{header:TXT.bic_name,dataIndex: 'bankName',menuDisabled: true,width:150},
		{header:TXT.case_currency,dataIndex:'bicCurrency',menuDisabled: true,width:100},
		{header:TXT.user_branch,dataIndex:'institution',menuDisabled: true,width:180},
		{header:TXT.correspondent_tel,dataIndex:'tel',menuDisabled: true,width:180},
		{header:TXT.correspondent_description,dataIndex:'description',menuDisabled: true,width:200},
		{header:TXT.correspondent_address,dataIndex:'address',menuDisabled: true,width:200},
		{header:TXT.correspondent_contactMan,dataIndex:'contactMan',menuDisabled: true,width:100},
		{header:TXT.correspondent_status,dataIndex:'isDeleted',menuDisabled: true,width:100,
				renderer : function(value) {
					if (value)
						return TXT.correspondent_isDelete;
					else
						return TXT.correspondent_isUse;
				}
		}
	];
	
	this.defaultGridConfig={
		title:TXT.correspondent_title,
		id:'CorrespondentGridId',
		pagination:true
	};
}

Ecp.CorrespondentGrid.prototype.show=function(){
	this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}
Ecp.CorrespondentGrid.prototype.search=function(params){
	if(this.defaultGridConfig['pagination']!=null)
	{
		params['start']=0;
		params['limit']=PAGE_SIZE;
	}
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load(params);
}
Ecp.extend(Ecp.CorrespondentGrid.prototype,new Ecp.ServiceGrid());
// currency toolbar
Ecp.CorrespondentToolBar=function(){
	Ecp.ServiceToolbar.call(this);
	this.defaultToolBarItemConfig=[totalToolBarItem.queryCorrespondent,
								   totalToolBarItem.addCorrespondent,
	                               totalToolBarItem.editCorrespondent,
	                               totalToolBarItem.deleteCorrespondent,
	                               totalToolBarItem.editCorrespondentPDF,
	                               totalToolBarItem.queryCorrespondentPDF];
	this.defaultToolBarConfig={
		id:'CorrespondentToolBar'
	};
}
Ecp.extend(Ecp.CorrespondentToolBar.prototype,new Ecp.ServiceToolbar());

/******************   Ecp.CorrespondentGrid    ************************/

/******************   Ecp.CorrespondentForm    ************************/
// modify by sunyue 20130123
Ecp.CorrespondentForm=function()
{	
	Ecp.ServiceForm.call(this);
	var currencyDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : DISPATCH_SERVLET_URL
		}),
		reader : new Ext.data.JsonReader({
			root : 'currencies',
			fields: [
			    {name: 'currencyCode'}
			]
		})
	});
	currencyDs.baseParams = {cmd:'cur',action:'findAll',limit:0};
	currencyDs.load();
	this.config={
		labelAlign:'left',
		region:'center',
		labelWidth:75,
		frame:true
	};
	this.items=[
				{
				  	xtype:'hidden',
				  	id: 'id',
				  	name: 'id'
				},
				{
				  	xtype:'hidden',
				  	id: 'internalCode',
				  	name: 'internalCode'
				},
				{
					xtype:'textfield',
					fieldLabel: TXT.bic_code,
					id: 'bic',
					name: 'bic',
					width:300,
					allowBlank:false,
					maxLength: 11,
				  	maxLengthText: TXT.bic_code_max_length,
				  	minLength: 8,
				  	minLengthText: TXT.bic_code_min_length
				},
                {
      				xtype:'combo',
					fieldLabel: TXT.case_currency,
					id: 'bicCurrency',
					name: 'bicCurrency',
					width:300,
					store: currencyDs,
					minLength : 3,
					maxLength : 3,
					forceSelection:true,
					displayField:'currencyCode',
					valueField: 'currencyCode',					                        			
					typeAhead: true,
					mode: 'local',
					editable: false,
					allowBlank: false,
					triggerAction: 'all',
					selectOnFocus:true
    			},
    			{
    				xtype:'trigger', 
    				fieldLabel: TXT.user_branch,
    				width:300,
    				id: 'institution',
    				name: 'institution',
    				triggerClass:'x-form-search-trigger',
    				allowBlank:false,
    				onTriggerClick:function(){
    					showInstitutionTreeWin(callBack);
    				},
    				editable:false
    			},
			    {
			    	xtype:'textfield',
			    	fieldLabel: TXT.bic_name,
			    	id: 'bankName',
			    	name: 'bankName',
			    	width:300,
			    	allowBlank:false,
			    	maxLength:32
			    },
			     {
			    	xtype:'textfield',
			    	fieldLabel: TXT.correspondent_tel,
			    	id: 'tel',
			    	name: 'tel',
			    	width:300
			    },
                {
                	xtype:'textfield',
                	fieldLabel: TXT.correspondent_description,
                	id: 'description',
                	width:300,
                	name: 'description'
                },
                {
                	xtype:'textfield',
                	fieldLabel: TXT.correspondent_address,
                	id: 'address',
                	width:300,
                	name: 'address'
                },
                {
                	xtype:'textfield',
                	fieldLabel: TXT.correspondent_contactMan,
                	id: 'contactMan',
                	width:300,
                	name: 'contactMan'
                }
	];
}
Ecp.extend(Ecp.CorrespondentForm.prototype,new Ecp.ServiceForm());
/******************   Ecp.CorrespondentForm    ************************/

/******************   Ecp.CorrespondentWindow    ************************/
Ecp.CorrespondentWindow=function()
{
	Ecp.ServiceWindow.call(this);
	this.config={
			title: TXT.correspondent_info,
	        width:450,
	        height:300,
	        autoScroll :false,
	        layout:'fit',
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
			text :TXT.common_btnOK,
				handler : function() {
					clickSaveCorrespondentBtn(this);
				}
			},{
				text :TXT.common_btnClose,
				handler : function() {
					var win=this['ecpOwner'];
					win.window.close();
				}
			}
		];
}
Ecp.CorrespondentWindow.prototype.setCloseAction=function(isClose){
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
Ecp.CorrespondentWindow.createCorrespondentWindow=function(fun){
	var correspondentForm=new Ecp.CorrespondentForm();
	if(fun && fun['correspondentForm'] && typeof fun['correspondentForm']=='function'){
		correspondentForm.handleWidgetConfig(fun['correspondentForm']);
	}
	// window
	var correspondentWin = new Ecp.CorrespondentWindow();
	if(fun && fun['correspondentWin'] && typeof fun['correspondentWin']=='function'){
		correspondentWin.handleWidgetConfig(fun['correspondentWin']);
	}
	correspondentWin.items=[correspondentForm.render()];
	correspondentWin.render();
	return correspondentWin.window;
}
Ecp.extend(Ecp.CorrespondentWindow.prototype,new Ecp.ServiceWindow());

/******************   Ecp.CorrespondentGrid    ************************/

/******************   Tree Control Callback ***************************/
function callBack(treeRecord,treeWin){
	if(treeRecord!=null)
	{
		Ecp.components.correspondentForm.form.setValue('internalCode', treeRecord.attributes.internalCode);
		Ecp.components.correspondentForm.form.setValue('institution', treeRecord.attributes.name);
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
Ecp.CorrespondentSearchForm=function(){
	Ecp.ServiceForm.call(this);
	this.config={
			labelAlign: 'left',
			region: 'center',
		    labelWidth:100,
		    frame:true
	}
	var currencyDs = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : DISPATCH_SERVLET_URL
		}),
		reader : new Ext.data.JsonReader({
			root : 'currencies',
			fields: [
			    {name: 'currencyCode'}
			]
		})
	});
	currencyDs.baseParams = {cmd:'cur',action:'findAll',limit:0};
	currencyDs.load();
	this.items=[
				{
				  	xtype:'hidden',
				  	id: 'internalCode',
				  	name: 'internalCode'
				},
				{
					xtype:'textfield',
					fieldLabel: TXT.bic_code,
					id: 'bic',
					name: 'bic',
					width:150
				},
                {
      				xtype:'combo',
					fieldLabel: TXT.case_currency,
					id: 'bicCurrency',
					name: 'bicCurrency',
					width:150,
					store: currencyDs,
					minLength : 3,
					maxLength : 3,
					forceSelection:true,
					displayField:'currencyCode',
					valueField: 'currencyCode',					                        			
					typeAhead: true,
					mode: 'local',
					editable: false,
					triggerAction: 'all',
					selectOnFocus:true
    			},
    			{
    				xtype:'trigger', 
    				fieldLabel: TXT.user_branch,
    				width:150,
    				id: 'institution',
    				name: 'institution',
    				triggerClass:'x-form-search-trigger',
    				onTriggerClick:function(){
    					showInstitutionTreeWin(searchCallBack);
    				},
    				editable:false
    			},
			    {
			    	xtype:'textfield',
			    	fieldLabel: TXT.bic_name,
			    	id: 'bankName',
			    	name: 'bankName',
			    	width:150,
			    	maxLength:32
			    },
                {
                	xtype:'textfield',
                	fieldLabel: TXT.correspondent_description,
                	id: 'description',
                	width:150,
                	name: 'description'
                },
                {
                	xtype:'textfield',
                	fieldLabel: TXT.correspondent_address,
                	id: 'address',
                	width:150,
                	name: 'address'
                },
                {
                	xtype:'textfield',
                	fieldLabel: TXT.correspondent_contactMan,
                	id: 'contactMan',
                	width:150,
                	name: 'contactMan'
                }
	];
}
Ecp.extend(Ecp.CorrespondentSearchForm.prototype,new Ecp.ServiceForm());
Ecp.CorrespondentSearchWin=function(){
	Ecp.ServiceWindow.call(this);
	this.config={
		 	title:TXT.common_search,
			width:330,
	        height:320,
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
			clickQueryCorrespondentBtn(this);
		}
	},{
		text:TXT.common_reset,
		handler:function(){
			searchFormReset(this);
		}
	}];
}
Ecp.CorrespondentSearchWin.createWindow=function(fun){
	var correspondentSearchForm = new Ecp.CorrespondentSearchForm();
	if(fun && fun['searchFormFun'] && typeof fun['searchFormFun']=='function'){
		correspondentSearchForm.handleWidgetConfig(fun['searchFormFun']);
	}
	var correspondentSearchWin = new Ecp.CorrespondentSearchWin();
	if(fun && fun['searchWinFun'] && typeof fun['searchWinFun']=='function'){
		correspondentSearchWin.handleWidgetConfig(fun['searchWinFun']);
	}
	correspondentSearchWin.items=[correspondentSearchForm.render()];
	correspondentSearchWin.render();
	return correspondentSearchWin.window;
}
function searchCallBack(treeRecord,treeWin){
	if(treeRecord!=null)
	{
		Ecp.components.correspondentSearchForm.form.setValue('internalCode', treeRecord.attributes.internalCode);
		Ecp.components.correspondentSearchForm.form.setValue('institution', treeRecord.attributes.name);
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
Ecp.extend(Ecp.CorrespondentSearchWin.prototype,new Ecp.ServiceWindow());

Ecp.CorrespondentImportForm=function(){
	Ecp.ServiceForm.call(this); 
	this.config={ 
			height : 400,
			labelWidth : 70,  
			region : 'north',
			padding:10,
			fileUpload:true,
			frame:true
	};
	this.items=[{
					xtype: 'fileuploadfield',
                    id: 'cor-file',
                    width:250,
                    emptyText: TXT.common_please_select,
                    fieldLabel: TXT.common_please_select,
                    name: 'photo-path',
                    buttonText: TXT.common_please_select
				}];
 }
Ecp.extend(Ecp.CorrespondentImportForm.prototype, new Ecp.ServiceForm());
Ecp.CorrespondentImportWindow=function(){
	Ecp.ServiceWindow.call(this); 
	this.config={
	        width:400,
	        height:150,
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
			importFile(this);
		}
	},{
		text:TXT.common_btnClose,
		scope:this,
		handler:function(){
			this.window.window.close();
		}
	}];
}
Ecp.CorrespondentImportWindow.createWindow=function(fun){
	var correspondentImportForm = new Ecp.CorrespondentImportForm();
	if(fun && fun['importFormFun'] && typeof fun['importFormFun']=='function'){
		correspondentImportForm.handleWidgetConfig(fun['importFormFun']);
	}
	var correspondentImportWindow = new Ecp.CorrespondentImportWindow();
	if(fun && fun['importWinFun'] && typeof fun['importWinFun']=='function'){
		correspondentImportWindow.handleWidgetConfig(fun['importWinFun']);
	}
	correspondentImportWindow.items=[correspondentImportForm.render()];
	correspondentImportWindow.render();
	return correspondentImportWindow.window;
}
Ecp.extend(Ecp.CorrespondentImportWindow.prototype,new Ecp.ServiceWindow());