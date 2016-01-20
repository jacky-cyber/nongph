com.felix.interview.module.interviewer.InterviewFormPanel = function( iv ) {
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	
	var thoj = this;
    
   this.setConfig( { title: TXT.interview_interviewer_candidate_info,
	                   labelAlign :'left',
                      layout:'form',
                      labelWidth :60,
                      margins:'0 0 5 0',
                      frame :true
	         			} );

	this.setItems( [{ id :'positionName',
						   name :'positionName',
						   xtype :'displayfield',
						   fieldLabel :TXT.interview_interviewer_position,
						   width :300,
						   value:iv.applyPosition.name
		                 },
	                { id :'candidateName',
					      name :'candidateName',
					      xtype :'displayfield',
					      fieldLabel :TXT.interview_interviewer_candidate,
					      width :300,
					      value:iv.candidate.name
	                     },
                   { id :'phone',
							name :'phone',
							xtype :'displayfield',
							fieldLabel :TXT.interview_interviewer_phone,
					      width :300,
							value:iv.candidate.phone
			              },
				        {id :'age',
							name :'age',
							xtype :'displayfield',
							fieldLabel :TXT.interview_interviewer_age,
						   width :300,
						   value:iv.candidate.age
			             }
	                 ]
		            );
		
		
    	this.getButtons().push( { xtype: 'button',
								  text : TXT.interview_interviewer_view_resume,
								  width: 100,
								  iconCls: 'icoAdd',
								  scope: this,
								  handler:function(){
									  this.getParent().cleanConversation();
								  }
								} );
		
}