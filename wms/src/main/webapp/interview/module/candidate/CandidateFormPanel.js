com.felix.interview.module.candidate.CandidateFormPanel = function(candidate){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	var thoj = this;
	
	this.setConfig( { labelAlign :'left',
			  region :'center',
			  layout:'form',
			  height:70,
			  margins:'0 0 5 0',
			  frame :true,
			  fileUpload: true
	});
	
	this.setItems( [ { id:'id',
					   name: 'id',
					   xtype:"hidden"
					 },        
	                 { id :'name',
					   name :'name',
					   xtype :'textfield',
					   fieldLabel :TXT.common_human_name,
					   width :200,
					   tabIndex:1,
					   allowBlank:false
	                 },
	                 { id :'idCode',
					   name :'idCode',
					   xtype :'textfield',
					   fieldLabel :TXT.interview_candidate_id,
					   width :200,
					   tabIndex:2,
					   allowBlank:false
	                 },
	                 { id :'phone',
					   name :'phone',
					   xtype :'textfield',
					   fieldLabel :TXT.interview_candidate_phone,
					   width :200,
					   tabIndex:3,
					   allowBlank:false
	                 },
		             { id :'age',
					   name :'age',
					   xtype :'numberfield',
					   fieldLabel :TXT.interview_candidate_age,
					   width :200,
					   tabIndex:4,
					   allowBlank:false,
					   allowDecimals :false,
					   minValue :0
		             },
		             { id: 'resume',
		               name: 'resume',
		               xtype: 'fileuploadfield',
		               fieldLabel: TXT.interview_candidate_resume,
		               emptyText: 'Select an resume',
		               buttonText: '',
		               buttonCfg: { iconCls: 'icoUpload' },
		               width :200
		             }] );
	if( candidate ) {
		this.getItems()[0].value=candidate.id;
		this.getItems()[1].value=candidate.name;
		this.getItems()[2].value=candidate.idCode;
		this.getItems()[3].value=candidate.phone;
		this.getItems()[4].value=candidate.age;
		this.getItems().splice(5,1);
	}
	
	this.clearSuffix();
}