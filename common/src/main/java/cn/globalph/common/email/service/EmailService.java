package cn.globalph.common.email.service;

import cn.globalph.common.email.domain.EmailTarget;
import cn.globalph.common.email.service.info.EmailInfo;

import java.util.Map;

/**
 * @felix.wu
 *
 */
public interface EmailService {

    public boolean sendTemplateEmail(String emailAddress, EmailInfo emailInfo,  Map<String,Object> props);

    public boolean sendTemplateEmail(EmailTarget emailTarget, EmailInfo emailInfo, Map<String,Object> props);

    public boolean sendBasicEmail(EmailInfo emailInfo, EmailTarget emailTarget, Map<String,Object> props);

}
