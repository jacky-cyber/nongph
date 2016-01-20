/**
 * com.felix.nsf.wrap.GridPanel
 * 
 * usage:as a gridPanel,wrap Ext.grid.GridPanel
 * 
 * @augments grid gridConfig gridCompConfig gridEventConfig cmGridEventConfig eventBinder
 */
com.felix.nsf.wrap.GridPanel=function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.BaseWrap );
	
	//wraped Ext.grid.GridPanel
	var extGridPanel = null;
	
	var gridConfig = {};
	
	//configuration for Ext.grid.GridPanel
	var border, frame, loadMask, stripeRows;
	
	//configuration of Ext.grid.Grid panel's sub component such as ds,cm and so on.
	var dataStore = new com.felix.nsf.wrap.DataStore();
	 
	var columnModelConfig, selectModel, tbar;
	
	var pagination = true;	
	var editable = false;
	
	//event of grid
	var gridEventConfig = null;
	//event of grid's column such as 'headerClick'
	var cmEventConfig = null;
	//event of grid's selectModel such as 'headerClick'
	var smEventConfig = null;
	
	this.setGridConfig = function(gc){
		gridConfig = gc;
	};
	
	this.getGridConfig = function(){
		return gridConfig;
	};

	this.getDataStore = function(){
		return dataStore;
	};
	
	this.setDataStore = function( store ) {
		dataStore = store;
	}
	
	/**
	 * configuration of grid's column
	 * 
	 * @param {} columnConfigArray
	 * the system will automaticlly add a sequence column beyond the first column 
	 */
	this.getColumnModelConfig = function(){
		return columnModelConfig;
	}
	
	this.setColumnModelConfig = function(columnConfigArray){
		columnConfigArray.splice( 0, 0, new Ext.grid.RowNumberer() );
		columnModelConfig = columnConfigArray;
	};
	
	/**
	 * set the select mode of grid
	 * @param {} mode
	 * 
	 * 0 means using single selection ,1 means using mutiple selection ,2 means using mutiple selection by the way of clcik the checkBox
	 */
	this.setSelectModel = function(mode){
		if( mode==0 )
			selectModel = new Ext.grid.RowSelectionModel( {singleSelect:true} );
		else if( mode==1 )
			selectModel = new Ext.grid.RowSelectionModel( {} );
		else if( mode==2 )
			selectModel = new Ext.grid.CheckboxSelectionModel();
		else if( mode==3 )
			selectModel = new Ext.grid.CellSelectionModel();
		
		if( mode==2 )
			columnModelConfig.splice( 1, 0, selectModel );
		
	};

	this.getTopToolBar = function(){
		return tbar;
	}
	
	this.setTopToolBar = function(topToolBar){
		tbar = topToolBar;
		topToolBar.setParent( this );
	};
	
	this.setFrame = function( f ) {
		frame = f;
	};
	
	this.setLoadMask = function( lm ) {
		loadMask = lm;
	};
	
	this.setStripeRows = function( sr ) {
		stripeRows = sr;
	};
	
	this.setPagination = function( p ) {
		pagination = p;
	};
	
	this.setEditable = function( ea ) {
		editable = ea;
	};

	this.setGridEventConfig = function(config){
		if( config['keyboard'] ) {
			if( Ext.isIE )
				config['keydown']=config['keyboard'];
			else
				config['keypress']=config['keyboard'];
		}
		
		gridEventConfig = config;
	};

	this.setCmEventConfig = function( cmConfig ) {
		cmEventConfig = cmConfig;
	};
	
	this.setSmEventConfig = function( smConfig ) {
		smEventConfig = smConfig;
	};
	
	this.getDataCount = function(){
		if( pagination )
			return extGridPanel.store.getTotalCount();
		else
			return extGridPanel.store.getCount();
	};
	
	this.getSelectedId = function(isAlert){
		if(!this.mandatorySelect(isAlert))
			return false;
		return extGridPanel.selModel.getSelected().id;
	}
	
	this.getSelectedIds = function(isAlert){
		if(!this.mandatorySelect(isAlert))
			return false;
		var recordArray = extGridPanel.selModel.getSelections();
		var idArray=[];
		for(var i=0;i<recordArray.length;i++){
			idArray.push(recordArray[i].id);
		}
		return idArray;
	}
	
	this.getSelectedRecordData = function(isAlert){
		if(!this.mandatorySelect(isAlert))
			return false;
      return extGridPanel.selModel.getSelected().data;
	}
	
	this.getSelectedRecordDatas = function(isAlert){
		if(!this.mandatorySelect(isAlert))
			return false;
		var recordArray = extGridPanel.selModel.getSelections();
		var dataArray=[];
		for(var i=0;i<recordArray.length;i++){
			dataArray.push(recordArray[i].data);
		}
		return dataArray;
	}
	
	this.getSelected = function(isAlert){
		if( !this.mandatorySelect(isAlert) )
			return false;
		
		if( selectModel instanceof Ext.grid.CellSelectionModel )
			return selectModel.getSelectedCell();
		else
			return selectModel.getSelected();
	}
	
	this.getSelections = function(isAlert){
		if( !this.mandatorySelect(isAlert) )
			return false;
		
		if( selectModel instanceof Ext.grid.CellSelectionModel )
			return selectModel.getSelectedCell();
		else
			return selectModel.getSelections();
	}
	
	this.getSelectedCount = function(){
		if( selectModel instanceof Ext.grid.CellSelectionModel  ) {
			var selectedCell = extGridPanel.selModel.getSelectedCell();
			if( selectedCell==null )
				return 0;
			return selectedCell.length;
		} else {
			return extGridPanel.selModel.getCount();
		}
	};
	
	this.mandatorySelect=function(isAlert){
		if(this.getSelectedCount()==0){
			/*Ext.MessageBox.show({
					title:TXT.common_hint,
					msg:TXT.common_selectOneRecord,
					buttons: Ext.MessageBox.OK,
					icon:Ext.MessageBox.WARNING
				});*/
			if(isAlert)
				Ext.MessageBox.alert(TXT.common_hint,TXT.common_at + gridConfig.title+TXT.common_in+TXT.common_selectOneRecord);
			return false;
		}
		return true;
	};
	
	this.selectFirstRow=function(){
		if( this.getDataCount()!=0 ) {
			extGridPanel.getSelectionModel().selectFirstRow();
		}
	};
	
	this.selectRowById = function( recordId ){
		var index = dataStore.getExtStore().indexOfId( recordId );
		selectModel.selectRow(index, true);
	};
	
	this.selectRowByIds = function( recordIds ){
		for(var i = 0; i < recordIds.length; i++){
			this.selectRowById( recordIds[i] );
		}
	};
	
	this.clearSelections=function(){
		if(this.getDataCount()!=0){
			extGridPanel.getSelectionModel().clearSelections();
		}
	};
	
	this.getExtGridPanel = function(){
		if( extGridPanel == null ) {
			gridConfig.ds = dataStore.getExtStore();
			gridConfig.sm = selectModel;
			gridConfig.autoDestroy = true;
			gridConfig.cm = new Ext.grid.ColumnModel( columnModelConfig );
			gridConfig.cm.remoteSort = true;
			
			if( tbar )    gridConfig.tbar = tbar.getExtToolBar();
			if( border )  gridConfig.border = border;
			if( frame )   gridConfig.frame = frame;
			if(loadMask)  gridConfig.loadMask = loadMask;
			if(stripeRows)gridConfig.stripeRows = stripeRows;
			
			if( pagination ) {
				gridConfig.pagination = true;
				gridConfig.bbar = new Ext.PagingToolbar({ pageSize: com.felix.nsf.GlobalConstants.PAGE_SIZE,
						            			   		  store: dataStore.getExtStore(),
												   		  displayInfo:true
												  		 });
			}
			
			if( editable ) {
				gridConfig.type = "edit";
				gridConfig.clicksToEdit = 1;
				extGridPanel = new Ext.grid.EditorGridPanel( gridConfig );
			} else {
				extGridPanel = new Ext.grid.GridPanel( gridConfig );
			}
			
			com.felix.nsf.Util.bindEvent( extGridPanel, gridEventConfig );
			com.felix.nsf.Util.bindEvent( extGridPanel.colModel, cmEventConfig );
			com.felix.nsf.Util.bindEvent( extGridPanel.selModel, smEventConfig );
		}
		
		return extGridPanel;
	}
	
	this.search = function( params ) {
		dataStore.getExtStore()['baseParams'] = params;
		this.load();
	};
	
	this.load = function(){
		if( pagination )
			dataStore.getExtStore().load( { params:{ start:0, limit:com.felix.nsf.GlobalConstants.PAGE_SIZE } } );
		else
			dataStore.getExtStore().load();
	}

	this.reload = function(){
		dataStore.getExtStore().reload();
	}
}