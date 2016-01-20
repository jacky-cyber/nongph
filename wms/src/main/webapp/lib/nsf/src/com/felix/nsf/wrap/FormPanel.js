com.felix.nsf.wrap.FormPanel = function() {
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.BaseWrap );
	/**
	 * the original formPanel object of ext
	 */
	var extFormPanel=null;
	
	/**
	 * the config information of the formPanel, such as the height of the formPanel
	 */
	var config={};
	
	/**
	 * the items set of the formPanel
	 */
	var items=[];
	
	/**
	 * the buttons of formPanel
	 */
	var buttons=[];
	
	/**
	 * add the unique suffix for every field of the formPanel
	 */
	var suffix = com.felix.nsf.wrap.FormPanel.uniqueStr + new Date().getTime() + '_' + (com.felix.nsf.wrap.FormPanel.fieldIdSuffixIdx++);
	
	/**
	 * set the config information of the form
	 */
	this.setConfig = function( c ) {
		config = c;
	}
	
	/**
	 * set the items set of the form
	 */
	this.setItems = function(is) {
		items = is;
	}
	
	this.getItems = function(){
		return items;
	};
	/**
	 * set the buttons of the form
	 */
	this.setButtons = function( bs ){
		buttons = bs;
	}
	
	this.getButtons = function(){
		return buttons;
	}
	/**
	 * find the value by the form's field id
	 * params: fieldName: the field id
	 * return: the field value
	 */
	this.getValueById = function(fieldName){
		var field = this.findFieldById(fieldName);
		if( field==null )
			return '';
		else if( field instanceof Ext.form.DateField )
			return field.getRawValue();
		else if( field instanceof Ext.form.CheckboxGroup ) {
			var result = [];
			var cbs = field.getValue();
			for( var i=0; i<cbs.length; i++) {
				result.push( cbs[i].getName().substring( 0, cbs[i].getName().indexOf(suffix) ) );
			}
			return result;
		} else
			return field.getValue();
	}
	
	this.findRawValueById=function(fieldName){
		return this.findFieldById(fieldName).getRawValue();
	}
	
	this.findFieldById = function(fieldName){
		return extFormPanel.getForm().findField(fieldName+suffix);
	}
	
	/**
	 * validate whether every field of the form is valid
	 *
	 * return
	 * true if the form is valid, otherwise false
	 */
	this.isValid = function(){
		return extFormPanel.form.isValid();
	}
	
	this.setReadOnly = function(){
		this.setItemsReadOnly( items );
	}
	
	this.setItemsReadOnly = function(its){
		for( var i=0; i<its.length; i++ ) {
			var item = its[i];
			if(typeof item.name != 'undefined' )
				this.setFieldReadOnly( item.name.substring(0,item.name.indexOf(suffix)), true );
			if( item.items )
				this.setItemsReadOnly( item.items );
		}
	}
	
	var changeFieldAttribute = function( its ){	
		for( var i=0; i<its.length; i++ ) {
			var it = its[i];
			if(typeof it.id!='undefined') {
				if( it.id.indexOf(com.felix.nsf.wrap.FormPanel.uniqueStr)==-1)
					it.id += suffix;
				else
					it.id = it.id.substring(0, it.id.indexOf(com.felix.nsf.wrap.FormPanel.uniqueStr) ) + suffix;
			}
			if(typeof it.name!='undefined') {
				if( it.name.indexOf( com.felix.nsf.wrap.FormPanel.uniqueStr)==-1 )
					it.name += suffix;
				else
					it.name = it.name.substring(0, it.name.indexOf(com.felix.nsf.wrap.FormPanel.uniqueStr) ) + suffix;
			}
			
			if( typeof it.items!='undefined' )	
				changeFieldAttribute( it.items );
		}
	}
		
	/**
	 * render function
	 * return: the ext's form
	 */
	this.getExtFormPanel = function() {
		if( extFormPanel == null) {
			changeFieldAttribute( items );
			
			config['items'] = items;
			if( buttons!=null && buttons.length>0 )
				config['buttons'] = buttons;
			config['autoDestroy']=true;
			
			extFormPanel = new Ext.FormPanel( config );
		}
		return extFormPanel;
	}

	/**
	 * set the values of the form
	 *
	 * params:
	 * json: key of the json is the field's id and value is the field's value
	 */
	this.setValues = function(json) {
		for( var id in json ) 
			this.setValue(id, json[id])
	}
	
	this.setValue = function( fieldName, value ) {
		var field = this.findFieldById( fieldName );
		if( field!=null ) {
			if( field instanceof Ext.form.CheckboxGroup ) {
				for( var i=0; i<value.length; i++) {
					field.eachItem(function(item){
						if( item.getName()==value[i]+suffix ) {
							item.setValue(true);
							return false;
						}
					});
				}
			} else {	
				field.setValue(value);
			}
		}
	}
	
	/**
	 * get the values of form
	 * 
	 * params:
	 * asString: boolean type
	 *
	 * return:
	 * json object, key of the json is the field's id and value is the field's value if asString is true, url string otherwise
	 */
	this.getValues = function(asString) {
		var values={};
		this.getItemValues(items,values);
		if(!asString)
			return values;
		return Ext.urlEncode(values);
	}
	
	this.getItemValues = function( items, values ) {
		for( var i=0; i<items.length; i++ ) {
			if( typeof items[i]['id']!='undefined' ) {
				var id = items[i]['id'];
				id = id.substring( 0, id.lastIndexOf(suffix) );
				values[id] = this.getValueById(id);
			}
			if( typeof items[i]['items']!='undefined' )
				this.getItemValues(items[i]['items'],values);
		}
	}
	
	/**
	 * set whether one filed of the form is readonly
	 *
	 * params:
	 * fieldName: the id of the field
	 * isReadOnly: true is readonly, otherwise is editable
	 */
	this.setFieldReadOnly = function( fieldName, isReadOnly ) {
		this.findFieldById(fieldName).setReadOnly(isReadOnly);
	}
	
	this.setAllowBlank = function(fieldName, isAllowBlank) {
		this.findFieldById(fieldName).allowBlank =isAllowBlank;
	}
	
	/**
	 * reset all fields
	 */
	this.reset=function(){
		extFormPanel.form.reset();
	}
	
	this.clearSuffix = function(){
		suffix = '';
	}
};
com.felix.nsf.wrap.FormPanel.uniqueStr='__$';
com.felix.nsf.wrap.FormPanel.fieldIdSuffixIdx = 0;