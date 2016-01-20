package cn.globalph.passport.dao;

import cn.globalph.passport.domain.ChallengeQuestion;

import java.util.List;

public interface ChallengeQuestionDao {

    public List<ChallengeQuestion> readChallengeQuestions();
    public ChallengeQuestion readChallengeQuestionById(long challengeQuestionId);
  
}
