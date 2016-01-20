package cn.globalph.b2c.offer.service.discount;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.b2c.offer.domain.OfferItemCriteria;

import java.io.Serializable;

/**
 * Records the usage of this item as qualifier or target of
 * the promotion.   The discount amount will be 0 if this
 * item was only used as a qualifier.
 * 
 * @felix.wu
 */
public class PromotionQualifier implements Serializable{ 
    private static final long serialVersionUID = 1L;
    
    private Offer promotion;
    private OfferItemCriteria itemCriteria;
    private int quantity;
    private int finalizedQuantity;
    
    public Offer getPromotion() {
        return promotion;
    }
    public void setPromotion(Offer promotion) {
        this.promotion = promotion;
    }
    public OfferItemCriteria getItemCriteria() {
        return itemCriteria;
    }
    public void setItemCriteria(OfferItemCriteria itemCriteria) {
        this.itemCriteria = itemCriteria;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public int getFinalizedQuantity() {
        return finalizedQuantity;
    }
    public void setFinalizedQuantity(int finalizedQuantity) {
        this.finalizedQuantity = finalizedQuantity;
    }
    
    public void incrementQuantity(int quantity) {
        this.quantity = this.quantity + quantity;
    }
    
    public PromotionQualifier copy() {
        PromotionQualifier pq = new PromotionQualifier();
        pq.setItemCriteria(itemCriteria);
        pq.setPromotion(promotion);
        pq.setQuantity(quantity);
        pq.setFinalizedQuantity(finalizedQuantity);
        return pq;
    }
    
    public void resetQty(int qty) {
        quantity = qty;
        finalizedQuantity = qty;
    }

    public PromotionQualifier split(int splitItemQty) {
        PromotionQualifier returnQualifier = copy();
        int newQty = finalizedQuantity - splitItemQty;

        if (newQty <= 0) {
            throw new IllegalArgumentException("Splitting PromotionQualifier resulted in a negative quantity");
        }

        setFinalizedQuantity(newQty);
        setQuantity(newQty);

        returnQualifier.setQuantity(splitItemQty);
        returnQualifier.setFinalizedQuantity(splitItemQty);

        return returnQualifier;
    }

    public boolean isFinalized() {
        return quantity == finalizedQuantity;
    }

}
