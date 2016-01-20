package cn.globalph.common.vendor.service.monitor.handler;

import cn.globalph.common.email.domain.EmailTarget;
import cn.globalph.common.email.service.EmailService;
import cn.globalph.common.email.service.info.EmailInfo;
import cn.globalph.common.vendor.service.monitor.StatusHandler;
import cn.globalph.common.vendor.service.type.ServiceStatusType;

import javax.annotation.Resource;

public class EmailStatusHandler implements StatusHandler {

    @Resource(name="blEmailService")
    protected EmailService emailService;

    protected EmailInfo emailInfo;
    protected EmailTarget emailTarget;

    public void handleStatus(String serviceName, ServiceStatusType status) {
        String message = serviceName + " is reporting a status of " + status.getType();
        EmailInfo copy = emailInfo.clone();
        copy.setMessageBody(message);
        copy.setSubject(message);
        emailService.sendBasicEmail(copy, emailTarget, null);
    }

    public EmailInfo getEmailInfo() {
        return emailInfo;
    }

    public void setEmailInfo(EmailInfo emailInfo) {
        this.emailInfo = emailInfo;
    }

    public EmailTarget getEmailTarget() {
        return emailTarget;
    }

    public void setEmailTarget(EmailTarget emailTarget) {
        this.emailTarget = emailTarget;
    }

}
