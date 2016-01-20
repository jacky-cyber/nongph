package cn.globalph.admin.web.rulebuilder.service;

import cn.globalph.common.presentation.RuleIdentifier;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.web.rulebuilder.dto.FieldData;
import cn.globalph.openadmin.web.rulebuilder.service.AbstractRuleBuilderFieldService;

import org.springframework.stereotype.Service;

/**
 * An implementation of a RuleBuilderFieldService
 * that constructs metadata necessary
 * to build the supported fields for a Category entity
 *
 *@author Andre Azzolini (apazzolini)
 */
@Service("blCategoryFieldService")
public class CategoryFieldServiceImpl extends AbstractRuleBuilderFieldService {

    @Override
    public void init() {
        fields.add(new FieldData.Builder()
                .label("rule_categoryName")
                .name("name")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_categoryId")
                .name("id")
                .operators("blcOperators_Numeric")
                .options("[]")
                .type(SupportedFieldType.ID)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_categoryUrl")
                .name("url")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_categoryLongDescription")
                .name("longDescription")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
    }

    @Override
    public String getName() {
        return RuleIdentifier.CATEGORY;
    }

    @Override
    public String getDtoClassName() {
        return "cn.globalph.b2c.catalog.domain.CategoryImpl";
    }

}
