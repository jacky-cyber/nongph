com.felix.interview.module.interview.DateTimeFormPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	

	this.setConfig( { labelAlign :'left',
					  layout:'form',
			  		  labelWidth :70,
			  		  frame :true
					} );

	this.setItems( [ { fieldLabel:TXT.interview_interview_date,
					   xtype: 'compositefield',
					   combineErrors: false,
					   width :300,
					   items: [
					           { id :'interviewDate',
					         	 name :'interviewDate',
					  		     xtype :'datefield',
					  		     width :120,
					  		     format :'Y-m-d',
					  		     allowBlank:false,
					  		     tabIndex:1
					  		   }, 
					  		   { xtype: 'displayfield',
					  			 width :50,  
					             value: '&nbsp;&nbsp;&nbsp;&nbsp;时间:'
					           },
					  		   new Ext.form.TimeField( {
					      			  id :'interviewTime',
								      name :'interviewTime',
					                  width :120,
					                  allowBlank:false,
					                  minValue: '9:00am',
					                  maxValue: '5:00pm',
					                  increment: 30,
					                  format:'H:i',
					                  tabIndex:2
					              } ) ]
					 }
	                ] );
};