com.felix.exam.module.examination.ExaminationFormPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	var thoj = this;
	
	var positionStore = new com.felix.exam.module.position.PositionDataStore();
	positionStore.addListener('load', com.felix.nsf.Context.currentComponents.setPositionSyncUnit.addEvent());
	positionStore.load();
	
	this.setConfig( { labelAlign :'left',
			  region :'center',
			  layout:'column',
			  height:70,
			  margins:'0 0 5 0',
			  frame :true
	});

	this.setItems( [
	                { columnWidth :.38,
	                  layout:'form',
	                  labelWidth :60,
	                  items:[ { id:'id',
								name: 'id',
								xtype:"hidden"
							  },
	                          { id :'positionId',
								name :'positionId',
								xtype :'combo',
								fieldLabel :TXT.exam_examination_position,
								width :200,
								tabIndex:1,
								allowBlank:false,
								store :positionStore,
								mode :'local',
								displayField :'name',
								valueField :'id',
								readOnly: true
			                   } ] },
	                 { columnWidth :.40,
			           layout:'form',
			           labelWidth :50,
	                   items:[ { id :'name',
								 name :'name',
								 xtype :'textfield',
								 fieldLabel :TXT.exam_examination_name,
								 width :200,
								 tabIndex:2,
								 allowBlank:false
			                	} ] }, 
		             { columnWidth :.22,
			           layout:'form',
			           labelWidth :70,
		               items:[ { id :'time',
								 name :'time',
								 xtype :'numberfield',
								 fieldLabel :TXT.exam_examination_time,
								 width :60,
								 tabIndex:3,
								 allowBlank:false,
								 allowDecimals :false,
								 minValue :0
		                	    } ] }
		              
	                ] );
	this.setButtons([{
		xtype: 'button',
		text : TXT.exam_examination_add_catalog,
		width: 100,
		iconCls: 'icoAdd',
		scope: this,
		handler: function(){
			var catalog = new com.felix.exam.module.examination.CatalogPanel();
			this.getParent().addItem( catalog );
			this.getParent().refresh();
		}
	}]);
}