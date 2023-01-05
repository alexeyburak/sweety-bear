package by.bsuir.sweetybear.dto;

import by.bsuir.sweetybear.annotation.PasswordMatch;
import by.bsuir.sweetybear.annotation.PasswordValid;
import by.bsuir.sweetybear.annotation.UsernameValid;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PasswordMatch
public class UserDTO {
    @UsernameValid
    String name;
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    String email;
    @PasswordValid
    String password;
    @PasswordValid
    String confirmPassword;
}
