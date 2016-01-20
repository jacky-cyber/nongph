com.felix.nsf.ShortcutMap = (function(thoj){
	var keyMap = new Ext.KeyMap(Ext.getDoc());
	
	thoj.add = function( shortcut ) {
		keyMap.addBinding( shortcut );
	};
	
	return thoj;
})({});