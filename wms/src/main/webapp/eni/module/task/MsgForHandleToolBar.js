com.felix.eni.module.task.MsgForHandleToolBar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	this.setToolBarItemsConfig([ totalToolBarItem.messageSearchMenuForHandle ]);
	
	this.setToolBarConfig( {id:'msgForHandleToolBar'});
}