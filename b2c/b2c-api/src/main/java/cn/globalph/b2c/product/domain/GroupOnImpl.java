package cn.globalph.b2c.product.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import cn.globalph.common.money.Money;
import org.hibernate.annotations.Type;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderImpl;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationCollection;
import cn.globalph.common.presentation.AdminPresentationToOneLookup;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.SupportedFieldType;

@Entity
@Table(name = "PH_GROUP_ON")
@AdminPresentationClass(friendlyName = "团购活动",populateToOneFields = PopulateToOneFieldsEnum.TRUE)
public class GroupOnImpl implements GroupOn {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "GROUP_ON_ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(targetEntity = SkuImpl.class)
	@JoinColumn(name = "SKU_ID")
	@AdminPresentation(friendlyName = "团购商品")
	@AdminPresentationToOneLookup
	private Sku sku;
	
	@Column(name = "SKU_QUANTITY")
	@AdminPresentation(friendlyName = "团购商品数量",prominent = true)
	private Integer skuQuantity;
	
	@Column(name = "PARTICIPATE_IN_COUNT")
	@AdminPresentation(friendlyName = "参团人数",prominent = true)
	private Integer participateInCount;

	@Column(name = "RULE_DESC", length = Integer.MAX_VALUE - 1)
    @Type(type = "org.hibernate.type.StringClobType")
    @AdminPresentation(friendlyName = "团购规则说明",
            largeEntry = true,
            fieldType = SupportedFieldType.HTML_BASIC,
            translatable = true)
	private String ruleDesc;
	
	@Column(name = "START_DATE")
	@AdminPresentation(friendlyName = "活动开始时间",prominent = true)
	private Date startDate;
	
	@Column(name = "END_DATE")
	@AdminPresentation(friendlyName = "活动结束时间",prominent = true)
	private Date endDate;
	
	@OneToMany(mappedBy = "groupOn", targetEntity = GroupOnAttrImpl.class)
	@AdminPresentationCollection(friendlyName = "团购活动属性")
	private List<GroupOnAttr> groupOnAttrs = new ArrayList<GroupOnAttr>();
	
	@OneToMany(mappedBy = "groupOn", targetEntity = OrderImpl.class)
	private List<Order> orders = new ArrayList<Order>();

	/*add by jenny s*/
	@AdminPresentation(friendlyName = "每份商品价格",prominent = true)
	@Column(name = "GROUP_PRICE", precision = 19, scale = 5)
	private BigDecimal groupPrice;

	@Column(name = "GROUP_UNIT")
	@AdminPresentation(friendlyName = "商品计量单位",prominent = true)
	private String groupUnit;

	@Column(name = "MIN_COUNT")
	@AdminPresentation(friendlyName = "起团数",prominent = true)
	private Integer minCount;

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Sku getSku() {
		return this.sku;
	}

	@Override
	public void setSku(Sku sku) {
		this.sku = sku;
	}

	@Override
	public Integer getSkuQuantity() {
		return this.skuQuantity;
	}

	@Override
	public void setSkuQuantity(Integer skuQuantity) {
		this.skuQuantity = skuQuantity;
	}

	@Override
	public Integer getParticipateInCount() {
		return this.participateInCount;
	}

	@Override
	public void setParticipateInCount(Integer participateInCount) {
		this.participateInCount = participateInCount;
	}

	@Override
	public String getRuleDesc() {
		return this.ruleDesc;
	}

	@Override
	public void setRuleDesc(String ruleDesc) {
		this.ruleDesc = ruleDesc;
	}

	@Override
	public Date getStartDate() {
		return this.startDate;
	}

	@Override
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	public Date getEndDate() {
		return this.endDate;
	}

	@Override
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public List<GroupOnAttr> getGroupOnAttrs() {
		return this.groupOnAttrs;
	}

	@Override
	public void setGroupOnAttrs(List<GroupOnAttr> groupOnAttrs) {
		this.groupOnAttrs = groupOnAttrs;
	}

	@Override
	public List<Order> getOrders() {
		return this.orders;
	}

	@Override
	public void setOrders(List<Order> orders) {
		this.orders = orders;	
	}

	@Override
	public boolean isValid() {
		Date current = new Date(System.currentTimeMillis());
		if(current.after(getStartDate()) && current.before(getEndDate())){
			return true;
		}else{
			return false;
		}
	}
	/*add by jenny 09/09 s*/
	@Override
	public void setGroupPrice(Money groupPrice) {
		if(groupPrice != null){
			this.groupPrice = groupPrice.getAmount();
		}else{
			this.groupPrice = Money.ZERO.getAmount();
		}
	}

	@Override
	public Money getGroupPrice() {
		if(this.groupPrice != null){
			return new Money(this.groupPrice);
		}else{
			return Money.ZERO;
		}
	}

	@Override
	public void setGroupUnit(String groupUnit) {
		this.groupUnit = groupUnit;
	}

	@Override
	public String getGroupUnit() {
		return this.groupUnit;
	}

	@Override
	public void setMinCount(Integer minCount) {
		this.minCount = minCount;
	}

	@Override
	public Integer getMinCount() {
		return this.minCount;
	}
	/*add by jenny 09/09 e*/
}
