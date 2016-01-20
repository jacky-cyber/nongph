com.felix.exam.module.examinationView.ExaminationViewFormPanel = function(exam, answer){
	com.felix.nsf.Util.extend( this, com.felix.nsf.wrap.FormPanel );
	var thoj = this;

	this.setConfig( { labelAlign :'left',
					  layout:'form',
					  margins:'0 0 5 0',
					  frame :true
					});
	
	var baseInfo =[{ columnWidth :.30, 
			         layout:'form', 
			         labelWidth :60,
					 items:[ { id :'positionId', 
						 	   name :'positionId', 
						 	   xtype :'displayfield',
						 	   fieldLabel :TXT.exam_examination_position,
						 	   value:exam.position.name} ] },
				   { columnWidth :.30,
					 layout:'form',
					 labelWidth :50,
					 items:[ { id :'name',
						 	   name :'name',
						 	   xtype :'displayfield',
						 	   fieldLabel :TXT.exam_examination_name,
						 	   value:exam.name} ] }, 
				   { columnWidth :.20,
					 layout:'form',
					 labelWidth :70,
					 items:[ { id :'time',
						 	   name :'time',
						 	   xtype :'displayfield',
						 	   fieldLabel :TXT.exam_examination_time,
						 	   value:exam.time} ] } ];
	
	var formItems = [{ xtype:'fieldset',
					   title: TXT.exam_examinationView_exam_info,
					   collapsible: true,
				       layout:'column',
				       items:baseInfo } ];	
	for( var i=0; i<exam.catalogs.length; i++){
		var catalog = exam.catalogs[i];
		var catalogItem = {
						   xtype: 'fieldset',
						   title: (i+1)+'. '+catalog.name,
						   collapsible:true,
						   labelAlign: 'top',
						   labelSeparator:'<br/>',
						   items:[{
							   	  	xtype:'displayfield',
							   	  	fieldLabel:catalog.description,
							   	  	value:''
						   		  }]
						   };
		for(var j=0; j<catalog.questions.length; j++){
			var cq = catalog.questions[j];
			var label = '<b>'+(i+1)+'.'+(j+1);
			if( answer ) {
				if( answer[cq.id] && answer[cq.id][1]>=1 ) {
					label += '(<img src="/msxt2/lib/nsf/images/tick.png"/>)';
				} else {
					label += '(<img src="/msxt2/lib/nsf/images/tick2.png"/>' + answer[cq.id][0] + ')'; 
				}
				label +='</b>.<br/>';
			} else {
				label +=+'</b>. ';
			}
			label += cq.question.content;
			
			var questionItem = {
								xtype: cq.question.questionType.id==1?'radiogroup':'checkboxgroup',
								fieldLabel: label,
								columns:1,
								items:[]
							    };
			for(var k=0; k<cq.question.choiceItems.length; k++){
				var choice = cq.question.choiceItems[k];
				questionItem.items.push({boxLabel: choice.labelName+'. '+choice.content,
										 name: i+'question'+j,
										 checked:cq.question.rightAnswer==choice.labelName?true:false	
										});
			}
			catalogItem.items.push( questionItem );
		}
		formItems.push( catalogItem );
	}
	
	this.setItems( formItems );
}