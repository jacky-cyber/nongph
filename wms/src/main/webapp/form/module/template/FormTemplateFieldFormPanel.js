com.felix.form.module.template.FormTemplateFieldFormPanel = function(){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.FormPanel);
	var thoj = this;
	var afterSelectType = function(c, r, i){
		var gridPanel = thoj.getParent().getItem(1);
		var type = r.get("value");
        if( type=='comboBox' || type =='checkBox' )	
        	gridPanel.getExtGridPanel().show();
        else
        	gridPanel.getExtGridPanel().hide();
        	
        if( type=='text')
    		thoj.getExtFormPanel().findById('textInfo').show();
        else
        	thoj.getExtFormPanel().findById('textInfo').hide();
	};
	
	this.setConfig( { labelAlign :'left',
					  region :'center',
					  layout:'form',
					  labelWidth :70,
					  margins:'0 0 5 0',
					  frame :true,
					  autoScroll:true
					} );
	
	var baseItems = [{ id:'rowIndex',
					   name: 'rowIndex',
					   xtype:"hidden"
					 },
	                 { id:'id',
					   name: 'id',
					   xtype:"hidden"
					 },      
					 { id:'type',
				       name:'type',
				       xtype:'combo',
				       fieldLabel :TXT.form_template_field_type,
				       width :280,
					   tabIndex:1,
					   mode: 'local',
					   store: new Ext.data.ArrayStore( {id: 0,
													    fields: ['value', 'label'],
													    data: [ ['text', 'text'],
													            ['textarea', 'textarea'],
													            ['number','number'],
													            ['hidden','hidden'],
													            ['date', 'date'],
													            ['comboBox', 'comboBox'],
													            ['checkBox', 'checkBox']
													            ]
													    } ),
					   valueField: 'value',
					   displayField: 'label',
					   triggerAction:'all',
					   editable:false,
					   allowBlank:false,
					   listeners:{
						   'select':afterSelectType
					   }
					 },
			         { id :'name',
					   name :'name',
					   xtype :'textfield',
					   fieldLabel :TXT.form_template_field_name,
					   width :280,
					   tabIndex:2,
					   allowBlank:false
			         },
			         { id :'label',
					   name :'label',
					   xtype :'textfield',
					   fieldLabel :TXT.form_template_field_label,
					   width :280,
					   tabIndex:3,
					   allowBlank:false
			         },
			         { id:'readOnly',
				       name:'readOnly',
				       xtype:'combo',
				       fieldLabel :TXT.form_template_field_readOnly,
				       width :280,
					   tabIndex:4,
					   mode: 'local',
					   store: new Ext.data.ArrayStore( {id: 0,
													    fields: ['value', 'label'],
													    data: [ ['false', '否'],
													            ['true', '是']
													            ]
													    } ),
					   valueField: 'value',
					   displayField: 'label',
					   triggerAction:'all',
					   editable:false,
					   allowBlank:false
					 },
					 { id:'mandatory',
				       name:'mandatory',
				       xtype:'combo',
				       fieldLabel :TXT.form_template_field_mandatory,
				       width :280,
					   tabIndex:5,
					   mode: 'local',
					   store: new Ext.data.ArrayStore( {id: 0,
													    fields: ['value', 'label'],
													    data: [ ['false', '否'],
													            ['true', '是']
													            ]
													    } ),
					   valueField: 'value',
					   displayField: 'label',
					   triggerAction:'all',
					   editable:false,
					   allowBlank:false
					 },
					 { id :'initValue',
					   name :'initValue',
					   xtype :'textfield',
					   fieldLabel :TXT.form_template_field_initValue,
					   width :280,
					   tabIndex:6
			         },
			         { id :'action',
					   name :'action',
					   xtype :'textarea',
					   fieldLabel :TXT.form_template_field_action,
					   width :280,
					   height:160,
					   tabIndex:7
			         } ];
	var textItems = [ { id :'maxSize',
					    name :'maxSize',
					    xtype :'numberfield',
					    fieldLabel :'最大长度',
					    width :280,
					    tabIndex:8,
					    allowNegative:false, 
					    allowDecimals:false,
						minValue:1,
						maxValue:500
				      },
				      { id :'regex',
				    	name :'regex',
				    	xtype :'textfield',
				    	fieldLabel:"正则表达式",
				    	width :280,
				    	tabIndex:9
				      },
				      { id :'regexText',
				    	name :'regexText',
				    	xtype :'textfield',
				    	fieldLabel :"错误提示",
				    	width :280,
				    	tabIndex:10
				      } ];
	 
	this.setItems( [ { xtype:'fieldset',
        			   title: '基本信息',
        			   collapsible: true,
        			   autoHeight:true,
        			   width:400,
        			   items : baseItems 
        			 },
        			 { id: 'textInfo', 
        			   xtype:'fieldset',
        			   title: '扩展信息',
	                   collapsible: true,
	                   autoHeight:true,
	                   width:400,
	                   items:textItems
        			 } ] );
	this.clearSuffix();
	
	this.setValues = function(json){
		this.super.setValues(json);
		var type = this.getExtFormPanel().findById('type'); 
		type.fireEvent( 'select', null, new baseItems[2].store.recordType( {value: json.type} ) );
	}
}