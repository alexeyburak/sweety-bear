package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.dto.UserDTO;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.repository.UserRepository;
import by.bsuir.sweetybear.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    @Value("${server.port}")
    private String serverPort;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderImpl mailSender;

    public boolean setCodeToResetUserPassword(String email) {
        User user = userService.getUserByEmail(email);
        if (user == null || user.isActive()) return false;

        user.setResetPasswordCode(UUID.randomUUID().toString());
        sendResetPasswordLinkToUser(user);
        userService.save(user);

        log.warn("Send email to reset user password.");
        return true;
    }

    private void sendResetPasswordLinkToUser(final User user) {
        String message = String.format(
                "%s, click link to set new password" +
                        "\nActivate your email: http://localhost:" + serverPort + "/reset_password/%s ",
                user.getName(),
                user.getResetPasswordCode()
        );
        String title = "Reset password";

        mailSender.send(user.getEmail(), title, message);
    }

    public boolean changeUserPassword(String resetPasswordCode, User user) {
        User userDb = userService.getUserByResetPasswordCode(resetPasswordCode);
        if (userDb == null) return false;

        userDb.setPassword(passwordEncoder.encode(user.getPassword()));
        userDb.setResetPasswordCode(null);

        log.info("User reset password. User email: {}", userDb.getEmail());
        userService.save(userDb);
        return true;
    }
}
