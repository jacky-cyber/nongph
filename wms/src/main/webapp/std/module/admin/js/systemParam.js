Ecp.components.globalView=new Ecp.GlobalView('system');
function demoLayout(){
	var systemtParamtsGrid=new Ecp.SystemtParamtsGrid();
	var systemtParamtsGridToolbar=new Ecp.SystemtParamtsGridToolbar();
	systemtParamtsGridToolbar.setWidgetEvent({
			querySystemParam: clickShowQuerySystemParamWin,
			addSystemParam: clickAddSystemParamWin,
			deleteSystemParam: clickDeleteSystemParam
	});
	systemtParamtsGrid.setToolBar(systemtParamtsGridToolbar.render());
	systemtParamtsGrid.setWidgetEvent({
		grid:{
			dblclick:dblclickEditSystemParamWin
		},
		store:{
			load:loadSystemParam
		}
	});
	Ecp.components.globalView.addModuleComp(systemtParamtsGrid.render());
	Ecp.components.globalView.render(TXT.systemparam);
	systemtParamtsGrid.show();
	Ecp.components.systemtParamtsGrid=systemtParamtsGrid;
}
Ecp.onReady(demoLayout);
