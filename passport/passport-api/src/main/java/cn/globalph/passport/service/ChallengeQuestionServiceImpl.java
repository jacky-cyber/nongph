package cn.globalph.passport.service;

import cn.globalph.passport.dao.ChallengeQuestionDao;
import cn.globalph.passport.domain.ChallengeQuestion;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

@Service("blChallengeQuestionService")
public class ChallengeQuestionServiceImpl implements ChallengeQuestionService {

    @Resource(name="blChallengeQuestionDao")
    protected ChallengeQuestionDao challengeQuestionDao;

    public List<ChallengeQuestion> readChallengeQuestions() {
        return challengeQuestionDao.readChallengeQuestions();
    }

    public ChallengeQuestion readChallengeQuestionById(long challengeQuestionId) {
        return challengeQuestionDao.readChallengeQuestionById(challengeQuestionId);
    }

}
