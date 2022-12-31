package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

public interface UserService {

    // Registration user
    boolean addUserToDatabase(User user);
    // Return user list, if email != null, return users with email
    List<User> userList(String email);
    void addUserAfterOauthLoginSuccess(String email, String name);
    // Find user by id
    User getUserById(Long id);
    // Find one user by email
    User getUserByEmail(String email);
    // Find user by principal
    User getUserByPrincipal(Principal principal);
    User getUserByResetPasswordCode(String resetPasswordCode);
    // Ban user logic
    void banUserAccountById(Long id);
    // Update user info
    void updateUserDataById(Long id, User userUpdate, MultipartFile multipartFile) throws IOException;
    void updateUserPassword(Long id, User userUpdate);
    // Change user roles
    void changeUserRole(User user);
    // Save user to database
    void save(User user);
    // Activate user email after registration
    boolean activateUserAccountAfterRegistration(String code);
    // Delete user account with saving his orders and bucket
    void deleteUserAccountById(Long id);
    // Delete user avatar
    void deleteUserAvatarById(Long id);
    void addProductToFavoritesAndRemoveIfExists(String authorizedEmail, Product product);
}
