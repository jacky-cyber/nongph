com.felix.interview.module.candidate.CandidateSearchFormPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
		
	this.setConfig( { labelAlign :'left',
					  region :'center',
					  labelWidth :70,
					  layout:'form',
					  margins:'0 0 2 0',
					  cmargins:'0 0 2 0',
					  frame :true
	});
	
	this.setItems( [
					{
						xtype:'textfield',
						fieldLabel: TXT.common_human_name,
						id :'pattern',
						name :'pattern',
						tabIndex:3
					}		
				] );
}