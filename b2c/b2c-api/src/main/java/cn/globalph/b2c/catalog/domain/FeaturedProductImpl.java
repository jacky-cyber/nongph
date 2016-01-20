package cn.globalph.b2c.catalog.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.math.BigDecimal;

import cn.globalph.b2c.product.domain.Product;
import cn.globalph.b2c.product.domain.ProductImpl;
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

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_PRODUCT_FEATURED")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blCategories")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class FeaturedProductImpl implements FeaturedProduct {

    private static final long serialVersionUID = 1L;

    /** The id. */
    @Id
    @GeneratedValue(generator= "FeaturedProductId")
    @GenericGenerator(
        name="FeaturedProductId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="FeaturedProductImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.catalog.domain.FeaturedProductImpl")
        }
    )
    @Column(name = "FEATURED_PRODUCT_ID")
    protected Long id;
    
    @Column(name = "SEQUENCE", precision = 10, scale = 6)
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    protected BigDecimal sequence;

    @Column(name = "PROMOTION_MESSAGE")
    @AdminPresentation(friendlyName = "FeaturedProductImpl_Featured_Product_Promotion_Message", largeEntry=true)
    protected String promotionMessage;
    
    @ManyToOne(targetEntity = CategoryImpl.class)
    @JoinColumn(name = "CATEGORY_ID")
    @Index(name="PRODFEATURED_CATEGORY_INDEX", columnNames={"CATEGORY_ID"})
    protected Category category = new CategoryImpl();

    @ManyToOne(targetEntity = ProductImpl.class)
    @JoinColumn(name = "PRODUCT_ID")
    @Index(name="PRODFEATURED_PRODUCT_INDEX", columnNames={"PRODUCT_ID"})
    protected Product product = new ProductImpl();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public void setSequence(BigDecimal sequence) {
        this.sequence = sequence;
    }

    @Override
    public BigDecimal getSequence() {
        return this.sequence;
    }

    @Override
    public String getPromotionMessage() {
        return promotionMessage;
    }

    @Override
    public void setPromotionMessage(String promotionMessage) {
        this.promotionMessage = promotionMessage;
    }

    @Override
    public Category getCategory() {
        return category;
    }

    @Override
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public Product getProduct() {
        return product;
    }

    @Override
    public void setProduct(Product product) {
        this.product = product;
    }
    
    @Override
    public Product getRelatedProduct() {
        return product;
    }
    
}
