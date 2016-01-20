package cn.globalph.admin.web.rulebuilder.service;

import cn.globalph.common.presentation.RuleIdentifier;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.web.rulebuilder.dto.FieldData;
import cn.globalph.openadmin.web.rulebuilder.service.AbstractRuleBuilderFieldService;

import org.springframework.stereotype.Service;

/**
 * An implementation of a RuleBuilderFieldService
 * that constructs metadata necessary
 * to build the supported fields for a Request entity
 *
 * @author Andre Azzolini (apazzolini)
 */
@Service("blRequestFieldService")
public class RequestFieldServiceImpl extends AbstractRuleBuilderFieldService {

    @Override
    public void init() {
        fields.add(new FieldData.Builder()
                .label("rule_requestFullUrl")
                .name("fullUrlWithQueryString")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
        
        fields.add(new FieldData.Builder()
                .label("rule_requestUri")
                .name("requestURI")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
        
        fields.add(new FieldData.Builder()
                .label("rule_requestIsSecure")
                .name("secure")
                .operators("blcOperators_Boolean")
                .options("[]")
                .type(SupportedFieldType.BOOLEAN)
                .build());
    }

    @Override
    public String getName() {
        return RuleIdentifier.REQUEST;
    }

    @Override
    public String getDtoClassName() {
        return "cn.globalph.common.RequestDTOImpl";
    }
}
