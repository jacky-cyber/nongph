package cn.globalph.admin.web.rulebuilder.service;

import cn.globalph.common.presentation.RuleIdentifier;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.openadmin.web.rulebuilder.dto.FieldData;
import cn.globalph.openadmin.web.rulebuilder.service.AbstractRuleBuilderFieldService;

import org.springframework.stereotype.Service;

/**
 * An implementation of a RuleBuilderFieldService
 * that constructs metadata necessary
 * to build the supported fields for an Order Item entity
 *
 * @author Elbert Bautista (elbertbautista)
 */
@Service("blOrderItemFieldService")
public class OrderItemFieldServiceImpl extends AbstractRuleBuilderFieldService {

    //TODO: extensibility mechanism, support i18N
    @Override
    public void init() {
        fields.add(new FieldData.Builder()
                .label("rule_orderItemName")
                .name("name")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemPrice")
                .name("price")
                .operators("blcOperators_Numeric")
                .options("[]")
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemQuantity")
                .name("quantity")
                .operators("blcOperators_Numeric")
                .options("[]")
                .type(SupportedFieldType.INTEGER)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemRetailPrice")
                .name("retailPrice")
                .operators("blcOperators_Numeric")
                .options("[]")
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemSalePrice")
                .name("salePrice")
                .operators("blcOperators_Numeric")
                .options("[]")
                .type(SupportedFieldType.MONEY)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemSkuFulfillmentType")
                .name("sku.fulfillmentType")
                .operators("blcOperators_Enumeration")
                .options("blcOptions_FulfillmentType")
                .type(SupportedFieldType.BROADLEAF_ENUMERATION)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemSkuInventoryType")
                .name("sku.inventoryType")
                .operators("blcOperators_Enumeration")
                .options("blcOptions_InventoryType")
                .type(SupportedFieldType.BROADLEAF_ENUMERATION)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemSkuDescription")
                .name("sku.description")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemSkuLongDescription")
                .name("sku.longDescription")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemSkuStartDate")
                .name("sku.activeStartDate")
                .operators("blcOperators_Date")
                .options("[]")
                .type(SupportedFieldType.DATE)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemSkuEndDate")
                .name("sku.activeEndDate")
                .operators("blcOperators_Date")
                .options("[]")
                .type(SupportedFieldType.DATE)
                .build());
        fields.add(new FieldData.Builder()
                .label("rule_orderItemProductProvider")
                .name("sku.product.provider.name")
                .operators("blcOperators_Text")
                .options("[]")
                .type(SupportedFieldType.STRING)
                .build());
    }

    @Override
    public String getName() {
        return RuleIdentifier.ORDERITEM;
    }

    @Override
    public String getDtoClassName() {
        return "cn.globalph.b2c.order.domain.OrderItemImpl";
    }
}
