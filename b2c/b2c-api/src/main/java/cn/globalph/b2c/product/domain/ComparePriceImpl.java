package cn.globalph.b2c.product.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationToOneLookup;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import cn.globalph.common.money.Money;

@Table(name = "PH_COMPARE_PRICE")
@Entity
public class ComparePriceImpl implements ComparePrice {
	@Column(name = "COMPARE_PRICE_ID")
	@Id
	@GeneratedValue(generator = "ComparePriceId")
	@GenericGenerator(
			name = "ComparePriceId",
			strategy = "cn.globalph.common.persistence.IdOverrideTableGenerator",
			parameters = {
		            @Parameter(name = "segment_value", value = "SkuImpl"),
		            @Parameter(name = "entity_name", value = "cn.globalph.b2c.product.domain.SkuImpl")
			}
	)
	private Long id;
		
	@ManyToOne(targetEntity = SkuImpl.class)
    @JoinColumn(name = "SKU_ID")
	@AdminPresentation(friendlyName = "平台对应商品",
	order = 10000,
	tab = "一般",
	tabOrder = 1,
	group = "一般",
	groupOrder = 1, gridOrder = 1)
    @AdminPresentationToOneLookup
	private Sku sku;
	
    @Column(name = "NAME")
	@AdminPresentation(friendlyName = "名称",
			order = 10000,
			tab = "一般",
			tabOrder = 1,
			group = "一般",
			groupOrder = 1, prominent = true, gridOrder = 1)
	private String name;
	
    @Column(name = "PRICE")
	@AdminPresentation(friendlyName = "价格",
			order = 10000,
			tab = "一般",
			tabOrder = 1,
			group = "一般",
			groupOrder = 1, prominent = true, gridOrder = 2)
	private BigDecimal price;
	
    @Column(name = "SOURCE")
	@AdminPresentation(friendlyName = "来源",
			order = 10000,
			tab = "一般",
			tabOrder = 1,
			group = "一般",
			groupOrder = 1, prominent = true, gridOrder = 3)
	private String source;
	
    @Column(name = "SOURCE_URL")
	private String sourceUrl;
	
    @Column(name = "CREATE_TIME")
	@AdminPresentation(friendlyName = "添加时间",
			order = 10000,
			tab = "一般",
			tabOrder = 1,
			group = "一般",
			groupOrder = 1, prominent = true, gridOrder = 4)
	private Date createTime;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Money getPrice() {
		return new Money(this.price);
	}

	@Override
	public void setPrice(Money price) {
		if(price != null){
			this.price = Money.toAmount(price);
		}
		
		this.price = null;
	}

	@Override
	public String getSource() {
		return this.source;
	}

	@Override
	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public String getSourceUrl() {
		return this.sourceUrl;
	}

	@Override
	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	@Override
	public Date getCreateTime() {
		return this.createTime;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
