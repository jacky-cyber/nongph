function main(){
	var gridPanel = new com.felix.interview.module.candidate.CandidateGridPanel();
	
	com.felix.nsf.Context.globalView = new com.felix.nsf.GlobalView('interview_candidate', 1);
	com.felix.nsf.Context.globalView.mainPanel.layout = 'border';
	com.felix.nsf.Context.globalView.addModuleComp( gridPanel.getExtGridPanel() );
	com.felix.nsf.Context.globalView.render( TXT.interview_candidate );
	
	gridPanel.load();
}
com.felix.nsf.Util.onReady(main);