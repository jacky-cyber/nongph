com.felix.interview.module.interview.ExamRoundPanel = function(){
	com.felix.nsf.Util.extend( this, com.felix.interview.module.interview.RoundPanel );
        
    this.setTitle( TXT.interview_interview_round_type_exam );
    this.setGrid( new com.felix.interview.module.interview.ExaminationGridPanel() ); 

    this.toJSON = function(){
        var json = this.super.toJSON();
        json.type = 'EXAM';

        return json;
    };
    
    this.init = function( round ){
        this.super.init(round);
        var interviewRoundExaminations = round.interviewRoundExaminations;
        for(var i=0; i<interviewRoundExaminations.length; i++){
            var ire = interviewRoundExaminations[i];
            this.getGrid().addExamination( ire.exam.id, ire.exam.name, ire.exam.position.name, ire.examConfuse);
        }
    };
};

