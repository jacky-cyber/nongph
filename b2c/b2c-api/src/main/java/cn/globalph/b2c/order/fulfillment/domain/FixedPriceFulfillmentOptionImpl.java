package cn.globalph.b2c.order.fulfillment.domain;

import cn.globalph.b2c.order.domain.FulfillmentOptionImpl;
import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.AdminPresentationClass;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import java.math.BigDecimal;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_FULFILLMENT_OPTION_FIXED")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
@AdminPresentationClass(friendlyName = "Fixed Price Fulfillment")
public class FixedPriceFulfillmentOptionImpl extends FulfillmentOptionImpl implements FixedPriceFulfillmentOption {

    private static final long serialVersionUID = 1L;

    @Column(name = "PRICE", precision=19, scale=5, nullable=false)
    protected BigDecimal price;
    
    @Override
    public Money getPrice() {
        return price == null ? null : new Money(price);
     }

    @Override
    public void setPrice(Money price) {
        this.price = Money.toAmount(price);
     }
}
