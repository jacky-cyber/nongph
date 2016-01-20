package cn.globalph.common.email.service;

import java.io.ByteArrayOutputStream;

import javax.activation.DataHandler;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * @author Andre Azzolini (apazzolini)
 */
public class LoggingMailSender extends JavaMailSenderImpl {
    private static final Log LOG = LogFactory.getLog(LoggingMailSender.class);

    @Override
    public void send(MimeMessagePreparator[] mimeMessagePreparators) throws MailException {
        for (MimeMessagePreparator preparator : mimeMessagePreparators) {
            try {
                MimeMessage mimeMessage = createMimeMessage();
                preparator.prepare(mimeMessage);
                LOG.info("\"Sending\" email: ");
                if (mimeMessage.getContent() instanceof MimeMultipart) {
                    MimeMultipart msg = (MimeMultipart) mimeMessage.getContent();
                    DataHandler dh = msg.getBodyPart(0).getDataHandler();
                    ByteArrayOutputStream baos = null;
                    try {
                        baos = new ByteArrayOutputStream();
                        dh.writeTo(baos);
                    } catch (Exception e) {
                        // Do nothing
                    } finally {
                        try {
                            baos.close();
                        } catch (Exception e) {
                            LOG.error("Couldn't close byte array output stream");
                        }
                    }
                } else {
                    LOG.info(mimeMessage.getContent());
                }
            } catch (Exception e) {
                LOG.error("Could not create message", e);
            }
        }
    }

}
