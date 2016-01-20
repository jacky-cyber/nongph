package cn.globalph.b2c.order.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import cn.globalph.b2c.order.service.type.RefundStatus;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransform;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformMember;
import cn.globalph.common.extensibility.jpa.copy.DirectCopyTransformTypes;
import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.SupportedFieldType;

@Entity
@Table(name = "PH_REFUND")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "申请退货")
@DirectCopyTransform({
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.PREVIEW, skipOverlaps=true),
        @DirectCopyTransformMember(templateTokens = DirectCopyTransformTypes.MULTITENANT_SITE)
})
public class RefundImpl implements Refund {
    
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(generator = "RefundId")
    @GenericGenerator(
        name = "RefundId",
        strategy = "cn.globalph.common.persistence.IdOverrideTableGenerator",
        parameters = {
            @Parameter(name = "segment_value", value = "RefundImpl"),
            @Parameter(name = "entity_name", value = "cn.globalph.b2c.order.domain.RefundImpl")
        }
    )
    @Column(name = "REFUND_ID")
	private Long refundId;
	
    @Column(name = "REFUND_DESC")
    @AdminPresentation(friendlyName = "退款描述", group = "申请退款",
    order=1000, prominent=true, groupOrder = 1000,
    gridOrder = 1000,readOnly=true)
    private String refundDesc;
	
    @Column(name = "REFUND_STATUS")
    @AdminPresentation(friendlyName = "退款状态", group = "申请退款",
    order=2000, prominent=true, fieldType=SupportedFieldType.BROADLEAF_ENUMERATION,
    broadleafEnumeration="cn.globalph.b2c.order.service.type.RefundStatus",
    groupOrder = 2000, gridOrder = 2000)
	private String refundStatus;
	
    @Column(name = "REFUND_TIME")
    @AdminPresentation(friendlyName = "申请退款时间", group = "申请退款",
    order=3000, groupOrder = 3000, prominent = true,
    gridOrder = 3000,readOnly=true)
	private Date refundTime;
	
    @Column(name = "REFUND_BY")
    @AdminPresentation(friendlyName = "申请退款处理人", group = "申请退款",
    order=4000, groupOrder = 4000, prominent = true,
    gridOrder = 4000,readOnly=false)
	private String refundBy;
    
    @AdminPresentation(friendlyName = "申请数量", group = "申请退款",
    order=5000, groupOrder = 5000,
    gridOrder = 5000,readOnly=true)
    @Column(name = "REFUND_NUM")
    private Integer refundNum;
	
    @OneToOne(targetEntity=OrderItemImpl.class)
    @JoinColumn(name = "ORDER_ITEM_ID")
    @AdminPresentation(friendlyName = "退款订单项", tab = "退款订单项",
    tabOrder = 2000, excluded=true)
	private OrderItem orderItem;
    
    @OneToOne(targetEntity=OrderImpl.class)
    @JoinColumn(name = "ORDER_ID")
    @AdminPresentation(friendlyName = "退款订单", tab = "退款订单",
    tabOrder = 2000, excluded=true)
	private Order order;

    @OneToMany(targetEntity = RefundMediaImpl.class, mappedBy = "refund")
//    @AdminPresentationCollection(friendlyName="上传图片")
    private List<RefundMedia> refundMedia = new ArrayList<RefundMedia>();
    
	public Long getRefundId() {
		return refundId;
	}

	public void setRefundId(Long refundId) {
		this.refundId = refundId;
	}

	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}

	public RefundStatus getRefundStatus() {
		return RefundStatus.getInstance(refundStatus);
	}

	public void setRefundStatus(RefundStatus refundStatus) {
		this.refundStatus = refundStatus.getType();
	}

	public Date getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(Date refundTime) {
		this.refundTime = refundTime;
	}

	public String getRefundBy() {
		return refundBy;
	}

	public void setRefundBy(String refundBy) {
		this.refundBy = refundBy;
	}

	public OrderItem getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(OrderItem orderItem) {
		this.orderItem = orderItem;
	}

	@Override
	public List<RefundMedia> getRefundMedia() {
		return this.refundMedia;
	}

	@Override
	public void setRefundMedia(List<RefundMedia> refundMedia) {
		this.refundMedia = refundMedia;
	}

	@Override
	public Integer getRefundNum() {
		return this.refundNum;
	}

	@Override
	public void setRefundNum(Integer refundNum) {
		this.refundNum = refundNum;	
	}

	@Override
	public Order getOrder() {
		return this.order;
	}

	@Override
	public void setOrder(Order order) {
		this.order = order;
	}
}
