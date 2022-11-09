package by.bsuir.sweetybear.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

@Data
@NoArgsConstructor
public class UserDTO {

    @Size(min = 5, max = 30, message = "Name must be between 5 and 30 characters")
    @NotBlank(message = "name is required")
    private String name;
    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Password confirm is required")
    private String confirmPassword;

}
