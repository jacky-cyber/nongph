package cn.globalph.coupon.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.coupon.type.CouponAttributeCategoryType;
import cn.globalph.coupon.type.CouponAttributeNameType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "PH_COUPON_ATTRIBUTE")
@AdminPresentationClass(friendlyName = "优惠券属性")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.SANDBOX, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_CATALOG)
})
public class CouponAttributeImpl implements CouponAttribute {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Column(name = "ID")
	@Id
	@GeneratedValue(generator = "CouponAttributeId")
    @GenericGenerator(
            name = "CouponAttributeId",
            strategy = "cn.globalph.common.persistence.IdOverrideTableGenerator",
            parameters = {
                @Parameter(name = "segment_value", value = "CouponAttributeImpl"),
                @Parameter(name = "entity_name", value = "cn.globalph.coupon.domain.CouponAttributeImpl")
            }
        )
	private Long id;
	
	@Column(name = "CATEGORY")
	@AdminPresentation(friendlyName = "类别",
	prominent = true,
    fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
    broadleafEnumeration = "cn.globalph.coupon.type.CouponAttributeCategoryType"
	)
    private String category;
	
	@Column(name = "ATTR")
	@AdminPresentation(friendlyName = "属性名称",
	prominent = true,
    fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
    broadleafEnumeration = "cn.globalph.coupon.type.CouponAttributeNameType"
	)
	private String attr;
	
	@Column(name = "VALUE")
	@AdminPresentation(friendlyName = "属性值",
	prominent = true)
	private String attrValue;
	
	@OneToOne(targetEntity=CouponImpl.class)
	@JoinColumn(name = "COUPON_ID")
    private Coupon coupon;

    @Override
    public Long getId(){
    	return this.id;
    }
    
    @Override
    public void setId(Long id){
    	this.id = id;
    }

    @Override
    public CouponAttributeCategoryType getCategory() {
        return CouponAttributeCategoryType.getInstance(category);
    }
    
    @Override
    public void setCategory(CouponAttributeCategoryType category) {
        this.category = category.getType();
    }

    @Override
    public CouponAttributeNameType getAttr() {
        return CouponAttributeNameType.getInstance(attr);
    }

    @Override
    public void setAttr(CouponAttributeNameType attr) {
        this.attr = attr.getType();
    }

    @Override
    public String getAttrValue() {
        return attrValue;
    }

    @Override
    public void setAttrValue(String attrValue) {
        this.attrValue = attrValue;
    }

	@Override
	public Coupon getCoupon() {
		return this.coupon;
	}

	@Override
	public void setCoupon(Coupon coupon) {
		this.coupon = coupon;	
	}
}
