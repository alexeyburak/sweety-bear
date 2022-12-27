package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.User;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public interface MailSender {

    void sendEmailWithResetPasswordLinkToUser(final User user);
    void sendEmailWithActivationLinkToUser(final User user);
    void sendEmailWithPasswordToUser(final User user, final String temporaryPassword);

}
