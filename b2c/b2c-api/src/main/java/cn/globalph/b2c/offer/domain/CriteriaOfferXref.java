package cn.globalph.b2c.offer.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The purpose of this entity is to simply enforce the not null constraints on the two
 * columns in this join table. Otherwise, during DDL creation, Hibernate was
 * creating incompatible SQL for MS SQLServer.
 * 
 * @felix.wu
 *
 */
@Entity
@Table(name = "NPH_QUAL_CRIT_OFFER_XREF")
@Inheritance(strategy=InheritanceType.JOINED)
public class CriteriaOfferXref {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The category id. */
    @EmbeddedId
    CriteriaOfferXrefPK criteriaOfferXrefPK = new CriteriaOfferXrefPK();

    public CriteriaOfferXrefPK getCriteriaOfferXrefPK() {
        return criteriaOfferXrefPK;
    }

    public void setCriteriaOfferXrefPK(final CriteriaOfferXrefPK criteriaOfferXrefPK) {
        this.criteriaOfferXrefPK = criteriaOfferXrefPK;
    }

    public static class CriteriaOfferXrefPK implements Serializable {
        
        /** The Constant serialVersionUID. */
        private static final long serialVersionUID = 1L;

        @ManyToOne(targetEntity = OfferImpl.class, optional=false)
        @JoinColumn(name = "OFFER_ID")
        protected Offer offer = new OfferImpl();
        
        @ManyToOne(targetEntity = OfferItemCriteriaImpl.class, optional=false)
        @JoinColumn(name = "OFFER_ITEM_CRITERIA_ID")
        protected OfferItemCriteria offerCriteria = new OfferItemCriteriaImpl();

        public Offer getOffer() {
            return offer;
        }

        public void setOffer(Offer offer) {
            this.offer = offer;
        }

        public OfferItemCriteria getOfferCriteria() {
            return offerCriteria;
        }

        public void setOfferCriteria(OfferItemCriteria offerCriteria) {
            this.offerCriteria = offerCriteria;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((offer == null) ? 0 : offer.hashCode());
            result = prime * result + ((offerCriteria == null) ? 0 : offerCriteria.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CriteriaOfferXrefPK other = (CriteriaOfferXrefPK) obj;
            if (offer == null) {
                if (other.offer != null)
                    return false;
            } else if (!offer.equals(other.offer))
                return false;
            if (offerCriteria == null) {
                if (other.offerCriteria != null)
                    return false;
            } else if (!offerCriteria.equals(other.offerCriteria))
                return false;
            return true;
        }

    }
}
