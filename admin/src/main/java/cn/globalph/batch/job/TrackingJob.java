package cn.globalph.batch.job;

import cn.globalph.b2c.order.service.OrderAddressService;
import cn.globalph.b2c.order.service.OrderLogService;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.batch.core.SingletonJob;
import cn.globalph.batch.core.annotation.JobId;
import cn.globalph.batch.core.context.JobContext;
import cn.globalph.logistics.service.LogisticsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author steven
 * @since 7/10/15
 */
@Component("TrackingJob")
@JobId("TrackingJob")
public class TrackingJob extends SingletonJob {
    protected final Log logger = LogFactory.getLog(this.getClass());
    @Resource(name = "blOrderService")
    protected OrderService orderService;
    @Resource(name = "phOrderLogService")
    protected OrderLogService orderLogService;
    @Resource(name = "nphOrderAddressService")
    protected OrderAddressService orderAddressService;
    @Resource(name = "phLogisticsService")
    protected LogisticsService logisticsService;

    @Override
    protected void doExecute(JobContext jobContext) throws Exception {
        logisticsService.requestTrack();
    }
}
