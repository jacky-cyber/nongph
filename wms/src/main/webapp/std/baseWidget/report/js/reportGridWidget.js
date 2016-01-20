/**
 * add by jinlijun 2013/1/28
 */
Ecp.ReportGridWidget=function(title, fields, columns, action){
	Ecp.ServiceGrid.call(this);
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		fields: fields
	};
	
	this.defaultStoreConfig.baseParams={
		cmd:'statisticsReport',
		action: action
	};
	this.defaultCmConfig=columns;
	
	this.defaultGridConfig={
		title:title,
		id:'caseReport',
		region :'center',
		stripeRows :true,
		pagination:true
	};
}

Ecp.ReportGridWidget.prototype.show=function(){
	this.dataStore.store.load();
}
Ecp.ReportGridWidget.prototype.statistic=function(params){
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load();
}
Ecp.extend(Ecp.ReportGridWidget.prototype,new Ecp.ServiceGrid());

// case report toolbar
Ecp.ReportGridToolbar=function()
{
	Ecp.ServiceToolbar.call(this);
	this.defaultToolBarConfig={
		id:'reportGridToolbar'
	};
	this.defaultToolBarItemConfig=[totalToolBarItem.queryCaseReport, totalToolBarItem.exportCaseReport];
}
Ecp.extend(Ecp.ReportGridToolbar.prototype,new Ecp.ServiceToolbar());

/**
 * Case Report Information Form
 */
Ecp.CaseReportQueryFormWithRemittance=function()
{
	this.form=null;
	this.config={
		labelAlign: 'center',
		region: 'center',
		labelWidth: 90,
		frame:true
    };
	
	this.items = [
		{
			xtype: 'hidden',
			id: 'institutionId',
			name: 'institutionId'
		},
		{
			xtype: 'hidden',
			id: 'requestFrom',
			name: 'requestFrom'
		},
		{
			xtype: 'hidden',
			id: 'sum',
			name: 'sum'
		},
		{
			xtype: 'trigger',
			fieldLabel: TXT.branch,
			id: 'statistic',
			name: 'statistic',
			width: 170,
			onTriggerClick: function(){
    			showInstitutionTreeWin(showInstitutionTree);
    		},
			triggerClass: 'x-form-search-trigger',
			editable: false
		},
        {
        	xtype: 'datefield',
            fieldLabel: TXT.report_date_from,
            id: 'dateFrom',
            name: 'dateFrom',
            width: 170,
			format: 'Y-m-d',
			allowBlank : false,
			editable:false
        },
        {
        	xtype: 'datefield',
			fieldLabel : TXT.report_date_to,
			id : 'dateTo',
			name : 'dateTo',
			width: 170,
			format: 'Y-m-d',
			allowBlank : false,
			editable:false
		}, 
		{
			xtype: 'combo',
			fieldLabel :TXT.report_remittance,
			id :'remittance',
			name :'remittance',
			store :new Ext.data.SimpleStore({
				fields:['label','name'],
				data:[[TXT.report_remittance_all,'A'],[TXT.report_remittance_in,'I'],[TXT.report_remittance_out,'O'],[TXT.report_remittance_undefined,'N']]
			}),
			width:170,
			forceSelection :true,
			displayField :'label',
			valueField :'name',
			value :'',
			typeAhead :true,
			mode :'local',
			editable :false,
			triggerAction :'all',
			selectOnFocus :true
		}
	];
	
	this.buttons=[];
}

Ecp.extend(Ecp.CaseReportQueryFormWithRemittance.prototype, new Ecp.ServiceForm());
//--------------------------------------------------------------------------------
Ecp.CaseReportQueryFormWithoutRemittance=function()
{
	this.form=null;
	this.config={
		labelAlign: 'center',
		region: 'center',
		labelWidth: 90,
		frame:true
    };
	
	this.items = [
		{
			xtype: 'hidden',
			id: 'institutionId',
			name: 'institutionId'
		},
		{
			xtype: 'hidden',
			id: 'requestFrom',
			name: 'requestFrom'
		},
		{
			xtype: 'hidden',
			id: 'sum',
			name: 'sum'
		},
		{
			xtype: 'trigger',
			fieldLabel: TXT.branch,
			id: 'statistic',
			name: 'statistic',
			width: 170,
			onTriggerClick: function(){
    			showInstitutionTreeWin(showInstitutionTree);
    		},
			triggerClass: 'x-form-search-trigger',
			editable: false
		},
        {
        	xtype: 'datefield',
            fieldLabel: TXT.report_date_from,
            id: 'dateFrom',
            name: 'dateFrom',
            width: 170,
			format: 'Y-m-d',
			allowBlank : false,
			editable:false
        },
        {
        	xtype: 'datefield',
			fieldLabel : TXT.report_date_to,
			id : 'dateTo',
			name : 'dateTo',
			width: 170,
			format: 'Y-m-d',
			allowBlank : false,
			editable:false
		}
	];
	
	this.buttons=[];
}

