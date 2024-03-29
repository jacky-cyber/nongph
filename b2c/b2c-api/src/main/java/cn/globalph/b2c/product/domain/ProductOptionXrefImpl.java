package cn.globalph.b2c.product.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentationClass;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

/**
 * @author felix.wu
 */
@Entity
@Polymorphism(type = PolymorphismType.EXPLICIT)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "NPH_PRODUCT_OPTION_XREF")
@AdminPresentationClass(excludeFromPolymorphism = false)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="blProducts")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class ProductOptionXrefImpl implements ProductOptionXref {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator= "ProductOptionXrefId")
    @GenericGenerator(
        name="ProductOptionXrefId",
        strategy="cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name="segment_value", value="ProductOptionXrefImpl"),
            @Parameter(name="entity_name", value="cn.globalph.b2c.product.domain.ProductOptionXrefImpl")
        }
    )
    @Column(name = "PRODUCT_OPTION_XREF_ID")
    protected Long id;

    @ManyToOne(targetEntity = ProductImpl.class, optional=false)
    @JoinColumn(name = "PRODUCT_ID")
    protected Product product = new ProductImpl();

    @ManyToOne(targetEntity = ProductOptionImpl.class, optional=false)
    @JoinColumn(name = "PRODUCT_OPTION_ID")
    protected ProductOption productOption = new ProductOptionImpl();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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
    public ProductOption getProductOption() {
        return productOption;
    }

    @Override
    public void setProductOption(ProductOption productOption) {
        this.productOption = productOption;
    }
}
