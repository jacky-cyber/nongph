Ecp.components.globalView = new Ecp.GlobalView('system');
function telegraphCodeLayout() {
	var telegraphCodeToolBar=new Ecp.TelegraphCodeToolBar();
	var telegraphCodeGrid = new Ecp.TelegraphCodeGrid();
	telegraphCodeToolBar.setWidgetEvent({ 
		//addTelegraphCode:showAddTelegraphCodeWin,
		//editTelegraphCode:showEditTelegraphCodeWin,
		//delTelegraphCode:showConfirmDel,
		searchTelegraphCode:showSearchTelegraphCodeWindow
	}); 
	telegraphCodeGrid.setToolBar(telegraphCodeToolBar.render());
	telegraphCodeGrid.setWidgetEvent({
		grid : {
			//dblclick : showTelegraphCodeDetailInfo
		},
		store : {
			load : loadTelegraphCodeData
		}
	});
	Ecp.components.globalView.addModuleComp(telegraphCodeGrid.render());
	Ecp.components.globalView.render(TXT.message_Telegraph_Code);
	telegraphCodeGrid.show();
	Ecp.components.telegraphCodeGrid = telegraphCodeGrid;
};
Ecp.onReady(telegraphCodeLayout);