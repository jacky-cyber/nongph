package cn.globalph.common.email.service.message;

import cn.globalph.common.email.domain.EmailTarget;
import cn.globalph.common.email.service.info.EmailInfo;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import java.util.Map;

public abstract class MessageCreator {

    private JavaMailSender mailSender;
        
    public MessageCreator(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendMessage(final Map<String,Object> props) throws MailException {
        MimeMessagePreparator preparator = buildMimeMessagePreparator(props);
        this.mailSender.send(preparator);
    }
    
    public abstract String buildMessageBody(EmailInfo info, Map<String,Object> props);
    
    public MimeMessagePreparator buildMimeMessagePreparator(final Map<String,Object> props) {
         MimeMessagePreparator preparator = new MimeMessagePreparator() {
             public void prepare(MimeMessage mimeMessage) throws Exception {
                 EmailTarget emailUser = (EmailTarget) props.get(EmailPropertyType.USER.getType());
                 EmailInfo info = (EmailInfo) props.get(EmailPropertyType.INFO.getType());
                 MimeMessageHelper message = new MimeMessageHelper(mimeMessage, (info.getAttachments() != null && info.getAttachments().size() > 0),"utf-8");
                 message.setTo(emailUser.getEmailAddress());
                 message.setFrom(info.getFromAddress());
                //中文乱码
                 String subject = info.getSubject();
                 if(subject.equals("registerSuccess")){
                	 message.setSubject("感谢您注册品荟(GlobalPH.com)"); 
                 }else if(subject.equals("resetPasswordRequest")){
                	 message.setSubject("品荟-用户密码找回"); 
                 }else if(subject.equals("orderFromPh")){
                	 message.setSubject("您的品荟订单");
                 }else if(subject.equals("emailValidation")){
                	 message.setSubject("品荟账户绑定邮箱变动提醒");
                 }else{
                	 message.setSubject(subject);
                 }
                 
                 
                 if (emailUser.getBCCAddresses() != null && emailUser.getBCCAddresses().length > 0) {
                     message.setBcc(emailUser.getBCCAddresses());
                 }
                 if (emailUser.getCCAddresses() != null && emailUser.getCCAddresses().length > 0) {
                     message.setCc(emailUser.getCCAddresses());
                 }
                 String messageBody = info.getMessageBody();
                 if (messageBody == null) {                  
                     messageBody = buildMessageBody(info, props);
                 }
                 message.setText(messageBody, true);
                 for (Attachment attachment : info.getAttachments()) {
                     ByteArrayDataSource dataSource = new ByteArrayDataSource(attachment.getData(), attachment.getMimeType());
                     message.addAttachment(attachment.getFilename(), dataSource);
                 }
             }
         };
         return preparator;
                
    }

    public JavaMailSender getMailSender() {
        return mailSender;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
}
