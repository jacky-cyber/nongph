package cn.globalph.passport.dao;

import cn.globalph.passport.domain.ChallengeQuestion;

import org.hibernate.ejb.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

@Repository("blChallengeQuestionDao")
public class ChallengeQuestionDaoImpl implements ChallengeQuestionDao {

    @PersistenceContext(unitName = "blPU")
    protected EntityManager em;

    @SuppressWarnings("unchecked")
    public List<ChallengeQuestion> readChallengeQuestions() {
        Query query = em.createNamedQuery("BC_READ_CHALLENGE_QUESTIONS");
        query.setHint(QueryHints.HINT_CACHEABLE, true);
        return query.getResultList();
    }

    public ChallengeQuestion readChallengeQuestionById(long challengeQuestionId) {
        Query query = em.createNamedQuery("BC_READ_CHALLENGE_QUESTION_BY_ID");
        query.setParameter("question_id", challengeQuestionId);
        List<ChallengeQuestion> challengeQuestions = query.getResultList();
        return challengeQuestions == null || challengeQuestions.isEmpty() ? null : challengeQuestions.get(0);
    }

}
