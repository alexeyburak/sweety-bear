package by.bsuir.sweetybear.dto;

import by.bsuir.sweetybear.annotation.ForgotPasswordMatch;
import by.bsuir.sweetybear.annotation.PasswordValid;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Dec 2022
 */

@Data
@NoArgsConstructor
@ForgotPasswordMatch
public class UserForgotPasswordDTO {

    @PasswordValid
    private String password;
    @PasswordValid
    private String confirmPassword;
}
