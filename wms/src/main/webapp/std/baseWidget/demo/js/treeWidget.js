Ecp.InstitutionTreeGrid=function(){
	this.treeGrid=new Ecp.TreeGrid();
	this.topToolBar=null;
	this.config={
		id : 'institution',
		title : '机构管理',
		border : true,
		dataUrl:DISPATCH_SERVLET_URL+'?cmd=institution&action=find'
	};
	
	this.columnConfig=[ {
			header :'机构名称',
			width :350,
			dataIndex :'name'
		}, {
			header :'内部编码',
			width :100,
			dataIndex :'internalCode'
		}, {
			header :'类型',
			width :100,
			dataIndex :'type',
			renderer : function(value, cellmeta, record, rowIndex,
								columnIndex, store) {
				if(value=="1")
					return TXT_institution_type_hq;
				else
					return TXT_institution_type_sub;
			}
		},{
			header :'bic码',
			width :100,
			dataIndex :'bicCode'
		},  {
			header :'发往swift',
			width :100,
			dataIndex :'sendToSwift',
			renderer : function(value) {
				if (value == true) {
					return '是';
				} else {
					return '否';
				}
			}
		}, {
			header :'是否具有账户行',
			width :100,
			dataIndex :'hasSubAccount',
			renderer : function(value) {
				if (value == "1") {
					return '是';
				} else {
					return '否';
				}
			}
		}];
	this.cusObj=null;
	this.eventObj=null;
}

Ecp.InstitutionTreeGrid.prototype.setTopToolBar=function(toolBar){
	this.topToolBar=toolBar;
}

Ecp.InstitutionTreeGrid.prototype.handleWidgetConfig=function(handler){
	handler(this);
}

Ecp.InstitutionTreeGrid.prototype.setTreeGridEvent=function(obj){
	this.eventObj=obj;
}

Ecp.InstitutionTreeGrid.prototype.customization=function(obj){
	this.cusObj=obj;
}

Ecp.InstitutionTreeGrid.prototype.render=function(){
	this.treeGrid.setTreeConfig(this.config);
	this.treeGrid.setTreeColumnConfig(this.columnConfig);
	if(this.topToolBar!=null)
		this.treeGrid.setTopToolBar(this.topToolBar);
	if(this.eventObj!=null)
		this.treeGrid.setTreeEventConfig(this.eventObj);
	this.treeGrid.customization(this.cusObj);
	return this.treeGrid.render();
}