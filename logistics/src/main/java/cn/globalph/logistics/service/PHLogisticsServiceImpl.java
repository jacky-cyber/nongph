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
import cn.globalph.logistics.ph.Express;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author steven
 * @since 7/13/15
 */

@Service("phLogisticsService")
public class PHLogisticsServiceImpl implements LogisticsService {
    protected final Log logger = LogFactory.getLog(this.getClass());
    @Resource(name = "blOrderService")
    protected OrderService orderService;
    @Resource(name = "blOrderItemService")
    protected OrderItemService orderItemService;
    @Resource(name = "phOrderLogService")
    protected OrderLogService orderLogService;
    @Resource(name = "nphOrderAddressService")
    protected OrderAddressService orderAddressService;
    @Resource(name = "phExpressService")
    protected ExpressService expressService;

    @Value("${environment}")
    protected String environment;

    @Override
    @Transactional("blTransactionManager")
    public List<Express> requestExpressNo(List<Order> orderList) throws Exception {
        List<Express> expressList = new ArrayList<>();
        for (Order order : orderList) {
            if (!order.isShipped()) {
                Express express = buildExpress(order);
                if (express != null) {
                    /*delete by jenny start*/
                   // order.setShipped(true);
//                    orderService.save(order, false);
                        
                  /*  OrderLog orderLog = orderLogService.create();
                    orderLog.setOrder(order);
                    orderLog.setOperator("SYSTEM");
                    orderLog.setDateCreated(new Date());
                    orderLog.setType(OrderLog.ORDER_LOG_TYPE_EXPRESSION);
                    orderLog.setMessage("已发货, 运单号: " + express.getExpressNo());*/
                    //orderLogService.save(orderLog);
                    orderAddressService.save(order.getOrderAddress());
                    expressService.save(express);
                    expressList.add(express);
                    /*delete by jenny end*/
//                    Customer customer = order.getCustomer();
//                    SMSUtil.sendMessage(customer.getPhone(), "您的订单（"
//                            + order.getOrderNumber()
//                            + "）已出库，快递单号"
//                            + express.getExpressNo() + "，请签收时验货。");
                }
            }

        }
        return expressList;
    }

    private Express buildExpress(Order order) {
        PickupAddress senderAddress = getSenderAddress(order);
        OrderAddress orderAddress = order.getOrderAddress();

        if (orderAddress == null) {
            logger.error("未找到收货地址!");
            return null; //如果没有收货地址 不生成此单
        }

        Express express = expressService.create();
        String expressNo = expressService.generateExpressNo();
        express.setExpressNo(expressNo);

        express.setOrderNo(order.getOrderNumber());

        if (senderAddress != null) {
            express.setSender(senderAddress.getSender());
            express.setSenderPhone(senderAddress.getPhone());
            express.setSenderAddress(senderAddress.getAddress());
        }
        express.setReceiver(orderAddress.getReceiver());
        express.setReceiverPhone(orderAddress.getPhone());

        String address = orderAddress.getProvince() + orderAddress.getCity()
                + orderAddress.getDistrict() + (StringUtils.isEmpty(orderAddress.getCommunity()) ? "" : orderAddress.getCommunity()) + orderAddress.getAddress();
        express.setReceiverAddress(address);

        StringBuilder description = new StringBuilder();

        for (OrderItem orderItem : order.getOrderItems()) {
            description.append(orderItem.getName());
            description.append(" 数量:");
            description.append(orderItem.getQuantity());
            description.append("\n");
        }
        express.setDescription(description.toString());
        express.setDateCreated(new Date());

        orderAddress.setExNo(expressNo);
        orderAddress.setExName("品荟生活");
        return express;
    }

    private PickupAddress getSenderAddress(Order order) {
        PickupAddress senderAddress = null;
        List<OrderItem> orderItems = orderItemService.findOrderItemsByOrderId(order.getId());
        if (orderItems != null && !orderItems.isEmpty()) {
            order.setOrderItems(orderItems);
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
        return senderAddress;
    }

    @Override
    public File generateExcel(List<Express> expressList, String filePath, String sendDeliveryInfoEmail) {

        SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmm");
        String dateStr = fmt.format(new Date());

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();

        HSSFRow header = sheet.createRow(0);

        HSSFCell cell00 = header.createCell(0);
        cell00.setCellValue("express_no");

        HSSFCell cell01 = header.createCell(1);
        cell01.setCellValue("order_no");

        HSSFCell cell02 = header.createCell(2);
        cell02.setCellValue("receiver");

        HSSFCell cell03 = header.createCell(3);
        cell03.setCellValue("receiver_phone");

        HSSFCell cell04 = header.createCell(4);
        cell04.setCellValue("receiver_address");

        HSSFCell cell09 = header.createCell(5);
        cell09.setCellValue("description");

        int rowNum = 1;
        for (Express express : expressList) {
            HSSFRow row = sheet.createRow(rowNum++);

            //运单号
            HSSFCell expressNo = row.createCell(0);
            expressNo.setCellValue(express.getExpressNo());

            //订单号
            HSSFCell orderNo = row.createCell(1);
            orderNo.setCellValue(express.getOrderNo());

            //收件人
            HSSFCell receiver = row.createCell(2);
            receiver.setCellValue(express.getReceiver());

            //收件人电话
            HSSFCell receiverPhone = row.createCell(3);
            receiverPhone.setCellValue(express.getReceiverPhone());

            //收件人地址
            HSSFCell receiverAddress = row.createCell(4);
            receiverAddress.setCellValue(express.getReceiverAddress());

            //备注
            HSSFCell description = row.createCell(5);
            description.setCellValue(express.getDescription());

        }
        File file = new File(filePath + "(" + dateStr + ").xls");
        if (file.exists()) {
            file.delete();
        }
        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            workbook.write(fOut);
            fOut.flush();
            workbook.close();
            return file;

        } catch (IOException e) {
            logger.error("export excel throw exception:" + e.getMessage());
        }
        return null;
    }


    @Override
    public void requestTrack(List<String> expressNoList) throws Exception {

    }

    @Override
    public void requestTrack() throws Exception {

    }


}
