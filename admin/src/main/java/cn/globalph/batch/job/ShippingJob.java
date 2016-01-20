package cn.globalph.batch.job;

import cn.globalph.b2c.order.domain.Order;
import cn.globalph.b2c.order.service.OrderService;
import cn.globalph.batch.core.SingletonJob;
import cn.globalph.batch.core.annotation.JobId;
import cn.globalph.batch.core.context.JobContext;
import cn.globalph.common.email.service.EmailService;
import cn.globalph.common.email.service.info.EmailInfo;
import cn.globalph.logistics.ph.Express;
import cn.globalph.logistics.service.LogisticsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author steven
 * @since 7/10/15
 */
@Component("ShippingJob")
@JobId("ShippingJob")
public class ShippingJob extends SingletonJob {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Resource(name = "blOrderService")
    protected OrderService orderService;
    @Resource(name = "phLogisticsService")
    protected LogisticsService logisticsService;
    @Resource(name = "phShippingEmailInfo")
    protected EmailInfo shippingEmailInfo;
    @Resource(name = "blEmailService")
    protected EmailService emailService;

    @Value("${batch.shippingJob.exportFilePath}")
    protected String exportFilePath;
    @Value("${batch.shippingJob.toEmail}")
    protected String shippingEmail;

    @Override
    protected void doExecute(JobContext jobContext) throws Exception {

        List<Order> orders = orderService.findNotShippedOrders();
        logisticsService.requestExpressNo(orders);
        /*
        Map<String, List<Order>> providerOrders = orderService.getNotShippedProviderOrders();
        List<File> files = new ArrayList<File>();
        for(Map.Entry<String, List<Order>> entry : providerOrders.entrySet()){
        	String fileName = exportFilePath + "/" + "express_" + entry.getKey();
        	File file = logisticsService.generateExcel(expressList, fileName, shippingEmail);
        	files.add(file);
        }

        List<Attachment> attachments = new ArrayList<Attachment>();

        for(File file : files){
        	if (file != null) {
                Attachment attachment = new Attachment();
                attachment.setFilename(file.getName());
                FileInputStream fIn = new FileInputStream(file);
                ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
                byte[] b = new byte[1000];
                int n;
                while ((n = fIn.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                fIn.close();
                bos.close();
                attachment.setData(bos.toByteArray());
                attachment.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                attachments.add(attachment);
            }

        }

        if (attachments.size() > 0) {
            Map<String, Object> props = new HashMap<String, Object>();
            EmailInfo emailInfo = shippingEmailInfo.clone();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd_HHmm");
            String dateStr = fmt.format(new Date());
            emailInfo.setSubject("express_" + dateStr);
            emailInfo.setAttachments(attachments);
            emailService.sendTemplateEmail(shippingEmail, emailInfo, props);
        }
        */
    }
}
