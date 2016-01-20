com.felix.interview.module.interview.InterviewFormPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	
	var thoj = this;
	var afterGetExamRoundName = function(btn, text){
            if( btn =='ok' ) {
                text = text.trim();
                if( text ) { 
                    var round = new com.felix.interview.module.interview.ExamRoundPanel();
                    round.setTitle(text +' - ' + round.getTitle());
                    thoj.getParent().addItem( round );
                    thoj.getParent().refresh();
                }
            }
        };    
        
        var afterGetViewRoundName = function(btn, text){
             if( btn =='ok' ) {
                text = text.trim();
                if( text ) { 
                    var round = new com.felix.interview.module.interview.ViewRoundPanel();
                    round.setTitle(text +' - ' + round.getTitle());
                    thoj.getParent().addItem( round );
                    thoj.getParent().refresh();
                }
            }
        };   

	this.setConfig( { labelAlign :'left',
                          layout:'form',
                          labelWidth :60,
                          margins:'0 0 5 0',
                          frame :true
		         } );

	this.setItems( [ { id:'id',
					   name: 'id',
					   xtype:"hidden"
					 },      
					 { id:'candidateId',
					   name: 'candidateId',
					   xtype:"hidden"
					 },
					 { id:'positionId',
					   name: 'positionId',
					   xtype:"hidden"
					 },
	                 { id :'candidateName',
					   name :'candidateName',
					   xtype :'displayfield',
					   fieldLabel :TXT.interview_interview_candidate,
					   width :300,
					   tabIndex:1,
					   readOnly: true
	                 },
	                 { id :'positionName',
					   name :'positionName',
					   xtype :'displayfield',
					   fieldLabel :TXT.interview_interview_position,
					   width :300,
					   tabIndex:2,
					   readOnly: true
	                 },
	                 { id :'loginName',
					   name :'loginName',
					   xtype :'textfield',
					   fieldLabel :TXT.interview_interview_loginName,
					   width :300,
					   tabIndex:3,
					   allowBlank:false
	                 },
	                 { id :'loginPassword',
					   name :'loginPassword',
					   xtype :'textfield',
					   fieldLabel :TXT.interview_interview_loginPassword,
					   width :300,
					   tabIndex:4,
					   allowBlank:false
	                 } ]
		            );
    	this.setButtons([
            { xtype: 'button',
              text : TXT.interview_interview_round_add_exam,
              width: 100,
	      iconCls: 'icoAdd',
              scope: this,
              handler: function(){
                    Ext.MessageBox.prompt(TXT.interview_interview_round_add_exam, 
                                          TXT.interview_interview_round_set_name, 
                                          afterGetExamRoundName);
                    
		}
            },
            { xtype: 'button',
	      text : TXT.interview_interview_round_add_view,
	      width: 100,
	      iconCls: 'icoAdd',
	      scope: this,
              handler: function(){
			 Ext.MessageBox.prompt(TXT.interview_interview_round_add_view, 
                                          TXT.interview_interview_round_set_name, 
                                          afterGetViewRoundName);
              }
	} ] );
}