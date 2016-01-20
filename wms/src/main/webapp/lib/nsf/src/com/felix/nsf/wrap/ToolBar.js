com.felix.nsf.wrap.ToolBar = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.BaseWrap );
	
	//Ext.ToolBar
	var extToolBar = null;
	
	//premission config
	var toolBarPremissionConfig = {};
	
	//toolBar config
	var toolBarConfig = null;
	
	//json object set up properties
	var toolBarItemsConfig = null;
	
	//toolBar event config
	var toolBarEventConfig = null;
	
	/**
	 * configure the toolBar.
	 * @param {} config
	 */
	this.setToolBarConfig = function(config){
		toolBarConfig = config;
		toolBarConfig['items']=[];
	};
	
	/**
	 * configure the toolBar's items including menus and buttons.
	 * @param {} itemsConfig
	 */
	this.setToolBarItemsConfig = function(itemsConfig){
		toolBarItemsConfig=itemsConfig;
	};
	/**
	 * set the premission as the decesion of whether the button shows.
	 * @param {} serveResult
	 */
	this.setToolBarPremission = function(serveResult){
		toolBarPremissionConfig = serveResult;
	};
	/**
	 * configure the event of toolBar such as click and so on.
	 * @param {} toolBarEventConfig
	 */
	this.setToolBarEvent = function( ec ) {
		toolBarEventConfig = ec;
	};
	
	this.addToolBarEvent = function(key, value){
		toolBarEventConfig[key]=value;
	};
	
	this.deleteToolBarEvent = function(key){
		delete toolBarEventConfig[key];	
	};
	
	/**
	 * util function to travel the menu tree
	 * @param {} menu childrens
	 */
	this.treeWalker = function( menu, childrens ) {
		Ext.each( childrens, function(element){
				if( element.code==null || (element.code!=null&&  toolBarPremissionConfig.indexOf(element.code)>=0 )){
					if( element.type == 'button' ) {
						var buttonConfig = { id:element.id,	text:element.text };
						if( element.iconCls )
							buttonConfig.iconCls = element.iconCls;
						if( element.url!=null )
							buttonConfig.handler = function() { location.href = element.url+'?locale='+locale; };
						if( element.handler!=null ) {
							if( typeof element.handler=='function')
								buttonConfig.handler = element.handler;
							else
								buttonConfig.handler = toolBarEventConfig[ element.handler ];
						}
						menu.add( buttonConfig );
					} else {
						if(element.code==null||(element.code!=null&&this.toolBarPremissionConfig[element.code]!=null)){
							var subMenu = new Ext.menu.Menu( { id:element.id+'menu' } );
							this.treeWalker( subMenu, element.childrens );
							if( subMenu.items!=undefined ) {
								var menuConfig = { id: element.id, text: element.text, menu: subMenu };
								if( element.iconCls!=null )
									menuConfig.iconCls = element.iconCls;
								menu.add( menuConfig );
							}	
						}
					}
				}
			}, this );
	};
	
	this.getSortParams = function(){
		var doSortParams=[];
		
		if( toolBarConfig['id'] ) {
			var toolbarItems = Ext.getCmp( toolBarConfig['id'] ).items.items;
			for( var i=0; i<toolbarItems.length; i++ ) {
				if( toolbarItems[i].pressed && toolbarItems[i].sortingOrderId && toolbarItems[i].sortingOrderId.startWith('sortingOrder_') ){
					var sortData = toolbarItems[i].sortData;
					sortData['idx']=i;
					doSortParams.push( sortData );
				}
			}
			return doSortParams;
		}
		return null;
	};

	this.getExtToolBar = function(){
		if( extToolBar!=null )
			return extToolBar;
			
		Ext.each( toolBarItemsConfig, function(element, index) {
			if( element.code==null || (element.code!=null&&  toolBarPremissionConfig.indexOf(element.code)>=0 )) {
				var toolBarItem=null;
				if( element.type=='button' ) {
					toolBarItem = new Ext.Button( { id:element.id, text:element.text} );
					if( element.iconCls!=null )
						toolBarItem.iconCls = element.iconCls;
					if( element.url!=null )
						toolBarItem.handler = function(){
											  	location.href = element.url+'?locale='+locale;
											  };
					if( element.handler!=null ) {
						toolBarItem.handler = toolBarEventConfig[ element.handler ];
						if( element.shortcut ) {
							element.shortcut['fn'] = eval( element.handler );
							element.shortcut['scope'] = this;
							com.felix.nsf.ShortcutMap.add( element.shortcut );
						}
					}
					toolBarConfig['items'].push( toolBarItem );
				} else if( element.type=='menu' ) {
					if(element.code==null||(element.code!=null&& toolBarPremissionConfig[element.code]!=null)){
						var menu = new Ext.menu.Menu( {id:element.id+'menu'} );
						this.treeWalker( menu, element.childrens );
						if( menu.items!=undefined ) {
							toolBarItem = new Ext.Button( {	id:element.id, text:element.text, menu:menu	} );
							if( element.iconCls!=null )
								toolBarItem.iconCls = element.iconCls;
							toolBarConfig['items'].push( toolBarItem );
						}	
					}
				} else {
					toolBarConfig['items'].push(element);
				}
			}
		},this);
		
		var items = toolBarConfig['items'];
		var length = items.length;
		for( var i=1; i<length; i++ ) {
			if( items[2*i-1]=='->' ) {
				length=-1;
				continue;
			}
			items.splice( 2*i-1, 0, '-' );
		}
		extToolBar = new Ext.Toolbar( toolBarConfig );
		return extToolBar;
	};
	
	this.disable = function(){
		this.getExtToolBar().disable();
	}
};