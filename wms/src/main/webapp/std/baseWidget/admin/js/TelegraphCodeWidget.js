Ecp.TelegraphCodeToolBar=function(){
	Ecp.ServiceToolbar.call(this);
	// grid的toolBar信息定义在menu.js里面 
	this.defaultToolBarItemConfig=[//swift_totalToolBarItem.addTelegraphCode,
	                               //swift_totalToolBarItem.editTelegraphCode,
	                               //swift_totalToolBarItem.delTelegraphCode,
	                               totalToolBarItem.searchTelegraphCode];
 
	this.defaultToolBarConfig={
		id:'telegraphCodeToolBar'
	};
}
Ecp.extend(Ecp.TelegraphCodeToolBar.prototype,new Ecp.ServiceToolbar());


Ecp.TelegraphCodeGrid = function() { 
	Ecp.ServiceGrid.call(this); 
	this.defaultStoreConfig={
		 url:DISPATCH_SERVLET_URL,
		 root :'telegraphCodes',
		 totalProperty :'totalCount',
		 id :'id',
		 fields:[
			 {name: 'code'},
			 {name: 'simplifiedChinese'},
			 {name: 'traditionalChinese'}
		 ]
	 };
	this.defaultStoreConfig.baseParams={
		cmd:'telegraphCode',
		action:'find'
	};
	this.defaultCmConfig = [{
				header : TXT.telegraphCode_code,
				dataIndex : 'code',
				menuDisabled : true,
				width : 50
			}, {
				header : TXT.simplifiedChinese,
				dataIndex : 'simplifiedChinese',
				menuDisabled : true,
				width : 100
			}, {
				header : TXT.traditionalChinese,
				dataIndex : 'traditionalChinese',
				menuDisabled : true,
				width : 100
			}];

	this.defaultGridConfig = { 
		title:TXT.system_menu_TelegraphCode,
		id : 'telegraphCodeGrid', 
		region :'center',
		stripeRows :true,
		pagination:true
	};
}
Ecp.TelegraphCodeGrid.prototype.show=function(){
	this.dataStore.store.load({params:{start:0, limit:PAGE_SIZE}});
}
Ecp.TelegraphCodeGrid.prototype.search=function(params){
	params['start']=0;
	params['limit']=PAGE_SIZE;
	this.dataStore.store['baseParams']=params;
	this.dataStore.store.load(params);
}
Ecp.extend(Ecp.TelegraphCodeGrid.prototype,new Ecp.ServiceGrid());

Ecp.TelegraphCodeInfoWindow=function()
{
	Ecp.ServiceWindow.call(this); 
	this.config={
	        width:380,
	        height:180,
	        autoScroll :false,
	        layout:'fit',
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
				text :TXT.common_btnClose,
				handler : closeTelegraphCodeInfoWindow
			 
			}]; 
	this.items=[];
}
Ecp.extend(Ecp.TelegraphCodeInfoWindow.prototype,new Ecp.ServiceWindow());

Ecp.TelegraphCodeInfoWindow.prototype.render=function()
{
	this.window=new Ecp.Window();
	this.window.init();
	var winObj={};
	winObj['config']=this.config;
	winObj['buttons']=this.buttons;
	winObj['items']=this.items;
	this.window.customization(winObj); 
	// window加全局，便于close
	Ecp.components.telegraphCodeInfoWindow = this.window;
	return this.window.render();
}

// 双击form
Ecp.TelegraphCodeInfoForm=function()
{
	Ecp.ServiceForm.call(this); 
	this.config={
		labelAlign: 'left',
		region: 'center',
		labelWidth:60,
		frame:true
	};
	this.items=[{
   				
   				fieldLabel: TXT.telegraphCode_code,
   				id: 'code',
   				name: 'code',
   				xtype : 'textfield', 
   				regex:/^\d+$/,
				maxLength:4,
				minLength:4,
   				disabled :true,
   				width: 230
   			  },{
 				xtype:'textfield', 
   				fieldLabel: TXT.simplifiedChinese,
   				id: 'simplifiedChinese',
   				name: 'simplifiedChinese',
   				disabled :true,
   				width: 230
   			  },{
 				xtype:'textfield', 
   				fieldLabel: TXT.traditionalChinese,
   				id: 'traditionalChinese',
   				name: 'traditionalChinese',
   				disabled :true,
   				width: 230
   			  }
	]; 
}
Ecp.extend(Ecp.TelegraphCodeInfoForm.prototype,new Ecp.ServiceForm());


//search window
Ecp.TelegraphCodeSearchWindow=function()
{
	Ecp.ServiceWindow.call(this); 
	this.config={
	        width:380,
	        height:180,
	        title:TXT.common_search,
	        autoScroll :false,
	        layout:'fit',
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
				text :TXT.common_btnClose,
				handler : closeTelegraphCodeSearchWindow
			 
			}]; 
	this.items=[];
}

Ecp.extend(Ecp.TelegraphCodeSearchWindow.prototype,new Ecp.ServiceWindow());

//search form
Ecp.TelegraphCodeSearchForm=function()
{
	Ecp.ServiceForm.call(this); 
	this.config={
		labelAlign: 'left',
		region: 'center',
		labelWidth:60,
		frame:true
	};
	this.items=[{
   				xtype : 'textfield', 
   				regex:/^\d+$/,
				maxLength:4,
   				fieldLabel: TXT.telegraphCode_code,
   				id: 'code',
   				name: 'code',
   				width: 230
   			  },{
 				xtype:'textfield', 
   				fieldLabel: TXT.simplifiedChinese,
   				id: 'simplifiedChinese',
   				name: 'simplifiedChinese',
   				regex:/^[\u4E00-\u9FA5]{0,1}$/i,
   				regexText:TXT.telegraphCode_code_only_one,
   				width: 230
   			  },{
 				xtype:'textfield', 
   				fieldLabel: TXT.traditionalChinese,
   				id: 'traditionalChinese',
   				name: 'traditionalChinese',
   				regex:/^[\u4E00-\u9FA5]{0,1}$/i,
   				regexText:TXT.telegraphCode_code_only_one,
   				width: 230
   			  }
	]; 
}
Ecp.extend(Ecp.TelegraphCodeSearchForm.prototype,new Ecp.ServiceForm());

