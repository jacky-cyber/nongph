package cn.globalph.passport.domain;

import java.io.Serializable;

public interface ChallengeQuestion extends Serializable {

    public Long getId();

    public void setId(Long id);

    public String getQuestion();

    public void setQuestion(String question);
}
