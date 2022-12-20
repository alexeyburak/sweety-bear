package by.bsuir.sweetybear.service;

public interface MailSender {

    // Send email to user
    void send(String emailTo, String subject, String message);
}
