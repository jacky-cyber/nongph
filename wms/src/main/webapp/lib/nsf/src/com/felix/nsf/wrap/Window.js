com.felix.nsf.wrap.Window = function() {
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.BaseWrap );
	/**
	 * the ext window object
	 */
	var extWindow=null;
	
	/**
	 * the config of the window, such as the height of the window
	 */
	var config={};
	
	/**
	 * the items set of the window
	 */
	var items=[];
	
	/**
	 * the buttons set of the window
	 */
	var buttons=[];
	
	var listeners = null;
	
	var closeConfirm = true;
	
	/**
	 * set the config of window
	 */
	this.setConfig = function(c){
		config = c;
	}
	
	this.getConfig = function(){
		return config;
	}
	/**
	 * set the items set of window
	 */
	this.setItems=function(i) {
		items = i;
		for(var i=0; i<items.length; i++)
			if( items[i].setParent )
				items[i].setParent( this );
	}
	
	this.addItem = function( item ) {
		items.push( item );
		if( item.setParent )
			item.setParent( this );
		if( extWindow != null )		
			extWindow.add( this.getExtComponent( item ) );
	};
	
	this.removeItem = function( item ) {
		for( var i=0; i<items.length; i++ ) {
			if( items[i]==item||items[i].super==item ) {
				items.splice(i, 1);
				return true;
			}
		}
		return false;
	}
	/**
	 * set the buttons of the window
	 */
	this.getButtons = function(){
		return buttons;
	}
	this.setButtons=function(b){
		buttons = b;
	}
	
	this.setCloseConfirm = function(cc){
		closeConfirm = cc;
	}
	
	this.setListeners = function( ls ) {
		listeners = ls;
	}
	/**
	 * render function
	 *
	 * return:
	 * the ext window
	 */
	this.getExtWindow = function() {	
		if( extWindow == null ) {		
			config['items'] = this.getExtComponent(items);
			config['buttons'] = buttons;
			
			config['listeners'] = listeners;
			config['shadow'] = false;
			config['autoDestroy'] = true;
			
			extWindow = new Ext.Window(config);
			if( closeConfirm ) {
				extWindow.addListener( 'beforeclose', this.confirmBeforeClose, this ); 
				extWindow.addListener( 'beforedestroy', this.beforeDestroy, this );
			}
		}
		return extWindow;
	};
	
	this.confirmBeforeClose = function(){
		if( closeConfirm ) {
			com.felix.nsf.MessageBox.confirm( TXT.common_close_window, function(){
																	       extWindow.destroy();
																	   } );
			return false;
		} else {
			return true;
		}
	};
	
	this.beforeDestroy = function(){
		return true;
	};
	
	this.focusFirstTextField = function() {			
		extWindow.on('show', function(){
			this.findByType('textfield')[0].focus(true, true);
		});
	};
	
	this.close = function(){
		extWindow.close();
	};
	
	this.hide = function(){
		extWindow.hide();
	};
	
	this.getItem = function(index) {
		return items[index];
	};
	
	this.getItems = function() {
		return items;
	};
	
	this.show = function(){
		this.getExtWindow().show();
	};
	
	this.refresh = function(){
		this.getExtWindow().doLayout();
	};
	
	this.maximize = function(){
		this.getExtWindow().maximize();
	};
}