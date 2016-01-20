Ecp.components.globalView=new Ecp.GlobalView('system');
function demoLayout(){
		var cPanel = new Ext.Panel({
					id : 'date',
					region : 'center',
					margins : '5 0 0 5',
					width : '70%',
					bodyStyle : {
						background : '#DFE8F6',
						padding : '7px'
					},
					autoLoad :{
						url : 'calendarDetail.html',
						scripts : true,
	                    scope : this
					}
				});
		var bottomPanel = new Ext.Panel({
					// frame : true,
					title : TXT.calendarMaintain,
					border : false,
					layout : 'border',
					items : [cPanel]
				});
		Ecp.components.globalView.addModuleComp(bottomPanel);
		Ecp.components.globalView.render(TXT.calendarMaintain);
}
Ecp.onReady(demoLayout);