Ecp.TelegraphCodeSearchWindow.prototype.render=function()
{
	this.window=new Ecp.Window();
	this.window.init();
	var winObj={};
	winObj['config']=this.config;
	winObj['buttons']=this.buttons;
	winObj['items']=this.items;
	this.window.customization(winObj); 
	// window加全局，便于close
	Ecp.components.telegraphCodeSearchWindow = this.window;
	//Ecp.components.telegraphCodeSearchForm	=this.form;		   
	return this.window.render();
}

// 双击创建window
Ecp.TelegraphCodeInfoWindow.createTelegraphCodeInfoWindow=function(){
	// form
	var telegraphCodeInfoForm=new Ecp.TelegraphCodeInfoForm();
	Ecp.components.telegraphCodeInfoForm = telegraphCodeInfoForm;
	// window
	var telegraphCodeInfoWin = new Ecp.TelegraphCodeInfoWindow();
	
	telegraphCodeInfoWin.items = [telegraphCodeInfoForm.render()]; 
	telegraphCodeInfoWin.config.height=180;
	telegraphCodeInfoWin.render(); 
	 
	return telegraphCodeInfoWin.window;
}


// 添加，修改共用此form
Ecp.TelegraphCodeEditForm=function()
{
	Ecp.ServiceForm.call(this); 
	this.config={
		labelAlign: 'left',
		region: 'center',
		labelWidth:60,
		frame:true
	};
	
	this.items=[{
				xtype : 'textfield', 
   				regex:/^\d+$/,
				fieldLabel: TXT.telegraphCode_code,
				name : 'code',
				id : 'code',
				allowBlank:false,
				maxLength:4,
				minLength:4,
				width: 230
			   },{
   				xtype:'textfield', 
   				fieldLabel: TXT.simplifiedChinese,
   				id: 'simplifiedChinese',
   				name: 'simplifiedChinese',
   				allowBlank:false,
   				regex:/^[\u4e00-\u9fa5]{1}$/,
   				regexText : TXT.put_chinese_sim,
   				width: 230
   			  },{
  				xtype:'textfield',
                fieldLabel: TXT.traditionalChinese,
				id: 'traditionalChinese',
				name: 'traditionalChinese', 
				allowBlank:false,
				regex:/^[\u4e00-\u9fa5]{1}$/,
   				regexText : TXT.put_chinese_fan,
				width: 230
  			  }
	]; 
}

Ecp.extend(Ecp.TelegraphCodeEditForm.prototype,new Ecp.ServiceForm());

// 修改   创建window
Ecp.TelegraphCodeInfoWindow.createTelegraphCodeEditWindow=function(){
	// form
	var telegraphCodeEditForm=new Ecp.TelegraphCodeEditForm();
 
	Ecp.components.telegraphCodeEditForm = telegraphCodeEditForm;  
	// window
	var telegraphCodeEditWin = new Ecp.TelegraphCodeInfoWindow();
	var button = [{
				  text :TXT.jdt_edit,
				  handler : editTelegraphCodeInfo
			     },{
				  text :TXT.common_btnClose,
				  handler : closeTelegraphCodeInfoWindow
			     }];
	telegraphCodeEditWin.buttons = button;
	telegraphCodeEditWin.items = [telegraphCodeEditForm.render()]; 
	telegraphCodeEditWin.render();
	return telegraphCodeEditWin.window;
}

// 添加   创建window
Ecp.TelegraphCodeInfoWindow.createTelegraphCodeAddWindow=function(){
	// form
	var telegraphCodeAddForm=new Ecp.TelegraphCodeEditForm();
	Ecp.components.telegraphCodeAddForm = telegraphCodeAddForm;  
	// window
	var telegraphCodeAddWin = new Ecp.TelegraphCodeInfoWindow();
	var button = [{
				  text :TXT.common_btnOK,
				  handler : addTelegraphCodeInfo
			     },{
				  text :TXT.common_btnClose,
				  handler : closeTelegraphCodeInfoWindow
			     }];
	telegraphCodeAddWin.buttons = button;
	telegraphCodeAddWin.items = [telegraphCodeAddForm.render()]; 
	telegraphCodeAddWin.render();
	return telegraphCodeAddWin.window;
}
//查询 创建 window
Ecp.TelegraphCodeSearchWindow.createTelegraphCodeSearchWindow=function(){
	// form
	var telegraphCodeSearchForm=new Ecp.TelegraphCodeSearchForm();
	Ecp.components.telegraphCodeSearchForm = telegraphCodeSearchForm;  
	// window
	var telegraphCodeSearchWin = new Ecp.TelegraphCodeSearchWindow();
	var button = [{
				  text :TXT.common_search,
				  handler : TelegraphCodeSearchBtn
			     },{
				  text :TXT.common_btnClose,
				  handler : closeTelegraphCodeSearchWindow
			     }];
	telegraphCodeSearchWin.buttons = button;
	telegraphCodeSearchWin.items = [telegraphCodeSearchForm.render()]; 
	telegraphCodeSearchWin.render();
	return telegraphCodeSearchWin.window;
}
