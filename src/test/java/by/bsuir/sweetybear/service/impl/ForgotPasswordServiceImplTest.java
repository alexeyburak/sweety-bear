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

}
