package cn.globalph.common.email.service;

import cn.globalph.common.email.domain.EmailTarget;
import cn.globalph.common.email.service.info.EmailInfo;

import java.util.Map;

/**
 * This null implementation class can be used during development to work around
 * the need to have a working SMTP service to route emails through.
 * 
 * @author aazzolini
 */
public class NullEmailServiceImpl implements EmailService {

    @Override
    public boolean sendTemplateEmail(String emailAddress, EmailInfo emailInfo, Map<String, Object> props) {
        return true;
    }

    @Override
    public boolean sendTemplateEmail(EmailTarget emailTarget, EmailInfo emailInfo, Map<String, Object> props) {
        return true;
    }

    @Override
    public boolean sendBasicEmail(EmailInfo emailInfo, EmailTarget emailTarget, Map<String, Object> props) {
        return true;
    }

}