Ecp.extend(Ecp.CaseReportQueryFormWithoutRemittance.prototype, new Ecp.ServiceForm());

//--------------------------------------------------------------------------------
Ecp.CaseReportQueryFormWithRemittanceForYear=function()
{
	this.form=null;
	this.config={
		labelAlign: 'center',
		region: 'center',
		labelWidth: 90,
		frame:true
    };
	
	this.items = [
		{
			xtype: 'hidden',
			id: 'institutionId',
			name: 'institutionId'
		},
		{
			xtype: 'hidden',
			id: 'requestFrom',
			name: 'requestFrom'
		},
		{
			xtype: 'hidden',
			id: 'sum',
			name: 'sum'
		},
		{
			xtype: 'trigger',
			fieldLabel: TXT.branch,
			id: 'statistic',
			name: 'statistic',
			width: 170,
			onTriggerClick: function(){
    			showInstitutionTreeWin(showInstitutionTree);
    		},
			triggerClass: 'x-form-search-trigger',
			editable: false
		},
        {
        	xtype: 'datefield',
            fieldLabel: TXT.report_date_from,
            id: 'dateFrom',
            name: 'dateFrom',
            width: 170,
			format: 'Y',
			allowBlank : false,
			editable:false
        },
        {
        	xtype: 'datefield',
			fieldLabel : TXT.report_date_to,
			id : 'dateTo',
			name : 'dateTo',
			width: 170,
			format: 'Y',
			allowBlank : false,
			editable:false
		}, 
		{
			xtype: 'combo',
			fieldLabel :TXT.report_remittance,
			id :'remittance',
			name :'remittance',
			store :new Ext.data.SimpleStore({
				fields:['label','name'],
				data:[[TXT.report_remittance_all,'A'],[TXT.report_remittance_in,'I'],[TXT.report_remittance_out,'O'],[TXT.report_remittance_undefined,'N']]
			}),
			width:170,
			forceSelection :true,
			displayField :'label',
			valueField :'name',
			value :'',
			typeAhead :true,
			mode :'local',
			editable :false,
			triggerAction :'all',
			selectOnFocus :true
		}
	];
	
	this.buttons=[];
}

Ecp.extend(Ecp.CaseReportQueryFormWithRemittanceForYear.prototype, new Ecp.ServiceForm());

//====================================================================================
Ecp.CaseReportQueryFormWindow=function(formicAction){
	Ecp.ServiceWindow.call(this);
	this.config={
		title: TXT.report_condition,
		width:400,
		height:270,
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
	this.buttons=[
		{
			text :TXT.report_statisticBtn,
			handler : function() {
				clickStatisticBtn(formicAction);
			}
		}
	];
	this.observers=[];
	this.items=[];
}
Ecp.CaseReportQueryFormWindow.createWindow=function(formicAction, needRemittance, onlyYear){
	var reportQueryForm;
	if(needRemittance){
		if(onlyYear){
			reportQueryForm = new Ecp.CaseReportQueryFormWithRemittanceForYear();
		}else{
			reportQueryForm = new Ecp.CaseReportQueryFormWithRemittance();
		}
	}else{
		reportQueryForm = new Ecp.CaseReportQueryFormWithoutRemittance();
	}
	var reportQueryWindow = new Ecp.CaseReportQueryFormWindow(formicAction);
	reportQueryWindow.items=[reportQueryForm.render()];
	reportQueryWindow.render();
	return reportQueryWindow.window;
}
Ecp.extend(Ecp.CaseReportQueryFormWindow.prototype,new Ecp.ServiceWindow());