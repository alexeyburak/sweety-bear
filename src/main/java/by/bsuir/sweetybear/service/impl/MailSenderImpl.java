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
                        <h4> %s, click button to set new password</h4>
                        <form action="http://localhost:%s/reset_password/%s" target="_blank"> <button style="background-color: #555555;
                                                              border: none;
                                                              color: white;
                                                              text-align: center;
                                                              margin-top: 10px;
                                                              text-decoration: none;
                                                              font-size: 20px;">Reset password</button>
                        </form>
                """,
                user.getName(),
                serverPort,
                user.getResetPasswordCode()
        );
        title = "Reset password";

        new Thread(() -> sendWithMime(user.getEmail(), title, message)).start();
        log.warn("Send resetting password message.");
    }

    @Override
    public void sendEmailWithActivationLinkToUser(final User user) {
        message = String.format(
                """
                        <h4>%s, we hope that we will not quarrel!</h4>
                        <form action="http://localhost:%s/activate/%s" target="_blank"> <button style="background-color: #555555;
                                                              border: none;
                                                              color: white;
                                                              text-align: center;
                                                              margin-top: 10px;
                                                              text-decoration: none;
                                                              font-size: 20px;">Activate account</button>
                        </form>
                """,
                user.getName(),
                serverPort,
                user.getActivationCode()
        );
        title = "Thanks for registration!";

        new Thread(() -> sendWithMime(user.getEmail(), title, message)).start();
        log.warn("Send greeting message.");
    }

    @Override
    public void sendEmailWithPasswordToUser(final String email, final String temporaryPassword) {
        message = String.format(
                """
                        <h4>There are your account data</h4>
                        Email: %s
                        Password: %s
                """,
                email,
                temporaryPassword
        );
        title = "Thanks for registration!";

        new Thread(() -> sendWithMime(email, title, message)).start();
        log.warn("Send message with password.");
    }
}
