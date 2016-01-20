package cn.globalph.b2c.order.fulfillment.domain;

import cn.globalph.b2c.order.domain.FulfillmentOptionImpl;
import cn.globalph.common.presentation.AdminPresentationClass;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.List;

/**
 * 
 * @author Phillip Verheyden
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_FULFILLMENT_OPT_BANDED_PRC")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
@AdminPresentationClass(friendlyName = "Banded Price Fulfillment Option")
public class BandedPriceFulfillmentOptionImpl extends FulfillmentOptionImpl implements BandedPriceFulfillmentOption {

    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy="option", targetEntity=FulfillmentPriceBandImpl.class)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    protected List<FulfillmentPriceBand> bands;

    @Override
    public List<FulfillmentPriceBand> getBands() {
        return bands;
    }

    @Override
    public void setBands(List<FulfillmentPriceBand> bands) {
        this.bands = bands;
    }

}
