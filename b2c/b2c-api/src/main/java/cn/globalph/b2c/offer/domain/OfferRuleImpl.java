package cn.globalph.b2c.offer.domain;

import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * 
 * @author felix.wu
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_OFFER_RULE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blOffers")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true)
})
public class OfferRuleImpl implements OfferRule {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "OfferRuleId")
    @GenericGenerator(
        name="OfferRuleId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="OfferRuleImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.offer.domain.OfferRuleImpl")
        }
    )
    @Column(name = "OFFER_RULE_ID")
    protected Long id;
    
    @Lob
    @Type(type = "org.hibernate.type.StringClobType")
    @Column(name = "MATCH_RULE", length = Integer.MAX_VALUE - 1)
    protected String matchRule;

    /* (non-Javadoc)
     * @see cn.globalph.b2c.offer.domain.OfferRule#getId()
     */
    @Override
    public Long getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.offer.domain.OfferRule#setId(java.lang.Long)
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.offer.domain.OfferRule#getMatchRule()
     */
    @Override
    public String getMatchRule() {
        return matchRule;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.offer.domain.OfferRule#setMatchRule(java.lang.String)
     */
    @Override
    public void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((matchRule == null) ? 0 : matchRule.hashCode());
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
        OfferRuleImpl other = (OfferRuleImpl) obj;
        
        if (id != null && other.id != null) {
            return id.equals(other.id);
        }
        
        if (matchRule == null) {
            if (other.matchRule != null)
                return false;
        } else if (!matchRule.equals(other.matchRule))
            return false;
        return true;
    }
    
}
