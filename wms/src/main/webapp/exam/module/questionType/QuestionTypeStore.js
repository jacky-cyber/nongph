com.felix.exam.module.questionType.QuestionTypeStore = function(){
	com.felix.nsf.Util.extend( this, Ext.data.Store );
	
	this.constructor({
		proxy :new Ext.data.HttpProxy( {
			url :com.felix.nsf.GlobalConstants.DISPATCH_SERVLET_URL + "/questionTypeAction/search"
		}),
		reader :new Ext.data.JsonReader( {
			fields : [ { name :'id' },
			           { name :'name' } ]
		})
	});
}	