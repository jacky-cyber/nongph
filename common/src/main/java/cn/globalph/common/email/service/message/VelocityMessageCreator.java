package cn.globalph.common.email.service.message;

import org.apache.velocity.app.VelocityEngine;

import cn.globalph.common.email.service.info.EmailInfo;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.HashMap;
import java.util.Map;

public class VelocityMessageCreator extends MessageCreator {

    private VelocityEngine velocityEngine;
    private Map<String, Object> additionalConfigItems;
    
    public VelocityMessageCreator(VelocityEngine velocityEngine, JavaMailSender mailSender, Map<String, Object> additionalConfigItems) {
        super(mailSender);
        this.additionalConfigItems = additionalConfigItems;
        this.velocityEngine = velocityEngine;        
    }

    @Override
    public String buildMessageBody(EmailInfo info, Map<String,Object> props) {
        if (props == null) {
            props = new HashMap<String, Object>();
        }

        if (props instanceof HashMap) {
            HashMap<String, Object> hashProps = (HashMap<String, Object>) props;
            @SuppressWarnings("unchecked")
            Map<String,Object> propsCopy = (Map<String, Object>) hashProps.clone();
            if (additionalConfigItems != null) {
                propsCopy.putAll(additionalConfigItems);
            }
            return VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, info.getEmailTemplate(), info.getEncoding(), propsCopy);
        }

        throw new IllegalArgumentException("Property map must be of type HashMap<String, Object>");
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public Map<String, Object> getAdditionalConfigItems() {
        return additionalConfigItems;
    }

    public void setAdditionalConfigItems(
            Map<String, Object> additionalConfigItems) {
        this.additionalConfigItems = additionalConfigItems;
    }   
}
