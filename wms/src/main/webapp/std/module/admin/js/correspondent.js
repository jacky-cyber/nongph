Ecp.components.globalView=new Ecp.GlobalView('system');
function correspondentLayout(){

	var correspondentToolBar=new Ecp.CorrespondentToolBar();
	correspondentToolBar.setWidgetEvent({
		queryCorrespondent:showQueryCorrespondentWin,
		addCorrespondent:showAddCorrespondentWin,
		editCorrespondent:showEditCorrespondentWin,
		deleteCorrespondent:showDeleteCorrespondentWin,
		editCorrespondentPDF:showUploadFileWin,
		queryCorrespondentPDF:downloadFile
	});

	var correspondentGrid=new Ecp.CorrespondentGrid();
	correspondentGrid.setToolBar(correspondentToolBar.render());

	correspondentGrid.setWidgetEvent({
		grid:{
			dblclick:showEditCorrespondentWin,
			click:changeAddOrEdit
		},
		store:{
			load:loadCorrespondentData
		}
	});
	Ecp.components.globalView.addModuleComp(correspondentGrid.render());
	Ecp.components.globalView.render(TXT.cor);
	correspondentGrid.show();
	Ecp.components.correspondentToolBar=correspondentToolBar;
	Ecp.components.correspondentGrid=correspondentGrid;
};

Ecp.onReady(correspondentLayout);