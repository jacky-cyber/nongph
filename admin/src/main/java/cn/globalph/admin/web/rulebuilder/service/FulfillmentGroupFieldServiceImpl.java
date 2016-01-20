package cn.globalph.admin.web.rulebuilder.service;

import cn.globalph.common.presentation.RuleIdentifier;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.web.rulebuilder.dto.FieldData;
import cn.globalph.openadmin.web.rulebuilder.service.AbstractRuleBuilderFieldService;

import org.springframework.stereotype.Service;

/**
 * An implementation of a RuleBuilderFieldService
 * that constructs metadata necessary
 * to build the supported fields for a Fulfillment Group entity
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blFulfillmentGroupFieldService")
public class FulfillmentGroupFieldServiceImpl  extends AbstractRuleBuilderFieldService {

    @Override
    public void init() {
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupFirstName")
                .name("address.receiver")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupAddresLine1")
                .name("address.address")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupPostalCode")
                .name("address.postalCode")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupPrimaryPhone")
                .name("address.phone")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupTotal")
                .name("total")
                .operators("blcOperators_Numeric")
                .options("[]")
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupPrice")
                .name("fulfillmentPrice")
                .operators("blcOperators_Numeric")
                .options("[]")
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupRetailPrice")
                .name("retailFulfillmentPrice")
                .operators("blcOperators_Numeric")
                .options("[]")
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupSalePrice")
                .name("saleFulfillmentPrice")
                .operators("blcOperators_Numeric")
                .options("[]")
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupType")
                .name("type")
                .operators("blcOperators_Enumeration")
                .options("blcOptions_FulfillmentType")
                .type(SupportedFieldType.BROADLEAF_ENUMERATION)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupMerchandiseTotal")
                .name("merchandiseTotal")
                .operators("blcOperators_Numeric")
                .options("[]")
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_fulfillmentGroupFulfillmentOption")
                .name("fulfillmentOption.name")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
    }

    @Override
    public String getName() {
        return RuleIdentifier.FULFILLMENTGROUP;
    }

    @Override
    public String getDtoClassName() {
        return "cn.globalph.b2c.order.domain.FulfillmentGroupImpl";
    }
}
