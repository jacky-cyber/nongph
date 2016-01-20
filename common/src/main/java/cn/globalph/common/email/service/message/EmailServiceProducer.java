package cn.globalph.common.email.service.message;

import java.util.Map;


/**
 * 将邮件发往外部系统，进行邮件发送.
 * 例如JMS实现
 *     public void send(final Map props) {
 *       if (props instanceof Serializable) {
 *           final Serializable sProps = (Serializable) props;
 *           emailServiceTemplate.send(emailServiceDestination, new MessageCreator() {
 *               public Message createMessage(Session session) throws JMSException {
 *                   ObjectMessage message = session.createObjectMessage(sProps);
 *                   EmailInfo info = (EmailInfo) props.get(EmailPropertyType.INFO.getType());
 *                   message.setJMSPriority(Integer.parseInt(info.getSendAsyncPriority()));
 *                   return message;
 *               }
 *           });
 *       }
 *       throw new IllegalArgumentException("The properties map must be Serializable");
 *   }
 *
 */
public interface EmailServiceProducer {

    public void send(final Map props);
    
}
