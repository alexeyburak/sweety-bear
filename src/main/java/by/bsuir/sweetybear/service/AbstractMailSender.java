package by.bsuir.sweetybear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@RequiredArgsConstructor
public abstract class AbstractMailSender implements MailSender {
    public final JavaMailSender mailSender;
    protected String title;
    protected String message;
    @Value("${server.port}")
    protected String serverPort;
    @Value("${spring.mail.username}")
    private String username;

    protected void sendWithMime(String emailTo, String subject, String message) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

            mimeMessageHelper.setFrom(username);
            mimeMessageHelper.setTo(emailTo);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
