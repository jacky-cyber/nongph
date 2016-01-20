package cn.global.passport.web.controller;

import cn.globalph.passport.domain.ChallengeQuestion;
import cn.globalph.passport.service.ChallengeQuestionService;

import java.beans.PropertyEditorSupport;

public class CustomChallengeQuestionEditor extends PropertyEditorSupport {
    
    private ChallengeQuestionService challengeQuestionService;
    
    public CustomChallengeQuestionEditor(ChallengeQuestionService challengeQuestionService) {
        this.challengeQuestionService = challengeQuestionService;
    }

    @Override
    public String getAsText() {
        ChallengeQuestion question = (ChallengeQuestion) getValue();
        if (question == null) {
            return null;
        } else {
            return question.getId().toString();
        }
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(challengeQuestionService.readChallengeQuestionById((Long.parseLong(text))));
    }

}
