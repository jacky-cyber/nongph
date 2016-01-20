package cn.globalph.b2c.product.domain;

import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.client.VisibilityEnum;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Index;
import org.hibernate.annotations.Parameter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The Class SkuAttributeImpl is the default implementation of {@link SkuAttribute}.
 * A SKU Attribute is a designator on a SKU that differentiates it from other similar SKUs
 * (for example: Blue attribute for hat).
 * If you want to add fields specific to your implementation you should extend
 * this class and add your fields.  If you need to make significant changes to the SkuImpl then you
 * should implement your own version of {@link Sku}.
 * <br>
 * <br>
 * This implementation uses a Hibernate implementation of JPA configured through annotations.
 * The Entity references the following tables:
 * NPH_SKU_ATTRIBUTES,
 * 
 * 
 *   @see {@link SkuAttribute}, {@link SkuImpl}
 *   @author btaylor
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="NPH_SKU_ATTRIBUTE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blProducts")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true)
})
public class SkuAttributeImpl implements SkuAttribute {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The id. */
    @Id
    @GeneratedValue(generator= "SkuAttributeId")
    @GenericGenerator(
        name="SkuAttributeId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="SkuAttributeImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.product.domain.SkuAttributeImpl")
        }
    )
    @Column(name = "SKU_ATTR_ID")
    protected Long id;
    
    /** The name. */
    @Column(name = "NAME", nullable=false)
    @Index(name="SKUATTR_NAME_INDEX", columnNames={"NAME"})
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    protected String name;

    /** The value. */
    @Column(name = "VALUE", nullable=false)
    @AdminPresentation(friendlyName = "SkuAttributeImpl_Attribute_Value", order=2, group = "SkuAttributeImpl_Description", prominent=true)
    protected String value;

    /** The searchable. */
    @Column(name = "SEARCHABLE")
    @AdminPresentation(excluded = true)
    protected Boolean searchable = false;
  
    /** The sku. */
    @ManyToOne(targetEntity = SkuImpl.class, optional=false)
    @JoinColumn(name = "SKU_ID")
    @Index(name="SKUATTR_SKU_INDEX", columnNames={"SKU_ID"})
    protected Sku sku;

    /* (non-Javadoc)
     * @see cn.globalph.b2c.product.domain.SkuAttribute#getId()
     */
    @Override
    public Long getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.product.domain.SkuAttribute#setId(java.lang.Long)
     */
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.product.domain.SkuAttribute#getValue()
     */
    @Override
    public String getValue() {
        return value;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.product.domain.SkuAttribute#setValue(java.lang.String)
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.product.domain.SkuAttribute#getSearchable()
     */
    @Override
    public Boolean getSearchable() {
        if (searchable == null) {
            return Boolean.FALSE;
        } else {
            return searchable;
        }
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.product.domain.SkuAttribute#setSearchable(java.lang.Boolean)
     */
    @Override
    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }
    
    /* (non-Javadoc)
     * @see cn.globalph.b2c.product.domain.SkuAttribute#getName()
     */
    @Override
    public String getName() {
        return name;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.product.domain.SkuAttribute#setName(java.lang.String)
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return value;
    }
    
    /* (non-Javadoc)
     * @see cn.globalph.b2c.product.domain.SkuAttribute#getSku()
     */
    @Override
    public Sku getSku() {
        return sku;
    }

    /* (non-Javadoc)
     * @see cn.globalph.b2c.product.domain.SkuAttribute#setSku(cn.globalph.b2c.product.domain.Sku)
     */
    @Override
    public void setSku(Sku sku) {
        this.sku = sku;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((sku == null) ? 0 : sku.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        SkuAttributeImpl other = (SkuAttributeImpl) obj;

        if (id != null && other.id != null) {
            return id.equals(other.id);
        }

        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (sku == null) {
            if (other.sku != null)
                return false;
        } else if (!sku.equals(other.sku))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

}
