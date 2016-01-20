com.felix.interview.module.interviewView.ViewFormPanel = function(ir){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );

	this.setConfig( { labelAlign :'left',
					  layout:'form',
			  		  labelWidth :70,
			  		  frame :true
					} );
	
	this.setItems( [ { id:'id',
					   name: 'id',
					   xtype:"hidden",
					   value:ir.id
					 }, 
	                 { fieldLabel:TXT.interview_interview_date,
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
					  		     tabIndex:1,
					  		     value: ir.planStartTime?Date.parseDate( ir.planStartTime.substring(0,10), "Y-m-d"):'',
					  		     readOnly: ir.state=='NEW'||ir.state=='WAITING'||ir.state=='PENDING'?false:true
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
					                  tabIndex:2,
					                  value: ir.planStartTime?ir.planStartTime.substring(11,16):'',
					                  readOnly: ir.state=='NEW'||ir.state=='WAITING'||ir.state=='PENDING'?false:true
					              } ) ]
					 }
	                ] );
	
	if( ir.state=='NEW' ) {
		this.getButtons().push( { xtype: 'button',
						          text : TXT.commom_btnSave,
						          width: 100,
						          iconCls: 'icoOk',
						          scope: this,
						          handler: function(){
						              
						                 
						          }
						         } );
	}
	
	if( ir.state=='WAITING' || ir.state=='PENDING' ) {
 		this.getButtons().push( { xtype: 'button',
					              text : TXT.interview_interviewView_mark_absent,
					              width: 100,
					              iconCls: 'icoAdd',
					              scope: this,
					              handler: function(){
					                   
					                    
					              }
					            } );
 		this.getButtons().push( { xtype: 'button',
					              text : TXT.commom_btnSave,
					              width: 100,
					              iconCls: 'icoOk',
					              scope: this,
					              handler: function(){
					                
					                   
					              }
					             } );
 		this.getButtons().push( { xtype: 'button',
						          text : TXT.common_btnDelete,
						          width: 100,
						          iconCls: 'icoAdd',
						          scope: this,
						          handler: function(){
						               
						                  
						            }
						          } );
	}
};