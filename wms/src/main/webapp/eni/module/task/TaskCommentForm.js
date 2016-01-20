/**
 * TaskCommentForm
 */
com.felix.eni.module.task.TaskCommentForm = function(){
	com.felix.nsf.Util.extend(this, com.felix.nsf.wrap.FormPanel);

	this.setConfig({labelAlign :'left',
					region :'south',
					labelWidth :70,
					height :120,
					margins :'4 0 0 0',
					cmargins :'4 0 0 0',
					// width: 500,
					// height :200,
					autoScroll :true,
					frame :true,
					layout :'form'
					});
	this.setItems([{id :'lastComments',
					name :'lastComments',
					xtype :'textarea',
					fieldLabel :TXT.task_last_comments,
					width :442,
					maxLength :255,
					height :45,
					readOnly:true},
				   {id :'comments',
					name :'comments',
					xtype :'textarea',
					fieldLabel :TXT.task_comments,
					width :442,
					maxLength :255,
					height :45}]);
}