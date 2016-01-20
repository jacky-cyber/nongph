Ecp.components.globalView=new Ecp.GlobalView('message');
function messageLayout(){
	var cmtStoreObj={};
	typeof cmt_message_dsConfig=='undefined'?1:cmtStoreObj['cmt_dsConfig']=cmt_message_dsConfig;
	typeof cmt_message_dsEventConfig=='undefined'?1:cmtStoreObj['cmt_ds_eventConfig']=cmt_message_dsEventConfig;
	
	var cmtGridObj={};
	typeof cmt_message_gridConfig=='undefined'?1:cmtGridObj['cmt_gridConfig']=cmt_message_gridConfig;
	typeof cmt_message_cmConfig=='undefined'?1:cmtGridObj['cmt_cmConfig']=cmt_message_cmConfig;
	typeof cmt_message_gridEventConfig=='undefined'?1:cmtGridObj['cmt_grid_eventConfig']=cmt_message_gridEventConfig;
	
	var toolBarCusObj={};
	typeof cmt_message_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_message_toolBarFn;
	typeof cmt_message_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_message_toolBarConfig;
	
	var messageToolBar=new Ecp.MessageToolBarWidget();
	messageToolBar.defaultToolBarConfig.id='messageListToolBar';
	messageToolBar.setWidgetEvent({
		searchMessage:showSearchMessageWin,
		searchNotwithMessage:showSearchNotwithMessageWin,
		searchOutcomingMessage:searchOutcomingMessage,
		messageCase:showCaseInMessageListWin,
//		messageNotRead:clickMessageIsReadWin,
		messageRead:clickMessageReadWin,
		copyMessage:showCopyMessageWinInMessageList,
		auditMessageRecord:showAuditMessageRecordWin,
		nakErrorCode:showNakErrorCodeWin,
		relateMessage:clickRelateMessageBtn,
		relateOutcomingMessage:relateOutcomingMessage
	});
	messageToolBar.setPremission(Ecp.userInfomation.menu);
	messageToolBar.customization(toolBarCusObj);
	
	var messageGrid=new Ecp.MessageGridWidget();
	messageGrid.defaultGridConfig.id='msgGridListId';
	messageGrid.setToolBar(messageToolBar.render());
	messageGrid.setWidgetEvent({
		grid:{
			//click:showMessageWin,
			dblclick:showMessageWin
		},
		sm:{
			rowselect:selectMessageRecord
		},
		store:{
			load:function(){
				messageGrid.grid.selectFirstRow()
			}
		}
	});
	messageGrid.customization({
		store:cmtStoreObj,
		grid:cmtGridObj
	});
	
	Ecp.components.globalView.addModuleComp(messageGrid.render());
	Ecp.components.globalView.render(TXT.message);
	
	messageGrid.show();
	
	// register service widgets
	Ecp.components.messageToolBar=messageToolBar;
	Ecp.components.messageGrid=messageGrid;
};

Ecp.onReady(messageLayout);