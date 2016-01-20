package cn.globalph.b2c.order.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @author steven.wang
 */
public interface OrderLog extends Serializable {
    String ORDER_LOG_TYPE_SYSTEM = "SYSTEM";
    String ORDER_LOG_TYPE_CUSTOMER = "CUSTOMER";
    String ORDER_LOG_TYPE_EXPRESSION = "EXPRESS";

    Long getId();

    void setId(Long id);

    Order getOrder();

    void setOrder(Order order);

    String getType();

    void setType(String type);

    String getMessage();

    void setMessage(String message);

    String getOperator();

    void setOperator(String operator);

    String getRemark();

    void setRemark(String remark);

    Boolean isDisplay();

    void setDisplay(Boolean isDisplay);

    Date getDateCreated();

    void setDateCreated(Date dateCreated);
}
