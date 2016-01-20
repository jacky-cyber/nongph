package cn.globalph.admin.web.rulebuilder.service;

import cn.globalph.common.presentation.RuleIdentifier;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.web.rulebuilder.dto.FieldData;
import cn.globalph.openadmin.web.rulebuilder.service.AbstractRuleBuilderFieldService;

import org.springframework.stereotype.Service;

/**
 * An implementation of a RuleBuilderFieldService
 * that constructs metadata necessary
 * to build the supported fields for an Order entity
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blOrderFieldService")
public class OrderFieldServiceImpl extends AbstractRuleBuilderFieldService {

    @Override
    public void init() {
//        fields.add(new FieldData.Builder()
//                .label("rule_orderCurrenceIsDefault")
//                .name("currency.defaultFlag")
//                .operators("blcOperators_Boolean")
//                .options("[]")
//                .type(SupportedFieldType.BOOLEAN)
//                .build());
//        fields.add(new FieldData.Builder()
//                .label("rule_orderCurrencyCode")
//                .name("currency.currencyCode")
//                .operators("blcOperators_Text")
//                .options("[]")
//                .type(SupportedFieldType.STRING)
//                .build());
//        fields.add(new FieldData.Builder()
//                .label("rule_orderCurrencyName")
//                .name("currency.friendlyName")
//                .operators("blcOperators_Text")
//                .options("[]")
//                .type(SupportedFieldType.STRING)
//                .build());
//        fields.add(new FieldData.Builder()
//                .label("rule_orderLocaleIsDefault")
//                .name("locale.defaultFlag")
//                .operators("blcOperators_Boolean")
//                .options("[]")
//                .type(SupportedFieldType.BOOLEAN)
//                .build());
//        fields.add(new FieldData.Builder()
//                .label("rule_orderLocaleCode")
//                .name("locale.localeCode")
//                .operators("blcOperators_Text")
//                .options("[]")
//                .type(SupportedFieldType.STRING)
//                .build());
//        fields.add(new FieldData.Builder()
//                .label("rule_orderLocaleName")
//                .name("locale.friendlyName")
//                .operators("blcOperators_Text")
//                .options("[]")
//                .type(SupportedFieldType.STRING)
//                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderSubtotal")
                .name("subTotal")
                .operators("blcOperators_Numeric")
                .options("[]")
                .type(SupportedFieldType.MONEY)
                .build());
    }

    @Override
    public String getName() {
        return RuleIdentifier.ORDER;
    }

    @Override
    public String getDtoClassName() {
        return "cn.globalph.b2c.order.domain.OrderImpl";
    }
}
