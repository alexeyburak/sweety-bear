package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.AbstractMailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Service
@Slf4j
public class MailSenderImpl extends AbstractMailSender {

    public MailSenderImpl(JavaMailSender mailSender) {
        super(mailSender);
    }

    @Override
    public void sendEmailWithResetPasswordLinkToUser(final User user) {
        message = String.format(
                """
                        %s, click link to set new password
                        Reset your password: http://localhost:%s/reset_password/%s""",
                user.getName(),
                serverPort,
                user.getResetPasswordCode()
        );
        title = "Reset password";

        send(user.getEmail(), title, message);
        log.warn("Send resetting password message.");
    }

    @Override
    public void sendEmailWithActivationLinkToUser(final User user) {
        message = String.format(
                """
                        %s, we hope that we will not quarrel!
                        Activate your email: http://localhost:%s/activate/%s""",
                user.getName(),
                serverPort,
                user.getActivationCode()
        );
        title = "Thanks for registration!";

        send(user.getEmail(), title, message);
        log.warn("Send greeting message.");
    }

    @Override
    public void sendEmailWithPasswordToUser(final User user, final String temporaryPassword) {
        message = String.format(
                """
                        %s, there are your account data
                        Email: %s
                        Password: %s""",
                user.getName(),
                user.getEmail(),
                temporaryPassword
        );
        title = "Thanks for registration!";

        send(user.getEmail(), title, message);
        log.warn("Send message with password.");
    }
}
