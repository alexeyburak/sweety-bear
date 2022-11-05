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
    boolean createUser(User user);
    // Return user list, if email != null, return users with email
    List<User> userList(String email);
    // Find user by id
    User getUserById(Long id);
    // Find one user by email
    User getUserByEmail(String email);
    // Find user by principal
    User getUserByPrincipal(Principal principal);
    // Ban user logic
    void banUser(Long id);
    // Update user info
    void updateUserById(Long id, User userUpdate, MultipartFile file1) throws IOException;
    // Change user roles
    void changeUserRole(User user);
    // Save user to database
    void save(User user);
}
