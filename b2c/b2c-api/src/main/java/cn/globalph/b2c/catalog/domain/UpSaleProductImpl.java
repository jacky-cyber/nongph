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
@Table(name="NPH_PRODUCT_UP_SALE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blStandardElements")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class UpSaleProductImpl implements RelatedProduct {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "UpSaleProductId")
    @GenericGenerator(
        name="UpSaleProductId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="UpSaleProductImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.catalog.domain.UpSaleProductImpl")
        }
    )
    @Column(name = "UP_SALE_PRODUCT_ID")
    private Long id;
    
    @Column(name = "PROMOTION_MESSAGE")
    @AdminPresentation(friendlyName = "UpSaleProductImpl_Upsale_Promotion_Message", largeEntry=true)
    private String promotionMessage;

    @Column(name = "SEQUENCE", precision = 10, scale = 6)
    @AdminPresentation(visibility = VisibilityEnum.HIDDEN_ALL)
    private BigDecimal sequence;
    
    @ManyToOne(targetEntity = ProductImpl.class)
    @JoinColumn(name = "PRODUCT_ID")
    @Index(name="UPSALE_PRODUCT_INDEX", columnNames={"PRODUCT_ID"})
    private Product product;
    
    @ManyToOne(targetEntity = CategoryImpl.class)
    @JoinColumn(name = "CATEGORY_ID")
    @Index(name="UPSALE_CATEGORY_INDEX", columnNames={"CATEGORY_ID"})
    protected Category category;

    @ManyToOne(targetEntity = ProductImpl.class)
    @JoinColumn(name = "RELATED_SALE_PRODUCT_ID", referencedColumnName = "PRODUCT_ID")
    @Index(name="UPSALE_RELATED_INDEX", columnNames={"RELATED_SALE_PRODUCT_ID"})
    private Product relatedSaleProduct = new ProductImpl();

    @Override
    public Long getId() {
        return id;
    } 
    
    @Override
    public void setId(Long id) {
        this.id = id;
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
    public BigDecimal getSequence() {
        return sequence;
    }
    
    @Override
    public void setSequence(BigDecimal sequence) {
        this.sequence = sequence;
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
    public Category getCategory() {
        return category;
    }

    @Override
    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public Product getRelatedProduct() {
        return relatedSaleProduct;
    }   

    @Override
    public void setRelatedProduct(Product relatedSaleProduct) {
        this.relatedSaleProduct = relatedSaleProduct;
    }
}
