package cn.globalph.b2c.offer.service.processor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferItemCriteria;
import cn.globalph.b2c.offer.service.OfferServiceExtensionManager;
import cn.globalph.b2c.offer.service.discount.CandidatePromotionItems;
import cn.globalph.b2c.offer.service.discount.ItemOfferComparator;
import cn.globalph.b2c.offer.service.discount.OrderOfferComparator;
import cn.globalph.b2c.offer.service.discount.PromotionDiscount;
import cn.globalph.b2c.offer.service.discount.domain.PromotableCandidateItemOffer;
import cn.globalph.b2c.offer.service.discount.domain.PromotableCandidateOrderOffer;
import cn.globalph.b2c.offer.service.discount.domain.PromotableFulfillmentGroup;
import cn.globalph.b2c.offer.service.discount.domain.PromotableOrder;
import cn.globalph.b2c.offer.service.discount.domain.PromotableOrderItem;
import cn.globalph.b2c.offer.service.discount.domain.PromotableOrderItemPriceDetail;
import cn.globalph.b2c.offer.service.type.OfferType;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.dto.OrderItemHolder;
import cn.globalph.common.money.Money;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Filter and apply order item offers.
 */
@Service("blItemOfferProcessor")
public class ItemOfferProcessorImpl extends OrderOfferProcessorImpl implements ItemOfferProcessor, ItemOfferMarkTargets {
    
    protected static final Log LOG = LogFactory.getLog(ItemOfferProcessorImpl.class);

    @Override
    public void filterItemLevelOffer(PromotableOrder order, List<PromotableCandidateItemOffer> qualifiedItemOffers, Offer offer) {
        boolean isNewFormat = !CollectionUtils.isEmpty(offer.getQualifyingItemCriteria()) || !CollectionUtils.isEmpty(offer.getTargetItemCriteria());
        boolean itemLevelQualification = false;
        boolean offerCreated = false;

        for (PromotableOrderItem promotableOrderItem : order.getDiscountableOrderItems()) {
            if(couldOfferApplyToOrder(offer, order, promotableOrderItem)) {
                if (!isNewFormat) {
                    //support legacy offers                   
                    PromotableCandidateItemOffer candidate = createCandidateItemOffer(qualifiedItemOffers, offer, order);
                   
                    if (!candidate.getLegacyCandidateTargets().contains(promotableOrderItem)) {
                        candidate.getLegacyCandidateTargets().add(promotableOrderItem);
                    }
                    offerCreated = true;
                    continue;
                }
                itemLevelQualification = true;
                break;
            }
            for (PromotableFulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
                if(couldOfferApplyToOrder(offer, order, promotableOrderItem, fulfillmentGroup)) {
                    if (!isNewFormat) {
                        //support legacy offers
                        PromotableCandidateItemOffer candidate = createCandidateItemOffer(qualifiedItemOffers, offer, order);
                        if (!candidate.getLegacyCandidateTargets().contains(promotableOrderItem)) {
                            candidate.getLegacyCandidateTargets().add(promotableOrderItem);
                        }
                        offerCreated = true;
                        continue;
                    }
                    itemLevelQualification = true;
                    break;
                }
            }
        }
        //Item Qualification - new for 1.5!
        if (itemLevelQualification && !offerCreated) {
            CandidatePromotionItems candidates = couldOfferApplyToOrderItems(offer,
                    order.getDiscountableOrderItems(offer.getApplyDiscountToSalePrice()));
            PromotableCandidateItemOffer candidateOffer = null;
            if (candidates.isMatchedQualifier()) {
                //we don't know the final target yet, so put null for the order item for now
                candidateOffer = createCandidateItemOffer(qualifiedItemOffers, offer, order);
                candidateOffer.getCandidateQualifiersMap().putAll(candidates.getCandidateQualifiersMap());
            }
            if (candidates.isMatchedTarget() && candidates.isMatchedQualifier()) {
                if (candidateOffer == null) {
                    //we don't know the final target yet, so put null for the order item for now
                    candidateOffer = createCandidateItemOffer(qualifiedItemOffers, offer, order);
                }

                candidateOffer.getCandidateTargetsMap().putAll(candidates.getCandidateTargetsMap());
            }
        }
    }
    
