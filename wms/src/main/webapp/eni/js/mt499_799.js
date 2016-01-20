/* change by sunyue 20121212 */
Ecp.components.globalView=new Ecp.GlobalView('mt499_799');
function demoLayout(){
	var mt499_799Grid=new Ecp.MT499_799Grid();
	
	var mt499_799GridToolbar=new Ecp.MT499_799GridToolbar();
	
	mt499_799GridToolbar.setWidgetEvent({
		mt499_799Find:showFinMT499_799Win,
		mt499_799Abolish:clickAbolishMT499_799Btn
	});
	
	mt499_799Grid.setToolBar(mt499_799GridToolbar.render());
	
	mt499_799Grid.setWidgetEvent({
		grid:{
			dblclick:showMT499_799ContentWin
		},
		store:{
			load:loadMT499_799Data
		}
	});
	
	Ecp.components.globalView.addModuleComp(mt499_799Grid.render());
	Ecp.components.globalView.render(TXT.mt499_799);
	mt499_799Grid.show();
	
	Ecp.components.mt499_799Grid=mt499_799Grid;
};

Ecp.onReady(demoLayout);
