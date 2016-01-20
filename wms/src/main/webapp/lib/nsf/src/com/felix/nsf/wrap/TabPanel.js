com.felix.nsf.wrap.TabPanel = function() {
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.BaseWrap );
	
	var extTabPanel=null;
	var config={};
	var items=[];

	this.setConfig = function( cfg ) {
		config = cfg;
	}
	
	this.setItems = function( its )	{
		items = its;
	}

	this.getExtTabPanel = function() {
		if( extTabPanel == null ) {
			config['items'] = this.getExtComponent( items );
			extTabPanel = new Ext.TabPanel( config );
		}
		return extTabPanel;
	}
	
	this.getItem = function(index) {
		return items[index];
	}
	
	this.reset = function(){
		extTabPanel.setActiveTab(0);
	}
}