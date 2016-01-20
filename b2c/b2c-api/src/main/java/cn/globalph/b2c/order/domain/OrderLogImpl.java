package cn.globalph.b2c.order.domain;

import cn.globalph.common.presentation.AdminPresentation;
import cn.globalph.common.presentation.AdminPresentationClass;
import cn.globalph.common.presentation.PopulateToOneFieldsEnum;
import cn.globalph.common.presentation.client.VisibilityEnum;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "NPH_ORDER_LOG")
@AdminPresentationClass(populateToOneFields = PopulateToOneFieldsEnum.TRUE, friendlyName = "OrderImpl_Log")
public class OrderLogImpl implements OrderLog {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LOG_ID")
    @AdminPresentation(friendlyName = "OrderLogImpl_Id", group = "OrderLogImpl_OrderLog",
            visibility = VisibilityEnum.HIDDEN_ALL)
    protected Long id;

    @ManyToOne(targetEntity = OrderImpl.class)
    @JoinColumn(name = "ORDER_ID")
    @AdminPresentation(excluded = true)
    protected Order order;

    @Column(name = "TYPE", nullable = false)
    @AdminPresentation(friendlyName = "OrderLogImpl_Type", order = 2000, group = "OrderLogImpl_OrderLog",
            prominent = true, gridOrder = 1)
    protected String type;

    @Column(name = "MESSAGE")
    @AdminPresentation(friendlyName = "OrderLogImpl_Message", order = 2000, group = "OrderLogImpl_OrderLog",
            prominent = true, gridOrder = 2)
    protected String message;

    @Column(name = "OPERATOR")
    @AdminPresentation(friendlyName = "OrderLogImpl_Operator", order = 2000, group = "OrderLogImpl_OrderLog",
            prominent = true, gridOrder = 3)
    protected String operator;

    @Column(name = "REMARK")
    @AdminPresentation(friendlyName = "OrderLogImpl_Remark", order = 2000, group = "OrderLogImpl_OrderLog",
            prominent = true, gridOrder = 4)
    protected String remark;

    @Column(name = "IS_DISPLAY")
    @AdminPresentation(friendlyName = "OrderLogImpl_IsDisplay", order = 2000, group = "OrderLogImpl_OrderLog",
            prominent = true, gridOrder = 5)
    protected Boolean isDisplay = true;

    @Column(name = "DATE_CREATED", nullable = false)
    @AdminPresentation(friendlyName = "OrderLogImpl_Date_Created", group = "OrderLogImpl_OrderLog",
            gridOrder = 6)
    protected Date dateCreated = new Date();

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Order getOrder() {
        return order;
    }

    @Override
    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getOperator() {
        return operator;
    }

    @Override
    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public Boolean isDisplay() {
        return isDisplay;
    }

    @Override
    public void setDisplay(Boolean isDisplay) {
        this.isDisplay = isDisplay;
    }

    @Override
    public Date getDateCreated() {
        return dateCreated;
    }

    @Override
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
