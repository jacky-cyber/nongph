Ecp.components.globalView=new Ecp.GlobalView('system');
function institutionLayout(){
	// 1、initialize json data
	var cmtTreeObj={};
	typeof cmt_inst_config=='undefined'?1:cmtTreeObj['cmtTreeColumn']=cmt_inst_config;
	typeof cmt_inst_columnconfig=='undefined'?1:cmtTreeObj['cmtConfig']=cmt_inst_columnconfig;
	typeof cmt_inst_eventconfig=='undefined'?1:cmtTreeObj['cmtTreeEvent']=cmt_inst_eventconfig;
	
	var toolBarCusObj={};
	typeof cmt_institution_toolBarFn=='undefined'?1:toolBarCusObj['cmt_toolBar']=cmt_institution_toolBarFn;
	typeof cmt_institution_toolBarConfig=='undefined'?1:toolBarCusObj['cmt_toolBar_eventConfig']=cmt_institution_toolBarConfig;
	
	// 2、initialize widget
	// initialize toolbar
	var institutionToolBar=new Ecp.InstitutionToolBarWidget();
	institutionToolBar.setWidgetEvent({
		//addInstitution:showAddInstitWin,
		//editInstitution:showEditInstitWin,
		//deleteInstitution:clickDeleteInstitBtn,
		showInstitution:showEditInstitWin,
		queryInstitution:showQueryInstitWin
	});
	institutionToolBar.customization(toolBarCusObj);
	
	// initialize grid
	var institutionGrid=new Ecp.InstitutionGridWidget();
	institutionGrid.setToolBar(institutionToolBar);
	// add grid event
//	institutionGrid.setTreeGridEvent({
//		grid:{
//			dblclick:showEditInstitWin
//		}
//	});
	// Project Customization
	institutionGrid.customization(cmtTreeObj);
	
	// 3、display main panel
	Ecp.components.globalView.addModuleComp(institutionGrid.render());
	Ecp.components.globalView.render(TXT.inst);

	// register service widgets
	//Ecp.components.institutionToolBar=institutionToolBar;
	Ecp.components.institutionGrid=institutionGrid;
};

Ecp.onReady(institutionLayout);