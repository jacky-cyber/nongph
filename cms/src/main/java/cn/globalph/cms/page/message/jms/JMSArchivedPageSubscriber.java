package cn.globalph.cms.page.message.jms;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import cn.globalph.cms.page.service.PageService;

/**
 * Receives JMS message with a String that indicates the cache key
 * to invalidate.
 *
 * @author bpolster
 */
public class JMSArchivedPageSubscriber implements MessageListener {

    @Resource(name = "blPageService")
    private PageService pageService;

    /*
     * (non-Javadoc)
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    @SuppressWarnings("unchecked")
    public void onMessage(Message message) {
        String basePageCacheKey = null;
        try {
            basePageCacheKey = ((TextMessage) message).getText();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

}
