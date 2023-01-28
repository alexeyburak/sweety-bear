package by.bsuir.sweetybear.dto.user;

import by.bsuir.sweetybear.annotation.ForgotPasswordMatch;
import by.bsuir.sweetybear.annotation.PasswordValid;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Data
@NoArgsConstructor
@ForgotPasswordMatch
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserChangePasswordDTO {
    @PasswordValid
    String password;
    @PasswordValid
    String confirmPassword;
}
