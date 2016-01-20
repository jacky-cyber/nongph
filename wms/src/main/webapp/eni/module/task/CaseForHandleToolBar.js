com.felix.eni.module.task.CaseForHandleToolBar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	this.setToolBarItemConfig( [totalToolBarItem.caseSearchMenuForHandle] );
	
	this.setToolBarConfig({id:'caseForHandleToolBarWidget'});
}