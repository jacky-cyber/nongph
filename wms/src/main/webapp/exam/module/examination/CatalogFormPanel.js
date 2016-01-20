com.felix.exam.module.examination.CatalogFormPanel = function(){
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
						xtype :'textfield',
						fieldLabel :TXT.exam_examination_catalog_name,
						id :'catalogName',
						name :'catalogName',
						tabIndex:1,
						allowBlank:false,
						width :400
	                },{
						xtype:'textarea',
						fieldLabel: TXT.exam_examination_catalog_desc,
						id :'catalogDesc',
						name :'catalogDesc',
						tabIndex:2,
						allowBlank:false,
						width :400,
						heigth:200
	                }		
	                ] );
}