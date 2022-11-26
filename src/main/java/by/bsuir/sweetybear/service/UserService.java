package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Nov 2022
 */

public interface UserService {

    // Registration user
    boolean addUserToDatabase(User user);
    // Return user list, if email != null, return users with email
    List<User> userList(String email);
    // Find user by id
    User getUserById(Long id);
    // Find one user by email
    User getUserByEmail(String email);
    // Find user by principal
    User getUserByPrincipal(Principal principal);
    // Ban user logic
    void banUserAccountById(Long id);
    // Update user info
    void updateUserById(Long id, User userUpdate, MultipartFile multipartFile) throws IOException;
    // Change user roles
    void changeUserRole(User user);
    // Save user to database
    void save(User user);
    // Activate user email after registration
    boolean activateUserAccountAfterRegistration(String code);
    // Delete user account with saving his orders and bucket
    void deleteUserAccountById(Long id);
    // Set address to user after making order
    void setAddressToUser(final User user, final String address);
}
