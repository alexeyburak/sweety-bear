package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.Role;
import by.bsuir.sweetybear.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean createUser(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_ADMIN);
        log.info("Saving User. Email {}", email);
        userRepository.save(user);
        return true;
    }

    public List<User> userList() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setActive(false);
            log.info("Ban user. Id: {}, Email: {}", user.getId(), user.getEmail());
        }
        assert user != null;
        userRepository.save(user);
    }

    public void updateUserById(Long id, User userUpdate) {
        log.info("Update user. Id: {}", id);
        User user = getUserById(id);
        assert user != null;
        user.setName(userUpdate.getName());
        user.setEmail(userUpdate.getEmail());
        user.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        userRepository.save(user);
    }
}
