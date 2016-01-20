Ecp.components.globalView=new Ecp.GlobalView('system');
function currencyLayout(){
	var cmtTreeObj={};
	typeof cmt_inst_config=='undefined'?1:cmtTreeObj['cmtTreeColumn']=cmt_inst_config;
	typeof cmt_inst_columnconfig=='undefined'?1:cmtTreeObj['cmtConfig']=cmt_inst_columnconfig;
	typeof cmt_inst_eventconfig=='undefined'?1:cmtTreeObj['cmtTreeEvent']=cmt_inst_eventconfig;
	var toolBarCusObj={};
	var currencyToolBar=new Ecp.CurrencyToolBarWidget();
	currencyToolBar.setWidgetEvent({
		queryCurrency:function(){
			var reTree=tree.treeGrid;
			reTree.reload();
		}
	});
	currencyToolBar.customization(toolBarCusObj);
	
	var tree=new Ecp.InstitutionTreeGrid();
	tree.setTopToolBar(currencyToolBar);
	tree.customization(cmtTreeObj);
	Ecp.components.globalView.addModuleComp(tree.render());
	Ecp.components.globalView.render('Institution List');
};

Ext.onReady(currencyLayout);