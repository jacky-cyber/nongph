Ecp.components.globalView=new Ecp.GlobalView('system');
function messageTypeLayout(){
	var cmtStoreObj={};
	typeof cmt_message_type_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_message_type_dsConfig;
	typeof cmt_message_type_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_message_type_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_message_type_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_message_type_gridConfig;
	typeof cmt_message_type_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_message_type_cmConfig;
	typeof cmt_message_type_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_message_type_gridEventConfig;
	
	var toolBarCusObj={};
	typeof cmt_message_type_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_message_type_toolBarFn;
	typeof cmt_message_type_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_message_type_toolBarConfig;

	var messageTypeToolBar=new Ecp.MessageTypeToolBar();
	messageTypeToolBar.setWidgetEvent({editMessageType:showEditMessageTypeWin});
	messageTypeToolBar.customization(toolBarCusObj);
	
	var messageTypeGrid=new Ecp.MessageTypeGrid();
	messageTypeGrid.setToolBar(messageTypeToolBar.render());

	messageTypeGrid.setWidgetEvent({
		grid:{
			dblclick:showEditMessageTypeWin
		},
		store:{
			load:loadMessageTypeData
		}
	});
	messageTypeGrid.customization({
		store:cmtStoreObj,
		grid:cmtGridObj
	});
	
	Ecp.components.globalView.addModuleComp(messageTypeGrid.render());
	Ecp.components.globalView.render(TXT.msgType);
	messageTypeGrid.show();
	
	// register service widgets
	//Ecp.components.swiftServiceToolBar=swiftServiceToolBar;
	Ecp.components.messageTypeGrid=messageTypeGrid;
};

Ecp.onReady(messageTypeLayout);