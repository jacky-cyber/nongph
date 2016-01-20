com.felix.exam.module.question.QuestionSearchFormPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	
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
	
	var positionStore = new Ext.data.Store( {
		proxy :new Ext.data.HttpProxy( {
			url :com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL + "/positionAction/getAll"
		}),
		reader :new Ext.data.JsonReader( {
			fields : [ { name :'id' },
			           { name :'name' } ]
		})
	});
	positionStore.load();
	
	this.setConfig( { labelAlign :'left',
					  region :'center',
					  labelWidth :100,
					  layout:'form',
					  margins:'0 0 2 0',
					  cmargins:'0 0 2 0',
					  frame :true
	});
	
	this.setItems( [
					{
						xtype :'combo',
						fieldLabel :TXT.question_type,
						id :'questionTypeId',
						name :'questionTypeId',
						store :questionTypeStore,
						displayField :'name',
						valueField :'id',
						typeAhead :true,
						mode :'local',
						triggerAction :'all',
						tabIndex:1
					},{
						xtype :'combo',
						fieldLabel :TXT.question_position,
						id :'positionId',
						name :'positionId',
						store :positionStore,
						displayField :'name',
						valueField :'id',
						typeAhead :true,
						mode :'local',
						triggerAction :'all',
						tabIndex:2
					},{
						xtype:'textfield',
						fieldLabel: TXT.question_name,
						id :'pattern',
						name :'pattern',
						tabIndex:3
					}		
				] );
}