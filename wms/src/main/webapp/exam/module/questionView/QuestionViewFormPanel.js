com.felix.exam.module.questionView.QuestionViewFormPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	thoj = this;

	this.setConfig( { 
					  id:'questionFormPanel',
					  labelAlign :'left',
					  region :'center',
					  labelWidth :70,
					  layout:'form',
					  margins:'0 0 2 0',
					  cmargins:'0 0 2 0',
					  defaultType: 'textfield',
					  frame :true
					} );

	this.setItems( [
	                {
						id:   'questionType',
						name: 'questionType',
						fieldLabel: TXT.question_type,
						width: 500,
						tabIndex:1
					}, {
						id :  'position',
						name: 'position',
						fieldLabel :TXT.question_position,
						width: 500,
						tabIndex:2
					},{
						id :'name',
						name :'name',
						fieldLabel: TXT.question_name,
						width :500,
						tabIndex:3
					},{
						id :'content',
						name :'content',
						xtype:'textarea',
						fieldLabel: TXT.question_content,
						width :500,
						height:200,
						tabIndex:4
					},{
						id :'rightAnswer',
						name :'rightAnswer',
						xtype:'textarea',
						fieldLabel: TXT.question_right_answer,
						width :500,
						tabIndex:5
					}			
				] );
}