    /**
     * Create a candidate item offer based on the offer in question and a specific order item
     * 
     * @param qualifiedItemOffers the container list for candidate item offers
     * @param offer the offer in question
     * @return the candidate item offer
     */
    protected PromotableCandidateItemOffer createCandidateItemOffer(List<PromotableCandidateItemOffer> qualifiedItemOffers,
            Offer offer, PromotableOrder promotableOrder) {

        PromotableCandidateItemOffer promotableCandidateItemOffer =
                promotableItemFactory.createPromotableCandidateItemOffer(promotableOrder, offer);
        qualifiedItemOffers.add(promotableCandidateItemOffer);
        
        return promotableCandidateItemOffer;
    }
    
    @Override
    public void applyAllItemOffers(List<PromotableCandidateItemOffer> itemOffers, PromotableOrder order) {
        // Iterate through the collection of CandidateItemOffers. Remember that each one is an offer that may apply to a
        // particular OrderItem.  Multiple CandidateItemOffers may contain a reference to the same OrderItem object.
        // The same offer may be applied to different Order Items
        
        for (PromotableCandidateItemOffer itemOffer : itemOffers) {
            if (offerMeetsSubtotalRequirements(order, itemOffer)) {
                applyItemOffer(order, itemOffer);
            }
        }
    }
    
    
    protected boolean offerMeetsSubtotalRequirements(PromotableOrder order, PromotableCandidateItemOffer itemOffer) {
        if (itemOffer.getOffer().getQualifyingItemSubTotal() == null || itemOffer.getOffer().getQualifyingItemSubTotal().lessThanOrEqual(Money.ZERO)) {
            return true;
        }

        //TODO:  Check subtotal requirement before continuing
           
        return false;
    }

