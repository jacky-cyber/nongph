package cn.globalph.logistics.service;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.domain.OrderAddress;
import cn.globalph.b2c.order.domain.OrderItem;
import cn.globalph.b2c.order.domain.OrderLog;
import cn.globalph.b2c.order.service.OrderAddressService;
import cn.globalph.b2c.order.service.OrderItemService;
import cn.globalph.b2c.order.service.OrderLogService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.b2c.product.domain.PickupAddress;
import cn.globalph.b2c.product.domain.Provider;
import cn.globalph.logistics.hd.OrderRequest;
import cn.globalph.logistics.hd.OrderResponse;
import cn.globalph.logistics.hd.TrackRequest;
import cn.globalph.logistics.hd.TrackResponse;
import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author steven
 * @since 7/13/15
 */

@Service("hdLogisticsService")
//public class HDLogisticsServiceImpl implements LogisticsService {
public class HDLogisticsServiceImpl {
    protected final Log logger = LogFactory.getLog(this.getClass());

    private static List<Order> tempOrderList;
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Resource(name = "blOrderService")
    protected OrderService orderService;
    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;
    @Resource(name = "phOrderLogService")
    protected OrderLogService orderLogService;
    @Resource(name = "nphOrderAddressService")
    protected OrderAddressService orderAddressService;
    @Value("${environment}")
    protected String environment;
    @Value("${md5Key}")
    private String md5Key;
    @Value("${partners}")
    private String partners;

    @Transactional("blTransactionManager")
    public void requestExpressNo(List<Order> orderList) throws Exception {
        String requestXml = buildOrderRequestXml(orderList);
        logger.info("Order request: \n" + requestXml);
        String responseXml;
        if ("PRD".equals(environment)) {
            responseXml = getBill(requestXml);
        } else {
            responseXml = getDummyBill(orderList);
        }
        logger.info("Order Response: \n" + responseXml);
        XStream xStream = new XStream();
        xStream.alias("OrderResponse", OrderResponse.class);
        xStream.autodetectAnnotations(true);
        xStream.addImplicitCollection(OrderResponse.class, "resultList");
        Object response = xStream.fromXML(responseXml);
        OrderResponse orderResponse = (OrderResponse) response;
        List<OrderResponse.Result> resultList = orderResponse.getResultList();
        for (OrderResponse.Result result : resultList) {
            Order order = orderService.findOrderByOrderNumber(result.getDoIds());
            if (1 == result.getFlag()) {
                if (StringUtils.isNotEmpty(result.getBillNo())) {
                    OrderAddress orderAddress = orderAddressService.readOrderAddressByOrderId(order.getId());
                    orderAddress.setExNo(result.getBillNo());
                    orderAddress.setExName("宏递快运");
                    orderAddressService.save(orderAddress);

                    OrderLog orderLog = orderLogService.create();
                    orderLog.setOrder(order);
                    orderLog.setOperator("SYSTEM");
                    orderLog.setDateCreated(new Date());
                    orderLog.setType(OrderLog.ORDER_LOG_TYPE_EXPRESSION);
                    orderLog.setMessage("已发宏递快运, 运单号: " + result.getBillNo());
                    orderLog.setRemark(result.getRemark());
                    orderLogService.save(orderLog);
                } else {
                    OrderLog orderLog = orderLogService.create();
                    orderLog.setOrder(order);
                    orderLog.setOperator("SYSTEM");
                    orderLog.setDateCreated(new Date());
                    orderLog.setType(OrderLog.ORDER_LOG_TYPE_EXPRESSION);
                    orderLog.setRemark(result.getRemark());
                    orderLog.setDisplay(false);
                    orderLogService.save(orderLog);
                    logger.error(result.getDoIds() + " - " + result.getRemark());
                }
                order.setShipped(true);
                orderService.save(order, false);
                Order parentOrder = order.getOrder();
                if (parentOrder != null) {
                    List<Order> suborderList = orderService.readSuborderList(parentOrder.getId());
                    boolean isShipped = true;
                    for (Order suborder : suborderList) {
                        if (!suborder.isShipped()) {
                            isShipped = false;
                        }
                    }
                    if (isShipped) {
                        parentOrder.setShipped(true);
                        orderService.save(parentOrder, false);
                    }
                }
            } else {
                OrderLog orderLog = orderLogService.create();
                orderLog.setOrder(order);
                orderLog.setOperator("SYSTEM");
                orderLog.setDateCreated(new Date());
                orderLog.setType(OrderLog.ORDER_LOG_TYPE_EXPRESSION);
                orderLog.setRemark(result.getRemark());
                orderLog.setDisplay(false);
                orderLogService.save(orderLog);
                logger.error(result.getDoIds() + " - " + result.getRemark());
            }
        }
    }

