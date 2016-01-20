com.felix.interview.module.interviewView.ViewToolbar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.ToolBar );
	
	var thoj = this;
	
	var doDelete = function(){
		var records = thoj.getParent().getSelections(true);
		if( records ) {
			var store = thoj.getParent().getExtGridPanel().getStore();
			for( var i=0; i<records.length; i++ )
				store.remove( records[i] );
		}
		thoj.getParent().getExtGridPanel().getView().refresh();
	};
	
	this.setToolBarConfig( {} );
	
	var items = [];
	items.push( {type:'button', text:TXT.interview_interviewView_dlt_interviewer, handler:'deleteAction',iconCls:'icoDel'} )
	this.setToolBarItemsConfig( items );
	
	this.setToolBarEvent( {deleteAction: doDelete} );
}