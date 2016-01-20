Ecp.components.globalView=new Ecp.GlobalView('system');
function messageTemplateLayout(){
	// 1、initialize json data
	var cmtStoreObj={};
	typeof cmt_messageTemplate_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_messageTemplate_dsConfig;
	typeof cmt_messageTemplate_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_messageTemplate_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_messageTemplate_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_messageTemplate_gridConfig;
	typeof cmt_messageTemplate_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_messageTemplate_cmConfig;
	typeof cmt_messageTemplate_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_messageTemplate_gridEventConfig;
	
	var toolBarCusObj={};
	typeof cmt_messageTemplate_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_messageTemplate_toolBarFn;
	typeof cmt_messageTemplate_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_messageTemplate_toolBarConfig;
	
	// 2、initialize widget
	// initialize toolbar
	var messageTempToolBar=new Ecp.MessageTemplateToolBarWidget();
	messageTempToolBar.setWidgetEvent({
		editMessageTemp:showEditMessageTempWin
	});
	messageTempToolBar.customization(toolBarCusObj);
	
	// initialize grid
	var messageTempGrid=new Ecp.MessageTemplateGridWidget();
	messageTempGrid.setToolBar(messageTempToolBar.render());
	// add grid event
	messageTempGrid.setWidgetEvent({
		grid:{
			// double click event
			dblclick:showEditMessageTempWin
		},
		store:{
			load:loadMessageTemplateData
		}
	});
	// Project Customization
	messageTempGrid.customization({
		store:cmtStoreObj,
		grid:cmtGridObj
	});
	
	// 3、Display main panel
	Ecp.components.globalView.addModuleComp(messageTempGrid.render());
	Ecp.components.globalView.render(TXT.msgTemp);
	messageTempGrid.show();
	
	// register service widgets
	//Ecp.components.messageTempToolBar=messageTempToolBar;
	Ecp.components.messageTempGrid=messageTempGrid;
};

Ecp.onReady(messageTemplateLayout);