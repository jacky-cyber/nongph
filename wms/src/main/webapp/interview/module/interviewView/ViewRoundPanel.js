com.felix.interview.module.interviewView.ViewRoundPanel = function( ir ) {
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.BaseWrap );
	
	var config = {
			collapsible:false,
			closable:false,
			border  :true,
			margins:'0 0 5 0',
			title: ir.name + ' - ' +  TXT.interview_interviewView_round_type_view + ' - ' + ir.state
		};
	
	var form = new com.felix.interview.module.interviewView.ViewFormPanel( ir );
	var grid = new com.felix.interview.module.interviewView.ViewGridPanel( ir ); 
	grid.setParent( this );
	
	var extPanel;
    
	this.getExtPanel = function(){
        if( !extPanel ) {
            config["items"] = this.getExtComponent( [form,grid] );
            extPanel = new Ext.Panel( config );
        }
        return extPanel;
	};

   this.init = function(){
	   var interviewRoundUsers = ir.interviewRoundUsers;
	   for(var i=0; i<interviewRoundUsers.length; i++ ){
	      var iru = interviewRoundUsers[i];
	      grid.addInterviewer( iru.id, iru.user.name, iru.form.formTemplate.name, iru.state, iru.startTime, iru.finishTime, iru.form.formTemplate.id );
	    }
    };
};
