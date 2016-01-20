/**
 * TaskTabpanel
 */
com.felix.eni.module.task.TaskNewInfoTabPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.TabPanel );

	this.setConfig({activeTab :0,
			        border :true,
			        frame :false,
			        height :300,
		            split:true,
		            minSize: 175,
		            maxSize: 400,
			        region :'south',
			        defaults : { autoScroll :false }
					});
	
	var nmGridPanel = new com.felix.eni.module.task.TaskNewMessageGridPanel();
	var ncGridPanel = new com.felix.eni.module.task.TaskNewCaseGridPanel();
	
	this.setItems([nmGridPanel, ncGridPanel])
}