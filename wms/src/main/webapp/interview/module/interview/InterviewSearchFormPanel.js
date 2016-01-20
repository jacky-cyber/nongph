com.felix.interview.module.interview.InterviewSearchFormPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	
	var positionStore = new com.felix.exam.module.position.PositionDataStore();
	positionStore.load();
	
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
						xtype :'combo',
						fieldLabel :TXT.interview_interview_position,
						id :'searchCriteria.positionId',
						name :'searchCriteria.positionId',
						store :positionStore,
						displayField :'name',
						valueField :'id',
						typeAhead :true,
						mode :'local',
						triggerAction :'all',
						tabIndex:1
					}, {
						xtype :'datefield',
						fieldLabel :TXT.interview_interview_date_start,
						format :'Y-m-d',
						id :'searchCriteria.startBegin',
						name :'searchCriteria.startBegin'
					}, {
						xtype :'datefield',
						fieldLabel :TXT.interview_interview_date_end,
						format :'Y-m-d',
						id :'searchCriteria.startEnd',
						name :'searchCriteria.startEnd'
					}, {
						xtype:'textfield',
						fieldLabel: TXT.common_human_name,
						id :'searchCriteria.pattern',
						name :'searchCriteria.pattern',
						tabIndex:2
					}		
				] );
}