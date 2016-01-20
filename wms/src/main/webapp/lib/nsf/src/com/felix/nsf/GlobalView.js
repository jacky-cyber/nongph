com.felix.nsf.GlobalView = function(viewId, menuType, isMainPage, isLoginPage){
	//properties start
	this.mainPanel     = null;
	this.headerPanel   = null;
	this.footPanel     = null;
	this.globaView     = null;
	var mainToolBar    = null;
	this.isLoginPage   = isLoginPage;
	this.isMainPage    = isMainPage;
	this.menuType      = menuType;
	
	//method start
	var initMenu = function() {
		if(menuType==1){
			for( var i=0; i<menu_data.length; i++ ) {
				if( menu_data[i].id==viewId ) { 
					var text = menu_data[i].text;
					var targText = text.substring(text.indexOf('>')+1,text.indexOf('</'));
					menu_data[i].text = "<div><b>"+targText+"</b></div>";
				}
			}
		}
		mainToolBar = new com.felix.nsf.wrap.ToolBar();
		mainToolBar.setToolBarConfig( { id:'mainToolbar', cls :'top-toobar'} );
		mainToolBar.setToolBarItemsConfig( menu_data );
	};

	this.loadPage = function(node){
		//this.mainPanel.load({url: href});
		//this.mainPanel.getUpdater().refresh();
		//location.href=node.attributes.href+'&nodePath='+node.getPath();
		var fun = node.attributes.handler;
		if( fun )
			fun();
		else {
			if( isMainPage )
				this.mainPanel.setActiveTab( node.id+'Panel' );
			this.mainPanel.add( { title:node.text,
								  border:false,
								  id: node.id+'Panel',
								  closable:true,
								  iconCls:'tabs',
								  nodePath:node.getPath(),
								  html:"<iframe name='" + node.id + "' id='" + node.id + "' frameborder='0' width='100%' height='100%' src='"
										+ node.attributes.href+'&r='+new Date().getTime()+'&nodePath='+node.getPath() + "'></iframe>"
								} );
			
			opendPages.push( node.id );
			this.mainPanel.setActiveTab(node.id+'Panel');
		}
	}

	this.addModuleComp = function( comp ) {
		if(this.menuType==2) {
			comp.border=false;
			comp.closable=true;
			comp.iconCls= 'tabs';
		}
		this.mainPanel.add(comp);
	};
	
	this.render = function(title){
		var anchor = 'none -29';
		if( !Ext.isIE )
			anchor='none -28';
			
		//create header
		this.headerPanel = new Ext.Panel( { border :false,
											region :'north',
												layout :'anchor',
												items : [ {
													xtype :'box',
													//el :'header',
													html:"<div id='header'><div class='ecp-title'><b>Felix</b> <font class='ecp-subtitle'>Examinations and Interviews "+com.felix.nsf.GlobalConstants.APP_VERSION+"</font></div></div>",
													border :false,
													anchor :anchor
												} ]
											});
		if( menuType==1 )
			this.headerPanel.height=62;
		else if( menuType==2 )
			this.headerPanel.height=34;
		if( this.isLoginPage )
			this.headerPanel.height=36;
		
		//create foot
		var footerBar = new Ext.Toolbar( { border :true,
										   cls :'bottom-toolbar',
										   items : [ { border :true,
													   frame :true,
												       xtype :'box',
												       html:"<div id='footer'><div class='ecp-footer'>"+com.felix.nsf.GlobalConstants.APP_COPYRIGHT+"</div></div>",  
													   //el :'footer',
													   margins :'5 0 0 0',
													   cmargins :'5 0 0 0'
											} ]
										  } );
		this.footPanel = new Ext.Panel( { region :'south',
										  layout :'fit',
										  baseCls :'x-plain',
										  height :23,
										  items : [ footerBar ]
										});
		
		//create body
		var mainFrame=null;
		if( this.isLoginPage ) {
			this.mainPanel = new Ext.Panel( { border :false,
											  region :'center',
											  frame:true
											 } );
			mainFrame = new Ext.Viewport( {	layout :'border',
											items : [ this.headerPanel, this.mainPanel, this.footPanel ]
										  } );
			var divHeader = document.getElementById("header");
			if( com.felix.nsf.GlobalConstants.USING_SKIN && (document.cookie || navigator.cookieEnabled) )
				divHeader.innerHTML += "<div id='skin' class='ecp-logout-text' style='cursor:pointer'><img src='../../../../lib/ncs/images/skin.png'/></div>";
		} else {
			mainToolBar.setToolBarPremission( com.felix.nsf.Context.currentUser.privileges );
			if( menuType==1 ) {
				this.headerPanel.add( mainToolBar.getExtToolBar() );
				var mainFrame = new Ext.Viewport( {	layout :'border',
													items : [ this.headerPanel, this.mainPanel, this.footPanel ]
												   });
			} else if(menuType==2) {
				if(isMainPage) {
					var menuData = toolbarToTreeMenu( mainToolBar.toolBarItemsConfig, mainToolBar.toolBarPremissionConfig );
					var ecpMenuTree = new EcpMenuTreePanel(menuData);
					Ecp.components.ecpMenuTree = ecpMenuTree;
					ecpMenuTree.on( 'click', function( node, e ){
											         if( node.isLeaf() ) {
											            e.stopEvent();
											            Ecp.components.globalView.loadPage( node );
											            //location.href = node.attributes.href;
											         }
				    							} );
					mainFrame = new Ext.Viewport( {	layout :'border',
													items : [ this.headerPanel,ecpMenuTree, this.mainPanel, this.footPanel ]
												  } );
					
					var path = queryFromUrl('nodePath');
					ecpMenuTree.expandPath(path);
					ecpMenuTree.selectPath(path);
					ecpMenuTree.expandAll();
				} else {
					mainFrame = new Ext.Viewport( {	layout :'border',
													items : [ this.mainPanel ]
												   } );
				}
			}
			
			var divHeader = document.getElementById("header");
			if( divHeader )	{
				divHeader.innerHTML += "<div class='ecp-logout-text '>  [<a href='javascript:logout();' class='logout'>"+TXT.app_quit+"</a>]</div>";
				if( com.felix.nsf.GlobalConstants.USING_SKIN && (document.cookie || navigator.cookieEnabled))
					divHeader.innerHTML += "<div id='skin' class='ecp-logout-text' style='cursor:pointer'><img src='../../../../lib/ncs/images/skin.png'/></div>";
				divHeader.innerHTML += "<div class='ecp-logout-text '>"+com.felix.nsf.Context.currentUser.name+"@ "+com.felix.nsf.Context.currentUser.instName+"</div>";
			}
		}
		
		new Ext.ToolTip({
					target: 'skin',
					anchor: 'right',
					width: 140,
					autoHide: false,
					//closable: true,
					anchorOffset: 0,
					html:'<table width="100%" border="0" cellspacing="6" bgcolor="#FFFFFF">'
						  +'<tr>'
						  +'<td style="cursor:pointer" onclick=\'changeSkin("ext-all.css");\' height="25" width="25%" bgcolor="#D2E0F1">'
						  +'<td style="cursor:pointer" onclick=\'changeSkin("xtheme-gray.css");\' height="25" width="25%" bgcolor="#E9E9E9">'
						  +'<td style="cursor:pointer" onclick=\'changeSkin("xtheme-purple.css");\' height="25" width="25%" bgcolor="#CFC2FF">'
						  +'<td style="cursor:pointer" onclick=\'changeSkin("xtheme-green.css");\' height="25" width="25%" bgcolor="#A3D7C5">'
						  +'</tr>'
						  +'<tr>'
						  +'<td style="cursor:pointer" onclick=\'changeSkin("xtheme-pink.css");\' height="25" bgcolor="#E7C4DE">'
						  +'<td style="cursor:pointer" onclick=\'changeSkin("xtheme-olive.css");\' height="25" bgcolor="#9ACD68">'
						  +'<td style="cursor:pointer" onclick=\'changeSkin("xtheme-calista.css");\' height="25" bgcolor="#DBCA7C">'
						  +'<td style="cursor:pointer" onclick=\'changeSkin("xtheme-orange.css");\' height="25" bgcolor="#FF8C37">'
						  +'</tr>'
						  +'<tr>'
						  +'<td style="cursor:pointer" onclick=\'changeSkin("xtheme-darkgray.css");\' height="25" bgcolor="#858585">'
						  +'<td style="cursor:pointer" onclick=\'changeSkin("xtheme-slate.css");\' height="25" bgcolor="#485569">'
						  +'<td style="cursor:pointer" onclick=\'changeSkin("xtheme-indigo.css");\' height="25" bgcolor="#3D3D62">'
						  +'<td style="cursor:pointer" onclick=\'changeSkin("xtheme-midnight.css");\' height="25" bgcolor="#070B3A">'
						  +'</tr>'
						  +'</table>'
				});
		document.title = title;
	};

	// construct start
	if( menuType==1 ) {
		this.mainPanel = new Ext.Panel( { border :false,
										  region :'center',
										  layout :'fit',
										  margins :'5 5 5 5'} );
	} else if( isMainPage ) {
		this.mainPanel = new Ext.TabPanel( { activeTab: 0,
						                     plugins: new Ext.ux.TabCloseMenu(),
						                     margins :'5 5 5 0',
						                     region : 'center',
						                     enableTabScroll: true,
					    	                 listeners:{
					    					 	'tabchange': function(tab,p){
					    										if(p) {
					    											var path = '/root/welcomePage';
					    											if( p['nodePath'] )
							    										path = p['nodePath'];
																	Ecp.components.ecpMenuTree.expandPath( path );
																	Ecp.components.ecpMenuTree.selectPath( path );
																}
					    						}
					    					}
										   });
	} else {
		this.mainPanel = new Ext.Panel( { border :false,
										  region :'center',
										  layout :'fit',
										  margins :'0 0 0 0'
										});
	}
	
	if( this.isLoginPage===undefined ) {
		initMenu();
	}
}