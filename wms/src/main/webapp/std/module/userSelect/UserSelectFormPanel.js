com.felix.std.module.userSelect.UserSelectFormPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	
	var thoj = this;
		
	this.setConfig( { labelAlign :'left',
                          region :'north',
                          layout:'form',
                          margins:'0 0 2 0',
                          cmargins:'0 0 2 0',
                          frame :true,
                          height:70,
                          labelWidth:70
                          } );

	this.setItems( [ {
	        id: 'name',
	        name: 'name',
                fieldLabel: TXT.std_userSelect_name,
                xtype: 'textfield',
	        allowBlank:false,
	        width:100
                } ] );
            
	this.setButtons( [ {
		xtype: 'button',
		text : TXT.common_search,
		iconCls: 'icoFind',
		scope: this,
		handler: function(){
			var values = thoj.getValues();			
			thoj.getParent().getItem(1).search(values); 
		}
	} ] );
};