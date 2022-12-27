package by.bsuir.sweetybear.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@RequiredArgsConstructor
public abstract class AbstractMailSender implements MailSender {

    public final JavaMailSender mailSender;
    @Value("${server.port}")
    protected String serverPort;
    protected String title;
    protected String message;
    @Value("${spring.mail.username}")
    private String username;

    protected void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(username);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);
        mailSender.send(mailMessage);
    }

}
