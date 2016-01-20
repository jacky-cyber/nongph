package cn.globalph.batch.job;

import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.batch.core.SingletonJob;
import cn.globalph.batch.core.annotation.JobId;
import cn.globalph.batch.core.context.JobContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author steven
 * @since 7/10/15
 */
@Component("DetermineDeliveryTypeJob")
@JobId("DetermineDeliveryTypeJob")
public class DetermineDeliveryTypeJob extends SingletonJob {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Resource(name = "blOrderService")
    protected OrderService orderService;
    @Value("${logistics.minCount}")
    protected String minCountStr;

    @Override
    protected void doExecute(JobContext jobContext) throws Exception {
        int minCount = Integer.decode(minCountStr);
        int logisticsResult = orderService.determineLogisticsOrders(minCount);
        logger.info("Determine logistics orders count: " + logisticsResult);
        int expressResult1 = orderService.determineExpressOrdersWithCommunity(minCount);
        logger.info("Determine express orders with community count: " + expressResult1);
        int expressResult2 = orderService.determineExpressOrdersWithoutCommunity();
        logger.info("Determine express orders without community count: " + expressResult2);
    }
}
