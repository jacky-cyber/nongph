package cn.globalph.b2c.offer.service.processor;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.NullComparator;
import org.apache.commons.collections.comparators.ReverseComparator;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferRule;
import cn.globalph.b2c.offer.service.discount.CandidatePromotionItems;
import cn.globalph.b2c.offer.service.discount.FulfillmentGroupOfferPotential;
import cn.globalph.b2c.offer.service.discount.domain.PromotableCandidateFulfillmentGroupOffer;
import cn.globalph.b2c.offer.service.discount.domain.PromotableFulfillmentGroup;
import cn.globalph.b2c.offer.service.discount.domain.PromotableFulfillmentGroupAdjustment;
import cn.globalph.b2c.offer.service.discount.domain.PromotableOrder;
import cn.globalph.b2c.offer.service.discount.domain.PromotableOrderItem;
import cn.globalph.b2c.offer.service.type.OfferRuleType;
import cn.globalph.common.money.BankersRounding;
import cn.globalph.common.money.Money;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author felix.wu
 */
@Service("blFulfillmentGroupOfferProcessor")
public class FulfillmentGroupOfferProcessorImpl extends OrderOfferProcessorImpl implements FulfillmentGroupOfferProcessor {

    @Override
    public void filterFulfillmentGroupLevelOffer(PromotableOrder order, List<PromotableCandidateFulfillmentGroupOffer> qualifiedFGOffers, Offer offer) {
        for (PromotableFulfillmentGroup fulfillmentGroup : order.getFulfillmentGroups()) {
            boolean fgLevelQualification = false;
            // handle legacy fields in addition to the 1.5 order rule field
            if (couldOfferApplyToOrder(offer, order, fulfillmentGroup)) {
                fgLevelQualification = true;
            } else {
                for (PromotableOrderItem discreteOrderItem : order.getAllOrderItems()) {
                    if (couldOfferApplyToOrder(offer, order, discreteOrderItem, fulfillmentGroup)) {
                        fgLevelQualification = true;
                        break;
                    }
                }
            }
            if (fgLevelQualification) {
                if ( couldOfferApplyToFulfillmentGroup(offer, fulfillmentGroup) ) {
                	CandidatePromotionItems candidates = couldOfferApplyToOrderItems(offer, fulfillmentGroup.getDiscountableOrderItems());
                    if (candidates.isMatchedQualifier()) {
                        PromotableCandidateFulfillmentGroupOffer candidateOffer = createCandidateFulfillmentGroupOffer(offer, qualifiedFGOffers, fulfillmentGroup);
                        candidateOffer.getCandidateQualifiersMap().putAll(candidates.getCandidateQualifiersMap());
                    }
                }
            }
        }
    }

    @Override
    public void calculateFulfillmentGroupTotal(PromotableOrder order) {
        Money totalFulfillmentCharges = Money.ZERO;
        for (PromotableFulfillmentGroup fulfillmentGroupMember : order.getFulfillmentGroups()) {
            PromotableFulfillmentGroup fulfillmentGroup = fulfillmentGroupMember;
            Money fulfillmentCharges = fulfillmentGroup.getFinalizedPriceWithAdjustments();
            fulfillmentGroup.getFulfillmentGroup().setFulfillmentPrice(fulfillmentCharges);
            totalFulfillmentCharges = totalFulfillmentCharges.add(fulfillmentCharges);
          }
        order.setTotalFufillmentCharges(totalFulfillmentCharges);
    }

    protected boolean couldOfferApplyToFulfillmentGroup(Offer offer, PromotableFulfillmentGroup fulfillmentGroup) {
        boolean appliesToItem = false;
        OfferRule rule = offer.getOfferMatchRules().get(OfferRuleType.FULFILLMENT_GROUP.getType());
        if (rule != null && rule.getMatchRule() != null) {
            HashMap<String, Object> vars = new HashMap<String, Object>();
            fulfillmentGroup.updateRuleVariables(vars);
            Boolean expressionOutcome = executeExpression(rule.getMatchRule(), vars);
            if (expressionOutcome != null && expressionOutcome) {
                appliesToItem = true;
            }
        } else {
            appliesToItem = true;
        }

        return appliesToItem;
    }

