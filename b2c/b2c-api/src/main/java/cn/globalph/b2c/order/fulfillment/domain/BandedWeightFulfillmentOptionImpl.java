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
@Table(name = "NPH_FULFILLMENT_OPT_BANDED_WGT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blStandardElements")
@AdminPresentationClass(friendlyName = "Banded Weight Fulfillment Option")
public class BandedWeightFulfillmentOptionImpl extends FulfillmentOptionImpl implements BandedWeightFulfillmentOption {

    private static final long serialVersionUID = 1L;

    @OneToMany(mappedBy="option", targetEntity=FulfillmentWeightBandImpl.class)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
    protected List<FulfillmentWeightBand> bands;

    @Override
    public List<FulfillmentWeightBand> getBands() {
        return bands;
    }

    @Override
    public void setBands(List<FulfillmentWeightBand> bands) {
        this.bands = bands;
    }

}
