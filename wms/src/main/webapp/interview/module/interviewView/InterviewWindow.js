com.felix.interview.module.interviewView.InterviewWindow = function( iv ) {
	com.felix.nsf.Util.extend( this, com.felix.nsf.base.ServiceWindow );
	
	var thoj = this;
	
	var form = new com.felix.interview.module.interviewView.InterviewFormPanel( iv );
	
	var clickCloseBtn = function() {
		thoj.close();
	};
	
	this.setConfig( { title :TXT.interview_interviewView_view,
					  width :600,
					  height:600,
					  autoScroll :true,
					  border :false,
					  resizable :true,
					  modal:true,
					  minimizable :false,
					  maximizable :true,
					  shadow: false,
					  closeAction:'close',
					  buttonAlign :'right',
					  layout: { type:'vbox',
                                padding:'5, 20, 5, 5',
                                align:'stretch'
                                }
					} );
	
	this.setItems( [form] );
	
	this.setButtons( [ { text :TXT.common_btnClose, handler : clickCloseBtn } 
					 ] );
	
	this.setCloseConfirm( false );
	
	for(var i=0; i<iv.rounds.length; i++){
		  var roundPanel;
		  var round = iv.rounds[i];
        if( round.state=='WAITING' || round.state=='PENDING' ) {
        	   if( round.type==='EXAM' ) {
                roundPanel = new com.felix.interview.module.interview.ExamRoundPanel();
                roundPanel.setTitle( round.name +' - ' + TXT.interview_interview_round_type_exam);
            } else {
                roundPanel = new com.felix.interview.module.interview.ViewRoundPanel();
                roundPanel.setTitle( round.name +' - ' + TXT.interview_interview_round_type_view);
                }
        	   roundPanel.getConfig().closable = false;
        	   delete roundPanel.getConfig().tools;
        	   if( round.state=='PENDING' ) {
        		    roundPanel.getForm().getButtons().push( { xtype: 'button',
															             text : TXT.interview_interviewView_mark_absent,
															             width: 100,
															             iconCls: 'icoAdd',
															             scope: this,
															             handler: function(){
															            	 this.markAbsent( round.id );
															              	 }
															               } );
        	    }
        	   roundPanel.getForm().getButtons().push( { xtype: 'button',
													               text : TXT.commom_btnSave,
													               width: 100,
													               iconCls: 'icoOk',
													               scope: this,
													               handler: function(){
													                   	       this.saveRound( round.id, roundPanel );
													              		            }
													                  } );
        	   roundPanel.getForm().getButtons().push( { xtype: 'button',
													          	   text : TXT.common_btnDelete,
													               width: 100,
													               iconCls: 'icoAdd',
													               scope: this,
													               handler: function(){
													            	             this.deleteRound( round.id );
													               					}
		            												  } );
        } else {
            if( round.type==='EXAM' ) {
                 roundPanel = new com.felix.interview.module.interviewView.ExamRoundPanel( round );
            } else {
                 roundPanel = new com.felix.interview.module.interviewView.ViewRoundPanel( round );
             	}
           }
        this.addItem( roundPanel );
    }
	
	this.show = function(){
		this.getExtWindow().show( null, function(){
	        for(var i=0; i<iv.rounds.length; i++){
	            var round = iv.rounds[i];
	            this.getItem(i+1).init( round );
	        }
	    }, this );
	}
	
	this.addRound = function( round ){
		if( round.isValid() ) {
			 var reqParam = {id:iv.id};
		    reqParam.rounds =  Ext.encode( [round.toJSON()] );
	       com.felix.nsf.Ajax.requestWithMessageBox("interviewAction", 'addRounds',function(result){
	                  if( result.state==='SUCCESS' ) {
	                      this.setCloseConfirm( false );
	                      this.close();
	                      this.getParent().reload();
	                  } else {
	                      var message = TXT['interview_interview_error_'+result.errorDesc.code];
	                      if( !message )
	                            message = result.errorDesc.desc;
	                      com.felix.nsf.MessageBox.alert( message );
	                  }
	          }, reqParam, this);
		}
	}
	
	this.saveRound = function(id, roundPanel){
		 if( roundPanel.isValid() ) {
			 var reqParam = {roundId:id};
		    reqParam.round = Ext.encode( roundPanel.toJSON() );
	       com.felix.nsf.Ajax.requestWithMessageBox("interviewAction", 'saveRound',function(result){
	                  if( result.state==='SUCCESS' ) {
	                      this.setCloseConfirm( false );
	                      this.close();
	                  } else {
	                      var message = TXT['interview_interview_error_'+result.errorDesc.code];
	                      if( !message )
	                            message = result.errorDesc.desc;
	                      com.felix.nsf.MessageBox.alert( message );
	                  }
	          }, reqParam, this);
		 }
	}
	
	this.deleteRound = function( id ){
		 var reqParam = {roundId:id};
       com.felix.nsf.Ajax.requestWithMessageBox("interviewAction", 'deleteRound',function(result){
                  if( result.state==='SUCCESS' ) {
                      this.setCloseConfirm( false );
                      this.close();
                  } else {
                      var message = TXT['interview_interview_error_'+result.errorDesc.code];
                      if( !message )
                            message = result.errorDesc.desc;
                      com.felix.nsf.MessageBox.alert( message );
                  }
          }, reqParam, this);
	}
	
	this.cleanConversation = function(){
		var reqParam = {id:iv.id};
      com.felix.nsf.Ajax.requestWithMessageBox("interviewAction", 'cleanConversation',function(result){
              if( result.state==='SUCCESS' ) {
                  this.setCloseConfirm( false );
                  this.close();
              } else {
                  var message = TXT['interview_interview_error_'+result.errorDesc.code];
                  if( !message )
                	  message = result.errorDesc.desc;
                  com.felix.nsf.MessageBox.alert( message );
                  }
          }, reqParam, this);
	}
	
	this.markGoing = function( ireID ) {
	    var reqParam = {id:ireID};
	    com.felix.nsf.Ajax.requestWithMessageBox("interviewAction", 'markGoing',function(result){
	              if( result.state==='SUCCESS' ) {
	                  this.setCloseConfirm( false );
	                  this.close();
	              } else {
	                  var message = TXT['interview_interview_error_'+result.errorDesc.code];
	                  if( !message )
	                	  message = result.errorDesc.desc;
	                  com.felix.nsf.MessageBox.alert( message );
	                  }
	          }, reqParam, this);
	}
	
	this.markPending = function( ireID ) {
	    var reqParam = {id:ireID};
	    com.felix.nsf.Ajax.requestWithMessageBox("interviewAction", 'markPending',function(result){
	              if( result.state==='SUCCESS' ) {
	                  this.setCloseConfirm( false );
	                  this.close();
	              } else {
	                  var message = TXT['interview_interview_error_'+result.errorDesc.code];
	                  if( !message )
	                	  message = result.errorDesc.desc;
	                  com.felix.nsf.MessageBox.alert( message );
	                  }
	          }, reqParam, this);
	}
	
	this.markAbsent = function( roundId ) {
	    var reqParam = {roundId:roundId};
	    com.felix.nsf.Ajax.requestWithMessageBox("interviewAction", 'markAbsent',function(result){
	              if( result.state==='SUCCESS' ) {
	                  this.setCloseConfirm( false );
	                  this.close();
	              } else {
	                  var message = TXT['interview_interview_error_'+result.errorDesc.code];
	                  if( !message )
	                	  message = result.errorDesc.desc;
	                  com.felix.nsf.MessageBox.alert( message );
	                  }
	          }, reqParam, this);
	}
}