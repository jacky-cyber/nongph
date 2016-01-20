/* create by sunyue 20130122 */
Ecp.SystemtParamtsGrid=function(){
	Ecp.ServiceGrid.call(this);
	this.defaultStoreConfig={
		url:DISPATCH_SERVLET_URL,
		root:'systemtParams',
		idProperty:'id',
		fields:[
			 {name :'id'}, 
			 {name :'parameterName'},
			 {name :'parameterValue'}, 
			 {name :'parameterDescription'}
			   ]
	};
	
	this.defaultStoreConfig.baseParams={
		cmd:'sysparam',
		action:'find'
	};
	this.defaultCmConfig=[
					 {
						header :TXT.system_parameterName,
						dataIndex :'parameterName',
						menuDisabled :true,
						width :300
					}, {
						header :TXT.system_parameterValue,
						dataIndex :'parameterValue',
						menuDisabled :true,
						width :400
					}, 
					{
						header :TXT.system_parameterDescription,
						dataIndex :'parameterDescription',
						width :300,
						menuDisabled :true
					}
	];
	
	this.defaultGridConfig={
		title:TXT.systemparam,
		id:'systemtParamtsGrid',
		region :'center',
		stripeRows :true,
		pagination:true
	};
}

Ecp.SystemtParamtsGrid.prototype.show=function(){
	this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}
Ecp.SystemtParamtsGrid.prototype.search=function(params){
	if(this.defaultGridConfig['pagination']!=null)
	{
		params['start']=0;
		params['limit']=PAGE_SIZE;
	}
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load(params);
}
Ecp.extend(Ecp.SystemtParamtsGrid.prototype,new Ecp.ServiceGrid());

Ecp.SystemtParamtsGridToolbar=function()
{
	Ecp.ServiceToolbar.call(this);
	this.defaultToolBarConfig={
		id:'systemtParamtsGridToolbar'
	};
	this.defaultToolBarItemConfig=[totalToolBarItem.querySystemParam,totalToolBarItem.addSystemParam,totalToolBarItem.deleteSystemParam];
}
Ecp.extend(Ecp.SystemtParamtsGridToolbar.prototype,new Ecp.ServiceToolbar());

Ecp.SystemtParamtInfoForm=function()
{
	Ecp.ServiceForm.call(this);
	this.config={
		labelAlign: 'left',
		region: 'center',
		labelWidth:80,
		height: 125,
		frame:true
	};
	this.items=[
				{
				xtype: 'textfield',
  				fieldLabel: TXT.system_parameterName,
  				id: 'parameterName',
  				width :200,
  				allowBlank:false,
  				name: 'parameterName'
				},{
					xtype: 'textfield',
	  				fieldLabel: TXT.system_parameterValue,
	  				id: 'parameterValue',
	  				width :200,
	  				allowBlank:false,
	  				name: 'parameterValue'
				},{
					xtype: 'textfield',
	  				fieldLabel: TXT.system_parameterDescription,
	  				id: 'parameterDescription',
	  				width :200,
	  				name: 'parameterDescription'
				}
	];
}
Ecp.extend(Ecp.SystemtParamtInfoForm.prototype,new Ecp.ServiceForm());
Ecp.SystemParamInfoWindow=function()
{
	Ecp.ServiceWindow.call(this);
	this.config={
		    title:TXT.systemparam_title,
	        width:350,
	        height:150,
	        autoScroll :false,
	        border:false,
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
					clickSaveSystemParamBtn(this);
				}
			},{
				text :TXT.common_btnClose,
				handler : function() {
					this['ecpOwner'].window.close();
				}
			}];
}
Ecp.SystemParamInfoWindow.createWindow=function(fun){
	var systemtParamtInfoForm=new Ecp.SystemtParamtInfoForm();
	if(fun && fun['formFun'] && typeof fun['formFun']=='function')
		systemtParamtInfoForm.handleWidgetConfig(fun['formFun']);
	var systemParamInfoWindow = new Ecp.SystemParamInfoWindow();
	if(fun && fun['winFun'] && typeof fun['winFun']=='function')
		systemParamInfoWindow.handleWidgetConfig(fun['winFun']);
	systemParamInfoWindow.items=[systemtParamtInfoForm.render()];
	systemParamInfoWindow.render();
	return systemParamInfoWindow.window;
}
Ecp.extend(Ecp.SystemParamInfoWindow.prototype,new Ecp.ServiceWindow());

Ecp.SystemtParamtsSearchForm=function(){
	Ecp.ServiceForm.call(this);
	this.config={
			labelAlign: 'left',
			region: 'center',
		    labelWidth:100,
		    frame:true
	}
	this.items=[
				{
				xtype: 'textfield',
  				fieldLabel: TXT.system_parameterName,
  				id: 'parameterName',
  				width :200,
  				name: 'parameterName'
				},{
					xtype: 'textfield',
	  				fieldLabel: TXT.system_parameterValue,
	  				id: 'parameterValue',
	  				width :200,
	  				name: 'parameterValue'
				},{
					xtype: 'textfield',
	  				fieldLabel: TXT.system_parameterDescription,
	  				id: 'parameterDescription',
	  				width :200,
	  				name: 'parameterDescription'
				}
	];
}
Ecp.extend(Ecp.SystemtParamtsSearchForm.prototype,new Ecp.ServiceForm());


Ecp.SystemtParamtsSearchWin=function(){
	Ecp.ServiceWindow.call(this);
	this.config={
		 	title:TXT.common_search,
			width:330,
	        height:225,
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
			clickQueryParamsBtn(this);
		}
	},{
		text:TXT.common_reset,
		handler:function(){
			messageSearchFormReset(this);
		}
	}];
}
Ecp.SystemtParamtsSearchWin.createWindow=function(fun){
	var systemtParamtsSearchForm = new Ecp.SystemtParamtsSearchForm();
	if(fun && fun['searchFormFun'] && typeof fun['searchFormFun']=='function'){
		systemtParamtsSearchForm.handleWidgetConfig(fun['searchFormFun']);
	}
	var systemtParamtsSearchWin = new Ecp.SystemtParamtsSearchWin();
	if(fun && fun['searchWinFun'] && typeof fun['searchWinFun']=='function'){
		systemtParamtsSearchWin.handleWidgetConfig(fun['searchWinFun']);
	}
	systemtParamtsSearchWin.items=[systemtParamtsSearchForm.render()];
	systemtParamtsSearchWin.render();
	return systemtParamtsSearchWin.window;
}
Ecp.extend(Ecp.SystemtParamtsSearchWin.prototype,new Ecp.ServiceWindow());