    public void requestTrack() throws Exception {
        String requestXml = buildTrackRequestXml();
        logger.info("Track Request: \n" + requestXml);
        String responseXml;
        if ("PRD".equals(environment)) {
            responseXml = getTrack(requestXml);
        } else {
            responseXml = getDummyTrack();
        }
        logger.info("Track Response: \n" + responseXml);
        if (StringUtils.isNotEmpty(responseXml)) {
            XStream xStream = new XStream();
            xStream.alias("PushTrackResponse", TrackResponse.class);
            xStream.autodetectAnnotations(true);
            TrackResponse trackResponse = (TrackResponse) xStream.fromXML(responseXml);

            List<TrackResponse.OrderTrack> orderTrackList = trackResponse.getOrderTrackList();
            for (TrackResponse.OrderTrack orderTrack : orderTrackList) {
                Order order = orderService.findOrderByOrderNumber(orderTrack.getOrderNo());
                if (order != null) {
                    OrderLog orderLog = orderLogService.create();
                    orderLog.setOrder(order);
                    orderLog.setType(OrderLog.ORDER_LOG_TYPE_EXPRESSION);
                    Date createDate;
                    try {
                        createDate = sdf.parse(orderTrack.getOperatorDate());
                    } catch (ParseException e) {
                        createDate = new Date();
                    }
                    orderLog.setDateCreated(createDate);
                    orderLog.setOperator(orderTrack.getOperator());
                    String message;
                    switch (orderTrack.getStatus()) {
                        case "收件":
                            message = "收件";
                            break;
                        case "到件卸车":
                            message = orderTrack.getScanSite() + "到件卸车 上一站为" + orderTrack.getStation();
                            break;
                        case "发件装车":
                            message = orderTrack.getScanSite() + "发件装车 下一站为" + orderTrack.getStation();
                            break;
                        case "派件装车":
                            message = "派件装车";
                            break;
                        case "签收":
                            message = "签收 签收人:" + orderTrack.getCtrName();
                            break;
                        default:
                            message = "";
                    }
                    orderLog.setMessage(message);
                    orderLog.setRemark(orderTrack.getRemark());
                    orderLogService.save(orderLog);
                } else {
                    logger.error("OrderNo not found - " + orderTrack.getOrderNo());
                }
            }
        } else {
            logger.info("No logistics order track response!");
        }
    }

    public String buildOrderRequestXml(List<Order> orderList) {
        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);
        String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setMd5Key(md5Key);
        orderRequest.setPartners(partners);

        List<OrderRequest.Order> ordersList = new ArrayList<>();
        orderRequest.setOrders(ordersList);
        for (Order order : orderList) {
            List<Order> suborders = orderService.readSuborderList(order.getId());
            if (suborders != null && !suborders.isEmpty()) {
                for (Order subOrder : suborders) {
                    if (!subOrder.isShipped()) {
                        buildOrder(orderRequest, subOrder);
                    }
                }
            } else {
                if (!order.isShipped()) {
                    buildOrder(orderRequest, order);
                }
            }
        }

