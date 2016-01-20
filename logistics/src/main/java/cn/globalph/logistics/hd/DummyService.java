package cn.globalph.logistics.hd;

import cn.globalph.b2c.order.domain.Order;
import com.thoughtworks.xstream.XStream;

import java.util.ArrayList;
import java.util.List;

/**
 * @author steven
 * @since 7/14/15
 */
public class DummyService {

    public String getDummyBill(List<Order> orderList) {
        OrderResponse orderResponse = new OrderResponse();
        List<OrderResponse.Result> resultList = new ArrayList<>();
        for (Order order : orderList) {
            OrderResponse.Result result = new OrderResponse.Result();
            result.setFlag(1);
            result.setRemark("SUCCESS");
            result.setDoIds(order.getOrderNumber());
            result.setBillNo("HD" + order.getOrderNumber());
            resultList.add(result);
        }
        orderResponse.setResultList(resultList);
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        return xStream.toXML(orderResponse);
    }

}
