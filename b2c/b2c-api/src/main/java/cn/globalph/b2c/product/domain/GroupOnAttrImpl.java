package cn.globalph.b2c.product.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.globalph.common.money.Money;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.AdminPresentationToOneLookup;

@Entity
@Table(name = "PH_GROUP_ON_ATTR")
@AdminPresentationClass(friendlyName = "团购活动属性")
public class GroupOnAttrImpl implements GroupOnAttr {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "GROUP_ON_ATTR_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(targetEntity = GroupOnImpl.class)
	@JoinColumn(name = "GROUP_ON_ID")
	@AdminPresentation(friendlyName = "团购活动")
	@AdminPresentationToOneLookup
	private GroupOn groupOn;
	
	@Column(name = "COUNT_RANGE_FROM")
	@AdminPresentation(friendlyName = "参团人数（从）",prominent = true)
	private Integer countRangeFrom;
	
	@Column(name = "COUNT_RANGE_TO")
	@AdminPresentation(friendlyName = "参团人数（至）",prominent = true)
	private Integer countRangeTo;
	
	@AdminPresentation(friendlyName = "每份商品价格",prominent = true)
	@Column(name = "PRICE", precision = 19, scale = 5)
	private BigDecimal price;
	
	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public GroupOn getGroupOn() {
		return this.groupOn;
	}

	@Override
	public void setGroupOn(GroupOn groupOn) {
		this.groupOn = groupOn;
	}

	@Override
	public Integer getCountRangeFrom() {
		return this.countRangeFrom;
	}

	@Override
	public void setCountRangeFrom(Integer countRangeFrom) {
		this.countRangeFrom = countRangeFrom;
	}

	@Override
	public Integer getCountRangeTo() {
		return this.countRangeTo;
	}

	@Override
	public void setCountRangeTo(Integer countRangeTo) {
		this.countRangeTo = countRangeTo;
	}

	@Override
	public Money getPrice() {
		if(this.price != null){
			return new Money(this.price);
		}else{
			return Money.ZERO;
		}
	}

	@Override
	public void setPrice(Money price) {
		if(price != null){
			this.price = price.getAmount();
		}else{
			this.price = Money.ZERO.getAmount();
		}
	}
}
