package cn.globalph.common.rule;

import java.util.Map;

public interface RuleProcessor<T> {

    public abstract boolean checkForMatch(T sc, Map<String, Object> valueMap);

}
