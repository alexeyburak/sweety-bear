package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.exception.ApiRequestException;
import by.bsuir.sweetybear.model.Image;
import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.Role;
import by.bsuir.sweetybear.repository.BucketRepository;
import by.bsuir.sweetybear.repository.ImageRepository;
import by.bsuir.sweetybear.repository.OrderRepository;
import by.bsuir.sweetybear.repository.UserRepository;
import by.bsuir.sweetybear.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

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
    private final BucketRepository bucketRepository;
    private final OrderRepository orderRepository;

    @Override
    public boolean addUserToDatabase(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) return false;

        user.setActive(false);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.getRoles().add(Role.ROLE_USER);
        user.setActivationCode(UUID.randomUUID().toString());

        log.info("Saving User. Email {}", email);
        userRepository.save(user);

        mailSender.sendEmailWithActivationLinkToUser(user);
        return true;
    }

    @Override
    public void addUserAfterOauthLoginSuccess(String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setActive(true);
        user.getRoles().add(Role.ROLE_USER);
        String temporaryPassword = UUID.randomUUID().toString();
        user.setPassword(passwordEncoder.encode(temporaryPassword));

        log.info("Saving User. Email {}", email);
        userRepository.save(user);

        mailSender.sendEmailWithPasswordToUser(user, temporaryPassword);
    }

    @Override
    public List<User> userList(String email) {
        if (email != null) return userRepository.findAllByEmail(email);
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new ApiRequestException("User not found. Id: " + id));
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
    public User getUserByResetPasswordCode(String resetPasswordCode) {
        return userRepository.findByResetPasswordCode(resetPasswordCode);
    }

    @Override
    public void banUserAccountById(Long id) {
        User user = this.getUserById(id);

        user.setActive(!user.isActive());
        log.info("Ban/Unban user. Id: {}, Email: {}", user.getId(), user.getEmail());
        userRepository.save(user);
    }

    @Override
    public void updateUserById(Long id,
                               User userUpdate,
                               MultipartFile multipartFile) throws IOException {
        User user = this.getUserById(id);

        if (multipartFile.getSize() != 0)
            addAvatarToUser(user, multipartFile);

        user.setName(userUpdate.getName());
        user.setEmail(userUpdate.getEmail());
        user.setPassword(passwordEncoder.encode(userUpdate.getPassword()));

        changeSecurityAuthenticationEmail(user.getEmail());

        log.info("Update user. Id: {}", id);
        userRepository.save(user);
    }

    private void changeSecurityAuthenticationEmail(final String email) {
        User userDetails = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        userDetails.setEmail(email);
    }

    @Override
    public void deleteUserAvatarById(Long id) {
        User user = this.getUserById(id);

        user.setAvatar(null);
        imageRepository.markToDeleteByUserId(id, "toDelete");
        imageRepository.deleteByName("toDelete");
        log.info("Delete user avatar. User email: {}", user.getEmail());
    }

    private void addAvatarToUser(final User user,
                                 MultipartFile multipartFile) throws IOException {
        Image userAvatar;
        if (user.isAvatarNull()) {
            imageRepository.markToDeleteByUserId(user.getId(), "toDelete");
            imageRepository.deleteByName("toDelete");
            log.warn("Delete photo.");
        }
        userAvatar = toImageEntity(multipartFile);
        userAvatar.setPreviewImage(true);
        user.addAvatarToUser(userAvatar);
    }

    @Override
    public void changeUserRole(User user) {
        if (user.isAdmin()) {
            user.getRoles().clear();
            user.getRoles().add(Role.ROLE_USER);
        } else {
            user.getRoles().clear();
            user.getRoles().add(Role.ROLE_ADMIN);
        }
        log.info("Change role. User email: {}", user.getEmail());
        userRepository.save(user);
    }

    @Override
    public void save(User user) {
        log.info("Save user. Id: {}", user.getId());
        userRepository.save(user);
    }

    @Override
    public boolean activateUserAccountAfterRegistration(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user == null) return false;
        user.setActivationCode(null);
        user.setActive(true);
        userRepository.save(user);
        log.info("User activate account. User email: {}", user.getEmail());
        return true;
    }

    @Override
    public void deleteUserAccountById(Long id) {
        User user = this.getUserById(id);
        user.setBucket(null);
        user.setAddress(null);
        userRepository.save(user);
        bucketRepository.deleteByUserId(id);
        orderRepository.deleteByUserId(id);
        userRepository.delete(user);
        log.warn("Delete user account. User id: {}", id);
    }

    @Override
    public void addProductToFavoritesAndRemoveIfExists(String authorizedEmail, Product product) {
        User user = this.getUserByEmail(authorizedEmail);

        if (!user.getFavoriteProducts().contains(product))
            user.addProductToFavorites(product);
        else
            user.removeProductFromFavorites(product);

        userRepository.save(user);
        log.info("Update user favorites. User id: {}", user.getId());
    }

}
