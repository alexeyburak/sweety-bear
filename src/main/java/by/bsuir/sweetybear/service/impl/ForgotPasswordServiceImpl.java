package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderImpl mailSender;

    @Override
    public boolean setCodeToResetUserPassword(String email) {
        User user = userService.getUserByEmail(email);
        if (user == null || !user.isActive()) return false;

        user.setResetPasswordCode(UUID.randomUUID().toString());
        userService.save(user);
        log.info("Set reset password code to user. Email: {}", email);

        mailSender.sendEmailWithResetPasswordLinkToUser(user);
        return true;
    }

    @Override
    public boolean changeUserPasswordByCode(String resetPasswordCode, User userUpdate) {
        User userDb = userService.getUserByResetPasswordCode(resetPasswordCode);
        if (userDb == null) return false;

        userDb.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        userDb.setResetPasswordCode(null);

        userService.save(userDb);
        log.info("User rested password. User email: {}", userDb.getEmail());
        return true;
    }
}