    protected boolean isTotalitarianOfferAppliedToAnyItem(PromotableOrder order) {
        List<PromotableOrderItemPriceDetail> allPriceDetails = order.getAllPromotableOrderItemPriceDetails();       
        for (PromotableOrderItemPriceDetail targetItem : allPriceDetails) {
            if (targetItem.isTotalitarianOfferApplied()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * The itemOffer has been qualified and prior methods added PromotionDiscount objects onto the ItemPriceDetail.
     * This code will convert the PromotionDiscounts into Adjustments
     * @param order
     * @param itemOffer
     */
    protected void applyAdjustments(PromotableOrder order, PromotableCandidateItemOffer itemOffer) {
        List<PromotableOrderItemPriceDetail> itemPriceDetails = order.getAllPromotableOrderItemPriceDetails();
        offerServiceUtilities.applyAdjustmentsForItemPriceDetails(itemOffer, itemPriceDetails);
    }


    /**
     * Legacy adjustments use the stackable flag instead of item qualifiers and targets
     * @param order
     * @param itemOffer
     */
    protected void applyLegacyAdjustments(PromotableOrder order, PromotableCandidateItemOffer itemOffer) {
        for (PromotableOrderItem item : itemOffer.getLegacyCandidateTargets()) {
            for (PromotableOrderItemPriceDetail itemPriceDetail : item.getPromotableOrderItemPriceDetails()) {
                if (!itemOffer.getOffer().isCombinableWithOtherOffers()) {
                    if (itemPriceDetail.getCandidateItemAdjustments().size() != 0) {
                        continue;
                           }
                } else {
                    if (itemPriceDetail.hasNonCombinableAdjustments()) {
                        continue;
                    	   }
                	 }
                offerServiceUtilities.applyOrderItemAdjustment(itemOffer, itemPriceDetail);
            	}
            }
       }
     
    /**
     * Call out to extension managers.
     * Returns true if the core processing should still be performed for the passed in offer.  
     * 
     * @param order
     * @param itemOffer
     * @return
     */
    protected Boolean applyItemOfferExtension(PromotableOrder order,
            PromotableCandidateItemOffer itemOffer) {
        Map<String, Object> contextMap = new HashMap<String, Object>();

        if (extensionManager != null) {
            extensionManager.getHandlerProxy().applyItemOffer(order, itemOffer, contextMap);
            if (contextMap.get(OfferServiceExtensionManager.STOP_PROCESSING) != null) {
                // Returning false               
                return !Boolean.TRUE.equals(contextMap.get(OfferServiceExtensionManager.STOP_PROCESSING));
            }
        }
        return Boolean.TRUE;
    }

    protected void applyItemOffer(PromotableOrder order, PromotableCandidateItemOffer itemOffer) {
        if (applyItemOfferExtension(order, itemOffer)) {
            if (offerServiceUtilities.itemOfferCanBeApplied(itemOffer, order.getAllPromotableOrderItemPriceDetails())) {
                applyItemQualifiersAndTargets(itemOffer, order);

                if (itemOffer.isLegacyOffer()) {
                    applyLegacyAdjustments(order, itemOffer);
                } else {
                    applyAdjustments(order, itemOffer);
                }
            }
        }
    }

    /**
     * Some promotions can only apply to the retail price.    This method determines whether
     * retailPrice only promotions should be used instead of those that can apply to the sale
     * price as well.
     * 
     * @param order
     * @return
     */
    protected void chooseSaleOrRetailAdjustments(PromotableOrder order) {
        List<PromotableOrderItemPriceDetail> itemPriceDetails = order.getAllPromotableOrderItemPriceDetails();
        for (PromotableOrderItemPriceDetail itemDetail : itemPriceDetails) {
            itemDetail.chooseSaleOrRetailAdjustments();                
        }
        mergePriceDetails(order);
    }

    /**
     * Checks to see if any priceDetails need to be combined and if so, combines them.
     * 
     * @param order
     * @return
     */
    protected void mergePriceDetails(PromotableOrder order) {
        List<PromotableOrderItem> items = order.getAllOrderItems();
        for (PromotableOrderItem item : items) {
            item.mergeLikeDetails();
        }
    }

   
    protected void applyItemQualifiersAndTargets(PromotableCandidateItemOffer itemOffer, PromotableOrder order) {
        if (itemOffer.isLegacyOffer()) {
            LOG.warn("The item offer with id " + itemOffer.getOffer().getId() + " is a legacy offer which means that it" +
            		" does not have any item qualifier criteria AND does not have any target item criteria. As a result," +
            		" we are skipping the marking of qualifiers and targets which will cause issues if you are relying on" +
            		" 'maxUsesPerOrder' behavior. To resolve this, qualifier criteria is not required but you must at least" +
            		" create some target item criteria for this offer.");
            return;
        } else {
            markQualifiersAndTargets(order, itemOffer);
            splitDetailsIfNecessary(order.getAllPromotableOrderItemPriceDetails());
        }
    }

    protected List<PromotableOrderItemPriceDetail> buildPriceDetailListFromOrderItems(List<PromotableOrderItem> items) {
        List<PromotableOrderItemPriceDetail> itemPriceDetails = new ArrayList<PromotableOrderItemPriceDetail>();
        for (PromotableOrderItem item : items) {
            for (PromotableOrderItemPriceDetail detail : item.getPromotableOrderItemPriceDetails()) {
                itemPriceDetails.add(detail);
            }
        }
        return itemPriceDetails;
    }

    /**
     * Loop through ItemCriteria and mark qualifiers required to give the promotion to 1 or more targets.
     * @param itemOffer
     * @param order
     * @return
     */
    protected boolean markQualifiers(PromotableCandidateItemOffer itemOffer, PromotableOrder order) {
        for (OfferItemCriteria itemCriteria : itemOffer.getCandidateQualifiersMap().keySet()) {
            List<PromotableOrderItem> promotableItems = itemOffer.getCandidateQualifiersMap().get(itemCriteria);

            List<PromotableOrderItemPriceDetail> priceDetails = buildPriceDetailListFromOrderItems(promotableItems);
            int qualifierQtyNeeded = offerServiceUtilities.markQualifiersForCriteria(itemOffer, itemCriteria, priceDetails);

            if (qualifierQtyNeeded != 0) {
                return false;
            }
        }
        return true;
    }


    protected boolean markTargets(PromotableCandidateItemOffer itemOffer, PromotableOrder order, OrderItem relatedQualifier) {
        return markTargets(itemOffer, order, relatedQualifier, false);
    }
    
    /**
     * Loop through ItemCriteria and mark targets that can get this promotion to give the promotion to 1 or more targets.
     * @param itemOffer
     * @param order
     * @return
     */
    public boolean markTargets(PromotableCandidateItemOffer itemOffer, PromotableOrder order, OrderItem relatedQualifier,
            boolean checkOnly) {
        Offer promotion = itemOffer.getOffer();

        if (itemOffer.getCandidateTargetsMap().keySet().isEmpty()) {
            return false;
        }
        
        OrderItem relatedQualifierRoot = offerServiceUtilities.findRelatedQualifierRoot(relatedQualifier);

        for (OfferItemCriteria itemCriteria : itemOffer.getCandidateTargetsMap().keySet()) {
            List<PromotableOrderItem> promotableItems = itemOffer.getCandidateTargetsMap().get(itemCriteria);

            List<PromotableOrderItemPriceDetail> priceDetails = buildPriceDetailListFromOrderItems(promotableItems);
            offerServiceUtilities.sortTargetItemDetails(priceDetails, itemOffer.getOffer().getApplyDiscountToSalePrice());

            int targetQtyNeeded = itemCriteria.getQuantity();

            targetQtyNeeded = offerServiceUtilities.markTargetsForCriteria(itemOffer, relatedQualifier, checkOnly, promotion, relatedQualifierRoot, itemCriteria, priceDetails, targetQtyNeeded);

            if (targetQtyNeeded != 0) {
                return false;
            }
        }

        if (!checkOnly) {
            itemOffer.addUse();
        }
        return true;
    }

    /**
     * When the {@link Offer#getRequiresRelatedTargetAndQualifiers()} flag is set to true, we must make sure that we
     * identify qualifiers and targets together, as they must be related to each other based on the 
     * {@link OrderItem#getParentOrderItem()} / {@link OrderItem#getChildOrderItems()} attributes.
     * 
     * @param itemOffer
     * @param order
     * @return whether or not a suitable qualifier/target pair was found and marked
     */
    protected boolean markRelatedQualifiersAndTargets(PromotableCandidateItemOffer itemOffer, PromotableOrder order) {
        OrderItemHolder orderItemHolder = new OrderItemHolder(null);

        for (Entry<OfferItemCriteria, List<PromotableOrderItem>> entry : itemOffer.getCandidateQualifiersMap().entrySet()) {
            OfferItemCriteria itemCriteria = entry.getKey();
            List<PromotableOrderItem> promotableItems = entry.getValue();

            List<PromotableOrderItemPriceDetail> priceDetails = buildPriceDetailListFromOrderItems(promotableItems);
            int qualifierQtyNeeded = offerServiceUtilities.markRelatedQualifiersAndTargetsForItemCriteria(itemOffer, order,
                    orderItemHolder, itemCriteria, priceDetails, this);

            if (qualifierQtyNeeded != 0) {
                return false;
            }
        }
        
        return markTargets(itemOffer, order, orderItemHolder.getOrderItem(), false);
    }
    
    @Override
    public void filterOffers(PromotableOrder order, List<Offer> filteredOffers, List<PromotableCandidateOrderOffer> qualifiedOrderOffers, List<PromotableCandidateItemOffer> qualifiedItemOffers) {
        // set order subTotal price to total item price without adjustments
        order.setOrderSubTotalToPriceWithoutAdjustments();

        for (Offer offer : filteredOffers) {            
            if( offer.getType().equals(OfferType.ORDER) ) {
                filterOrderLevelOffer(order, qualifiedOrderOffers, offer);
            } else if(offer.getType().equals(OfferType.ORDER_ITEM)){
                filterItemLevelOffer(order, qualifiedItemOffers, offer);
            }
        }
    }
    
    /**
     * Provide an opportunity to for modules to override the potentialSavingsCalculation 
     * @param itemOffer
     * @param item
     * @param quantity
     * @return
     */
    protected Money calculatePotentialSavingsForOrderItem(PromotableCandidateItemOffer itemOffer,
            PromotableOrderItem item, int quantity) {
        if (extensionManager != null) {
            Map<String,Object> contextMap = new HashMap<String,Object>();
            extensionManager.getHandlerProxy().calculatePotentialSavings(itemOffer, item, quantity, contextMap);

            // If the extensionHandler added a savings element to the map, then return it 
            Object o = contextMap.get("savings");
            if (o != null && o instanceof Money) {
                return (Money) o;
            }
            
            // If the extensionHandler added a quantity element to the map, then return it 
            o = contextMap.get("quantity");
            if (o != null && o instanceof Integer) {
                quantity = ((Integer) o).intValue();
            }
        }

        return itemOffer.calculateSavingsForOrderItem(item, quantity);
    }

    /**
     * This method determines the potential savings for each item offer as if it was the only item offer being applied.
     * @param itemOffers
     * @param order
     */
    protected void calculatePotentialSavings(List<PromotableCandidateItemOffer> itemOffers, PromotableOrder order) {
        if (itemOffers.size() > 1) {
            for (PromotableCandidateItemOffer itemOffer : itemOffers) {
                Money potentialSavings = Money.ZERO;
                if (itemOffer.isLegacyOffer()) {
                    for (PromotableOrderItem item : itemOffer.getLegacyCandidateTargets()) {
                        potentialSavings = potentialSavings.add(
                                itemOffer.calculateSavingsForOrderItem(item, item.getQuantity()));
                    }
                } else {
                    markQualifiersAndTargets(order, itemOffer);
                    for (PromotableOrderItemPriceDetail detail : order.getAllPromotableOrderItemPriceDetails()) {
                        PromotableOrderItem item = detail.getPromotableOrderItem();
                        for (PromotionDiscount discount : detail.getPromotionDiscounts()) {
                            Money itemSavings = calculatePotentialSavingsForOrderItem(itemOffer, item, discount.getQuantity());
                            potentialSavings = potentialSavings.add(itemSavings);
                        }
                        // Reset state back for next offer
                        detail.getPromotionDiscounts().clear();
                        detail.getPromotionQualifiers().clear();
                    }
                }
                itemOffer.setPotentialSavings(potentialSavings);
            }
        }
    }

    protected void markQualifiersAndTargets(PromotableOrder order, PromotableCandidateItemOffer itemOffer) {
        boolean matchFound = true;

        if (itemOffer.isLegacyOffer()) {
            return;
        }

        int count = 1;
        do {
            boolean qualifiersFound = false;
            boolean targetsFound = false;

            if (itemOffer.getOffer().getRequiresRelatedTargetAndQualifiers()) {
                boolean qualifiersAndTargetsFound = markRelatedQualifiersAndTargets(itemOffer, order);
                qualifiersFound = qualifiersAndTargetsFound;
                targetsFound = qualifiersAndTargetsFound;
            } else {
                qualifiersFound = markQualifiers(itemOffer, order);
                targetsFound = markTargets(itemOffer, order, null);
            }

            if (qualifiersFound && targetsFound) {
                finalizeQuantities(order.getAllPromotableOrderItemPriceDetails());
            } else {
                clearAllNonFinalizedQuantities(order.getAllPromotableOrderItemPriceDetails());
                matchFound = false;
                break;
            }
            // If we found a match, try again to see if the promotion can be applied again.
        } while (matchFound);
    }

    protected boolean offerListStartsWithNonCombinable(List<PromotableCandidateItemOffer> offerList) {
        if (offerList.size() > 1) {
            PromotableCandidateItemOffer offer = offerList.get(0);
            if (offer.getOffer().isTotalitarianOffer() || !offer.getOffer().isCombinableWithOtherOffers()) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method could be overridden to potentially run all permutations of offers.
     * A reasonable alternative is to have a permutation with nonCombinable offers
     * and another with combinable offers. 
     *
     * @param offers
     * @return
     */
    protected List<List<PromotableCandidateItemOffer>> buildItemOfferPermutations(
            List<PromotableCandidateItemOffer> offers) {
        List<List<PromotableCandidateItemOffer>> listOfOfferLists = new ArrayList<List<PromotableCandidateItemOffer>>();
        // add the default list
        listOfOfferLists.add(offers);

        if (offerListStartsWithNonCombinable(offers)) {
            List<PromotableCandidateItemOffer> listWithoutTotalitarianOrNonCombinables =
                    new ArrayList<PromotableCandidateItemOffer>(offers);

            Iterator<PromotableCandidateItemOffer> offerIterator = listWithoutTotalitarianOrNonCombinables.iterator();
            while (offerIterator.hasNext()) {
                PromotableCandidateItemOffer offer = offerIterator.next();
                if (offer.getOffer().isTotalitarianOffer() || !offer.getOffer().isCombinableWithOtherOffers()) {
                    offerIterator.remove();
                }
            }

            if (listWithoutTotalitarianOrNonCombinables.size() > 0) {
                listOfOfferLists.add(listWithoutTotalitarianOrNonCombinables);
            }
        }

        return listOfOfferLists;
    }

    protected void restPriceDetails(PromotableOrderItem item) {
        item.resetPriceDetails();

        if (extensionManager != null) {
            extensionManager.getHandlerProxy().resetPriceDetails(item);
        }

    }

    protected void determineBestPermutation(List<PromotableCandidateItemOffer> itemOffers, PromotableOrder order) {
        List<List<PromotableCandidateItemOffer>> permutations = buildItemOfferPermutations(itemOffers);
        List<PromotableCandidateItemOffer> bestOfferList = null;
        Money lowestSubtotal = null;
        if (permutations.size() > 1) {
            for (List<PromotableCandidateItemOffer> offerList : permutations) {
                applyAllItemOffers(offerList, order);
                chooseSaleOrRetailAdjustments(order);
                Money testSubtotal = order.calculateSubtotalWithAdjustments();

                if (lowestSubtotal == null || testSubtotal.lessThan(lowestSubtotal)) {
                    lowestSubtotal = testSubtotal;
                    bestOfferList = offerList;
                }

                // clear price details
                for (PromotableOrderItem item : order.getDiscountableOrderItems()) {
                    item.resetPriceDetails();
                }
            }
        } else {
            bestOfferList = permutations.get(0);
        }
        applyAllItemOffers(bestOfferList, order);
    }

    @Override
    public void applyAndCompareOrderAndItemOffers(PromotableOrder order,
            List<PromotableCandidateOrderOffer> qualifiedOrderOffers,
            List<PromotableCandidateItemOffer> qualifiedItemOffers) {
        if (!qualifiedItemOffers.isEmpty()) {
            calculatePotentialSavings(qualifiedItemOffers, order);
            
            //after savings have been calculated, uses will have been marked on offers which can effect
            //the actual application of those offers. Thus the uses for each item offer needs to be reset
            for (PromotableCandidateItemOffer itemOffer : qualifiedItemOffers) {
                itemOffer.resetUses();
            }
            
            // Sort order item offers by priority and potential total discount
            Collections.sort(qualifiedItemOffers, ItemOfferComparator.INSTANCE);
            
            if (qualifiedItemOffers.size() > 1) {
                determineBestPermutation(qualifiedItemOffers, order);
            } else {
                applyAllItemOffers(qualifiedItemOffers, order);

            }
        }
        chooseSaleOrRetailAdjustments(order);
        if (extensionManager != null) {
            extensionManager.getHandlerProxy().chooseSaleOrRetailAdjustments(order);
        }
        order.setOrderSubTotalToPriceWithAdjustments();

        if (!qualifiedOrderOffers.isEmpty()) {
            // Sort order offers by priority and discount
            Collections.sort(qualifiedOrderOffers, OrderOfferComparator.INSTANCE);
            applyAllOrderOffers(qualifiedOrderOffers, order);
        }

        order.setOrderSubTotalToPriceWithAdjustments();
        
        // TODO: only do this if absolutely required.    If you find one that no longer qualifies, then 
        // pull it out and reapply.
        if (!qualifiedOrderOffers.isEmpty() && !qualifiedItemOffers.isEmpty()) {
            List<PromotableCandidateOrderOffer> finalQualifiedOrderOffers = new ArrayList<PromotableCandidateOrderOffer>();
            order.removeAllCandidateOrderOfferAdjustments();
            for (PromotableCandidateOrderOffer candidateOrderOffer : qualifiedOrderOffers) {
                // recheck the list of order offers and verify if they still apply with the new subtotal
                /*
                 * Note - there is an edge case possibility where this logic would miss an order promotion
                 * that had a subtotal requirement that was missed because of item deductions, but without
                 * the item deductions, the order promotion would have been included and ended up giving the 
                 * customer a better deal than the item deductions.
                 */
                if (couldOfferApplyToOrder(candidateOrderOffer.getOffer(), order)) {
                    finalQualifiedOrderOffers.add(candidateOrderOffer);
                }
            }

            // Sort order offers by priority and discount
            Collections.sort(finalQualifiedOrderOffers, OrderOfferComparator.INSTANCE);
            if (!finalQualifiedOrderOffers.isEmpty()) {
                applyAllOrderOffers(finalQualifiedOrderOffers, order);
                order.setOrderSubTotalToPriceWithAdjustments();
            }  
        }
    }
}
