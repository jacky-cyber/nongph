com.felix.exam.module.positionSelect.SelectPositionFormPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	
	var positionStore = new com.felix.exam.module.position.PositionDataStore();
	
	this.setConfig( { labelAlign :'left',
			  region :'center',
			  labelWidth :70,
			  layout:'form',
			  margins:'0 0 5 0',
			  frame :true
	});

	this.setItems( [
	                {
	                	id :'positionId',
						name :'positionId',
						xtype :'combo',
						fieldLabel :TXT.exam_positionSelect_position,
						width :200,
						tabIndex:2,
						allowBlank:false,
						store :positionStore,
						mode :'remote',
						displayField :'name',
						valueField :'id',
						triggerAction :'all',
						typeAhead :true,
						forceSelection: true,
						emptyText: TXT.exam_examination_select_positon
					}
	                ] );
}