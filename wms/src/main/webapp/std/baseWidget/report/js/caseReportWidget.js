/**
 * Case Report Tree Panel
 */
Ecp.CaseReportGridWidget=function(title, action){
	this.treeGrid=new Ecp.TreeGrid();
	this.topToolBar=null;
	this.config={
		id : 'caseReport',
		title : title,
		border : true,
		enableHdMenu: false,
		columnResize: false,
		enableSort : false,
		region: 'center',
		isFirstLevelExpand: true,
		dataUrl:DISPATCH_SERVLET_URL
	};
	this.config.baseParams={
		cmd:'report',
		action: action
	};

	this.columnConfig=[
			{header:TXT.report_separate,dataIndex: 'name',width:350,
				tpl: new Ext.XTemplate('{name:this.formatName}', {
					formatName: function(value) {
						var rendererJson={
                			'status_O':TXT.report_open,
                			'C':TXT.report_close,
                			'R':TXT.report_reopen,
                			'I':TXT.report_remittance_in,
                			'N':TXT.report_remittance_undefined,
               				'remi_O':TXT.report_remittance_out,
               				'OTHER':TXT.case_type_OTHER,
               				'RTCP':TXT.case_type_RTCP,
               				'RTMP':TXT.case_type_RTMP,
               				'UTA':TXT.case_type_UTA,
               				'CNR':TXT.case_type_CNR,
               				'rootReport':TXT.case_all_cases,
               				'check':TXT.report_check,
               				'MSG_SET_READ':TXT.report_set_read,
               				'CASE_ADD':TXT.report_case_add};
               			var result= rendererJson[value];
               			return result==null?value:result;        		
					}
				})
			},
			{header:TXT.report_count,dataIndex:'count',width:100},
			{header:TXT.report_percent,dataIndex:'percent',width:100,
				tpl: new Ext.XTemplate('{percent:this.formatPercent}', {
					formatPercent : function(value) {
					if (value != -1)
						return Math.round(value * 100) + "%";
					return "";
				}
			})
			}
	],
	this.cusObj=null;
	this.eventObj={
			'load':function(){
				Ext.MessageBox.hide();
			}
	};
}

//1 Product Customization
Ecp.CaseReportGridWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.CaseReportGridWidget.prototype.setToolBar=function(tbar){
	this.topToolBar=tbar;
}

//3 Tree Grid Event function
Ecp.CaseReportGridWidget.prototype.setTreeGridEvent=function(obj){
	this.eventObj=obj;
}
//4 Project Customization function
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

// case report toolbar
Ecp.CaseReportToolBarWidget=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.queryCaseReport];
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'caseReportToolBar'
	};
}

//1
Ecp.CaseReportToolBarWidget.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

//2
Ecp.CaseReportToolBarWidget.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.CaseReportToolBarWidget.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

//3
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

/**
 * Case Report Information Form
 */
Ecp.CaseReportInfoForm=function()
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
			xtype: 'trigger',
			fieldLabel: TXT.branch,
			id: 'statistic',
			name: 'statistic',
			width: 150,
			onTriggerClick: function(){
    			showInstitutionTreeWin(callBackCaseInstReport);
    		},
			triggerClass: 'x-form-search-trigger',
			editable: false
		},
        {
        	xtype: 'datefield',
            fieldLabel: TXT.report_date_from,
            id: 'dateFrom',
            name: 'dateFrom',
            width: 150,
			format: 'Y-m-d',
			allowBlank: false,
			editable: false
        },
        {
        	xtype: 'datefield',
			fieldLabel : TXT.report_date_to,
			id : 'dateTo',
			name : 'dateTo',
			width: 150,
			format: 'Y-m-d',
			allowBlank : false,
			editable: false
		}, 
		{
			xtype: 'checkbox',
			fieldLabel :TXT.report_summary,
			boxLabel :TXT.report_status,
			id :'status',
			name :'status'
		},
        {
        	xtype: 'checkbox',
            boxLabel: TXT.report_type,
            id :'caseType',
			name :'caseType',
			labelSeparator :''
        },
        {
        	xtype :'checkbox',
            boxLabel: TXT.report_remittance,
            id :'remittance',
			name :'remittance',
			labelSeparator :''                
		}
	];
	
	this.buttons=[];
}

// Product Customization
Ecp.CaseReportInfoForm.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// Project Customization
Ecp.CaseReportInfoForm.prototype.customization=function(obj)
{
	if(obj.config!=null)
		this.config=obj.config;
	if(obj.buttons!=null)
		this.buttons=obj.buttons;
	if(obj.items!=null)
		this.items=obj.items;
}

Ecp.CaseReportInfoForm.prototype.render=function()
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

/**
 * the case report window
 *
 * attribute: some of them have their own default values
 * method: should have the methods such as handleWidgetConfig, customization, render, setValues, getValues
 *
 * note:
 * if the window uses observer pattern, the class should have addObserver, notifyAll
 */
Ecp.CaseReportInfoWindow=function()
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
				clickStatisticCaseBtn(this);
			}
		},
		{
			text :TXT.report_excelBtn,
			handler : function() {
				clickExcelCaseBtn(this);
			}
		}
	];
	this.observers=[];
	this.items=[];
}

// Product Customization Interface
Ecp.CaseReportInfoWindow.prototype.handleWidgetConfig=function(handler)
{
	handler(this);
}

// Project Customization Interface
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

/**
 * single search window static array
 */
Ecp.CaseReportInfoWindow.singleSearchWindows=null;


/**
 * create single add window static function
 */
Ecp.CaseReportInfoWindow.createSingleSearchWindow=function(windowObj){
	if(!Ecp.CaseReportInfoWindow.singleSearchWindows) {
		// form
		var caseReportForm = new Ecp.CaseReportInfoForm();
	
		if(windowObj['items'] && windowObj['items'][0])
			caseReportForm.customization(windowObj['items'][0]);
			
		// window
		var caseReportSearchWin = new Ecp.CaseReportInfoWindow();
		// Product Customization
		caseReportSearchWin.handleWidgetConfig(function(obj){
			obj.items=[caseReportForm.render()];
			obj.setCloseAction(false);
		});

		// Project Customization
		caseReportSearchWin.customization(windowObj);
	
		caseReportSearchWin.render();
		Ecp.CaseReportInfoWindow.singleSearchWindows = caseReportSearchWin.window;
	}
	return Ecp.CaseReportInfoWindow.singleSearchWindows;
}