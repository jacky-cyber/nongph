package cn.globalph.logistics.service;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.logistics.ph.Express;

import java.io.File;
import java.util.List;

/**
 * @author steven
 * @since 7/13/15
 */
public interface LogisticsService {

    List<Express> requestExpressNo(List<Order> orderList) throws Exception;

    File generateExcel(List<Express> expressList, String filePath, String sendDeliveryInfoEmail);

    void requestTrack(List<String> expressNoList) throws Exception;

    void requestTrack() throws Exception;
}
