package cn.globalph.cms.structure.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.common.rule.AbstractRuleProcessor;
import cn.globalph.common.structure.dto.StructuredContentDTO;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * By default, this rule processor combines all of the rules from
 * {@link cn.globalph.cms.structure.domain.StructuredContent#getStructuredContentMatchRules()}
 * into a single MVEL expression.
 *
 * @author bpolster.
 *
 */
@Service("blContentDefaultRuleProcessor")
public class StructuredContentDefaultRuleProcessor extends AbstractRuleProcessor<StructuredContentDTO> {
    private static final Log LOG = LogFactory.getLog(StructuredContentDefaultRuleProcessor.class);

    /**
     * Returns true if all of the rules associated with the passed in <code>StructuredContent</code>
     * item match based on the passed in vars.
     *
     * Also returns true if no rules are present for the passed in item.
     *
     * @param sc - a structured content item to test
     * @param vars - a map of objects used by the rule MVEL expressions
     * @return the result of the rule checks
     */
    public boolean checkForMatch(StructuredContentDTO sc, Map<String, Object> vars) {
        String ruleExpression = sc.getRuleExpression();

        if (ruleExpression != null) {
            if (LOG.isTraceEnabled())  {
                LOG.trace("Processing content rule for StructuredContent with id " + sc.getId() +".   Value = " + ruleExpression);
            }
            boolean result = executeExpression(ruleExpression, vars);
            if (! result) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Content failed to pass rule and will not be included for StructuredContent with id " + sc.getId() +".   Value = " + ruleExpression);
                }
            }

            return result;
        } else {
            // If no rule found, then consider this a match.
            return true;
        }
    }
}
