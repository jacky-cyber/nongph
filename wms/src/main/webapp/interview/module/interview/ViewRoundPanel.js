com.felix.interview.module.interview.ViewRoundPanel = function(){
    com.felix.nsf.Util.extend( this, com.felix.interview.module.interview.RoundPanel );
    
    this.setTitle( TXT.interview_interview_round_type_view );
    
    this.setGrid( new com.felix.interview.module.interview.InterviewerGridPanel() );
    
    this.toJSON = function(){
        var json = this.super.toJSON();
        json.type = 'VIEW';

        return json;
    };
    
    this.init = function(round){
        if( this.getGrid().isReady() ) {
            this.super.init(round);
            var interviewRoundUsers = round.interviewRoundUsers;
            for(var i=0; i<interviewRoundUsers.length; i++ ){
                var iru = interviewRoundUsers[i];
                this.getGrid().addInterviewer( iru.user.id, iru.user.name, iru.form.formTemplate.id );
            }
        } else {
            this.init.defer(250, this, [round]);
        }
    };
};
