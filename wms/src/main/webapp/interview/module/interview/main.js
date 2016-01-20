function main(){
	var gridPanel = new com.felix.interview.module.interview.InterviewGridPanel();
	
	com.felix.nsf.Context.globalView = new com.felix.nsf.GlobalView('interview_interview', 1);
	com.felix.nsf.Context.globalView.mainPanel.layout = 'border';
	com.felix.nsf.Context.globalView.addModuleComp( gridPanel.getExtGridPanel() );
	com.felix.nsf.Context.globalView.render( TXT.interview_interview );
	
	gridPanel.load();
}
com.felix.nsf.Util.onReady(main);