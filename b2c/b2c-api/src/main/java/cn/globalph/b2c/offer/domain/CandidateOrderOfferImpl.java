package cn.globalph.b2c.offer.domain;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderImpl;
import cn.globalph.common.money.Money;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "NPH_CANDIDATE_ORDER_OFFER")
@Inheritance(strategy=InheritanceType.JOINED)
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
public class CandidateOrderOfferImpl implements CandidateOrderOffer {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "CandidateOrderOfferId")
    @GenericGenerator(
        name="CandidateOrderOfferId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="CandidateOrderOfferImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.offer.domain.CandidateOrderOfferImpl")
        }
    )
    @Column(name = "CANDIDATE_ORDER_OFFER_ID")
    protected Long id;

    @ManyToOne(targetEntity = OrderImpl.class)
    @JoinColumn(name = "ORDER_ID")
    @Index(name="CANDIDATE_ORDER_INDEX", columnNames={"ORDER_ID"})
    protected Order order;

    @ManyToOne(targetEntity = OfferImpl.class, optional=false)
    @JoinColumn(name = "OFFER_ID")
    @Index(name="CANDIDATE_ORDEROFFER_INDEX", columnNames={"OFFER_ID"})
    protected Offer offer;

    @Column(name = "DISCOUNTED_PRICE", precision=19, scale=5)
    protected BigDecimal discountedPrice;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int getPriority() {
        return offer.getPriority();
    }

    @Override
    public Offer getOffer() {
        return offer;
    }

    @Override
    public void setOffer(Offer offer) {
        this.offer = offer;
        discountedPrice = null;  // price needs to be recalculated
    }

    @Override
    public Money getDiscountedPrice() {
        return discountedPrice == null ? null : new Money(discountedPrice);
    }
    
    @Override
    public void setDiscountedPrice(Money discountedPrice) {
        this.discountedPrice = discountedPrice.getAmount();
    }

    @Override
    public Order getOrder() {
        return order;
    }

    @Override
    public void setOrder(Order order) {
        this.order = order;
        discountedPrice = null;  // price needs to be recalculated
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((discountedPrice == null) ? 0 : discountedPrice.hashCode());
        result = prime * result + ((offer == null) ? 0 : offer.hashCode());
        result = prime * result + ((order == null) ? 0 : order.hashCode());
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
        CandidateOrderOfferImpl other = (CandidateOrderOfferImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (discountedPrice == null) {
            if (other.discountedPrice != null) {
                return false;
            }
        } else if (!discountedPrice.equals(other.discountedPrice)) {
            return false;
        }
        if (offer == null) {
            if (other.offer != null) {
                return false;
            }
        } else if (!offer.equals(other.offer)) {
            return false;
        }
        if (order == null) {
            if (other.order != null) {
                return false;
            }
        } else if (!order.equals(other.order)) {
            return false;
        }
        return true;
    }

}
