com.felix.exam.module.examinationSelect.ExaminationSelectFormPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	
	var thoj = this;
	
	var positionStore = new com.felix.exam.module.position.PositionDataStore();
	positionStore.addListener("load", function(){
		thoj.setValue("positionId", thoj.getParent().getParent().getParent().getParent().getParent().getItem(0).getValues().positionId);
	});
	positionStore.load();
	
	this.setConfig( { labelAlign :'left',
					  region :'north',
					  layout:'column',
					  margins:'0 0 2 0',
					  cmargins:'0 0 2 0',
					  frame :true,
					  height:70,
					} );
	
	this.setItems( [ {
						columnWidth :.50,
						layout:'form',
						labelWidth :60,
						items:[
								{
									xtype :'combo',
									fieldLabel :TXT.exam_examination_select_position,
									id :'positionId',
									name :'positionId',
									store :positionStore,
									displayField :'name',
									valueField :'id',
									typeAhead :true,
									mode :'local',
									triggerAction :'all',
									tabIndex:2,
									width:180,
									readOnly:true
								}
						       ]
					  }, {
						columnWidth :.50,
						layout:'form',
						labelWidth :60,
						items:[
								{
									xtype:'textfield',
									fieldLabel: TXT.exam_examination_select_name,
									id :'pattern',
									name :'pattern',
									tabIndex:3,
									width:150
								}
						       ]
					}
				] );
	this.setButtons([{
		xtype: 'button',
		text : TXT.common_search,
		iconCls: 'icoFind',
		scope: this,
		handler: function(){
			var values = thoj.getValues();			
			thoj.getParent().getItem(1).search(values); 
		}
	}]);
}