        String xml = xstream.toXML(orderRequest);
        return xmlHeader + xml;
    }

    public void buildOrder(OrderRequest orderRequest, Order order) {
        OrderRequest.Order requestOrder = orderRequest.newOrder();
        requestOrder.setDoIds(order.getOrderNumber());
        requestOrder.setCount(1);
        if (order.getTotal() == null) {
            requestOrder.setValue(100f);
        } else {
            requestOrder.setValue(order.getTotal().getAmount().floatValue());
        }
        PickupAddress senderAddress = null;
        List<OrderItem> orderItems = orderItemService.findOrderItemsByOrderId(order.getId());
        if (orderItems != null && !orderItems.isEmpty()) {
            OrderItem orderItem = orderItems.get(0);
            Provider provider = orderItem.getSku().getProduct().getProvider();
            List<PickupAddress> pickupAddresses = provider.getPickupAddresses();
            for (PickupAddress pickupAddress : pickupAddresses) {
                if (pickupAddress.isDefault()) {
                    senderAddress = pickupAddress;
                    break;
                }
            }
        }
        if (senderAddress == null) return; //如果没设置默认pickup地址 不发送此单

        OrderRequest.SenderReceiver sender = orderRequest.newSenderReceiver();
        OrderAddress orderAddress = order.getOrderAddress();
        sender.setName(senderAddress.getSender());
        sender.setTel(senderAddress.getPhone());
        sender.setAddr(senderAddress.getAddress());
        requestOrder.setSender(sender);
        OrderRequest.SenderReceiver receiver = orderRequest.newSenderReceiver();
        receiver.setName(orderAddress.getReceiver());
        receiver.setTel(orderAddress.getPhone());
        String address = orderAddress.getProvince() + orderAddress.getCity()
                + orderAddress.getDistrict() + orderAddress.getAddress();
        receiver.setAddr(address);
        requestOrder.setReceiver(receiver);

        if ("PRD".equals(environment)) {
            requestOrder.setExtendField2("苏州");
        }
        orderRequest.getOrders().add(requestOrder);
    }

    public String buildTrackRequestXml() {
        XStream xstream = new XStream();
        xstream.autodetectAnnotations(true);
        String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        TrackRequest trackRequest = new TrackRequest();
        trackRequest.setMd5Key(md5Key);
        trackRequest.setPartners(partners);
        String xml = xstream.toXML(trackRequest);
        return xmlHeader + xml;
    }

    public String getBill(String xml) throws Exception {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        org.apache.cxf.endpoint.Client client1 = dcf.createClient("http://api.hd.com.cn/Service1.svc?wsdl");
        Object[] objects = client1.invoke("SLFBuildOrder", xml);
        return objects[0].toString();
    }

    public String getTrack(String xml) throws Exception {
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        org.apache.cxf.endpoint.Client client1 = dcf.createClient("http://api.hd.com.cn/Service1.svc?wsdl");
        Object[] objects = client1.invoke("PushTrack", xml);
        return objects[0].toString();
    }

    public String getDummyBill(List<Order> orderList) {
        OrderResponse orderResponse = new OrderResponse();
        List<OrderResponse.Result> resultList = new ArrayList<>();
        for (Order order : orderList) {
            List<Order> suborders = orderService.readSuborderList(order.getId());
            if (suborders != null && !suborders.isEmpty()) {
                for (Order suborder : suborders) {
                    OrderResponse.Result result = new OrderResponse.Result();
                    result.setFlag(1);
                    result.setRemark("SUCCESS");
                    result.setDoIds(suborder.getOrderNumber());
                    result.setBillNo("HD" + suborder.getOrderNumber());
                    resultList.add(result);
                }
            } else {
                OrderResponse.Result result = new OrderResponse.Result();
                result.setFlag(1);
                result.setRemark("SUCCESS");
                result.setDoIds(order.getOrderNumber());
                result.setBillNo("HD" + order.getOrderNumber());
                resultList.add(result);
            }
        }
        orderResponse.setResultList(resultList);
        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        tempOrderList = orderList;
        return xStream.toXML(orderResponse);
    }

    public String getDummyTrack() {
        TrackResponse trackResponse = new TrackResponse();
        List<TrackResponse.OrderTrack> resultList = new ArrayList<>();
        if (tempOrderList != null) {
            for (Order order : tempOrderList) {
                TrackResponse.OrderTrack result1 = new TrackResponse.OrderTrack();
                result1.setOperator("张三");
                result1.setContent("");
                result1.setStatus("收件");
                result1.setOperatorDate(sdf.format(new Date()));
                result1.setOrderNo(order.getOrderNumber());
                result1.setOrderCode("HD" + order.getOrderNumber());
                resultList.add(result1);

                TrackResponse.OrderTrack result2 = new TrackResponse.OrderTrack();
                result2.setOperator("张三");
                result2.setStatus("发件装车");
                result2.setScanSite("苏州新区分拨");
                result2.setStation("苏州园区分拨");
                result2.setRemark("浙HK1361");
                result2.setOperatorDate(sdf.format(new Date()));
                result2.setOrderNo(order.getOrderNumber());
                result2.setOrderCode("HD" + order.getOrderNumber());
                resultList.add(result2);

                TrackResponse.OrderTrack result3 = new TrackResponse.OrderTrack();
                result3.setOperator("张三");
                result3.setStatus("到件卸车");
                result3.setScanSite("苏州园区分拨");
                result3.setStation("苏州新区分拨");
                result3.setRemark("浙HK1361");
                result3.setOperatorDate(sdf.format(new Date()));
                result3.setOrderNo(order.getOrderNumber());
                result3.setOrderCode("HD" + order.getOrderNumber());
                resultList.add(result3);

                TrackResponse.OrderTrack result4 = new TrackResponse.OrderTrack();
                result4.setOperator("张三");
                result4.setStatus("签收");
                result4.setCtrName("李四");
                result4.setRemark("浙HK1361");
                result4.setOperatorDate(sdf.format(new Date()));
                result4.setOrderNo(order.getOrderNumber());
                result4.setOrderCode("HD" + order.getOrderNumber());
                resultList.add(result4);
            }
            trackResponse.setOrderTrackList(resultList);
            XStream xStream = new XStream();
            xStream.autodetectAnnotations(true);
            tempOrderList = null;
            return xStream.toXML(trackResponse);
        } else {
            return "";
        }
    }
}
