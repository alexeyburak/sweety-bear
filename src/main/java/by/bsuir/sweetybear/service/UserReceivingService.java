package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.dto.user.UserDTO;
import by.bsuir.sweetybear.model.User;

import java.security.Principal;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Jan 2023
 */

public interface UserReceivingService {
    User getUserById(Long id);
    User getUserByEmail(String email);
    User getUserByPrincipal(Principal principal);
    User getUserByResetPasswordCode(String resetPasswordCode);
    List<UserDTO> userList(String email);
}
