package cn.globalph.b2c.offer.service.discount.domain;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.offer.domain.OrderItemPriceDetailAdjustment;
import cn.globalph.b2c.offer.service.discount.PromotionQualifier;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.OrderItemContainer;
import cn.globalph.b2c.order.domain.OrderItemPriceDetail;
import cn.globalph.b2c.order.domain.OrderItemQualifier;
import cn.globalph.common.money.Money;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PromotableOrderItemImpl implements PromotableOrderItem {

    private static final Log LOG = LogFactory.getLog(PromotableOrderItem.class);

    private static final long serialVersionUID = 1L;
    
    protected PromotableOrder promotableOrder;
    protected OrderItem orderItem;
    protected PromotableItemFactory itemFactory;
    protected List<PromotableOrderItemPriceDetail> itemPriceDetails = new ArrayList<PromotableOrderItemPriceDetail>();
    protected boolean includeAdjustments;
    protected Map<String, Object> extraDataMap = new HashMap<String, Object>();

    public PromotableOrderItemImpl(OrderItem orderItem, PromotableOrder promotableOrder, PromotableItemFactory itemFactory,
            boolean includeAdjustments) {
        this.orderItem = orderItem;
        this.promotableOrder = promotableOrder;
        this.itemFactory = itemFactory;
        this.includeAdjustments = includeAdjustments;
        initializePriceDetails();
    }

    @Override
    public void resetPriceDetails() {
        itemPriceDetails.clear();
        initializePriceDetails();
    }

    private void initializePriceDetails() {
        if (includeAdjustments) {
            for (OrderItemPriceDetail detail : orderItem.getOrderItemPriceDetails()) {
                PromotableOrderItemPriceDetail poid =
                        itemFactory.createPromotableOrderItemPriceDetail(this, detail.getQuantity());
                itemPriceDetails.add(poid);
                poid.chooseSaleOrRetailAdjustments();
                for (OrderItemPriceDetailAdjustment adjustment : detail.getOrderItemPriceDetailAdjustments()) {
                    PromotableOrderItemPriceDetailAdjustment poidAdj =
                            new PromotableOrderItemPriceDetailAdjustmentImpl(adjustment, poid);
                    poid.addCandidateItemPriceDetailAdjustment(poidAdj);
                }

                List<OrderItemQualifier> oiqs = poid.getPromotableOrderItem().getOrderItem().getOrderItemQualifiers();
                if (CollectionUtils.isNotEmpty(oiqs)) {
                    for (OrderItemQualifier oiq : oiqs) {
                        PromotionQualifier pq = new PromotionQualifier();
                        pq.setPromotion(oiq.getOffer());
                        pq.setQuantity(oiq.getQuantity().intValue());
                        pq.setFinalizedQuantity(oiq.getQuantity().intValue());
                        poid.getPromotionQualifiers().add(pq);
                    }
                }
            }
        } else {
            PromotableOrderItemPriceDetail poid =
                    itemFactory.createPromotableOrderItemPriceDetail(this, orderItem.getQuantity());
            itemPriceDetails.add(poid);
        }
    }
    
    /**
     * Adds the item to the rule variables map.
     * @param ruleVars
     */
    public void updateRuleVariables(Map<String, Object> ruleVars) {
        ruleVars.put("orderItem", orderItem);
        ruleVars.put("discreteOrderItem", orderItem);
        ruleVars.put("bundleOrderItem", orderItem);
    }

    @Override
    public boolean isDiscountingAllowed() {
        return orderItem.isDiscountingAllowed();
    }
    
    @Override
    public boolean isOrderItemContainer() {
        return orderItem instanceof OrderItemContainer;
    }

    @Override
    public OrderItemContainer getOrderItemContainer() {
        if (orderItem instanceof OrderItemContainer) {
            return (OrderItemContainer) orderItem;
        }
        return null;
    }

    public List<PromotableOrderItemPriceDetail> getPromotableOrderItemPriceDetails() {
        return itemPriceDetails;
    }

    public Money getSalePriceBeforeAdjustments() {
        return orderItem.getSalePrice();
    }

    public Money getRetailPriceBeforeAdjustments() {
        return orderItem.getRetailPrice();
    }

    public Money getPriceBeforeAdjustments(boolean applyToSalePrice) {
        if (applyToSalePrice && getSalePriceBeforeAdjustments() != null) {
            return getSalePriceBeforeAdjustments();
        }
        return getRetailPriceBeforeAdjustments();
    }

    public Money getCurrentBasePrice() {
        if (orderItem.getIsOnSale()) {
            return orderItem.getSalePrice();
        } else {
            return orderItem.getRetailPrice();
        }
    }

    public int getQuantity() {
        return orderItem.getQuantity();
    }

    @Override
    public boolean isOnSale() {
        return orderItem.getIsOnSale();
    }

    @Override
    public void removeAllItemAdjustments() {
        Iterator<PromotableOrderItemPriceDetail> detailIterator = itemPriceDetails.iterator();

        boolean first = true;
        while (detailIterator.hasNext()) {
            PromotableOrderItemPriceDetail detail = detailIterator.next();
            if (first) {
                detail.setQuantity(getQuantity());
                detail.getPromotionDiscounts().clear();
                detail.getPromotionQualifiers().clear();
                detail.removeAllAdjustments();
                first = false;
            } else {
                // Get rid of all other details
                detailIterator.remove();
            }
        }
    }

    protected void mergeDetails(PromotableOrderItemPriceDetail firstDetail, PromotableOrderItemPriceDetail secondDetail) {
        int firstQty = firstDetail.getQuantity();
        int secondQty = secondDetail.getQuantity();
        
        if (LOG.isDebugEnabled()) {
            LOG.trace("Merging priceDetails with quantities " + firstQty + " and " + secondQty);
        }

        firstDetail.setQuantity(firstQty + secondQty);
    }

    @Override
    public void mergeLikeDetails() {
        if (itemPriceDetails.size() > 1) {
            Iterator<PromotableOrderItemPriceDetail> detailIterator = itemPriceDetails.iterator();
            Map<String, PromotableOrderItemPriceDetail> detailMap = new HashMap<String, PromotableOrderItemPriceDetail>();

            while (detailIterator.hasNext()) {
                PromotableOrderItemPriceDetail currentDetail = detailIterator.next();
                String detailKey = currentDetail.buildDetailKey();
                if (detailMap.containsKey(detailKey)) {
                    PromotableOrderItemPriceDetail firstDetail = detailMap.get(detailKey);
                    mergeDetails(firstDetail, currentDetail);
                    detailIterator.remove();
                } else {
                    detailMap.put(detailKey, currentDetail);
                }
            }
        }
    }

    @Override
    public Long getOrderItemId() {
        return orderItem.getId();
    }

    @Override
    public Money calculateTotalWithAdjustments() {
        Money returnTotal = Money.ZERO;
        for (PromotableOrderItemPriceDetail detail : itemPriceDetails) {
            returnTotal = returnTotal.add(detail.getFinalizedTotalWithAdjustments());
        }
        return returnTotal;
    }

    @Override
    public Money calculateTotalWithoutAdjustments() {
        return getCurrentBasePrice().multiply(orderItem.getQuantity());
    }

    @Override
    public Money calculateTotalAdjustmentValue() {
        Money returnTotal = Money.ZERO;
        for (PromotableOrderItemPriceDetail detail : itemPriceDetails) {
            returnTotal = returnTotal.add(detail.calculateTotalAdjustmentValue());
        }
        return returnTotal;
    }

    public PromotableOrderItemPriceDetail createNewDetail(int quantity) {
        if (includeAdjustments) {
            throw new RuntimeException("Trying to createNewDetail when adjustments have already been included.");
        }
        return itemFactory.createPromotableOrderItemPriceDetail(this, quantity);
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    @Override
    public Map<String, Object> getExtraDataMap() {
        return extraDataMap;
    }
}
