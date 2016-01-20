/**
 * TaskHistoryWindow
 */
com.felix.eni.module.task.TaskHistoryWindow = function() {
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.Window);
	
	var taskHistoryGridPanel = new com.felix.eni.module.task.TaskHistoryGridPanel();
	
	this.setConfig({width :800,
					height :450,
					autoScroll :false,
					layout :'border',
					border :false,
					title:TXT.task_detail,
					minimizable :false,
					maximizable :false,
					resizable :false,
					modal :true,
					closeAction: 'hide',
					layoutConfig : { animate :false	},
					buttonAlign :'center'
					});
	this.setButtons( [ {
					   text :TXT.common_btnClose,
					   handler : function() {
					  				var win=this['ecpOwner'];
									win.window.close();
					  				}	
					  } ] );

	this.setItems([taskHistoryGridPanel.getExtGridPanel()]);
	
}