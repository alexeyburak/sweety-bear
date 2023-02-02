package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class ForgotPasswordServiceImplTest {
    @Mock
    private UserServiceImpl userService;
    @Mock
    private MailSenderImpl mailSender;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private ForgotPasswordServiceImpl forgotPasswordService;

    @Test
    public void testSetCodeToResetUserPassword() {
        String email = "burakalexey@yahoo.com";
        User user = User.builder()
                .email(email)
                .active(false)
                .build();

        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);

        boolean result = forgotPasswordService.setCodeToResetUserPassword(email);

        Mockito.verify(userService, Mockito.times(1)).getUserByEmail(email);
        Mockito.verify(userService, Mockito.times(0)).save(user);

        Assertions.assertFalse(result);

        doNothing().when(mailSender).sendEmailWithResetPasswordLinkToUser(user);
        user.setActive(true);
        result = forgotPasswordService.setCodeToResetUserPassword(email);

        Assertions.assertTrue(result);
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getResetPasswordCode());
    }

    @Test
    public void changeUserPasswordByCodeTest() {
        String code = UUID.randomUUID().toString();
        String notExistCode = UUID.randomUUID().toString();
        User user = User.builder()
                .password(null)
                .resetPasswordCode(code)
                .build();
        User updatedUser = User.builder()
                .password(code)
                .build();

        Mockito.when(userService.getUserByResetPasswordCode(notExistCode)).thenReturn(null);
        Mockito.when(userService.getUserByResetPasswordCode(code)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(code)).thenReturn(code);

        boolean result = forgotPasswordService.changeUserPasswordByCode(notExistCode, user);

        Assertions.assertFalse(result);

        result = forgotPasswordService.changeUserPasswordByCode(code, updatedUser);

        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getPassword());
        Assertions.assertEquals(code, user.getPassword());
        Assertions.assertNull(user.getResetPasswordCode());
        Assertions.assertTrue(result);
    }
}
