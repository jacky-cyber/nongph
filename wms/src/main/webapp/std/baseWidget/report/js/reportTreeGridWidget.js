//add by jinlijun 2013/1/31
//Report Grid
Ecp.CaseReportGridWidget=function(title, columns, action){
	this.treeGrid=new Ecp.TreeGrid();
	this.topToolBar=null;
	this.config={
		id : 'caseReport',
		title : title,
		border : true,
		enableHdMenu: false,
		columnResize: true,
		enableSort : true,
		region: 'center',
		isFirstLevelExpand: true,
		dataUrl:DISPATCH_SERVLET_URL
	};
	this.config.baseParams={
		cmd:'statisticsReport',
		action: action
	};

	this.columnConfig=columns;
	
	this.cusObj=null;
	this.eventObj={
			'load':function(){
				Ext.MessageBox.hide();
			}
	};
}

Ecp.CaseReportGridWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.CaseReportGridWidget.prototype.setToolBar=function(tbar){
	this.topToolBar=tbar;
}

Ecp.CaseReportGridWidget.prototype.setTreeGridEvent=function(obj){
	this.eventObj=obj;
}
Ecp.CaseReportGridWidget.prototype.customization=function(obj){
	this.cusObj=obj;
}

Ecp.CaseReportGridWidget.prototype.render=function(){
	this.treeGrid.setIsFirstLevelExpand(true);
	this.treeGrid.setTreeConfig(this.config);
	this.treeGrid.setTreeColumnConfig(this.columnConfig);
	if(this.topToolBar!=null)
		this.treeGrid.setTopToolBar(this.topToolBar);
	if(this.eventObj!=null)
		this.treeGrid.setTreeEventConfig(this.eventObj);
	this.treeGrid.customization(this.cusObj);
	return this.treeGrid.render();
}

// report tool bar
Ecp.CaseReportToolBarWidget=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.queryCaseReport, totalToolBarItem.exportCaseReport];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'caseReportToolBar'
	};
}

Ecp.CaseReportToolBarWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

Ecp.CaseReportToolBarWidget.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.CaseReportToolBarWidget.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

Ecp.CaseReportToolBarWidget.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.CaseReportToolBarWidget.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}

// report query form with remittance
Ecp.CaseReportInfoFormWithRemittance=function()
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
			id: 'reportBy',
			name: 'reportBy'
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

Ecp.CaseReportInfoFormWithRemittance.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.CaseReportInfoFormWithRemittance.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.CaseReportInfoFormWithRemittance.prototype.render=function()
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
//-------------------------------------------------------------------------------
// report query form without remittance
Ecp.CaseReportInfoFormWithoutRemittance=function()
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
			id: 'reportBy',
			name: 'reportBy'
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

Ecp.CaseReportInfoFormWithoutRemittance.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.CaseReportInfoFormWithoutRemittance.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.CaseReportInfoFormWithoutRemittance.prototype.render=function()
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
//=================================================================================
//report query window
Ecp.CaseReportInfoWindow=function(action)
{
	this.window=null;
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
				clickStatisticBtn(this, action);
			}
		}
	];
	this.observers=[];
	this.items=[];
}

Ecp.CaseReportInfoWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

Ecp.CaseReportInfoWindow.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
}

Ecp.CaseReportInfoWindow.prototype.setItems=function(items)
{
	this.items=items;
}

Ecp.CaseReportInfoWindow.prototype.render=function()
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

Ecp.CaseReportInfoWindow.prototype.getItem=function(index)
{
	return this.window.getItem(index);
}

Ecp.CaseReportInfoWindow.prototype.setCloseAction=function(isClose){
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

Ecp.CaseReportInfoWindow.singleSearchWindows=null;

Ecp.CaseReportInfoWindow.createSingleSearchWindow=function(windowObj, action, needRemittance){
	if(!Ecp.CaseReportInfoWindow.singleSearchWindows) {
		var caseReportForm;
		if(needRemittance){
			caseReportForm = new Ecp.CaseReportInfoFormWithRemittance();
		}else{
			caseReportForm = new Ecp.CaseReportInfoFormWithoutRemittance();
		}
	
		if(windowObj['items'] && windowObj['items'][0])
			caseReportForm.customization(windowObj['items'][0]);
			
		var caseReportSearchWin = new Ecp.CaseReportInfoWindow(action);
		caseReportSearchWin.handleWidgetConfig(function(obj){
			obj.items=[caseReportForm.render()];
			obj.setCloseAction(false);
		});

		caseReportSearchWin.customization(windowObj);
	
		caseReportSearchWin.render();
		Ecp.CaseReportInfoWindow.singleSearchWindows = caseReportSearchWin.window;
	}
	return Ecp.CaseReportInfoWindow.singleSearchWindows;
}