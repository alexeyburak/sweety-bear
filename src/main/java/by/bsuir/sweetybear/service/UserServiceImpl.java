package by.bsuir.sweetybear.service;

import by.bsuir.sweetybear.model.Image;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.Role;
import by.bsuir.sweetybear.repository.ImageRepository;
import by.bsuir.sweetybear.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static by.bsuir.sweetybear.utils.Utils.toImageEntity;

/**
 * sweety-bear
 * Created by Alexey Burak
 * Oct 2022
 */

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSenderImpl mailSender;

    @Override
    public boolean createUser(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false;
        user.setActive(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_OWNER);
        log.info("Saving User. Email {}", email);
//        mailSender.send(email, "Thanks for registration!", user.getName() +
//                ", we hope that we will not quarrel! \nStart using our sait now: http://localhost:8085/");
        userRepository.save(user);
        return true;
    }

    @Override
    public List<User> userList(String email) {
        if (email != null) return Collections.singletonList(userRepository.findByEmail(email));
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User getUserByPrincipal(Principal principal) {
        if (principal == null) return new User();
        return userRepository.findByEmail(principal.getName());
    }

    @Override
    public void banUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setActive(!user.isActive());
            log.info("Ban/Unban user. Id: {}, Email: {}", user.getId(), user.getEmail());
        }
        assert user != null;
        userRepository.save(user);
    }

    @Override
    public void updateUserById(Long id, User userUpdate, MultipartFile file1) throws IOException {
        User user = getUserById(id);
        assert user != null;
        Image image1;
        if (file1.getSize() != 0) {
            if (user.isAvatarNull()) {
                imageRepository.markToDeleteByUserId(id, "toDelete");
                imageRepository.deleteByName("toDelete");
                log.warn("Delete photo.");
            }
            image1 = toImageEntity(file1);
            image1.setPreviewImage(true);
            user.addAvatarToUser(image1);
        }
        user.setName(userUpdate.getName());
        user.setEmail(userUpdate.getEmail());
        user.setPassword(passwordEncoder.encode(userUpdate.getPassword()));
        log.info("Update user. Id: {}", id);
        userRepository.save(user);
    }

    @Override
    public void changeUserRole(User user) {
        if (user.isAdmin()) {
            user.getRoles().clear();
            user.getRoles().add(Role.valueOf("ROLE_USER"));
        } else {
            user.getRoles().clear();
            user.getRoles().add(Role.valueOf("ROLE_ADMIN"));
        }
        log.info("Change role. User email: {}", user.getEmail());
        userRepository.save(user);
    }

    @Override
    public void save(User user) {
        log.info("Save user. Id: {}", user.getId());
        userRepository.save(user);
    }
}
