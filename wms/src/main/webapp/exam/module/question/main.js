function main(){
	var gridPanel = new com.felix.exam.module.question.QuestionGridPanel();
	
	com.felix.nsf.Context.globalView = new com.felix.nsf.GlobalView('exam_question', 1);
	com.felix.nsf.Context.globalView.mainPanel.layout = 'border';
	com.felix.nsf.Context.globalView.addModuleComp( gridPanel.getExtGridPanel() );
	com.felix.nsf.Context.globalView.render( TXT.task );
	
	gridPanel.load();
}
com.felix.nsf.Util.onReady(main);