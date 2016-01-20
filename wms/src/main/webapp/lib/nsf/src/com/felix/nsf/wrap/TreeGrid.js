com.felix.nsf.wrap.TreeGrid = function() {
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.BaseWrap );
	
	this.tree = null;
	this.topToolBar=null;
	this.treeColumnConfig = null;
	this.config = null;
	this.treeEvent = null;
	this.root = {expanded : true};
	this.eventBinder=new Ecp.EventBinder();
	this.isFirstLevelExpand=false;
	
	
	this.setTopToolBar = function(toolBar) {
		this.topToolBar=toolBar;
	}
	
	this.setTreeConfig = function(config) {
		this.config = config;
		if(this.config['dataUrl']!=null){
			this.config['loader']=new Ext.ux.tree.TreeGridLoader({
				dataUrl:this.config['dataUrl'],
				uiProviders : {
					'col' :Ext.ux.tree.TreeGridNodeUI
				},
				baseParams:this.config['baseParams']===undefined?{}:this.config['baseParams']
			});
			delete this.config['dataUrl'];
			if(this.config['baseParams']!=undefined)
				delete this.config['baseParams'];
		}
	}
	
	this.setTreeColumnConfig = function(column) {
		this.treeColumnConfig = column;
	}
	
	this.setTreeEventConfig = function(eventObj) {
		this.treeEvent = eventObj;
	}
	
	this.setRootData = function(rootData) {
		this.root['children'] = rootData;
	}
	
	this.reload=function(){
		this.tree['loader'].load(this.tree.root);
		
		//this.expendFirstLevel();
	}
	
	this.load=function(baseParams){
		this.tree['loader']['baseParams']=baseParams;
		this.reload();
	}
	
	this.expendFirstLevel=function(){
		this.tree.root['firstChild'].expand();
	}
	
	this.setIsFirstLevelExpand=function(isFirstLevelExpand){
		this.isFirstLevelExpand=isFirstLevelExpand;
	}
	
	this.getSelectedRecord=function(){
		var selectedNode=this.tree.getSelectionModel().getSelectedNode();
		if(selectedNode==null){
			Ext.MessageBox.alert(TXT.common_hint,TXT.common_selectOneRecord);
			return;
		}
		return this.tree.getSelectionModel().getSelectedNode()['attributes'];
	}
	
	this.customization = function(obj) {
		if (obj['cmtTreeColumn'] != undefined)
			this.treeColumnConfig = obj['cmtTreeColumn'];
		if (obj['cmtConfig'] != undefined)
			this.config = obj['config'];
		if(obj['cmtTreeEvent']!=null){
			Ext.iterate(obj['cmtTreeEvent'],function(key,value){
				if(this.treeEvent[key]!=null&&value==''){
					delete this.treeEvent[key];	
				}else
					this.treeEvent[key]=value;
			},this);
		}	
	}
	
	this.render = function() {
		this.config['columns'] = this.treeColumnConfig;
		this.config['root'] =  new Ext.tree.AsyncTreeNode(this.root);
		if(this.isFirstLevelExpand)
			this.config['loader'].addListener('load',function(){
				if(this.tree.root['firstChild']!=undefined)
					this.tree.root['firstChild'].expand();
			},this);
		if(this.topToolBar!=null)
			this.config['tbar']=this.topToolBar.render();
		this.tree = new Ext.ux.tree.TreeGrid(this.config);	
		this.eventBinder.bindEvent(this.tree,this.treeEvent);
		this.tree['ecpOwner']=this;
		return this.tree;
	}
}