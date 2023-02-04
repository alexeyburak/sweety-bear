package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.service.impl.ForgotPasswordServiceImpl;
import by.bsuir.sweetybear.service.impl.MailSenderImpl;
import by.bsuir.sweetybear.service.impl.UserServiceImpl;
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
    public void setCodeToResetUserPassword_UserIsNotActive_False() {
        //given
        String email = "burakalexey@yahoo.com";
        User user = User.builder()
                .email(email)
                .active(false)
                .build();

        //when
        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
        boolean result = forgotPasswordService.setCodeToResetUserPassword(email);

        //then
        Assertions.assertFalse(result);
    }

    @Test
    public void setCodeToResetUserPassword_UserIsActive_ShouldSetResetCodeAndReturnTrue() {
        //given
        String email = "burakalexey@yahoo.com";
        User user = User.builder()
                .email(email)
                .active(true)
                .build();

        //when
        Mockito.when(userService.getUserByEmail(email)).thenReturn(user);
        doNothing().when(mailSender).sendEmailWithResetPasswordLinkToUser(user);
        boolean result = forgotPasswordService.setCodeToResetUserPassword(email);

        //then
        Assertions.assertTrue(result);
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getResetPasswordCode());
    }

    @Test
    public void changeUserPasswordByCode_UserNotFound_False() {
        //given
        String notExistCode = UUID.randomUUID().toString();
        User user = User.builder()
                .password(null)
                .build();

        //when
        Mockito.when(userService.getUserByResetPasswordCode(notExistCode)).thenReturn(null);
        boolean result = forgotPasswordService.changeUserPasswordByCode(notExistCode, user);

        //then
        Assertions.assertFalse(result);
    }

    @Test
    public void changeUserPasswordByCode_ResetCodeExists_ShouldSetNullResetCodeSetPasswordToUserAndReturnTrue() {
        //given
        String resetPasswordCode = UUID.randomUUID().toString();
        User user = User.builder()
                .resetPasswordCode(resetPasswordCode)
                .build();
        User updatedUser = User.builder()
                .password(resetPasswordCode)
                .build();

        //when
        Mockito.when(userService.getUserByResetPasswordCode(resetPasswordCode)).thenReturn(user);
        Mockito.when(passwordEncoder.encode(resetPasswordCode)).thenReturn(resetPasswordCode);
        boolean result = forgotPasswordService.changeUserPasswordByCode(resetPasswordCode, updatedUser);

        //then
        Assertions.assertNotNull(user);
        Assertions.assertNotNull(user.getPassword());
        Assertions.assertEquals(resetPasswordCode, user.getPassword());
        Assertions.assertNull(user.getResetPasswordCode());
        Assertions.assertTrue(result);
    }
}
