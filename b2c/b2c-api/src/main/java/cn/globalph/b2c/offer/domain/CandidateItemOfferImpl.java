package cn.globalph.b2c.offer.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.OrderItemImpl;
import cn.globalph.common.money.Money;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import java.lang.reflect.Method;
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
@Table(name = "NPH_CANDIDATE_ITEM_OFFER")
@Inheritance(strategy=InheritanceType.JOINED)
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE, region="blOrderElements")
public class CandidateItemOfferImpl implements CandidateItemOffer, Cloneable {

    public static final Log LOG = LogFactory.getLog(CandidateItemOfferImpl.class);
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "CandidateItemOfferId")
    @GenericGenerator(
        name="CandidateItemOfferId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="CandidateItemOfferImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.offer.domain.CandidateItemOfferImpl")
        }
    )
    @Column(name = "CANDIDATE_ITEM_OFFER_ID")
    protected Long id;

    @ManyToOne(targetEntity = OrderItemImpl.class)
    @JoinColumn(name = "ORDER_ITEM_ID")
    @Index(name="CANDIDATE_ITEM_INDEX", columnNames={"ORDER_ITEM_ID"})
    protected OrderItem orderItem;

    @ManyToOne(targetEntity = OfferImpl.class, optional=false)
    @JoinColumn(name = "OFFER_ID")
    @Index(name="CANDIDATE_ITEMOFFER_INDEX", columnNames={"OFFER_ID"})
    protected Offer offer;

    @Column(name = "DISCOUNTED_PRICE", precision=19, scale=5)
    private BigDecimal discountedPrice;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public OrderItem getOrderItem() {
        return orderItem;
    }

    @Override
    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    @Override
    public void setOffer(Offer offer) {
        this.offer = offer;
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
    public Money getDiscountedPrice() {
        return discountedPrice == null ? null : new Money(discountedPrice);
    }
    
    @Override
    public void setDiscountedPrice(Money discountedPrice) {
        this.discountedPrice = discountedPrice.getAmount();
    }
    
    public void checkCloneable(CandidateItemOffer itemOffer) throws CloneNotSupportedException, SecurityException, NoSuchMethodException {
        Method cloneMethod = itemOffer.getClass().getMethod("clone", new Class[]{});
        if (cloneMethod.getDeclaringClass().getName().startsWith("org.broadleafcommerce") && !itemOffer.getClass().getName().startsWith("org.broadleafcommerce")) {
            //subclass is not implementing the clone method
            throw new CloneNotSupportedException("Custom extensions and implementations should implement clone in order to guarantee split and merge operations are performed accurately");
        }
    }
    
    @Override
    public CandidateItemOffer clone() {
        //instantiate from the fully qualified name via reflection
        CandidateItemOffer candidateItemOffer;
        try {
            candidateItemOffer = (CandidateItemOffer) Class.forName(this.getClass().getName()).newInstance();
            try {
                checkCloneable(candidateItemOffer);
            } catch (CloneNotSupportedException e) {
                LOG.warn("Clone implementation missing in inheritance hierarchy outside of Broadleaf: " + candidateItemOffer.getClass().getName(), e);
            }
            //candidateItemOffer.setCandidateQualifiersMap(getCandidateQualifiersMap());
            //candidateItemOffer.setCandidateTargets(getCandidateTargets());
            candidateItemOffer.setOffer(offer);
            candidateItemOffer.setOrderItem(orderItem);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        return candidateItemOffer;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((discountedPrice == null) ? 0 : discountedPrice.hashCode());
        result = prime * result + ((offer == null) ? 0 : offer.hashCode());
        result = prime * result + ((orderItem == null) ? 0 : orderItem.hashCode());
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
        CandidateItemOfferImpl other = (CandidateItemOfferImpl) obj;

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
        if (orderItem == null) {
            if (other.orderItem != null) {
                return false;
            }
        } else if (!orderItem.equals(other.orderItem)) {
            return false;
        }
        return true;
    }

}
