com.felix.form.module.template.FormTemplateFormPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	
	var thoj = this;

	this.setConfig( { labelAlign :'left',
					  region :'center',
					  layout:'form',
					  labelWidth :60,
					  margins:'0 0 5 0',
					  frame :true
					} );

	this.setItems( [ { id:'id',
					   name: 'id',
					   xtype:"hidden"
					 },      
	                 { id :'name',
					   name :'name',
					   xtype :'textfield',
					   fieldLabel :TXT.form_template_name,
					   width :300,
					   tabIndex:1,
					   allowBlank:false
	                 },
	                 { id:'layout',
		               name:'layout',
		               xtype:'combo',
		               fieldLabel :TXT.form_template_layout,
	                   width :300,
					   tabIndex:2,
					   mode: 'local',
					   store: new Ext.data.ArrayStore( {id: 0,
												        fields: ['value', 'label'],
												        data: [ ['HORIZONTAL', 'HORIZONTAL'], 
												                ['VERTICAL', 'VERTICAL']
												                ]
												       } ),
					    valueField: 'value',
					    displayField: 'label',
					    triggerAction:'all',
					    editable:false
     				  }]
		            );
}