com.felix.exam.module.question.QuestionFormPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	thoj = this;
	
	var questionTypeStore = new Ext.data.Store( {
		proxy :new Ext.data.HttpProxy( {
			url :com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL + "/questionTypeAction/search"
		}),
		reader :new Ext.data.JsonReader( {
			fields : [ { name :'id' },
			           { name :'name' } ]
		})
	});
	questionTypeStore.load();
	
	var afterSelectType = function(c, r, i){
		var choiceGridPanel = thoj.getParent().getItem(1);
		var typeId = r.data.id;
        if( typeId=='1' || typeId =='2' )	
        	choiceGridPanel.getExtGridPanel().show();
        else 
        	choiceGridPanel.getExtGridPanel().hide();
	};
	
	this.setConfig( { 
					  id:'questionFormPanel',
					  labelAlign :'left',
					  region :'center',
					  labelWidth :70,
					  layout:'form',
					  margins:'0 0 2 0',
					  cmargins:'0 0 2 0',
					  frame :true
	});

	this.setItems( [
	                {  id:'id',
					   name: 'id',
					   xtype:"hidden"
					 },	{
						id:   'questionTypeId',
						name: 'questionTypeId',
						xtype:'combo',
						fieldLabel: TXT.question_type,
						width: 500,
						tabIndex:1,
						allowBlank:false,
						store: questionTypeStore,
						mode :'local',
						displayField :'name',
						valueField :'id',
						triggerAction :'all',
						typeAhead :true,
						forceSelection: true,
						emptyText: TXT.exam_question_select_type,
						listeners:{
							'select':afterSelectType
						}
					}, {
						id :  'positionId',
						name: 'positionId',
						xtype:'checkboxgroup',
						fieldLabel :TXT.question_position,
						width: 500,
						tabIndex:2,
						allowBlank:false,
						columns:3
					},{
						id :'name',
						name :'name',
						xtype:'textfield',
						fieldLabel: TXT.question_name,
						width :500,
						tabIndex:3,
						allowBlank:false
					},{
						id :'content',
						name :'content',
						xtype:'textarea',
						fieldLabel: TXT.question_content,
						width :500,
						height:200,
						tabIndex:4,
						allowBlank:false
					},{
						id :'rightAnswer',
						name :'rightAnswer',
						xtype:'textarea',
						fieldLabel: TXT.question_right_answer,
						width :500,
						tabIndex:5,
						allowBlank:false
					}			
				] );
	
	this.setPositions = function(ps){
		var pcis = [];
		for(var i=0; i<ps.length; i++) {
			pcis.push( { boxLabel:ps[i].name, name: ps[i].id } );
		}
		
		this.getItems()[2].items = pcis;
	}
}