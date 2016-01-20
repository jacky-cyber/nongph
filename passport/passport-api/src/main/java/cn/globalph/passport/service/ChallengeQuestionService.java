package cn.globalph.passport.service;

import cn.globalph.passport.domain.ChallengeQuestion;

import java.util.List;

public interface ChallengeQuestionService {

    public List<ChallengeQuestion> readChallengeQuestions();
    public ChallengeQuestion readChallengeQuestionById(long challengeQuestionId);
}
