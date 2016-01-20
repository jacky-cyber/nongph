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


@Component("SendDeliveryInfoJob")
@JobId("SendDeliveryInfoJob")
public class SendDeliveryInfoJob extends SingletonJob {
    protected final Log logger = LogFactory.getLog(this.getClass());
    @Resource(name = "blOrderService")
    private OrderService orderService;

    @Value("${batch.sendDeliveryInfoJob.exportFilePath}")
    private String exportFilePath;
    
    @Value("${batch.sendDeliveryInfoJob.toEmail}")
    protected String sendDeliveryInfoEmail;
    
    @Override
    protected void doExecute(JobContext jobContext) throws Exception {
        orderService.exportOrder(exportFilePath, sendDeliveryInfoEmail);
    }
}
