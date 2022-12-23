package by.bsuir.sweetybear.dto;

import by.bsuir.sweetybear.annotation.PasswordMatch;
import by.bsuir.sweetybear.annotation.PasswordValid;
import by.bsuir.sweetybear.annotation.UsernameValid;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Data
@NoArgsConstructor
@PasswordMatch
public class UserDTO {

    @UsernameValid
    private String name;
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;
    @PasswordValid
    private String password;
    @PasswordValid
    private String confirmPassword;

}
