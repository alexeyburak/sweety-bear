package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.dto.user.UserDTO;
import by.bsuir.sweetybear.exception.UserNotFoundException;
import by.bsuir.sweetybear.model.Image;
import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.Role;
import by.bsuir.sweetybear.repository.BucketRepository;
import by.bsuir.sweetybear.repository.ImageRepository;
import by.bsuir.sweetybear.repository.OrderRepository;
import by.bsuir.sweetybear.repository.UserRepository;
import by.bsuir.sweetybear.service.UserReceivingService;
import by.bsuir.sweetybear.service.UserService;
import by.bsuir.sweetybear.service.mapper.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;

import static by.bsuir.sweetybear.utils.UUIDGenerator.generateUUID;
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
public class UserServiceImpl implements UserService, UserReceivingService {

    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;
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
        user.setActivationCode(generateUUID());

        log.info("Saving User. Email {}", email);
        userRepository.save(user);

        mailSender.sendEmailWithActivationLinkToUser(user);
        return true;
    }

    @Override
    public void addUserAfterOauthLoginSuccess(String email, String name) {
        String temporaryPassword = generateUUID();

        userRepository.save(
                User.builder()
                        .email(email)
                        .name(name)
                        .active(true)
                        .roles(Collections.singleton(Role.ROLE_USER))
                        .password(passwordEncoder.encode(temporaryPassword))
                        .build()
        );
        log.info("Saving User. Email {}", email);

        mailSender.sendEmailWithPasswordToUser(email, temporaryPassword);
    }

    @Override
    public List<UserDTO> userList(String email) {
        if (email != null) return userRepository
                .findAllByEmail(email)
                .stream()
                .map(userDTOMapper)
                .toList();
        return userRepository
                .findAll()
                .stream()
                .map(userDTOMapper)
                .toList();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found. Id: " + id));
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
    public void updateUserDataById(Long id,
                                   User userUpdate,
                                   MultipartFile multipartFile) {
        User user = this.getUserById(id);
        try {
            if (multipartFile.getSize() != 0)
                addAvatarToUser(user, multipartFile);
        } catch (IOException e) {
            log.error("Error while saving avatar. User id: {}", id);
            throw new RuntimeException(e);
        }

        user.setName(userUpdate.getName());
        user.setEmail(userUpdate.getEmail());


        changeSecurityAuthenticationEmail(user.getEmail());
        userRepository.save(user);
        log.info("Update user data. Id: {}", id);
    }

    @Override
    public void updateUserPasswordById(Long id, User userUpdate) {
        User user = this.getUserById(id);

        user.setPassword(passwordEncoder.encode(userUpdate.getPassword()));

        userRepository.save(user);
        log.info("Update user password. User id: {}", id);
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

        boolean isFavoritesContainsProduct = user.getFavoriteProducts().contains(product);
        if (!isFavoritesContainsProduct)
            user.addProductToFavorites(product);
        else
            user.removeProductFromFavorites(product);

        userRepository.save(user);
        log.info("Update user favorites. User id: {}", user.getId());
    }

}
