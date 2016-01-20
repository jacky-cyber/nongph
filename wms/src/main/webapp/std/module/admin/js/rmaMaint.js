Ecp.components.globalView=new Ecp.GlobalView('system');
function RmaSearchLayout() {
	var rmaSearchGrid = new Ecp.RmaSearchGrid();
	rmaSearchGrid.setWidgetEvent({
		grid : {
		},
		store : {
			load : 	loadRmaData
		}
	});
	Ecp.components.rmaSearchGrid = rmaSearchGrid;
	var rmaMaintWidgetToolBar=new Ecp.RmaMaintWidgetToolBar();
	Ecp.components.rmaMaintWidgetToolBar=rmaMaintWidgetToolBar; 
	rmaMaintWidgetToolBar.setWidgetEvent({ 
		queryRmaMaint: clickQueryRmaMaintBtn,
		importRmaMaint:clickRmaMaintImportBtn
	}); 
	rmaSearchGrid.setToolBar(rmaMaintWidgetToolBar.render());
	Ecp.components.globalView.addModuleComp(rmaSearchGrid.render());
	Ecp.components.globalView.render(TXT.systemparam);
	rmaSearchGrid.show();
	Ecp.components.rmaSearchGrid=rmaSearchGrid;
};
Ecp.onReady(RmaSearchLayout);