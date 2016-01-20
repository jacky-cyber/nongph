Ecp.BackUpForm=function(){
	
	this.form=new Ecp.FormPanel();
	this.config={
		title:TXT.case_backup_case,
		bodyStyle: 'background-color:#dfe8f6;padding:50 0 30 100', 
		layout: 'column'
		//bbar:undefined
		//autoHeight:true
	};
	this.items=[
			     {
			       layout: 'form',
			       columnWidth: .35,
			       baseCls: 'x-plain',
			       items: [
		               {
		                   xtype:'datefield',
		                   fieldLabel: TXT.case_dateTo,
		                   format: 'Y-m-d',
		                   id: 'dateTo',
		                   name: 'dateTo',
		                   width:220,
		                   editable:false,
		                   allowBlank:false
		               }
		           ]
			     },
			     {
			    	 layout: 'form',
			    	 columnWidth: .33,
			    	 baseCls: 'x-plain',
			    	 items:[
						{
							   xtype:'button',
							   text: TXT.backup,
							   minWidth: 75,
							   scope:this
							   //handler: function(){}
						}
			    	 ]
			     }
			];
	
	this.buttons=undefined;
	
	this.cusObj=null;
	
	this.eventConfig=null;

}

Ecp.BackUpForm.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.BackUpForm.prototype.customization=function(obj){
	this.cusObj=obj;
}

Ecp.BackUpForm.prototype.setBackupHandler=function(handler){
	this.items[1].items[0].handler=handler;
}

Ecp.BackUpForm.prototype.render=function(){
	this.form.init();
	this.form['config']=this.config;
	this.form['items']=this.items;
	this.form['buttons']=this.buttons;
	this.form.customization(this.cusObj);
	return this.form.render();
}

Ecp.CaseHistoryToolBar=function(){
	this.toolBar=new Ecp.ToolBar();
	this.defaultToolBarItemConfig=[totalToolBarItem.queryCase,totalToolBarItem.resumeCase]
	this.customizationConfig={};
	this.defaultToolBarConfig={
		id:'caseAccompanyMsgToolBar'
	};
}

Ecp.CaseHistoryToolBar.prototype.handleWidgetConfig=function(handler){
	handler(this);	
}

Ecp.CaseHistoryToolBar.prototype.setWidgetEvent=function(toolBarEventConfig){
	this.toolBar.setToolBarEvent(toolBarEventConfig);
}

Ecp.CaseHistoryToolBar.prototype.setPremission=function(premission){
	this.toolBar.setToolBarPremission(premission);
}

Ecp.CaseHistoryToolBar.prototype.customization=function(toolBarCustomization){
	this.customizationConfig=toolBarCustomization;
}

Ecp.CaseHistoryToolBar.prototype.render=function(){
	this.toolBar.setToolBarConfig(this.defaultToolBarConfig);
	this.toolBar.setToolBarItemsConfig(this.defaultToolBarItemConfig);
	this.toolBar.init();
	this.toolBar.customization(this.customizationConfig);
	return this.toolBar.render();
}