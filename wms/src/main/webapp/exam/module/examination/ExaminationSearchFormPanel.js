com.felix.exam.module.examination.ExaminationSearchFormPanel = function(){
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
						fieldLabel :TXT.exam_examination_position,
						id :'positionId',
						name :'positionId',
						store :positionStore,
						displayField :'name',
						valueField :'id',
						typeAhead :true,
						mode :'local',
						triggerAction :'all',
						tabIndex:2
					},{
						xtype:'textfield',
						fieldLabel: TXT.exam_examination_name,
						id :'pattern',
						name :'pattern',
						tabIndex:3
					}		
				] );
}