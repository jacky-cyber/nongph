package cn.globalph.cms.structure.message.jms;

import java.util.HashMap;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import cn.globalph.cms.structure.service.StructuredContentService;

/**
 * Receives JMS message with a String that indicates the cache key
 * to invalidate.
 *
 * @author bpolster
 */
public class JMSArchivedStructuredContentSubscriber implements MessageListener {

    @Resource(name = "blStructuredContentService")
    private StructuredContentService structuredContentService;

    /*
     * (non-Javadoc)
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    @SuppressWarnings("unchecked")
    public void onMessage(Message message) {
        String basePageCacheKey = null;
        try {
            HashMap<String,String> props = (HashMap<String,String>) ((ObjectMessage) message).getObject();
            if (props != null) {
                //structuredContentService.removeItemFromCache(props.get("nameKey"), props.get("typeKey"));
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

}
