com.felix.std.module.role.RoleFormPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	
	this.setConfig( { id: 'roleForm',
					  baseCls: 'x-plain',
					  height:   155,
					  labelAlign: 'left',
					  defaultType: 'textfield',
					  frame: false,
					  labelWidth:150,
					  layout: 'form',
					  region: 'north'
					} );
	
	this.setItems( [ { fieldLabel: TXT.role_name,
	        		   id:'name',
	        		   name: 'name',
	        		   allowBlank:false,
	        		   width:200 },
	        		 { fieldLabel: TXT.role_desc,
	        		   id: 'description',
	        		   name: 'description',
	        		   width:200 },
	        		 { id:'id',
	        		   name: 'id',
	        		   xtype : 'hidden' }
	        		] );
};