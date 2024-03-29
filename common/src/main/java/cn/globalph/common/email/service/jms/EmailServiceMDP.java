package cn.globalph.common.email.service.jms;

import cn.globalph.common.email.service.exception.EmailException;
import cn.globalph.common.email.service.message.MessageCreator;

import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import java.util.HashMap;

/**
 * @felix.wu
 */
public class EmailServiceMDP implements MessageListener {

    @Resource(name = "blMessageCreator")
    private MessageCreator messageCreator;

    /*
     * (non-Javadoc)
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    @SuppressWarnings("unchecked")
    public void onMessage(Message message) {
        try {
            HashMap props = (HashMap) ((ObjectMessage) message).getObject();
            messageCreator.sendMessage(props);
        } catch (MailAuthenticationException e) {
            throw new EmailException(e);
        } catch (MailPreparationException e) {
            throw new EmailException(e);
        } catch (MailParseException e) {
            throw new EmailException(e);
        } catch (MailSendException e) {
            /*
             * TODO find the specific exception that results from the smtp
             * server being down, and throw this as an EmailException.
             * Otherwise, log and then swallow this exception, as it may have
             * been possible that this email was actually sent.
             */
            throw new EmailException(e);
        } catch (JMSException e) {
            throw new EmailException(e);
        }
    }

}
