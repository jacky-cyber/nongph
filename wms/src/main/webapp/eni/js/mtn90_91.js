/* change by sunyue 20121212 */
Ecp.components.globalView=new Ecp.GlobalView('mtn90_91');
function demoLayout(){
	var mtn90_91Grid=new Ecp.MTn90_91Grid();
	
	var mtn90_91GridToolbar=new Ecp.MTn90_91GridToolbar();
	
	mtn90_91GridToolbar.setWidgetEvent({
			mtn90_91Find: showFindMTn90_91Win,
			mtn90_91Read: clickMessageReadWin,
			mtn90_91AssignCase: showAssignMessageWin
	});
	mtn90_91Grid.setToolBar(mtn90_91GridToolbar.render());
	
	mtn90_91Grid.setWidgetEvent({
		grid:{
			dblclick:showMTn90_91ContentWin,
			click:clickRecordForRead
		},
		store:{
			load:loadMTn90_91Data
		}
	});
	
	Ecp.components.globalView.addModuleComp(mtn90_91Grid.render());
	Ecp.components.globalView.render(TXT.mtn90_91);
	mtn90_91Grid.show();
	Ecp.components.mtn90_91GridToolbar=mtn90_91GridToolbar;
	Ecp.components.mtn90_91Grid=mtn90_91Grid;
};
Ecp.onReady(demoLayout);