    protected PromotableCandidateFulfillmentGroupOffer createCandidateFulfillmentGroupOffer(Offer offer, List<PromotableCandidateFulfillmentGroupOffer> qualifiedFGOffers, PromotableFulfillmentGroup fulfillmentGroup) {
        PromotableCandidateFulfillmentGroupOffer promotableCandidateFulfillmentGroupOffer =
                promotableItemFactory.createPromotableCandidateFulfillmentGroupOffer(fulfillmentGroup, offer);
        qualifiedFGOffers.add(promotableCandidateFulfillmentGroupOffer);

        return promotableCandidateFulfillmentGroupOffer;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean applyAllFulfillmentGroupOffers(List<PromotableCandidateFulfillmentGroupOffer> qualifiedFGOffers, PromotableOrder order) {
        Map<FulfillmentGroupOfferPotential, List<PromotableCandidateFulfillmentGroupOffer>> offerMap = new HashMap<FulfillmentGroupOfferPotential, List<PromotableCandidateFulfillmentGroupOffer>>();
        for (PromotableCandidateFulfillmentGroupOffer candidate : qualifiedFGOffers) {
            FulfillmentGroupOfferPotential potential = new FulfillmentGroupOfferPotential();
            potential.setOffer(candidate.getOffer());
            if (offerMap.get(potential) == null) {
                offerMap.put(potential, new ArrayList<PromotableCandidateFulfillmentGroupOffer>());
            }
            offerMap.get(potential).add(candidate);
        }
        List<FulfillmentGroupOfferPotential> potentials = new ArrayList<FulfillmentGroupOfferPotential>();
        for (FulfillmentGroupOfferPotential potential : offerMap.keySet()) {
            List<PromotableCandidateFulfillmentGroupOffer> fgOffers = offerMap.get(potential);
            Collections.sort(fgOffers, new ReverseComparator(new BeanComparator("discountedAmount", new NullComparator())));
            Collections.sort(fgOffers, new BeanComparator("priority", new NullComparator()));

            if (potential.getOffer().isLimitedUsePerOrder() && fgOffers.size() > potential.getOffer().getMaxUsesPerOrder()) {
                for (int j = potential.getOffer().getMaxUsesPerOrder(); j < fgOffers.size(); j++) {
                    fgOffers.remove(j);
                }
            }
            for (PromotableCandidateFulfillmentGroupOffer candidate : fgOffers) {
                if (potential.getTotalSavings().getAmount().equals(BankersRounding.ZERO)) {
                    potential.setTotalSavings(new Money(BigDecimal.ZERO));
                     }

                Money priceBeforeAdjustments = candidate.getFulfillmentGroup().calculatePriceWithoutAdjustments();
                Money discountedPrice = candidate.getDiscountedPrice();
                potential.setTotalSavings(potential.getTotalSavings().add(priceBeforeAdjustments.subtract(discountedPrice)));
                potential.setPriority(candidate.getOffer().getPriority());
            }

            potentials.add(potential);
        }

        // Sort fg potentials by priority and discount
        Collections.sort(potentials, new BeanComparator("totalSavings", Collections.reverseOrder()));
        Collections.sort(potentials, new BeanComparator("priority"));
        potentials = removeTrailingNotCombinableFulfillmentGroupOffers(potentials);

        boolean fgOfferApplied = false;
        for (FulfillmentGroupOfferPotential potential : potentials) {
            Offer offer = potential.getOffer();

            boolean alreadyContainsTotalitarianOffer = order.isTotalitarianOfferApplied();
            List<PromotableCandidateFulfillmentGroupOffer> candidates = offerMap.get(potential);
            for (PromotableCandidateFulfillmentGroupOffer candidate : candidates) {
                applyFulfillmentGroupOffer(candidate.getFulfillmentGroup(), candidate);
                fgOfferApplied = true;
            }
            for (PromotableFulfillmentGroup fg : order.getFulfillmentGroups()) {
                fg.chooseSaleOrRetailAdjustments();
            }

            if ((offer.isTotalitarianOffer() != null && offer.isTotalitarianOffer()) || alreadyContainsTotalitarianOffer) {
                fgOfferApplied = compareAndAdjustFulfillmentGroupOffers(order, fgOfferApplied);
                if (fgOfferApplied) {
                    break;
                }
            } else if (!offer.isCombinableWithOtherOffers()) {
                break;
            }
        }

        return fgOfferApplied;
    }

    protected boolean compareAndAdjustFulfillmentGroupOffers(PromotableOrder order, boolean fgOfferApplied) {
        Money regularOrderDiscountShippingTotal = Money.ZERO;
        regularOrderDiscountShippingTotal =
                regularOrderDiscountShippingTotal.add(order.calculateSubtotalWithoutAdjustments());
        for (PromotableFulfillmentGroup fg : order.getFulfillmentGroups()) {
            regularOrderDiscountShippingTotal =
                    regularOrderDiscountShippingTotal.add(fg.getFinalizedPriceWithAdjustments());
        }

        Money discountOrderRegularShippingTotal = Money.ZERO;
        discountOrderRegularShippingTotal = discountOrderRegularShippingTotal.add(order.calculateSubtotalWithAdjustments());
        for (PromotableFulfillmentGroup fg : order.getFulfillmentGroups()) {
            discountOrderRegularShippingTotal = discountOrderRegularShippingTotal.add(fg.calculatePriceWithoutAdjustments());
        }

        if (discountOrderRegularShippingTotal.lessThan(regularOrderDiscountShippingTotal)) {
            // order/item offer is better; remove totalitarian fulfillment group offer and process other fg offers
            order.removeAllCandidateFulfillmentOfferAdjustments();
            fgOfferApplied = false;
        } else {
            // totalitarian fg offer is better; remove all order/item offers
            order.removeAllCandidateOrderOfferAdjustments();
            order.removeAllCandidateItemOfferAdjustments();
            order.getOrder().setSubTotal(order.calculateSubtotalWithAdjustments());
          }
        return fgOfferApplied;
    }

    protected void applyFulfillmentGroupOffer(PromotableFulfillmentGroup promotableFulfillmentGroup, PromotableCandidateFulfillmentGroupOffer fulfillmentGroupOffer) {
        if (promotableFulfillmentGroup.canApplyOffer(fulfillmentGroupOffer)) {
            PromotableFulfillmentGroupAdjustment promotableFulfillmentGroupAdjustment = promotableItemFactory.createPromotableFulfillmentGroupAdjustment(fulfillmentGroupOffer, promotableFulfillmentGroup);
            promotableFulfillmentGroup.addCandidateFulfillmentGroupAdjustment(promotableFulfillmentGroupAdjustment);
        }
    }

    @Override
    public List<FulfillmentGroupOfferPotential> removeTrailingNotCombinableFulfillmentGroupOffers(List<FulfillmentGroupOfferPotential> candidateOffers) {
        List<FulfillmentGroupOfferPotential> remainingCandidateOffers = new ArrayList<FulfillmentGroupOfferPotential>();
        int offerCount = 0;
        for (FulfillmentGroupOfferPotential candidateOffer : candidateOffers) {
            if (offerCount == 0) {
                remainingCandidateOffers.add(candidateOffer);
            } else {
                boolean treatAsNewFormat = false;
                if (candidateOffer.getOffer().getTreatAsNewFormat() != null && candidateOffer.getOffer().getTreatAsNewFormat()) {
                    treatAsNewFormat = true;
                }
                if ((!treatAsNewFormat && candidateOffer.getOffer().isCombinableWithOtherOffers()) || (treatAsNewFormat && (candidateOffer.getOffer().isTotalitarianOffer() == null || !candidateOffer.getOffer().isTotalitarianOffer()))) {
                    remainingCandidateOffers.add(candidateOffer);
                }
            }
            offerCount++;
        }
        return remainingCandidateOffers;
    }
}
