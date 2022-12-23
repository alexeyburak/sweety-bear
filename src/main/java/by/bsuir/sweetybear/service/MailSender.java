package by.bsuir.sweetybear.service;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public interface MailSender {

    // Send email to user
    void send(String emailTo, String subject, String message);
}
