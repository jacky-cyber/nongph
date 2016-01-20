package cn.globalph.b2c.offer.service.discount;

import cn.globalph.b2c.offer.domain.Offer;
import cn.globalph.common.money.Money;

/**
 * 
 * @author felix.wu
 *
 */
public class FulfillmentGroupOfferPotential {
    
    protected Offer offer;
    protected Money totalSavings = Money.ZERO;
    protected int priority;
    
    public Offer getOffer() {
        return offer;
    }
    
    public void setOffer(Offer offer) {
        this.offer = offer;
    }
    
    public Money getTotalSavings() {
        return totalSavings;
    }
    
    public void setTotalSavings(Money totalSavings) {
        this.totalSavings = totalSavings;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((offer == null) ? 0 : offer.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FulfillmentGroupOfferPotential other = (FulfillmentGroupOfferPotential) obj;
        if (offer == null) {
            if (other.offer != null) {
                return false;
            }
        } else if (!offer.equals(other.offer)) {
            return false;
        }
        return true;
    }

}
