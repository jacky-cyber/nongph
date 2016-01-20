com.felix.eni.module.task.TaskNewCaseGridPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.eni.module.caseManage.CaseGridPanel );
	
	this.getGridConfig().title = TXT.task_new_case;
	this.getGridConfig.border=false;
	
	this.setGridEventConfig( { dblclick:showCaseInNewCaseListWin } );
	this.getDataStore().setEventConfig( { load: function(){ setTitle( this.getTotalCount() ) } } );
	
	var thoj = this;
	var setTitle = function(totalCount){
		thoj.getExtGridPanel().setTitle( TXT.task_new_case + '('+ totalCount + ')' );
	}
}