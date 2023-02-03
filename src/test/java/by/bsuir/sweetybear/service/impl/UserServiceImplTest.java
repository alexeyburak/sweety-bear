package by.bsuir.sweetybear.service.impl;

import by.bsuir.sweetybear.model.Product;
import by.bsuir.sweetybear.model.User;
import by.bsuir.sweetybear.model.enums.Role;
import by.bsuir.sweetybear.repository.UserRepository;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static by.bsuir.sweetybear.utils.UUIDGenerator.generateUUID;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private MailSenderImpl mailSender;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void checkAddUser() {
        // given
        String code = generateUUID();
        String email = "burakalexey@yahoo.com";
        User user = User.builder()
                .email(email)
                .active(false)
                .password(code)
                .roles(new HashSet<>(Collections.singletonList(Role.ROLE_USER)))
                .activationCode(code)
                .build();

        //when
        Mockito.when(userRepository.findByEmail(email)).thenReturn(null);
        Mockito.when(passwordEncoder.encode(code)).thenReturn(code);
        doNothing().when(mailSender).sendEmailWithActivationLinkToUser(user);
        userService.addUserToDatabase(user);

        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository, Mockito.times(1)).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        Assertions.assertNotNull(capturedUser);
        AssertionsForClassTypes.assertThat(capturedUser).isEqualTo(user);

    }

    @Test
    public void getUserByResetCode() {
        //given
        String resetCode = generateUUID();
        User user = User.builder()
                .resetPasswordCode(resetCode)
                .build();

        //when
        Mockito.when(userRepository.findByResetPasswordCode(resetCode)).thenReturn(user);
        User userFromDatabase = userService.getUserByResetPasswordCode(resetCode);

        //then
        Mockito.verify(userRepository, Mockito.times(1)).findByResetPasswordCode(resetCode);
        Assertions.assertNotNull(userFromDatabase);
        Assertions.assertEquals(user, userFromDatabase);
    }

    @Test
    public void banUserById() {
        //given
        User user = User.builder()
                .active(true)
                .build();

        //when
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //then
        Assertions.assertTrue(user.isActive());

        //when
        userService.banUserAccountById(1L);

        //then
        Mockito.verify(userRepository, Mockito.times(1)).findById(1L);
        Assertions.assertNotNull(user);
        Assertions.assertFalse(user.isActive());
    }

    @Test
    public void changeUserRole() {
        //given
        User user = User.builder()
                .roles(new HashSet<>(Collections.singletonList(Role.ROLE_USER)))
                .build();

        //when
        userService.changeUserRole(user);

        //then
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1, user.getRoles().size());
        Assertions.assertEquals(Set.of(Role.ROLE_ADMIN), user.getRoles());

        //when
        userService.changeUserRole(user);

        //then
        Assertions.assertNotNull(user);
        Assertions.assertEquals(1, user.getRoles().size());
        Assertions.assertEquals(Set.of(Role.ROLE_USER), user.getRoles());
    }

    @Test
    public void activateUserAccount() {
        //given
        String activationCode = generateUUID();
        User user = User.builder()
                .active(false)
                .activationCode(activationCode)
                .build();

        //when
        Mockito.when(userRepository.findByActivationCode(activationCode)).thenReturn(user);
        userService.activateUserAccountAfterRegistration(activationCode);

        //then
        Mockito.verify(userRepository, Mockito.times(1)).findByActivationCode(activationCode);
        Assertions.assertNotNull(user);
        Assertions.assertTrue(user.isActive());
        Assertions.assertNull(user.getActivationCode());
    }

    @Test
    public void addProductToFavoritesAndRemoveIfExistsTest() {
        //given
        Product firstProduct = createProduct(1L);
        Product secondProduct = createProduct(2L);
        Product thirdProduct = createProduct(3L);
        String email = "burakalexey@yahoo.com";
        User user = User.builder()
                .email(email)
                .favoriteProducts(new ArrayList<>(
                        List.of(
                                firstProduct,
                                secondProduct
                        )
                ))
                .build();


        //when
        Mockito.when(userRepository.findByEmail(email)).thenReturn(user);
        Assertions.assertEquals(2, user.getFavoriteProducts().size());
        userService.addProductToFavoritesAndRemoveIfExists(email, firstProduct);

        //then
        Assertions.assertNotNull(user.getFavoriteProducts());
        Assertions.assertEquals(1, user.getFavoriteProducts().size());
        Assertions.assertEquals(2L, user.getFavoriteProducts().get(0).getId());

        //when
        userService.addProductToFavoritesAndRemoveIfExists(email, thirdProduct);

        //then
        Assertions.assertNotNull(user.getFavoriteProducts());
        Assertions.assertEquals(2, user.getFavoriteProducts().size());
        Assertions.assertEquals(3L, user.getFavoriteProducts().get(1).getId());
    }

    private Product createProduct(Long id) {
        return Product.builder()
                .id(id)
                .build();
    }

    @Test
    public void updateUserPasswordById() {
        //given
        final String password = "password";
        final String newPassword = "newPassword";
        User user = User.builder()
                .password(password)
                .build();
        User updatedUser = User.builder()
                .password(newPassword)
                .build();

        //when
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Assertions.assertEquals(password, user.getPassword());
        userService.updateUserPasswordById(1L, updatedUser);

        //then
        Assertions.assertNotNull(user);
        Assertions.assertEquals(newPassword, user.getPassword());
    }

}
