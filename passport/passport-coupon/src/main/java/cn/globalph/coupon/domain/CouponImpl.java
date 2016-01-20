package cn.globalph.coupon.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.AddMethodType;
import cn.globalph.common.presentation.client.SupportedFieldType;
import cn.globalph.coupon.issue.event.CouponIssueEventType;
import cn.globalph.coupon.type.CouponAttributeCategoryType;
import cn.globalph.coupon.type.CouponAttributeNameType;
import cn.globalph.coupon.type.CouponStrategyType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "PH_COUPON")
@AdminPresentationClass(friendlyName="优惠券",populateToOneFields = PopulateToOneFieldsEnum.TRUE)
public class CouponImpl implements Coupon {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID")
	@Id
	@GeneratedValue(generator = "CouponId")
    @GenericGenerator(
            name = "CouponId",
            strategy = "cn.globalph.common.persistence.IdOverrideTableGenerator",
            parameters = {
                @Parameter(name = "segment_value", value = "CouponImpl"),
                @Parameter(name = "entity_name", value = "cn.globalph.coupon.domain.CouponImpl")
            }
        )
	private Long id;

	@Column(name = "STRATEGY")
    @AdminPresentation(friendlyName = "优惠券策略", order = 1000, group="优惠券",
    prominent = true, gridOrder = 1000,
    fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
    broadleafEnumeration = "cn.globalph.coupon.type.CouponStrategyType")
    private String strategy;

	@Column(name = "START_DATE")
	@AdminPresentation(friendlyName = "开始时间", order = 2000, group = "优惠券",
	prominent = true, gridOrder = 2000
	)
	private Date startDate;

	@AdminPresentation(friendlyName = "截止时间", order = 3000, group = "优惠券",
	prominent = true, gridOrder = 3000
	)
	@Column(name = "END_DATE")
    private Date endDate;

	@Column(name = "NAME")
	@AdminPresentation(friendlyName = "优惠券名称", order = 4000, group = "优惠券",
            prominent = true, gridOrder = 10
    )
	private String desc;
	
	@Column(name = "SCOPE")
	@AdminPresentation(friendlyName = "优惠券使用范围", order = 5000, group = "优惠券",
            prominent = true, gridOrder = 20
    )
    private String scope;

	@Column(name = "EVENT")
	@AdminPresentation(friendlyName = "触发事件", order = 6000, group = "优惠券",
            prominent = true, gridOrder = 50,
    fieldType = SupportedFieldType.BROADLEAF_ENUMERATION,
    broadleafEnumeration = "cn.globalph.coupon.issue.event.CouponIssueEventType"
	)
	private String event;

	@Column(name = "ISSUE_PRIORITY")
	@AdminPresentation(friendlyName = "优先级", order = 7000, group = "优惠券",
	prominent = true, gridOrder = 7000
	)
    private int issuePriority = 10;

	@Column(name = "NUMBER")
	@AdminPresentation(friendlyName = "优惠券数量", order = 8000, group = "优惠券",
            prominent = true, gridOrder = 40
    )
    private int number;

	@Column(name = "VALUE")
	@AdminPresentation(friendlyName = "优惠券数值", order = 9000, group = "优惠券",
            prominent = true, gridOrder = 30
    )
    private BigDecimal value;

	@OneToMany(targetEntity = CouponAttributeImpl.class, mappedBy="coupon")
    @AdminPresentationCollection(addType = AddMethodType.PERSIST, friendlyName = "优惠券属性")
    private List<CouponAttribute> couponAttributes = new ArrayList<CouponAttribute>();

	@Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public CouponStrategyType getStrategy() {
        return CouponStrategyType.getInstance(this.strategy);
    }

    @Override
    public void setStrategy(CouponStrategyType strategy) {
        this.strategy = strategy.getType();
    }

    @Override
    public Date getStartDate() {
        return startDate;
    }

    @Override
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String getDesc() {
        return desc;
    }

    @Override
    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public CouponIssueEventType getEvent() {
        return CouponIssueEventType.getInstance(event);
    }

    @Override
    public void setEvent(CouponIssueEventType event) {
        this.event = event.getType();
    }

    @Override
    public int getIssuePriority() {
        return issuePriority;
    }

    @Override
    public void setIssuePriority(int issuePriority) {
        this.issuePriority = issuePriority;
    }

    @Override
    public boolean isValidPeriod(Date checkDate) {
        return !checkDate.before(getStartDate()) && !checkDate.after(getEndDate());
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public BigDecimal getValue() {
        return value;
    }

    @Override
    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public List<CouponAttribute> getCouponAttributes() {
        return couponAttributes;
    }

    @Override
    public void setCouponAttributes(List<CouponAttribute> couponAttributes) {
        this.couponAttributes = couponAttributes;
    }

    @Override
    public Coupon addCouponAttribute(CouponAttribute couponAttribute) {
        this.couponAttributes.add(couponAttribute);
        return this;
    }

    @Override
    public String getAttrValue(String category, String attrName) {
        for (CouponAttribute attribute : couponAttributes) {
            if (attribute.getCategory().getType().equals(category)
                    && attribute.getAttr().getType().equals(attrName)) {
                return attribute.getAttrValue();
            }
        }
        return null;
    }

	@Override
	public String getMinPayAmount() {
		return getAttrValue(CouponAttributeCategoryType.APPLY_TO_ORDER_LMT.getType(), 
				CouponAttributeNameType.PAY_AMOUNT.getType());
	}

	@Override
	public String getScope() {
		return this.scope;
	}

	@Override
	public void setScope(String scope) {
		this.scope = scope;
	}   

}
